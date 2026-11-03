package com.score.servlet;

import com.score.dao.ClassDAO;
import com.score.dao.UserDAO;
import com.score.entity.User;
import com.score.util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 学生自助注册（教师与管理员账号由管理员后台创建）
 */
@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));
        String realName = trim(request.getParameter("realName"));
        String studentNo = trim(request.getParameter("studentNo"));
        String classIdStr = trim(request.getParameter("classId"));

        if (username.isEmpty() || password.isEmpty() || realName.isEmpty()) {
            fail(request, response, "账号、密码、姓名均为必填项");
            return;
        }
        if (userDAO.existsUsername(username)) {
            fail(request, response, "该账号已被注册，请换一个");
            return;
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(MD5Util.md5(password));
        u.setRealName(realName);
        u.setRole(2);
        u.setStudentNo(studentNo);
        try {
            u.setClassId(Integer.parseInt(classIdStr));
        } catch (Exception e) {
            u.setClassId(0);
        }
        userDAO.insert(u);

        request.setAttribute("success", "注册成功，请使用新账号登录");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void fail(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        request.setAttribute("error", msg);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("classes", classDAO.listAll());
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
