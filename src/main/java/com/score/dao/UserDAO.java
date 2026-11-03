package com.score.dao;

import com.score.entity.User;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问层
 */
public class UserDAO {

    private static final String BASE_SQL =
            "SELECT u.*, IFNULL(c.cls_name,'') AS cls_name "
            + "FROM user u LEFT JOIN class c ON u.class_id = c.cls_id ";

    private User map(ResultSet rs) throws Exception {
        User u = new User();
        u.setUid(rs.getInt("uid"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRealName(rs.getString("real_name"));
        u.setRole(rs.getInt("role"));
        u.setStudentNo(rs.getString("student_no"));
        u.setClassId(rs.getInt("class_id"));
        u.setPhone(rs.getString("phone"));
        u.setCreateTime(String.valueOf(rs.getTimestamp("create_time")));
        u.setClassName(rs.getString("cls_name"));
        return u;
    }

    /** 登录校验：密码传入时已是 MD5 */
    public User login(String username, String md5Password) {
        String sql = BASE_SQL + "WHERE u.username = ? AND u.password = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, md5Password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("登录查询失败", e);
        }
    }

    public User findById(int uid) {
        String sql = BASE_SQL + "WHERE u.uid = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("查询用户失败", e);
        }
    }

    public boolean existsUsername(String username) {
        String sql = "SELECT 1 FROM user WHERE username = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("查询账号失败", e);
        }
    }

    public boolean insert(User u) {
        String sql = "INSERT INTO user (username, password, real_name, role, student_no, class_id, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRealName());
            ps.setInt(4, u.getRole());
            ps.setString(5, u.getStudentNo() == null ? "" : u.getStudentNo());
            ps.setInt(6, u.getClassId());
            ps.setString(7, u.getPhone() == null ? "" : u.getPhone());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("新增用户失败：" + e.getMessage(), e);
        }
    }

    public boolean update(User u) {
        String sql = "UPDATE user SET real_name = ?, phone = ?, class_id = ?, student_no = ? WHERE uid = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getRealName());
            ps.setString(2, u.getPhone());
            ps.setInt(3, u.getClassId());
            ps.setString(4, u.getStudentNo());
            ps.setInt(5, u.getUid());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("修改用户失败：" + e.getMessage(), e);
        }
    }

    public boolean updatePassword(int uid, String md5Password) {
        String sql = "UPDATE user SET password = ? WHERE uid = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, md5Password);
            ps.setInt(2, uid);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("重置密码失败", e);
        }
    }

    public boolean delete(int uid) {
        String sql = "DELETE FROM user WHERE uid = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除用户失败", e);
        }
    }

    public List<User> listByRole(int role) {
        String sql = BASE_SQL + "WHERE u.role = ? ORDER BY u.class_id, u.student_no, u.uid";
        List<User> list = new ArrayList<User>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("查询用户列表失败", e);
        }
        return list;
    }

    public List<User> listStudentsByClass(int classId) {
        String sql = BASE_SQL + "WHERE u.role = 2 AND u.class_id = ? ORDER BY u.student_no";
        List<User> list = new ArrayList<User>();
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("查询班级学生失败", e);
        }
        return list;
    }

    public int countByRole(int role) {
        String sql = "SELECT COUNT(*) FROM user WHERE role = ?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("统计用户失败", e);
        }
    }
}
