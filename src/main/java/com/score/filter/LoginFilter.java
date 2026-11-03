package com.score.filter;

import com.score.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录与角色权限拦截：
 *   /teacher/*  仅教师(role=1)
 *   /student/*  仅学生(role=2)
 *   /admin/*    仅管理员(role=0)
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    private static final String[] PUBLIC = {
            "/login.jsp", "/loginServlet", "/register.jsp", "/registerServlet",
            "/index.jsp", "/logoutServlet", "/css/", "/js/", "/images/", "/favicon.ico"
    };

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String path = uri.substring(ctx.length());

        // 根路径与静态资源直接放行
        if ("/".equals(path) || isPublic(path)) {
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("loginUser");

        if (user == null) {
            response.sendRedirect(ctx + "/login.jsp?msg=timeout");
            return;
        }

        if (path.startsWith("/admin/") && user.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "仅管理员可访问");
            return;
        }
        if (path.startsWith("/teacher/") && user.getRole() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "仅教师可访问");
            return;
        }
        if (path.startsWith("/student/") && user.getRole() != 2) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "仅学生可访问");
            return;
        }

        chain.doFilter(req, resp);
    }

    private boolean isPublic(String path) {
        for (String p : PUBLIC) {
            if (path.equals(p) || path.startsWith(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
