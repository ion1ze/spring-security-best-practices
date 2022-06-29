package cn.ionize.spring.security.multi.repository;

import cn.ionize.spring.security.multi.model.entity.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@Repository
public interface MemberUserRepository extends JpaRepository<MemberUser,Long> {

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 用户信息实例
     */
    MemberUser findByUsername(String username);
}
