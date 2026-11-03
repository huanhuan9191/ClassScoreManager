<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="navTitle" value="成绩录入"/>
<c:set var="navActive" value="input"/>
<%@ include file="/pages/common/header.jspf" %>

<!-- 筛选条件 -->
<div class="card">
    <div class="card-title">选择录入范围</div>
    <form class="form-row" action="${ctx}/teacher/scoreInputServlet" method="get">
        <div class="field">
            <label>班级</label>
            <select class="select" name="classId">
                <c:forEach var="cls" items="${classes}">
                    <option value="${cls.clsId}" ${cls.clsId == classId ? 'selected' : ''}>
                        ${cls.clsName}（${cls.studentCount} 人）
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label>课程</label>
            <select class="select" name="courseId">
                <c:forEach var="co" items="${courses}">
                    <option value="${co.cid}" ${co.cid == courseId ? 'selected' : ''}>
                        ${co.courseName}（${co.credit} 学分）
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label>考试类型</label>
            <select class="select" name="examType">
                <option value="期末" ${examType == '期末' ? 'selected' : ''}>期末考试</option>
                <option value="期中" ${examType == '期中' ? 'selected' : ''}>期中考试</option>
                <option value="补考" ${examType == '补考' ? 'selected' : ''}>补考</option>
            </select>
        </div>
        <button class="btn btn-primary" type="submit">查询 / 切换</button>
        <a class="btn btn-ghost" href="${ctx}/teacher/statServlet?classId=${classId}&courseId=${courseId}&examType=${examType}">查看该范围统计 →</a>
    </form>
</div>

<c:if test="${not empty param.saved}">
    <div class="alert alert-success">
        保存成功：共提交 <b>${param.saved}</b> 条成绩<c:if test="${param.skipped > 0}">，跳过 <b>${param.skipped}</b> 条（空值或不在 0~100 范围内）</c:if>。
    </div>
</c:if>

<!-- 录入表格 -->
<div class="card">
    <div class="card-title">
        ${className} · ${courseName} · ${examType}
        <span class="sub">分数范围 0 ~ 100，留空表示暂不录入；已存在的成绩会回填，保存后直接覆盖</span>
    </div>

    <c:choose>
        <c:when test="${empty rows}">
            <div class="empty">该班级暂无学生，或尚未分配班级</div>
        </c:when>
        <c:otherwise>
            <form action="${ctx}/teacher/scoreSaveServlet" method="post">
                <input type="hidden" name="classId" value="${classId}">
                <input type="hidden" name="courseId" value="${courseId}">
                <input type="hidden" name="examType" value="${examType}">

                <table class="table">
                    <thead>
                    <tr>
                        <th style="width:60px">序号</th>
                        <th style="width:150px">学号</th>
                        <th style="width:120px">姓名</th>
                        <th style="width:140px">分数</th>
                        <th style="width:120px">状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="r" items="${rows}" varStatus="st">
                        <tr>
                            <td class="num">${st.count}</td>
                            <td>${r.studentNo}</td>
                            <td>${r.studentName}</td>
                            <td>
                                <input type="hidden" name="studentId" value="${r.studentId}">
                                <input class="input input-score" type="number" step="0.5" min="0" max="100"
                                       name="scoreValue" value="${r.scoreValue}" placeholder="--">
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty r.scoreValue}">
                                        <span class="badge badge-grey">未录入</span>
                                    </c:when>
                                    <c:when test="${r.scoreValue >= 60}">
                                        <span class="badge badge-pass">已及格</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-fail">不及格</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="mt8" style="margin-top:18px;display:flex;gap:12px;align-items:center;">
                    <button class="btn btn-primary" type="submit">批量保存成绩</button>
                    <span class="muted">共 ${rows.size()} 名学生</span>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/pages/common/footer.jspf" %>
