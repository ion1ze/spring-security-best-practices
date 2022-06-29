package cn.ionize.spring.security.multi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@FunctionalInterface
public interface SystemUserDetailService {

    /**
     * 通过用户名加载用户
     * @param username 用户名
     * @return 用户信息实例
     * @throws UsernameNotFoundException 当通过用户名没有找到用户会触发此异常
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
