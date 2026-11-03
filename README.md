# 班级学生成绩管理系统（JavaEE 期末大作业）

> 技术栈：Servlet 4.0 + JSP + JSTL + JDBC + MySQL 8.0 + Maven（WAR）
> 运行环境：JDK 8 / Tomcat 9.0 / MySQL 8.0
> 架构：MVC 分层（entity / dao / servlet / filter / util）

---

## 一、系统功能

### 1. 教师端
| 功能 | 说明 |
|---|---|
| 成绩录入 | 选择「班级 + 课程 + 考试类型」，整班学生一次提交；已录入的分数自动回填，可直接修改覆盖 |
| 成绩统计 | 参考人数、平均分、最高分、最低分、及格率、优秀率、五档分数段分布图、成绩排名表 |
| 权限范围 | 教师只能录入/统计自己任教的课程（`course.teacher_id`） |

### 2. 学生端
| 功能 | 说明 |
|---|---|
| 我的成绩 | 各科分数、等级、学分 |
| 个人统计 | 平均分、及格率（及格科目/总科目）、班级排名、学分加权均分 |
| 班级对比 | 每门课与「本班该科平均分」的差值（+ / -） |

### 3. 管理员端
| 功能 | 说明 |
|---|---|
| 控制台 | 班级数、教师数、学生数、课程数、全校及格率、最近录入的 10 条成绩 |
| 班级管理 | 班级增 / 删 / 改，指定班主任 |
| 课程管理 | 课程增 / 删 / 改，指定授课教师与学分 |
| 用户管理 | 教师 / 学生 / 管理员账号的创建、删除、密码重置（重置为 123456） |

### 4. 公共
- 首页（角色功能引导）、登录、学生自助注册、退出登录
- 登录状态校验与角色越权拦截（Filter）
- 全站 UTF-8 中文编码（Filter）

---

## 二、目录结构

```
ClassScoreManager/
├── pom.xml                       Maven 配置（war 打包，finalName=ClassScoreManager）
├── database_init.sql             建库 + 建表 + 演示数据（1 键初始化）
└── src/main/
    ├── java/com/score/
    │   ├── entity/               User / Clazz / Course / Score / ScoreRow / StatResult
    │   ├── dao/                  UserDAO / ClassDAO / CourseDAO / ScoreDAO
    │   ├── servlet/              LoginServlet / RegisterServlet / LogoutServlet
    │   │                         TeacherScoreServlet / ScoreSaveServlet / TeacherStatServlet
    │   │                         StudentScoreServlet
    │   │                         AdminDashboardServlet / ClassServlet / CourseServlet / UserServlet
    │   ├── filter/               EncodingFilter / LoginFilter
    │   └── util/                 DBUtil / MD5Util
    └── webapp/
        ├── WEB-INF/web.xml
        ├── css/style.css         全站样式（含纯 CSS 柱状图）
        ├── index.jsp             公开首页
        ├── login.jsp / register.jsp
        ├── pages/common/         header.jspf / footer.jspf（统一骨架）
        ├── pages/teacher/        scoreInput.jsp / statistics.jsp
        ├── pages/student/        myScore.jsp
        └── pages/admin/          dashboard.jsp / classManage.jsp / courseManage.jsp / userManage.jsp
```

---

## 三、数据库设计（库名 `class_score_db`）

| 表 | 说明 | 关键字段 |
|---|---|---|
| `class` | 班级 | cls_id, cls_name, grade, teacher_id |
| `user` | 用户（role：0 管理员 / 1 教师 / 2 学生） | uid, username, password(MD5), real_name, role, student_no, class_id |
| `course` | 课程 | cid, course_name, credit, teacher_id |
| `score` | 成绩（核心表） | sid, student_id, course_id, score, exam_type，唯一键 (student_id, course_id, exam_type) |

**核心统计 SQL**

```sql
-- 平均分
SELECT AVG(score) FROM score WHERE course_id=? AND student_id IN (该班学生);

-- 及格率
SELECT SUM(CASE WHEN score >= 60 THEN 1 ELSE 0 END) / COUNT(*) FROM score WHERE ...;

-- 班级排名（按平均分降序）
SELECT student_id, AVG(score) a FROM score s JOIN user u ON s.student_id=u.uid
WHERE u.class_id=? GROUP BY student_id ORDER BY a DESC;
```

> 唯一键 `uk_stu_course` 让「录入」用一条 `INSERT ... ON DUPLICATE KEY UPDATE` 完成新增与修改，不会产生重复成绩。

---

## 四、部署运行

> 前置条件：已安装 JDK 8、Tomcat 9、MySQL 8.0，并将 MySQL 的 `bin` 目录加入 PATH（或使用完整路径）。

### 方式一：一键脚本（Windows）
1. 打开 `deploy\start-class-score.bat`，把开头的 `JAVA_HOME`、`CATALINA_HOME`、`MYSQL_HOME` 改成你的实际安装路径；
2. 双击运行，自动启动 MySQL、Tomcat 并打开浏览器。

### 方式二：手动部署
```bash
# 1. 初始化数据库（只需一次；账号密码按你的 MySQL 实际情况调整）
mysql -h 127.0.0.1 -u root -p --default-character-set=utf8mb4 < database_init.sql

# 2. 打包
mvn clean package -DskipTests

# 3. 部署：把 target/ClassScoreManager.war 复制到 %TOMCAT_HOME%\webapps\
# 4. 启动 Tomcat（bin\startup.bat 或 catalina.bat run）
```

访问地址：**http://localhost:8080/ClassScoreManager/**

> 数据库连接配置在 `src/main/java/com/score/util/DBUtil.java`（URL / USER / PWD），按你的 MySQL 环境修改后重新打包。

### 方式三：IDEA
1. `File → Open` 选择 `pom.xml`，等依赖下载完
2. Maven 面板点「Reload All Maven Projects」
3. 配置 SmartTomcat：Context Path 填 `/ClassScoreManager`，端口 8080（需先停掉命令行 Tomcat）
4. Run 后访问上面的地址

---

## 五、测试账号（密码均为 123456）

| 角色 | 账号 | 说明 |
|---|---|---|
| 管理员 | `admin` | 全部后台功能 |
| 教师 | `teacher1` | 张明远，任教高等数学 / 数据结构 / Java程序设计 |
| 教师 | `teacher2` | 李文静，任教大学英语 / 计算机网络 |
| 学生 | `stu230101` | 张伟，计算机2301班 |
| 学生 | `stu230201` | 徐明，软件工程2302班 |

演示数据规模：2 个班级、3 名教师、20 名学生、5 门课程、100 条成绩（及格率约 81%）。

---

## 六、页面清单

| 角色 | URL |
|---|---|
| 首页 | `/ClassScoreManager/` |
| 登录 / 注册 | `/login.jsp`、`/registerServlet` |
| 教师-成绩录入 | `/teacher/scoreInputServlet` |
| 教师-成绩统计 | `/teacher/statServlet` |
| 学生-我的成绩 | `/student/myScoreServlet` |
| 管理员-控制台 | `/admin/dashboardServlet` |
| 管理员-班级 / 课程 / 用户 | `/admin/classServlet`、`/admin/courseServlet`、`/admin/userServlet?role=2` |

---

## 七、实现要点（答辩可讲）

1. **MVC 分层**：JSP 只负责展示，Servlet 收参数与转发，DAO 封装 JDBC，实体在各层之间传递。
2. **批量录入**：表单用同名的 `studentId[] / scoreValue[]` 提交，后端按索引配对，逐条校验 0~100，空值跳过。
3. **自动新增/修改**：`INSERT ... ON DUPLICATE KEY UPDATE` 依赖唯一键实现 upsert。
4. **统计在内存完成**：一次查出该班该课全部成绩，再在 Java 里算平均分、及格率、分数段与排名，避免多条 SQL。
5. **权限控制**：`LoginFilter` 按 URL 前缀（`/teacher/`、`/student/`、`/admin/`）校验 `session` 中的角色，未登录跳登录页，越权返回 403。
6. **密码安全**：MD5 摘要存储，登录时对明文做 MD5 再比对。
7. **纯 CSS 图表**：柱状图用 `flex` 布局 + 高度百分比实现，不依赖 ECharts/CDN，断网也能正常显示。

---

## 八、常见问题

| 现象 | 处理 |
|---|---|
| 页面 500，控制台报 `Communications link failure` | MySQL 没启动，运行启动脚本或 `mysqld --defaults-file=...` |
| 登录提示账号密码错误 | 确认数据库已执行 `database_init.sql`（密码为 123456 的 MD5） |
| 中文乱码 | 确认 `EncodingFilter` 生效、数据库为 `utf8mb4`、连接串带 `characterEncoding=utf8` |
| 教师登录后看不到课程 | 该教师没有任教课程时系统会自动展示全部课程，可用 admin 在「课程管理」里指派 |
| 8080 端口被占用 | 先关掉已有 Tomcat 进程，或改 `conf/server.xml` 的 Connector 端口 |
