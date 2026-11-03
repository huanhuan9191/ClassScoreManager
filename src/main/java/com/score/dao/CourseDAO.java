package com.score.dao;

import com.score.entity.Course;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程数据访问层
 */
public class CourseDAO {

    private static final String BASE_SQL =
            "SELECT c.cid, c.course_name, c.credit, c.teacher_id, "
            + "IFNULL(t.real_name,'未分配') AS teacher_name "
            + "FROM course c LEFT JOIN user t ON c.teacher_id = t.uid ";

    private Course map(ResultSet rs) throws Exception {
        Course c = new Course();
        c.setCid(rs.getInt("cid"));
        c.setCourseName(rs.getString("course_name"));
        c.setCredit(rs.getDouble("credit"));
        c.setTeacherId(rs.getInt("teacher_id"));
        c.setTeacherName(rs.getString("teacher_name"));
        return c;
    }

    public List<Course> listAll() {
        List<Course> list = new ArrayList<Course>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(BASE_SQL + "ORDER BY c.cid");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("查询课程失败", e);
        }
        return list;
    }

    /** teacherId 传 0 表示查全部（管理员/学生用） */
    public List<Course> listByTeacher(int teacherId) {
        List<Course> list = new ArrayList<Course>();
        String sql = BASE_SQL + "WHERE c.teacher_id = ? OR ? = 0 ORDER BY c.cid";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setInt(2, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("查询课程失败", e);
        }
        return list;
    }

    public Course findById(int cid) {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(BASE_SQL + "WHERE c.cid = ?")) {
            ps.setInt(1, cid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("查询课程失败", e);
        }
    }

    public boolean insert(Course c) {
        String sql = "INSERT INTO course (course_name, credit, teacher_id) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setDouble(2, c.getCredit());
            ps.setInt(3, c.getTeacherId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("新增课程失败：" + e.getMessage(), e);
        }
    }

    public boolean update(Course c) {
        String sql = "UPDATE course SET course_name = ?, credit = ?, teacher_id = ? WHERE cid = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setDouble(2, c.getCredit());
            ps.setInt(3, c.getTeacherId());
            ps.setInt(4, c.getCid());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("修改课程失败：" + e.getMessage(), e);
        }
    }

    public boolean delete(int cid) {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM course WHERE cid = ?")) {
            ps.setInt(1, cid);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除课程失败", e);
        }
    }

    public int count() {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM course");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException("统计课程失败", e);
        }
    }
}
