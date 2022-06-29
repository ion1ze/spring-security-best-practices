package cn.ionize.spring.security.multi.repository;

import cn.ionize.spring.security.multi.model.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser,Long> {

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 用户信息实例
     */
    SystemUser findByUsername(String username);
}
