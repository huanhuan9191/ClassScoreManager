package com.score.servlet;

import com.score.dao.ClassDAO;
import com.score.dao.CourseDAO;
import com.score.dao.ScoreDAO;
import com.score.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员 - 控制台总览
 */
@WebServlet("/admin/dashboardServlet")
public class AdminDashboardServlet extends HttpServlet {

    private final ClassDAO classDAO = new ClassDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ScoreDAO scoreDAO = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("classCount", classDAO.count());
        request.setAttribute("teacherCount", userDAO.countByRole(1));
        request.setAttribute("studentCount", userDAO.countByRole(2));
        request.setAttribute("courseCount", courseDAO.count());
        request.setAttribute("scoreCount", scoreDAO.countAll());
        request.setAttribute("passRate", scoreDAO.globalPassRate());
        request.setAttribute("classes", classDAO.listAll());
        request.setAttribute("recentScores", scoreDAO.listRecent(10));
        request.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(request, response);
    }
}
