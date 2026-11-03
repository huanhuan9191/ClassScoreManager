<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="navTitle" value="用户管理"/>
<c:set var="navActive" value="user"/>
<%@ include file="/pages/common/header.jspf" %>

<c:if test="${param.msg == 'add'}"><div class="alert alert-success">账号已创建，初始密码为提交时填写的密码（留空则默认 123456）</div></c:if>
<c:if test="${param.msg == 'reset'}"><div class="alert alert-success">密码已重置为 123456</div></c:if>
<c:if test="${param.msg == 'del'}"><div class="alert alert-success">账号已删除</div></c:if>

<div class="tabs">
    <a href="${ctx}/admin/userServlet?role=1" class="${role == 1 ? 'on' : ''}">教师</a>
    <a href="${ctx}/admin/userServlet?role=2" class="${role == 2 ? 'on' : ''}">学生</a>
    <a href="${ctx}/admin/userServlet?role=0" class="${role == 0 ? 'on' : ''}">管理员</a>
</div>

<div class="card">
    <div class="card-title">新增账号<span class="sub">学生需要选择班级并填写学号</span></div>
    <form class="form-row" action="${ctx}/admin/userServlet" method="post">
        <div class="field">
            <label>登录账号 *</label>
            <input class="input" type="text" name="username" placeholder="如 teacher4 / stu230111" required>
        </div>
        <div class="field">
            <label>初始密码</label>
            <input class="input" type="text" name="password" placeholder="留空默认 123456">
        </div>
        <div class="field">
            <label>姓名 *</label>
            <input class="input" type="text" name="realName" placeholder="真实姓名" required>
        </div>
        <div class="field">
            <label>角色</label>
            <select class="select" name="role">
                <option value="1" ${role == 1 ? 'selected' : ''}>教师</option>
                <option value="2" ${role == 2 ? 'selected' : ''}>学生</option>
                <option value="0" ${role == 0 ? 'selected' : ''}>管理员</option>
            </select>
        </div>
        <div class="field">
            <label>学号（学生）</label>
            <input class="input" type="text" name="studentNo" placeholder="如 2023010111">
        </div>
        <div class="field">
            <label>班级（学生）</label>
            <select class="select" name="classId">
                <option value="0">-- 不指定 --</option>
                <c:forEach var="cls" items="${classes}">
                    <option value="${cls.clsId}">${cls.clsName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label>手机号</label>
            <input class="input" type="text" name="phone" placeholder="选填">
        </div>
        <button class="btn btn-primary" type="submit">创建账号</button>
    </form>
</div>

<div class="card">
    <div class="card-title">
        <c:choose>
            <c:when test="${role == 1}">教师列表</c:when>
            <c:when test="${role == 2}">学生列表</c:when>
            <c:otherwise>管理员列表</c:otherwise>
        </c:choose>
        <span class="sub">共 ${users.size()} 个账号</span>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th style="width:60px">ID</th>
            <th style="width:140px">账号</th>
            <th style="width:110px">姓名</th>
            <c:if test="${role == 2}">
                <th style="width:140px">学号</th>
                <th style="width:150px">班级</th>
            </c:if>
            <th style="width:130px">手机号</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
            <tr>
                <td class="num">${u.uid}</td>
                <td>${u.username}</td>
                <td><b>${u.realName}</b></td>
                <c:if test="${role == 2}">
                    <td>${u.studentNo}</td>
                    <td>${empty u.className ? '未分班' : u.className}</td>
                </c:if>
                <td class="muted">${u.phone}</td>
                <td>
                    <form class="inline-form" action="${ctx}/admin/userServlet" method="post"
                          onsubmit="return confirm('确定把该账号密码重置为 123456 吗？');">
                        <input type="hidden" name="action" value="reset">
                        <input type="hidden" name="uid" value="${u.uid}">
                        <input type="hidden" name="role" value="${role}">
                        <button class="btn btn-sm btn-ghost" type="submit">重置密码</button>
                    </form>
                    <form class="inline-form" action="${ctx}/admin/userServlet" method="post"
                          onsubmit="return confirm('确定删除该账号吗？');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="uid" value="${u.uid}">
                        <input type="hidden" name="role" value="${role}">
                        <button class="btn btn-sm btn-danger" type="submit">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty users}">
            <tr><td colspan="8" class="empty">暂无账号数据</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/pages/common/footer.jspf" %>
