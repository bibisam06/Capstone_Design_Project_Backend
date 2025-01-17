package com.bibisam.dobee.Config.Filter;

import com.bibisam.dobee.Exceptions.UnAuthorizedException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@WebFilter(displayName = "loginFilter", urlPatterns = "/api/association/*")
public class AssociationFilter implements Filter {

    public static final List<String> ALLOWED_PATHS = Collections.unmodifiableList(Arrays.asList(
            "/check-to-join",
            "/find-pw",
            "/change-pw",
            "/forgot-password",
            "/login"
    ));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 메서드
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
        System.out.println(path);

        // 허용된 경로에 대해 필터링을 건너뜀
        for (String allowedPath : ALLOWED_PATHS) {
            if (path.contains(allowedPath)) {
                System.out.println("허용된 경로: " + path);
                filterChain.doFilter(servletRequest, servletResponse); // 다음 필터로 이동
                return; // 필터 종료 후 return
            }
        }

        // 로그인 세션 확인
        HttpSession loginSession = request.getSession();
        if (loginSession == null || loginSession.getAttribute("login") == null) {
            System.out.println("로그인 안됐네");
            throw new UnAuthorizedException("로그인이 필요합니다.");
        }

        // 로그인된 사용자 정보 (추가적인 로직이 필요할 수 있음)
        Object loginedUser = servletRequest.getAttribute("loginUser");
        System.out.println(loginedUser);

        // 로그인 된 사용자가 있으면 다음 필터로 넘어감
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // 필터 종료 처리
        Filter.super.destroy();
    }
}
