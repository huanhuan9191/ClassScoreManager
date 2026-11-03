package com.score.dao;

import com.score.entity.Clazz;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 班级数据访问层
 */
public class ClassDAO {

    private static final String BASE_SQL =
            "SELECT c.cls_id, c.cls_name, c.grade, c.teacher_id, "
            + "IFNULL(t.real_name,'未分配') AS teacher_name, "
            + "(SELECT COUNT(*) FROM user u WHERE u.class_id = c.cls_id AND u.role = 2) AS student_count "
            + "FROM class c LEFT JOIN user t ON c.teacher_id = t.uid ";

    private Clazz map(ResultSet rs) throws Exception {
        Clazz c = new Clazz();
        c.setClsId(rs.getInt("cls_id"));
        c.setClsName(rs.getString("cls_name"));
        c.setGrade(rs.getString("grade"));
        c.setTeacherId(rs.getInt("teacher_id"));
        c.setTeacherName(rs.getString("teacher_name"));
        c.setStudentCount(rs.getInt("student_count"));
        return c;
    }

    public List<Clazz> listAll() {
        List<Clazz> list = new ArrayList<Clazz>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(BASE_SQL + "ORDER BY c.cls_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("查询班级失败", e);
        }
        return list;
    }

    public List<Clazz> listByTeacher(int teacherId) {
        List<Clazz> list = new ArrayList<Clazz>();
        String sql = BASE_SQL + "WHERE c.teacher_id = ? OR ? = 0 ORDER BY c.cls_id";
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
            throw new RuntimeException("查询班级失败", e);
        }
        return list;
    }

    public Clazz findById(int clsId) {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(BASE_SQL + "WHERE c.cls_id = ?")) {
            ps.setInt(1, clsId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("查询班级失败", e);
        }
    }

    public boolean insert(Clazz c) {
        String sql = "INSERT INTO class (cls_name, grade, teacher_id) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getClsName());
            ps.setString(2, c.getGrade());
            ps.setInt(3, c.getTeacherId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("新增班级失败：" + e.getMessage(), e);
        }
    }

    public boolean update(Clazz c) {
        String sql = "UPDATE class SET cls_name = ?, grade = ?, teacher_id = ? WHERE cls_id = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getClsName());
            ps.setString(2, c.getGrade());
            ps.setInt(3, c.getTeacherId());
            ps.setInt(4, c.getClsId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("修改班级失败：" + e.getMessage(), e);
        }
    }

    public boolean delete(int clsId) {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM class WHERE cls_id = ?")) {
            ps.setInt(1, clsId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除班级失败", e);
        }
    }

    public int count() {
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM class");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException("统计班级失败", e);
        }
    }
}
