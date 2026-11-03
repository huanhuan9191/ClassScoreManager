<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 · 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="auth-wrap">
    <span class="blob b1"></span><span class="blob b3"></span><span class="blob b4"></span>
    <span class="float-emoji e1">📖</span>
    <span class="float-emoji e2">🌸</span>
    <span class="float-emoji e4">🎓</span>
    <div class="auth-card">
        <h2>班级学生成绩管理系统</h2>
        <p class="sub">教师 / 学生 / 管理员 统一登录入口</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${param.msg == 'timeout'}">
            <div class="alert alert-info">登录已过期，请重新登录</div>
        </c:if>
        <c:if test="${param.msg == 'logout'}">
            <div class="alert alert-info">你已安全退出登录</div>
        </c:if>

        <form action="${ctx}/loginServlet" method="post">
            <div class="field">
                <label>登录账号</label>
                <input class="input" type="text" name="username" value="${username}" placeholder="请输入账号" required>
            </div>
            <div class="field">
                <label>登录密码</label>
                <input class="input" type="password" name="password" placeholder="请输入密码" required>
            </div>
            <button class="btn btn-primary" type="submit">登 录</button>
        </form>

        <div class="auth-links">
            还没有账号？<a href="${ctx}/registerServlet">学生注册</a>
            &nbsp;|&nbsp;<a href="${ctx}/index.jsp">返回首页</a>
        </div>

        <div class="demo-box">
            <b>演示账号（密码均为 123456）</b><br>
            管理员：<code>admin</code>　教师：<code>teacher1</code>　学生：<code>stu230101</code>
        </div>
    </div>
</div>
</body>
</html>
