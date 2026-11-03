package com.score.servlet;

import com.score.dao.ScoreDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 教师 - 批量保存成绩：一次提交整班分数，自动区分新增/修改
 */
@WebServlet("/teacher/scoreSaveServlet")
public class ScoreSaveServlet extends HttpServlet {

    private final ScoreDAO scoreDAO = new ScoreDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int classId = TeacherScoreServlet.parseInt(request.getParameter("classId"), 0);
        int courseId = TeacherScoreServlet.parseInt(request.getParameter("courseId"), 0);
        String examType = request.getParameter("examType");
        if (examType == null || examType.trim().isEmpty()) {
            examType = "期末";
        }

        String[] ids = request.getParameterValues("studentId");
        String[] scores = request.getParameterValues("scoreValue");

        int saved = 0;
        int skipped = 0;
        if (ids != null && scores != null) {
            for (int i = 0; i < ids.length && i < scores.length; i++) {
                String val = scores[i] == null ? "" : scores[i].trim();
                if (val.isEmpty()) {
                    skipped++;
                    continue;
                }
                double v;
                try {
                    v = Double.parseDouble(val);
                } catch (NumberFormatException e) {
                    skipped++;
                    continue;
                }
                if (v < 0 || v > 100) {
                    skipped++;
                    continue;
                }
                scoreDAO.saveOrUpdate(Integer.parseInt(ids[i]), courseId, examType, v);
                saved++;
            }
        }

        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/teacher/scoreInputServlet?classId=" + classId
                + "&courseId=" + courseId + "&examType=" + enc(examType)
                + "&saved=" + saved + "&skipped=" + skipped);
    }

    private String enc(String s) {
        try {
            return java.net.URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
}
