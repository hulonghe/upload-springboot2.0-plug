package com.hlh.repo.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hlh.domain.entity.Login;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.HashMap;

@Projection(name = "restLogin", types = Login.class)
public interface RestLogin {
    String getId();                                      // 主键

    String getUsername();                                // 用户名

    String getPhone();                                   // 手机号

    String getEmail();                                   // 邮箱

    String getRole();                                    // 角色外键

    HashMap<String, Boolean> getIdentity();              // 权限组

    String getParentId();                                // 上级

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date getOperateTime();                               // 最后更新时间

    boolean isFlag();                                    // 状态
}
