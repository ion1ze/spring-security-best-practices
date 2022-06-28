package cn.ionize.spring.security.login.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code = 0;
    private String message = "success";
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setData(null);
        return result;
    }

    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<T>();
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<T>();
        result.setMessage(message);
        result.setData(data);
        return result;
    }


    public static <T> Result<T> failure(String message) {
        Result<T> result = new Result<T>();
        result.setCode(-1);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> failure(String message, Integer code) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
