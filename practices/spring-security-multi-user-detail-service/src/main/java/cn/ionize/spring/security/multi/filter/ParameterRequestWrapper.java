package cn.ionize.spring.security.multi.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return (String) super.getAttribute(name);
    }
}
