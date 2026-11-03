package com.score.servlet;

import com.score.dao.ClassDAO;
import com.score.dao.CourseDAO;
import com.score.dao.ScoreDAO;
import com.score.dao.UserDAO;
import com.score.entity.Clazz;
import com.score.entity.Course;
import com.score.entity.Score;
import com.score.entity.ScoreRow;
import com.score.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教师 - 成绩录入页：选班级 + 选课程 + 选考试类型，列出该班学生及已有成绩
 */
@WebServlet("/teacher/scoreInputServlet")
public class TeacherScoreServlet extends HttpServlet {

    private final ClassDAO classDAO = new ClassDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final UserDAO userDAO = new UserDAO();
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

        int classId = parseInt(request.getParameter("classId"), classes.isEmpty() ? 0 : classes.get(0).getClsId());
        int courseId = parseInt(request.getParameter("courseId"), courses.isEmpty() ? 0 : courses.get(0).getCid());
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

        // 学生 + 已录入分数
        Map<Integer, Double> existing = new HashMap<Integer, Double>();
        if (classId > 0 && courseId > 0) {
            for (Score s : scoreDAO.listByClassCourse(classId, courseId, examType)) {
                existing.put(s.getStudentId(), s.getScore());
            }
        }
        List<ScoreRow> rows = new ArrayList<ScoreRow>();
        if (classId > 0) {
            for (User stu : userDAO.listStudentsByClass(classId)) {
                ScoreRow row = new ScoreRow();
                row.setStudentId(stu.getUid());
                row.setStudentNo(stu.getStudentNo());
                row.setStudentName(stu.getRealName());
                row.setClassName(stu.getClassName());
                row.setScoreValue(existing.get(stu.getUid()));
                rows.add(row);
            }
        }

        request.setAttribute("classes", classes);
        request.setAttribute("courses", courses);
        request.setAttribute("rows", rows);
        request.setAttribute("classId", classId);
        request.setAttribute("courseId", courseId);
        request.setAttribute("className", className);
        request.setAttribute("courseName", courseName);
        request.setAttribute("examType", examType);
        request.getRequestDispatcher("/pages/teacher/scoreInput.jsp").forward(request, response);
    }

    static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}
