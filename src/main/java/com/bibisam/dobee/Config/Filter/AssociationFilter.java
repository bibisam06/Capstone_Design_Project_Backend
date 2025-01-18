//package com.bibisam.dobee.Config.Filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//import java.util.List;
//
//@Component
//@WebFilter(displayName = "loginFilter", urlPatterns = "/api/association/*")
//public class AssociationFilter implements Filter {
//
//    private static final List<String> ALLOWED_PATHS = List.of(
//            "/check-to-join",
//            "/find-pw",
//            "/change-pw",
//            "/forgot-password",
//            "/login",
//            "send-verification"
//    );
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // 필터 초기화
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        String path = request.getRequestURI();
//        System.out.println("요청 경로: " + path);
//
//        // 허용된 경로에 대해 필터를 건너뜀
//        if (isAllowedPath(path)) {
//            System.out.println("허용된 경로: " + path);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 로그인 세션 확인
//        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
//        if (session == null || session.getAttribute("login") == null) {
//            System.out.println("로그인되지 않음");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("로그인이 필요합니다.");
//            return; // 요청 처리 종료
//        }
//
//        // 로그인 된 사용자가 있을 경우
//        Object loggedInUser = session.getAttribute("loginUser");
//        System.out.println("로그인된 사용자: " + loggedInUser);
//
//        // 다음 필터로 요청 전달
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean isAllowedPath(String path) {
//        return ALLOWED_PATHS.stream().anyMatch(path::endsWith);
//    }
//
//    @Override
//    public void destroy() {
//        // 필터 종료 처리
//        Filter.super.destroy();
//    }
//}
