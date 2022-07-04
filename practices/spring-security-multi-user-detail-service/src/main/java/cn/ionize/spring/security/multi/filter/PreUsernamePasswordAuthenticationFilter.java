package cn.ionize.spring.security.multi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
public class PreUsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    private static final String DEFAULT_USERNAME_PARAMETER = "username";
    private static final String DEFAULT_PASSWORD_PARAMETER = "password";
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();
        ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(request);

        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)) {
            MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
            Map<String, String> params = objectMapper.readValue(request.getInputStream(), mapType);
            String username = params.get(DEFAULT_USERNAME_PARAMETER);
            String password = params.get(DEFAULT_PASSWORD_PARAMETER);

            username = (username != null) ? username : "";
            username = username.trim();
            password = (password != null) ? password : "";
            password = password.trim();

            parameterRequestWrapper.setAttribute(DEFAULT_USERNAME_PARAMETER, username);
            parameterRequestWrapper.setAttribute(DEFAULT_PASSWORD_PARAMETER, password);
        }
        filterChain.doFilter(parameterRequestWrapper, response);
    }
}
