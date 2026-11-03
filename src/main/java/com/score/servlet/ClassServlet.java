package com.score.servlet;

import com.score.dao.ClassDAO;
import com.score.dao.UserDAO;
import com.score.entity.Clazz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员 - 班级管理（增 / 改 / 删 / 查）
 */
@WebServlet("/admin/classServlet")
public class ClassServlet extends HttpServlet {

    private final ClassDAO classDAO = new ClassDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("classes", classDAO.listAll());
        request.setAttribute("teachers", userDAO.listByRole(1));
        request.getRequestDispatcher("/pages/admin/classManage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String msg;

        if ("delete".equals(action)) {
            int clsId = TeacherScoreServlet.parseInt(request.getParameter("clsId"), 0);
            classDAO.delete(clsId);
            msg = "del";
        } else {
            Clazz c = new Clazz();
            c.setClsId(TeacherScoreServlet.parseInt(request.getParameter("clsId"), 0));
            c.setClsName(nvl(request.getParameter("clsName")));
            c.setGrade(nvl(request.getParameter("grade")));
            c.setTeacherId(TeacherScoreServlet.parseInt(request.getParameter("teacherId"), 0));
            if (c.getClsId() == 0) {
                classDAO.insert(c);
                msg = "add";
            } else {
                classDAO.update(c);
                msg = "edit";
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/classServlet?msg=" + msg);
    }

    private String nvl(String s) {
        return s == null ? "" : s.trim();
    }
}
