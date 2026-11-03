-- ============================================================
-- 班级学生成绩管理系统  数据库初始化脚本
-- 数据库：class_score_db    字符集：utf8mb4
-- 说明：直接 source 本文件即可建库、建表并写入演示数据
-- ============================================================

DROP DATABASE IF EXISTS class_score_db;
CREATE DATABASE class_score_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE class_score_db;

-- ------------------------------------------------------------
-- 1. 班级表
-- ------------------------------------------------------------
CREATE TABLE class (
    cls_id      INT AUTO_INCREMENT PRIMARY KEY,
    cls_name    VARCHAR(50)  NOT NULL COMMENT '班级名称',
    grade       VARCHAR(20)  NOT NULL COMMENT '年级',
    teacher_id  INT          DEFAULT 0 COMMENT '班主任(教师uid)',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级';

-- ------------------------------------------------------------
-- 2. 用户表（管理员 / 教师 / 学生）
-- ------------------------------------------------------------
CREATE TABLE user (
    uid         INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录账号',
    password    VARCHAR(64)  NOT NULL COMMENT 'MD5 密码',
    real_name   VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    role        INT          NOT NULL DEFAULT 2 COMMENT '0管理员 1教师 2学生',
    student_no  VARCHAR(20)  DEFAULT '' COMMENT '学号(学生)',
    class_id    INT          DEFAULT 0 COMMENT '所属班级(学生)',
    phone       VARCHAR(20)  DEFAULT '',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- ------------------------------------------------------------
-- 3. 课程表
-- ------------------------------------------------------------
CREATE TABLE course (
    cid         INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50)   NOT NULL COMMENT '课程名称',
    credit      DECIMAL(3,1)  DEFAULT 3.0 COMMENT '学分',
    teacher_id  INT           DEFAULT 0 COMMENT '授课教师uid',
    create_time DATETIME      DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程';

-- ------------------------------------------------------------
-- 4. 成绩表（核心表）
-- ------------------------------------------------------------
CREATE TABLE score (
    sid         INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT           NOT NULL COMMENT '学生uid',
    course_id   INT           NOT NULL COMMENT '课程cid',
    score       DECIMAL(5,2)  NOT NULL COMMENT '分数',
    exam_type   VARCHAR(20)   DEFAULT '期末' COMMENT '考试类型',
    create_time DATETIME      DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_stu_course (student_id, course_id, exam_type),
    KEY idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩';

-- ============================================================
-- 初始化数据
-- 密码统一为 123456（MD5：e10adc3949ba59abbe56e057f20f883e）
-- ============================================================

-- 班级
INSERT INTO class (cls_name, grade, teacher_id) VALUES
('计算机2301班', '2023级', 2),
('软件工程2302班', '2023级', 3);

-- 管理员 + 教师
INSERT INTO user (username, password, real_name, role, student_no, class_id, phone) VALUES
('admin',    'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 0, '', 0, '13800000000'),
('teacher1', 'e10adc3949ba59abbe56e057f20f883e', '张明远',     1, '', 0, '13800000001'),
('teacher2', 'e10adc3949ba59abbe56e057f20f883e', '李文静',     1, '', 0, '13800000002'),
('teacher3', 'e10adc3949ba59abbe56e057f20f883e', '王志强',     1, '', 0, '13800000003');

-- 20 名学生（1 班 10 人，2 班 10 人）
INSERT INTO user (username, password, real_name, role, student_no, class_id, phone) VALUES
('stu230101', 'e10adc3949ba59abbe56e057f20f883e', '张伟',   2, '2023010101', 1, '13900000101'),
('stu230102', 'e10adc3949ba59abbe56e057f20f883e', '王芳',   2, '2023010102', 1, '13900000102'),
('stu230103', 'e10adc3949ba59abbe56e057f20f883e', '李娜',   2, '2023010103', 1, '13900000103'),
('stu230104', 'e10adc3949ba59abbe56e057f20f883e', '刘洋',   2, '2023010104', 1, '13900000104'),
('stu230105', 'e10adc3949ba59abbe56e057f20f883e', '陈静',   2, '2023010105', 1, '13900000105'),
('stu230106', 'e10adc3949ba59abbe56e057f20f883e', '杨帆',   2, '2023010106', 1, '13900000106'),
('stu230107', 'e10adc3949ba59abbe56e057f20f883e', '赵磊',   2, '2023010107', 1, '13900000107'),
('stu230108', 'e10adc3949ba59abbe56e057f20f883e', '黄敏',   2, '2023010108', 1, '13900000108'),
('stu230109', 'e10adc3949ba59abbe56e057f20f883e', '周杰',   2, '2023010109', 1, '13900000109'),
('stu230110', 'e10adc3949ba59abbe56e057f20f883e', '吴倩',   2, '2023010110', 1, '13900000110'),
('stu230201', 'e10adc3949ba59abbe56e057f20f883e', '徐明',   2, '2023020201', 2, '13900000201'),
('stu230202', 'e10adc3949ba59abbe56e057f20f883e', '孙丽',   2, '2023020202', 2, '13900000202'),
('stu230203', 'e10adc3949ba59abbe56e057f20f883e', '马超',   2, '2023020203', 2, '13900000203'),
('stu230204', 'e10adc3949ba59abbe56e057f20f883e', '朱婷',   2, '2023020204', 2, '13900000204'),
('stu230205', 'e10adc3949ba59abbe56e057f20f883e', '胡军',   2, '2023020205', 2, '13900000205'),
('stu230206', 'e10adc3949ba59abbe56e057f20f883e', '郭雪',   2, '2023020206', 2, '13900000206'),
('stu230207', 'e10adc3949ba59abbe56e057f20f883e', '何伟',   2, '2023020207', 2, '13900000207'),
('stu230208', 'e10adc3949ba59abbe56e057f20f883e', '高翔',   2, '2023020208', 2, '13900000208'),
('stu230209', 'e10adc3949ba59abbe56e057f20f883e', '林小雨', 2, '2023020209', 2, '13900000209'),
('stu230210', 'e10adc3949ba59abbe56e057f20f883e', '罗佳',   2, '2023020210', 2, '13900000210');

-- 课程
INSERT INTO course (course_name, credit, teacher_id) VALUES
('高等数学',      4.0, 2),
('大学英语',      3.0, 3),
('数据结构',      4.0, 2),
('Java程序设计',  3.5, 2),
('计算机网络',    3.0, 3);

-- 成绩：每位学生 × 每门课程 一条记录，分数由公式生成（52~99，保证有不及格样本）
INSERT INTO score (student_id, course_id, score, exam_type)
SELECT u.uid, c.cid, 52 + MOD(u.uid * 7 + c.cid * 13, 48), '期末'
FROM user u JOIN course c
WHERE u.role = 2;

-- 快速自检
SELECT '班级数' AS 项目, COUNT(*) AS 数量 FROM class
UNION ALL SELECT '教师数', COUNT(*) FROM user WHERE role = 1
UNION ALL SELECT '学生数', COUNT(*) FROM user WHERE role = 2
UNION ALL SELECT '课程数', COUNT(*) FROM course
UNION ALL SELECT '成绩记录', COUNT(*) FROM score;
