package com.hlh.repo.view;

import com.hlh.domain.view.ArticleInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleInfoViewRepo extends JpaRepository<ArticleInfoView, Long> {

}
