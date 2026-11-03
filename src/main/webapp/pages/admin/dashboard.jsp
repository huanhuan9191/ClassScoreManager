<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="navTitle" value="控制台"/>
<c:set var="navActive" value="dash"/>
<%@ include file="/pages/common/header.jspf" %>

<div class="stat-grid">
    <div class="stat-card">
        <div class="label">班级数量</div>
        <div class="value">${classCount}<small> 个</small></div>
    </div>
    <div class="stat-card purple">
        <div class="label">教师人数</div>
        <div class="value">${teacherCount}<small> 人</small></div>
    </div>
    <div class="stat-card orange">
        <div class="label">学生人数</div>
        <div class="value">${studentCount}<small> 人</small></div>
    </div>
    <div class="stat-card green">
        <div class="label">课程数量</div>
        <div class="value">${courseCount}<small> 门</small></div>
    </div>
    <div class="stat-card red">
        <div class="label">全校及格率</div>
        <div class="value">${passRate}<small> %</small></div>
    </div>
</div>

<div class="card">
    <div class="card-title">班级概览<span class="sub">共 ${scoreCount} 条成绩记录</span></div>
    <c:choose>
        <c:when test="${empty classes}">
            <div class="empty">还没有班级，请到「班级管理」新增</div>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                <tr>
                    <th style="width:70px">编号</th>
                    <th>班级名称</th>
                    <th style="width:130px">年级</th>
                    <th style="width:150px">班主任</th>
                    <th style="width:120px">学生人数</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="cls" items="${classes}">
                    <tr>
                        <td class="num">${cls.clsId}</td>
                        <td><b>${cls.clsName}</b></td>
                        <td>${cls.grade}</td>
                        <td>${cls.teacherName}</td>
                        <td class="num">${cls.studentCount} 人</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<div class="card">
    <div class="card-title">最近录入的成绩<span class="sub">最新 10 条</span></div>
    <c:choose>
        <c:when test="${empty recentScores}">
            <div class="empty">暂无成绩记录</div>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                <tr>
                    <th style="width:130px">学号</th>
                    <th style="width:110px">姓名</th>
                    <th style="width:150px">班级</th>
                    <th>课程</th>
                    <th style="width:90px">分数</th>
                    <th style="width:110px">考试类型</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${recentScores}">
                    <tr>
                        <td>${s.studentNo}</td>
                        <td>${s.studentName}</td>
                        <td>${s.className}</td>
                        <td>${s.courseName}</td>
                        <td class="num">
                            <c:choose>
                                <c:when test="${s.passed}"><b>${s.score}</b></c:when>
                                <c:otherwise><b style="color:#d03050">${s.score}</b></c:otherwise>
                            </c:choose>
                        </td>
                        <td><span class="badge badge-grey">${s.examType}</span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/pages/common/footer.jspf" %>
