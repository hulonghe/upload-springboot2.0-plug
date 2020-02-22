package com.hlh.repo;

import com.hlh.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long> {

    @Transactional
    @Modifying
    @Query("update Article a set a.flag = :#{#flag} where a.id = :#{#id}")
//    @RestResource(description = @Description(value = ""))
    int updateIsFlagById(@Param("id") Long id, @Param("flag") boolean flag);

    /**
     * 在实际生产环境中，不会轻易的删除用户数据，此时我们不希望DELETE的提交方式生效.
     * 可以添加@RestResource注解，并设置exported=false，即可屏蔽Spring Data REST的自动化方法
     */
    @RestResource(exported = false)
    void deleteById(Long id);

    @RestResource(exported = false)
    void delete(Article article);

    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Article> var1);

    @RestResource(exported = false)
    void deleteAll();

}
