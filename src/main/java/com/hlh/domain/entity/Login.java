package com.hlh.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.base.BaseParent;
import com.hlh.util.convert.MapAndStringConvert;
import com.hlh.util.convert.PwdEncryptionConvert;
import com.hlh.util.validator.PasswordBase;
import com.hlh.util.validator.PhoneBase;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 登录信息
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_LOGIN_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_LOGIN_NAME, comment = "登录信息")
public class Login extends BaseParent implements Serializable {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度应该在2-50之间")
    @Column(columnDefinition = "VARCHAR(50) COMMENT '用户名'", unique = true)
    private String username;                                // 用户名

    @NotBlank(message = "手机号不能为空")
    @PhoneBase
    @Column(nullable = false, columnDefinition = "VARCHAR(11) COMMENT '手机号'", unique = true)
    private String phone;                                   // 手机号

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @Column(columnDefinition = "VARCHAR(50) COMMENT '邮箱'", unique = true)
    private String email;                                   // 邮箱

    @NotBlank(message = "密码不能为空")
    @PasswordBase
    @Column(nullable = false, columnDefinition = "VARCHAR(128) COMMENT '密码'")
    @Convert(converter = PwdEncryptionConvert.class)
    @JsonIgnore
    private String password;                                // 密码

    @Column(columnDefinition = "BIGINT(20) COMMENT '角色外键'")
    private Long role;                                      // 角色外键

    @Column(columnDefinition = "TEXT COMMENT '权限组'")
    @Convert(converter = MapAndStringConvert.class)
    private HashMap<String, Boolean> identity;              // 权限组
//
//    @Column(columnDefinition = "BIGINT(20) COMMENT '上级用户外键'")
//    private Long parentId;                                  // 上级
//
//    @Column(columnDefinition = "TEXT COMMENT '上级目录索引'")
//    @Convert(converter = ListAndStringConvert.class)
//    private List<Long> parentIds;                           // 上级目录索引

//
//    /**
//     * 增加关系树
//     *
//     * @param login
//     */
//    public void addParentIds(@NotNull Login login) {
//        if (login.parentIds == null) {
//            login.parentIds = new ArrayList();
//        }
//
//        if (this.parentIds == null) {
//            this.parentIds = new ArrayList();
//        }
//
//        // 直接关系
//        this.parentId = login.getId();
//        // 关系树
//        this.parentIds.addAll(0, login.parentIds);
//        this.parentIds.add(login.getId());
//    }
}
