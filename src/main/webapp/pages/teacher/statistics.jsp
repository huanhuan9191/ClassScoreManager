<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="navTitle" value="成绩统计"/>
<c:set var="navActive" value="stat"/>
<%@ include file="/pages/common/header.jspf" %>

<!-- 筛选 -->
<div class="card">
    <div class="card-title">统计范围</div>
    <form class="form-row" action="${ctx}/teacher/statServlet" method="get">
        <div class="field">
            <label>班级</label>
            <select class="select" name="classId">
                <c:forEach var="cls" items="${classes}">
                    <option value="${cls.clsId}" ${cls.clsId == classId ? 'selected' : ''}>${cls.clsName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label>课程</label>
            <select class="select" name="courseId">
                <c:forEach var="co" items="${courses}">
                    <option value="${co.cid}" ${co.cid == courseId ? 'selected' : ''}>${co.courseName}</option>
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
        <button class="btn btn-primary" type="submit">开始统计</button>
        <a class="btn btn-ghost" href="${ctx}/teacher/scoreInputServlet?classId=${classId}&courseId=${courseId}&examType=${examType}">去录入该范围成绩 →</a>
    </form>
</div>

<!-- 统计卡片 -->
<div class="stat-grid">
    <div class="stat-card">
        <div class="label">参考人数</div>
        <div class="value">${stat.total}<small> 人</small></div>
    </div>
    <div class="stat-card purple">
        <div class="label">平均分</div>
        <div class="value">${stat.avg}<small> 分</small></div>
    </div>
    <div class="stat-card green">
        <div class="label">及格率（≥60）</div>
        <div class="value">${stat.passRate}<small> %</small></div>
    </div>
    <div class="stat-card orange">
        <div class="label">优秀率（≥90）</div>
        <div class="value">${stat.excellentRate}<small> %</small></div>
    </div>
    <div class="stat-card">
        <div class="label">最高分</div>
        <div class="value">${stat.max}<small> 分</small></div>
    </div>
    <div class="stat-card red">
        <div class="label">最低分</div>
        <div class="value">${stat.min}<small> 分</small></div>
    </div>
</div>

<c:choose>
    <c:when test="${stat.total == 0}">
        <div class="card">
            <div class="empty">该范围内还没有成绩记录，请先到「成绩录入」录入后再统计</div>
        </div>
    </c:when>
    <c:otherwise>

        <!-- 分数段分布（纯 CSS 柱状图，不依赖任何外部图表库） -->
        <div class="card">
            <div class="card-title">
                分数段分布
                <span class="sub">${stat.className} · ${stat.courseName} · ${examType}</span>
            </div>
            <c:set var="labels" value="${stat.distLabels}"/>
            <c:set var="percents" value="${stat.barPercents}"/>
            <div class="chart">
                <c:forEach var="d" items="${stat.distribution}" varStatus="s">
                    <div class="chart-col">
                        <div class="chart-bar-wrap">
                            <div class="chart-bar" style="height:${percents[s.index]}%"></div>
                        </div>
                        <div class="chart-val">${d} 人</div>
                        <div class="chart-label">${labels[s.index]}</div>
                    </div>
                </c:forEach>
            </div>
            <div class="muted" style="margin-top:14px;">
                及格 ${stat.passCount} 人 / 共 ${stat.total} 人，优秀 ${stat.excellentCount} 人。
            </div>
        </div>

        <!-- 排名表 -->
        <div class="card">
            <div class="card-title">成绩排名<span class="sub">按分数从高到低</span></div>
            <table class="table">
                <thead>
                <tr>
                    <th style="width:70px">名次</th>
                    <th style="width:150px">学号</th>
                    <th style="width:120px">姓名</th>
                    <th style="width:110px">分数</th>
                    <th style="width:110px">等级</th>
                    <th>是否及格</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${stat.ranking}" varStatus="st">
                    <tr>
                        <td class="num"><b>${st.count}</b></td>
                        <td>${s.studentNo}</td>
                        <td>${s.studentName}</td>
                        <td class="num"><b>${s.score}</b></td>
                        <td><span class="badge badge-blue">${s.level}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${s.passed}"><span class="badge badge-pass">及格</span></c:when>
                                <c:otherwise><span class="badge badge-fail">不及格</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/pages/common/footer.jspf" %>
