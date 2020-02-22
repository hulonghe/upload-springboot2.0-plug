package com.hlh.domain.view;

import com.hlh.domain.entity.Login;
import com.hlh.domain.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登陆成功后,保存在缓存中的信息
 */

@Data
@Builder
public class LoginInfoView implements Serializable {
    private Login login;
    private Role role;
}
