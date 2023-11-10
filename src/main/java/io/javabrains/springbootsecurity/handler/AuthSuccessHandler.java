package io.javabrains.springbootsecurity.handler;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {


    Logger log = LoggerFactory.getLogger(AuthSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String userAgent = request.getHeader("user-agent");
        String ipAdd = request.getRemoteAddr();
        log.info("userAgent " + userAgent);
        log.info("authentication successful for user " + authentication.getName());


        if (userAgent.contains("Java") || userAgent.contains("Python")) {

            JSONObject json = new JSONObject();
            json.put("msgDesc", "Success");
            json.put("user", authentication.getName());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toJSONString());

        } else {

            //set our response to OK status


            //since we have created our custom success handler, its up to us, to where
            //we will redirect the user after successfully login
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
//        String requestUrl = savedRequest.getRedirectUrl();
            response.sendRedirect((savedRequest == null || savedRequest.getRedirectUrl().isEmpty()) ? "/" : savedRequest.getRedirectUrl()); //requestUrl!=null?requestUrl:
        }
    }
}
