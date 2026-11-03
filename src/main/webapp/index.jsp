<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="hero">
    <span class="blob b1"></span><span class="blob b2"></span><span class="blob b3"></span><span class="blob b4"></span>
    <span class="float-emoji e1">📚</span>
    <span class="float-emoji e2">✏️</span>
    <span class="float-emoji e3">🎓</span>
    <span class="float-emoji e4">🌸</span>
    <h1>班级学生成绩管理系统</h1>
    <p class="lead">
        面向教师与学生的成绩管理平台 —— 教师在线批量录入成绩，系统自动统计平均分、及格率、优秀率与分数段分布；学生随时查询个人成绩、班级排名与班级平均水平。
    </p>
    <div class="btns">
        <a class="btn btn-primary" href="${ctx}/login.jsp">立即登录</a>
        <a class="btn btn-ghost" href="${ctx}/registerServlet">学生注册</a>
    </div>

    <div class="feature-grid">
        <div class="feature">
            <h3>📝 教师录入</h3>
            <p>选择班级与课程，整班成绩一次提交，已录入的分数自动回填、可随时修改。</p>
        </div>
        <div class="feature">
            <h3>📊 统计分析</h3>
            <p>平均分、最高最低分、及格率、优秀率、五档分数段分布，一屏看清班级学情。</p>
        </div>
        <div class="feature">
            <h3>🎓 学生查询</h3>
            <p>个人各科成绩、平均分、及格率、班级排名，并与班级平均分逐科对比。</p>
        </div>
        <div class="feature">
            <h3>🛠 管理员后台</h3>
            <p>班级、课程、教师与学生账号统一维护，重置密码、数据总览一站完成。</p>
        </div>
    </div>

    <div class="demo-box" style="margin-top:34px;background:rgba(255,255,255,.1);color:#b9c9e5;">
        演示账号：管理员 <b style="color:#fff">admin / 123456</b> ·
        教师 <b style="color:#fff">teacher1 / 123456</b> ·
        学生 <b style="color:#fff">stu230101 / 123456</b>
    </div>
</div>
</body>
</html>
