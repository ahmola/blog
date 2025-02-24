package com.example.user.interceptor;

import com.example.user.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class GlobalHeaderInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GlobalHeaderInterceptor.class);
    private final UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String username = request.getHeader("X-User-Id"); // auth service에서 추가된 인증 헤더
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info(GlobalHeaderInterceptor.class.toString() + " X-User-Id : " + username +
                " from : " + uri + " method : " + method);

        // 유저 생성에 대해서는 인증 헤더를 요구하지 않음
        if (uri.equals("/user/userCreated" ) && method.equals("POST"))
            return true;

        // 인증되지 않았으면 서비스 거부
        if (username == null || userService.findByUsername(username) == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Unauthorized");
            return false;
        }

        // 인증이 되었다면 요청한

        return true;
    }
}
