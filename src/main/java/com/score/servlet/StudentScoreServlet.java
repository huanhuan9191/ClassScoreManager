package com.score.servlet;

import com.score.dao.ScoreDAO;
import com.score.entity.Score;
import com.score.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生 - 我的成绩：各科成绩、平均分、及格率、班级排名、与班级平均分对比
 */
@WebServlet("/student/myScoreServlet")
public class StudentScoreServlet extends HttpServlet {

    private final ScoreDAO scoreDAO = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User student = (User) request.getSession().getAttribute("loginUser");

        List<Score> scores = scoreDAO.listByStudent(student.getUid());
        double[] summary = scoreDAO.studentSummary(student.getUid());   // {科目数, 平均分, 及格数}
        int[] rank = student.getClassId() > 0
                ? scoreDAO.classRank(student.getUid(), student.getClassId())
                : new int[]{0, 0};

        // 每门课与班级平均分的对比
        List<Double> classAvgList = new ArrayList<Double>();
        double creditSum = 0;
        double weighted = 0;
        for (Score s : scores) {
            classAvgList.add(scoreDAO.classCourseAvg(student.getClassId(), s.getCourseId(), s.getExamType()));
            creditSum += s.getCredit();
            weighted += s.getScore() * s.getCredit();
        }

        double passRate = summary[0] == 0 ? 0 : Math.round(summary[2] * 10000.0 / summary[0]) / 100.0;

        request.setAttribute("scores", scores);
        request.setAttribute("classAvgList", classAvgList);
        request.setAttribute("subjectCount", (int) summary[0]);
        request.setAttribute("avgScore", Math.round(summary[1] * 100) / 100.0);
        request.setAttribute("passCount", (int) summary[2]);
        request.setAttribute("passRate", passRate);
        request.setAttribute("rank", rank[0]);
        request.setAttribute("classSize", rank[1]);
        request.setAttribute("gpa", creditSum == 0 ? 0 : Math.round(weighted / creditSum * 100) / 100.0);

        request.getRequestDispatcher("/pages/student/myScore.jsp").forward(request, response);
    }
}
