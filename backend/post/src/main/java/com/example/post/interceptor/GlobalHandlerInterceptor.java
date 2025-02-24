package com.example.post.interceptor;

import com.example.post.post.PostService;
import com.example.post.user.UserClient;
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
public class GlobalHandlerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerInterceptor.class);

    private final PostService postService;

    private final UserClient userClient;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String username = request.getHeader("X-User-Id");
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info(GlobalHandlerInterceptor.class + " authenticated : " + username +
                "uri : " + uri + "method : " + method);

        // 인증되지 않았다면 서비스 거부
        if(username == null ||
                Boolean.FALSE.equals(    // user-service에게 헤더 정보가 유효한지 검증 요청
                        userClient.isXUserIdHeaderValidate(
                                "POST", username)
                                .getBody())){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Unauthorized");
            return false;
        }

        return true;
    }


}
