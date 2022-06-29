package cn.ionize.spring.security.multi.service.impl;

import cn.ionize.spring.security.multi.model.entity.MemberUser;
import cn.ionize.spring.security.multi.repository.MemberUserRepository;
import cn.ionize.spring.security.multi.service.MemberUserService;
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
public class MemberUserServiceImpl implements MemberUserService {

    private final MemberUserRepository memberUserRepository;

    public MemberUserServiceImpl(MemberUserRepository memberUserRepository) {
        this.memberUserRepository = memberUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberUser user = memberUserRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }

        List<String> roleNames = new ArrayList<>();
        roleNames.add("ROLE_USER");
        user.setRoleNames(roleNames);
        return user;
    }
}
