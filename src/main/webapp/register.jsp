<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生注册 · 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="auth-wrap">
    <span class="blob b1"></span><span class="blob b3"></span>
    <span class="float-emoji e1">📝</span>
    <span class="float-emoji e2">🌸</span>
    <div class="auth-card">
        <h2>学生注册</h2>
        <p class="sub">教师与管理员账号请由管理员在后台创建</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form action="${ctx}/registerServlet" method="post">
            <div class="field">
                <label>登录账号 *</label>
                <input class="input" type="text" name="username" placeholder="如 stu230111" required>
            </div>
            <div class="field">
                <label>登录密码 *</label>
                <input class="input" type="password" name="password" placeholder="请输入密码" required>
            </div>
            <div class="field">
                <label>真实姓名 *</label>
                <input class="input" type="text" name="realName" placeholder="请输入姓名" required>
            </div>
            <div class="field">
                <label>学号</label>
                <input class="input" type="text" name="studentNo" placeholder="如 2023010111">
            </div>
            <div class="field">
                <label>所属班级</label>
                <select class="select" name="classId">
                    <option value="0">-- 请选择班级 --</option>
                    <c:forEach var="cls" items="${classes}">
                        <option value="${cls.clsId}">${cls.clsName}（${cls.grade}）</option>
                    </c:forEach>
                </select>
            </div>
            <button class="btn btn-primary" type="submit">注 册</button>
        </form>

        <div class="auth-links">
            已有账号？<a href="${ctx}/login.jsp">返回登录</a>
        </div>
    </div>
</div>
</body>
</html>
