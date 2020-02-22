package com.hlh.repo;

import com.hlh.domain.entity.Login;
import com.hlh.repo.rest.RestLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "logins", excerptProjection = RestLogin.class)
public interface LoginRepo extends JpaRepository<Login, Long> {

    Login findFirstById(Long id);

    Login findFirstByUsernameOrPhoneOrEmail(String username, String tel, String email);

    /**
     * 在实际生产环境中，不会轻易的删除用户数据，此时我们不希望DELETE的提交方式生效.
     * 可以添加@RestResource注解，并设置exported=false，即可屏蔽Spring Data REST的自动化方法
     */
    @RestResource(exported = false)
    void deleteById(Long id);

    @RestResource(exported = false)
    void delete(Login login);

    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Login> var1);

    @RestResource(exported = false)
    void deleteAll();
}
