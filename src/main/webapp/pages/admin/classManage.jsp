<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="navTitle" value="班级管理"/>
<c:set var="navActive" value="cls"/>
<%@ include file="/pages/common/header.jspf" %>

<c:if test="${param.msg == 'add'}"><div class="alert alert-success">班级已新增</div></c:if>
<c:if test="${param.msg == 'edit'}"><div class="alert alert-success">班级信息已更新</div></c:if>
<c:if test="${param.msg == 'del'}"><div class="alert alert-success">班级已删除（该班学生需另行调整）</div></c:if>

<div class="card">
    <div class="card-title">新增 / 修改班级<span class="sub">点击右侧「编辑」会把该行数据填入下方表单</span></div>
    <form class="form-row" action="${ctx}/admin/classServlet" method="post">
        <input type="hidden" name="clsId" id="clsId" value="0">
        <div class="field">
            <label>班级名称 *</label>
            <input class="input" type="text" id="clsName" name="clsName" placeholder="如 计算机2303班" required>
        </div>
        <div class="field">
            <label>年级</label>
            <input class="input" type="text" id="grade" name="grade" placeholder="如 2023级">
        </div>
        <div class="field">
            <label>班主任</label>
            <select class="select" id="teacherId" name="teacherId">
                <option value="0">-- 暂不指定 --</option>
                <c:forEach var="t" items="${teachers}">
                    <option value="${t.uid}">${t.realName}</option>
                </c:forEach>
            </select>
        </div>
        <button class="btn btn-primary" type="submit">保存班级</button>
        <button class="btn btn-ghost" type="button" onclick="resetForm()">清空</button>
    </form>
</div>

<div class="card">
    <div class="card-title">班级列表</div>
    <table class="table">
        <thead>
        <tr>
            <th style="width:70px">编号</th>
            <th>班级名称</th>
            <th style="width:120px">年级</th>
            <th style="width:140px">班主任</th>
            <th style="width:110px">学生人数</th>
            <th style="width:170px">操作</th>
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
                <td>
                    <button class="btn btn-sm btn-ghost"
                            data-id="${cls.clsId}" data-name="${cls.clsName}"
                            data-grade="${cls.grade}" data-teacher="${cls.teacherId}"
                            onclick="editRow(this)">编辑</button>
                    <form class="inline-form" action="${ctx}/admin/classServlet" method="post"
                          onsubmit="return confirm('确定删除该班级吗？');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="clsId" value="${cls.clsId}">
                        <button class="btn btn-sm btn-danger" type="submit">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty classes}">
            <tr><td colspan="6" class="empty">暂无班级数据</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<script>
    function editRow(btn) {
        document.getElementById('clsId').value = btn.dataset.id;
        document.getElementById('clsName').value = btn.dataset.name;
        document.getElementById('grade').value = btn.dataset.grade;
        document.getElementById('teacherId').value = btn.dataset.teacher;
        window.scrollTo({top: 0, behavior: 'smooth'});
    }
    function resetForm() {
        document.getElementById('clsId').value = 0;
        document.getElementById('clsName').value = '';
        document.getElementById('grade').value = '';
        document.getElementById('teacherId').value = 0;
    }
</script>

<%@ include file="/pages/common/footer.jspf" %>
