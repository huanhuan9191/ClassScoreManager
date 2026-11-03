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
 * 管理员 - 用户管理（教师 / 学生 / 管理员的增删改、重置密码）
 */
@WebServlet("/admin/userServlet")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int role = TeacherScoreServlet.parseInt(request.getParameter("role"), 1);
        request.setAttribute("role", role);
        request.setAttribute("users", userDAO.listByRole(role));
        request.setAttribute("classes", classDAO.listAll());
        request.getRequestDispatcher("/pages/admin/userManage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String msg;

        if ("delete".equals(action)) {
            userDAO.delete(TeacherScoreServlet.parseInt(request.getParameter("uid"), 0));
            msg = "del";
        } else if ("reset".equals(action)) {
            userDAO.updatePassword(TeacherScoreServlet.parseInt(request.getParameter("uid"), 0),
                    MD5Util.md5("123456"));
            msg = "reset";
        } else {
            String username = nvl(request.getParameter("username"));
            User u = new User();
            u.setUsername(username);
            u.setRealName(nvl(request.getParameter("realName")));
            u.setRole(TeacherScoreServlet.parseInt(request.getParameter("role"), 2));
            u.setStudentNo(nvl(request.getParameter("studentNo")));
            u.setPhone(nvl(request.getParameter("phone")));
            u.setClassId(TeacherScoreServlet.parseInt(request.getParameter("classId"), 0));

            String pwd = nvl(request.getParameter("password"));
            u.setPassword(MD5Util.md5(pwd.isEmpty() ? "123456" : pwd));
            userDAO.insert(u);
            msg = "add";
        }

        String role = request.getParameter("role");
        response.sendRedirect(request.getContextPath() + "/admin/userServlet?msg=" + msg
                + (role == null ? "" : "&role=" + role));
    }

    private String nvl(String s) {
        return s == null ? "" : s.trim();
    }
}
