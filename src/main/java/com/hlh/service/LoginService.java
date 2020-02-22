package com.hlh.service;

import com.hlh.domain.entity.Login;
import com.hlh.repo.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginRepo loginRepo;

    public Login findFirstByUsernameOrPhoneOrEmail(String username) {
        return loginRepo.findFirstByUsernameOrPhoneOrEmail(username, username, username);
    }
}
