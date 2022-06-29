package cn.ionize.spring.security.multi.service.impl;

import cn.ionize.spring.security.multi.model.entity.SystemUser;
import cn.ionize.spring.security.multi.repository.SystemUserRepository;
import cn.ionize.spring.security.multi.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@Slf4j
@Service
public class SystemUserServiceImpl implements SystemUserService {

    private final SystemUserRepository systemUserRepository;

    public SystemUserServiceImpl(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = systemUserRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }

        List<String> roleNames = new ArrayList<>();
        roleNames.add("ROLE_ADMIN");
        user.setRoleNames(roleNames);
        return user;
    }
}
