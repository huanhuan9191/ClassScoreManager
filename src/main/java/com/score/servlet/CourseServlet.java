package com.score.servlet;

import com.score.dao.CourseDAO;
import com.score.dao.UserDAO;
import com.score.entity.Course;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员 - 课程管理（增 / 改 / 删 / 查）
 */
@WebServlet("/admin/courseServlet")
public class CourseServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("courses", courseDAO.listAll());
        request.setAttribute("teachers", userDAO.listByRole(1));
        request.getRequestDispatcher("/pages/admin/courseManage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String msg;

        if ("delete".equals(action)) {
            courseDAO.delete(TeacherScoreServlet.parseInt(request.getParameter("cid"), 0));
            msg = "del";
        } else {
            Course c = new Course();
            c.setCid(TeacherScoreServlet.parseInt(request.getParameter("cid"), 0));
            c.setCourseName(nvl(request.getParameter("courseName")));
            try {
                c.setCredit(Double.parseDouble(nvl(request.getParameter("credit"))));
            } catch (Exception e) {
                c.setCredit(3.0);
            }
            c.setTeacherId(TeacherScoreServlet.parseInt(request.getParameter("teacherId"), 0));
            if (c.getCid() == 0) {
                courseDAO.insert(c);
                msg = "add";
            } else {
                courseDAO.update(c);
                msg = "edit";
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/courseServlet?msg=" + msg);
    }

    private String nvl(String s) {
        return s == null ? "" : s.trim();
    }
}
