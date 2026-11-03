package com.score.servlet;

import com.score.dao.ClassDAO;
import com.score.dao.CourseDAO;
import com.score.dao.ScoreDAO;
import com.score.entity.Clazz;
import com.score.entity.Course;
import com.score.entity.StatResult;
import com.score.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 教师 - 成绩统计：平均分 / 最高分 / 最低分 / 及格率 / 优秀率 / 分数段分布 / 排名
 */
@WebServlet("/teacher/statServlet")
public class TeacherStatServlet extends HttpServlet {

    private final ClassDAO classDAO = new ClassDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final ScoreDAO scoreDAO = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User teacher = (User) request.getSession().getAttribute("loginUser");

        List<Clazz> classes = classDAO.listAll();
        List<Course> courses = courseDAO.listByTeacher(teacher.getUid());
        if (courses.isEmpty()) {
            courses = courseDAO.listAll();
        }

        int classId = TeacherScoreServlet.parseInt(request.getParameter("classId"),
                classes.isEmpty() ? 0 : classes.get(0).getClsId());
        int courseId = TeacherScoreServlet.parseInt(request.getParameter("courseId"),
                courses.isEmpty() ? 0 : courses.get(0).getCid());
        String examType = request.getParameter("examType");
        if (examType == null || examType.trim().isEmpty()) {
            examType = "期末";
        }

        String className = "";
        for (Clazz c : classes) {
            if (c.getClsId() == classId) {
                className = c.getClsName();
            }
        }
        String courseName = "";
        for (Course c : courses) {
            if (c.getCid() == courseId) {
                courseName = c.getCourseName();
            }
        }

        StatResult stat = scoreDAO.statByClassCourse(classId, courseId, examType, className, courseName);

        request.setAttribute("classes", classes);
        request.setAttribute("courses", courses);
        request.setAttribute("stat", stat);
        request.setAttribute("classId", classId);
        request.setAttribute("courseId", courseId);
        request.setAttribute("examType", examType);
        request.getRequestDispatcher("/pages/teacher/statistics.jsp").forward(request, response);
    }
}
