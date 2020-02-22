package com.hlh.domain.view;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.hlh.config.FinalPoolCfg;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户基本信息视图
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                                          //无参构造
@AllArgsConstructor                                         //有参构造
@Table(name = FinalPoolCfg.VIEW_LOGIN_USER_ROLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.VIEW_LOGIN_USER_ROLE_NAME, comment = "用户与登录与角色视图")
public class LoginUserRoleView {
    @Id
    private Long id;                                        // 主键

    private boolean flag;                                   // 状态

    private String username;                                // 用户名

    private String phone;                                   // 手机号

    private String name;                                    // 姓名

    private Boolean sex;                                    // 性别

    private Integer age;                                    // 年龄

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;                                     // 出生日期

    private Long nation;                                    // 民族

    private Long edu;                                       // 教育

    private Long work;                                      // 职称

    private String roleName;                                // 角色名

}
