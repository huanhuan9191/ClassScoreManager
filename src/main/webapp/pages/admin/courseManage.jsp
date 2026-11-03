<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="navTitle" value="课程管理"/>
<c:set var="navActive" value="course"/>
<%@ include file="/pages/common/header.jspf" %>

<c:if test="${param.msg == 'add'}"><div class="alert alert-success">课程已新增</div></c:if>
<c:if test="${param.msg == 'edit'}"><div class="alert alert-success">课程信息已更新</div></c:if>
<c:if test="${param.msg == 'del'}"><div class="alert alert-success">课程已删除</div></c:if>

<div class="card">
    <div class="card-title">新增 / 修改课程</div>
    <form class="form-row" action="${ctx}/admin/courseServlet" method="post">
        <input type="hidden" name="cid" id="cid" value="0">
        <div class="field">
            <label>课程名称 *</label>
            <input class="input" type="text" id="courseName" name="courseName" placeholder="如 操作系统" required>
        </div>
        <div class="field">
            <label>学分</label>
            <input class="input" type="number" step="0.5" min="0" max="20" id="credit" name="credit" value="3.0">
        </div>
        <div class="field">
            <label>授课教师</label>
            <select class="select" id="teacherId" name="teacherId">
                <option value="0">-- 暂不指定 --</option>
                <c:forEach var="t" items="${teachers}">
                    <option value="${t.uid}">${t.realName}</option>
                </c:forEach>
            </select>
        </div>
        <button class="btn btn-primary" type="submit">保存课程</button>
        <button class="btn btn-ghost" type="button" onclick="resetForm()">清空</button>
    </form>
</div>

<div class="card">
    <div class="card-title">课程列表</div>
    <table class="table">
        <thead>
        <tr>
            <th style="width:70px">编号</th>
            <th>课程名称</th>
            <th style="width:110px">学分</th>
            <th style="width:150px">授课教师</th>
            <th style="width:170px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="co" items="${courses}">
            <tr>
                <td class="num">${co.cid}</td>
                <td><b>${co.courseName}</b></td>
                <td class="num">${co.credit}</td>
                <td>${co.teacherName}</td>
                <td>
                    <button class="btn btn-sm btn-ghost"
                            data-id="${co.cid}" data-name="${co.courseName}"
                            data-credit="${co.credit}" data-teacher="${co.teacherId}"
                            onclick="editRow(this)">编辑</button>
                    <form class="inline-form" action="${ctx}/admin/courseServlet" method="post"
                          onsubmit="return confirm('确定删除该课程吗？相关成绩记录将失去课程信息。');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="cid" value="${co.cid}">
                        <button class="btn btn-sm btn-danger" type="submit">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty courses}">
            <tr><td colspan="5" class="empty">暂无课程数据</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<script>
    function editRow(btn) {
        document.getElementById('cid').value = btn.dataset.id;
        document.getElementById('courseName').value = btn.dataset.name;
        document.getElementById('credit').value = btn.dataset.credit;
        document.getElementById('teacherId').value = btn.dataset.teacher;
        window.scrollTo({top: 0, behavior: 'smooth'});
    }
    function resetForm() {
        document.getElementById('cid').value = 0;
        document.getElementById('courseName').value = '';
        document.getElementById('credit').value = '3.0';
        document.getElementById('teacherId').value = 0;
    }
</script>

<%@ include file="/pages/common/footer.jspf" %>
