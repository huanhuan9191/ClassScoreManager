package com.score.entity;

/**
 * 用户实体：管理员(0) / 教师(1) / 学生(2)
 */
public class User {

    private int uid;
    private String username;
    private String password;
    private String realName;
    private int role;
    private String studentNo;
    private int classId;
    private String phone;
    private String createTime;
    /** 关联查询字段：班级名称 */
    private String className;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /** 角色中文名，供页面直接使用 */
    public String getRoleName() {
        if (role == 0) {
            return "管理员";
        }
        if (role == 1) {
            return "教师";
        }
        return "学生";
    }
}
