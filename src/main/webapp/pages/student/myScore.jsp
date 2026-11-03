<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="navTitle" value="我的成绩"/>
<c:set var="navActive" value="my"/>
<%@ include file="/pages/common/header.jspf" %>

<!-- 个人信息 -->
<div class="card">
    <div class="card-title">个人信息</div>
    <div class="form-row">
        <div class="field"><label>姓名</label><div><b>${loginUser.realName}</b></div></div>
        <div class="field"><label>学号</label><div>${loginUser.studentNo}</div></div>
        <div class="field"><label>班级</label><div>${loginUser.className}</div></div>
        <div class="field"><label>账号</label><div class="muted">${loginUser.username}</div></div>
    </div>
</div>

<!-- 个人统计 -->
<div class="stat-grid">
    <div class="stat-card purple">
        <div class="label">平均分</div>
        <div class="value">${avgScore}<small> 分</small></div>
    </div>
    <div class="stat-card green">
        <div class="label">及格率</div>
        <div class="value">${passRate}<small> %</small></div>
    </div>
    <div class="stat-card orange">
        <div class="label">班级排名</div>
        <div class="value">
            <c:choose>
                <c:when test="${rank > 0}">${rank}<small> / ${classSize}</small></c:when>
                <c:otherwise>--</c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="stat-card">
        <div class="label">学分绩点（加权均分）</div>
        <div class="value">${gpa}</div>
    </div>
</div>

<!-- 成绩明细 -->
<div class="card">
    <div class="card-title">
        各科成绩
        <span class="sub">共 ${subjectCount} 门课程，及格 ${passCount} 门</span>
    </div>

    <c:choose>
        <c:when test="${empty scores}">
            <div class="empty">老师还没有录入你的成绩，请稍后再来查看</div>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                <tr>
                    <th style="width:170px">课程</th>
                    <th style="width:80px">学分</th>
                    <th style="width:90px">分数</th>
                    <th style="width:100px">等级</th>
                    <th style="width:110px">本班平均</th>
                    <th style="width:130px">与班级对比</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${scores}" varStatus="st">
                    <c:set var="cavg" value="${classAvgList[st.index]}"/>
                    <tr>
                        <td>${s.courseName}</td>
                        <td class="num">${s.credit}</td>
                        <td class="num"><b>${s.score}</b></td>
                        <td><span class="badge badge-blue">${s.level}</span></td>
                        <td class="num muted">${cavg}</td>
                        <td>
                            <c:choose>
                                <c:when test="${s.score - cavg >= 0}">
                                    <span class="badge badge-pass">+<fmt:formatNumber value="${s.score - cavg}" pattern="0.0"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-fail"><fmt:formatNumber value="${s.score - cavg}" pattern="0.0"/></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
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
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${subjectCount > 0}">
    <div class="card">
        <div class="card-title">学习提示</div>
        <div class="alert alert-info" style="margin-bottom:10px;">
            <c:choose>
                <c:when test="${passRate >= 100}">全部课程均已及格，继续保持！</c:when>
                <c:when test="${passRate >= 80}">整体表现不错，仍有 ${subjectCount - passCount} 门课程需要补强。</c:when>
                <c:otherwise>有 ${subjectCount - passCount} 门课程未及格，建议尽快联系任课教师。</c:otherwise>
            </c:choose>
        </div>
        <div class="muted">及格率 = 及格科目数 ÷ 总科目数；班级排名按所有科目平均分降序计算。</div>
    </div>
</c:if>

<%@ include file="/pages/common/footer.jspf" %>
