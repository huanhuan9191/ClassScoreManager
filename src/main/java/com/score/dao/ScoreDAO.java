package com.score.dao;

import com.score.entity.Score;
import com.score.entity.StatResult;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 成绩数据访问层：录入、查询、统计（平均分 / 及格率 / 优秀率 / 分数段 / 排名）
 */
public class ScoreDAO {

    private static final String BASE_SQL =
            "SELECT s.sid, s.student_id, s.course_id, s.score, s.exam_type, s.create_time, "
            + "u.student_no, u.real_name AS student_name, IFNULL(cl.cls_name,'未分班') AS cls_name, "
            + "c.course_name, c.credit "
            + "FROM score s "
            + "JOIN user u ON s.student_id = u.uid "
            + "JOIN course c ON s.course_id = c.cid "
            + "LEFT JOIN class cl ON u.class_id = cl.cls_id ";

    private Score map(ResultSet rs) throws Exception {
        Score s = new Score();
        s.setSid(rs.getInt("sid"));
        s.setStudentId(rs.getInt("student_id"));
        s.setCourseId(rs.getInt("course_id"));
        s.setScore(rs.getDouble("score"));
        s.setExamType(rs.getString("exam_type"));
        s.setCreateTime(String.valueOf(rs.getTimestamp("create_time")));
        s.setStudentNo(rs.getString("student_no"));
        s.setStudentName(rs.getString("student_name"));
        s.setClassName(rs.getString("cls_name"));
        s.setCourseName(rs.getString("course_name"));
        s.setCredit(rs.getDouble("credit"));
        return s;
    }

    /** 某班某课程某次考试的全部成绩，按学号排序 */
    public List<Score> listByClassCourse(int classId, int courseId, String examType) {
        String sql = BASE_SQL + "WHERE u.class_id = ? AND s.course_id = ? AND s.exam_type = ? "
                + "ORDER BY u.student_no";
        List<Score> list = new ArrayList<Score>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, courseId);
            ps.setString(3, examType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("查询成绩失败", e);
        }
        return list;
    }

    /** 某学生的所有成绩 */
    public List<Score> listByStudent(int studentId) {
        String sql = BASE_SQL + "WHERE s.student_id = ? ORDER BY c.cid";
        List<Score> list = new ArrayList<Score>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("查询个人成绩失败", e);
        }
        return list;
    }

    /** 全部成绩（管理员总览，取最近 50 条） */
    public List<Score> listRecent(int limit) {
        String sql = BASE_SQL + "ORDER BY s.create_time DESC, s.sid DESC LIMIT ?";
        List<Score> list = new ArrayList<Score>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("查询成绩失败", e);
        }
        return list;
    }

    /** 录入或修改：同一学生 + 同一课程 + 同一考试类型视为同一条记录 */
    public boolean saveOrUpdate(int studentId, int courseId, String examType, double score) {
        String sql = "INSERT INTO score (student_id, course_id, score, exam_type) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE score = VALUES(score)";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDouble(3, score);
            ps.setString(4, examType);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("保存成绩失败：" + e.getMessage(), e);
        }
    }

    public boolean delete(int sid) {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM score WHERE sid = ?")) {
            ps.setInt(1, sid);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除成绩失败", e);
        }
    }

    /** 单科统计：平均分、最高/最低分、及格率、优秀率、分数段分布、排名 */
    public StatResult statByClassCourse(int classId, int courseId, String examType,
                                        String className, String courseName) {
        List<Score> list = listByClassCourse(classId, courseId, examType);
        StatResult r = new StatResult();
        r.setClassName(className);
        r.setCourseName(courseName);
        r.setRanking(list);

        if (list.isEmpty()) {
            return r;
        }
        double sum = 0;
        double max = -1;
        double min = 101;
        int pass = 0;
        int excellent = 0;
        int[] dist = new int[5];

        for (Score s : list) {
            double v = s.getScore();
            sum += v;
            if (v > max) {
                max = v;
            }
            if (v < min) {
                min = v;
            }
            if (v >= 60) {
                pass++;
            }
            if (v >= 90) {
                excellent++;
            }
            int idx;
            if (v < 60) {
                idx = 0;
            } else if (v < 70) {
                idx = 1;
            } else if (v < 80) {
                idx = 2;
            } else if (v < 90) {
                idx = 3;
            } else {
                idx = 4;
            }
            dist[idx]++;
        }

        r.setTotal(list.size());
        r.setAvg(sum / list.size());
        r.setMax(max);
        r.setMin(min);
        r.setPassCount(pass);
        r.setExcellentCount(excellent);
        r.setDistribution(dist);

        // 排名：按分数降序
        list.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return r;
    }

    /** 学生个人汇总：科目数、平均分、及格科目数 */
    public double[] studentSummary(int studentId) {
        String sql = "SELECT COUNT(*) AS cnt, IFNULL(AVG(score),0) AS avg_score, "
                + "SUM(CASE WHEN score >= 60 THEN 1 ELSE 0 END) AS pass_cnt "
                + "FROM score WHERE student_id = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new double[]{rs.getInt("cnt"), rs.getDouble("avg_score"), rs.getInt("pass_cnt")};
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("统计个人成绩失败", e);
        }
        return new double[]{0, 0, 0};
    }

    /** 班级内按平均分排名，返回 {名次, 班级人数} */
    public int[] classRank(int studentId, int classId) {
        String sql = "SELECT s.student_id, AVG(s.score) AS a FROM score s "
                + "JOIN user u ON s.student_id = u.uid "
                + "WHERE u.class_id = ? GROUP BY s.student_id ORDER BY a DESC";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                int pos = 0;
                int total = 0;
                int rank = 0;
                while (rs.next()) {
                    total++;
                    int sid = rs.getInt("student_id");
                    if (sid == studentId) {
                        rank = pos + 1;
                    }
                    pos++;
                }
                return new int[]{rank, total};
            }
        } catch (Exception e) {
            throw new RuntimeException("计算排名失败", e);
        }
    }

    /** 某班某课程的平均分（学生端做"我与班级平均分"对比用） */
    public double classCourseAvg(int classId, int courseId, String examType) {
        String sql = "SELECT IFNULL(AVG(s.score),0) FROM score s JOIN user u ON s.student_id = u.uid "
                + "WHERE u.class_id = ? AND s.course_id = ? AND s.exam_type = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, courseId);
            ps.setString(3, examType);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? StatResult.round(rs.getDouble(1)) : 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("统计班级平均分失败", e);
        }
    }

    public int countAll() {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM score");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException("统计成绩条数失败", e);
        }
    }

    /** 全校总体及格率（管理员控制台用） */
    public double globalPassRate() {
        String sql = "SELECT COUNT(*) AS c, SUM(CASE WHEN score >= 60 THEN 1 ELSE 0 END) AS p FROM score";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int c = rs.getInt("c");
                int p = rs.getInt("p");
                return c == 0 ? 0 : StatResult.round(p * 100.0 / c);
            }
        } catch (Exception e) {
            throw new RuntimeException("统计及格率失败", e);
        }
        return 0;
    }
}
