package LikeLion.UnderTheCBackend.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void addCookie(Cookie cookie) {
        if ("JSESSIONID".equals(cookie.getName())) {
            super.addHeader("Set-Cookie", getCookieValue(cookie));
        } else {
            super.addCookie(cookie);
        }
    }

    private String getCookieValue(Cookie cookie) {
        System.out.println("쿠키 설정 cookie = " + cookie);
        StringBuilder builder = new StringBuilder();
        builder.append(cookie.getName()).append('=').append(cookie.getValue());
        builder.append(";Path=").append(cookie.getPath());
        if (cookie.isHttpOnly()) {
            builder.append(";HttpOnly");
        }
        if (cookie.getSecure()) {
            builder.append(";Secure");
        }
        // here you can append other attributes like domain / max-age etc.
        builder.append(";SameSite=None");
        return builder.toString();
    }
}