package com.score.servlet;

import com.score.dao.UserDAO;
import com.score.entity.User;
import com.score.util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 登录：按数据库中的角色自动路由到教师台 / 学生台 / 管理员后台
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            request.setAttribute("error", "账号和密码不能为空");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.login(username.trim(), MD5Util.md5(password));
        if (user == null) {
            request.setAttribute("error", "账号或密码错误，请重新输入");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);
        session.setMaxInactiveInterval(30 * 60);

        String ctx = request.getContextPath();
        if (user.getRole() == 0) {
            response.sendRedirect(ctx + "/admin/dashboardServlet");
        } else if (user.getRole() == 1) {
            response.sendRedirect(ctx + "/teacher/scoreInputServlet");
        } else {
            response.sendRedirect(ctx + "/student/myScoreServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
