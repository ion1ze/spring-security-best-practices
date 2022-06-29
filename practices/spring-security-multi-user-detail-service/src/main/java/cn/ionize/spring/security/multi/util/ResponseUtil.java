package cn.ionize.spring.security.multi.util;

import cn.ionize.spring.security.multi.model.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
public class ResponseUtil {

    public static void toJson(HttpServletResponse response, Result<Object> result) throws IOException {
        // 需要使用 APPLICATION_JSON_UTF8_VALUE 不然会导致乱码
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        String resultString = new ObjectMapper().writeValueAsString(result);
        out.write(resultString);
        out.flush();
        out.close();
    }
}
