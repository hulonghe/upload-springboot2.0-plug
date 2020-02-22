package com.hlh.repo.view;

import com.hlh.domain.view.LoginUserRoleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginUserRoleViewRepo extends JpaRepository<LoginUserRoleView, Long> {



}
