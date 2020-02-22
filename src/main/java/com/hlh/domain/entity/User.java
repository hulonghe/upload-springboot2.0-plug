package com.hlh.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hlh.config.FinalPoolCfg;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户信息
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Table(name = FinalPoolCfg.TB_USER_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_USER_NAME, comment = "用户信息")
public class User{
    @Id
    @GeneratedValue(generator = "snow-flake-id")
    @GenericGenerator(name = "snow-flake-id", strategy = FinalPoolCfg.CFG_GENERIC_GENERATOR_ID_PAG_PATH)
    @Column(columnDefinition = "BIGINT(20) NOT NULL COMMENT '唯一键'")
    private Long id;                                         // 主键

    @Size(min = 2, max = 20, message = "姓名长度应该在2-20之间")
    @Column(columnDefinition = "VARCHAR(20) COMMENT '姓名'")
    private String name;        // 姓名

    @Column(columnDefinition = "BIT DEFAULT 0 COMMENT '性别'")
    private Boolean sex;        // 性别

    @Column(columnDefinition = "INT(3) DEFAULT 0 COMMENT '年龄'")
    private Integer age;        // 年龄

    @Past(message = "出生日期必须是一个过去的")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(columnDefinition = "TIMESTAMP DEFAULT '0000-00-00 00:00:00' COMMENT '出生日期'")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;         // 出生日期

    @Column(columnDefinition = "BIGINT(20) COMMENT '民族'")
    private Long nation;      // 民族

    @Column(columnDefinition = "BIGINT(20) COMMENT '教育'")
    private Long edu;         // 教育

    @Column(columnDefinition = "BIGINT(20) COMMENT '职称'")
    private Long work;        // 职称

    @Column(columnDefinition = "VARCHAR(255) COMMENT '地址'")
    private String place;       // 地址

    @Column(columnDefinition = "VARCHAR(50) COMMENT '真实姓名'")
    private String trueName;    // 真实姓名

    @Column(columnDefinition = "BIGINT(20) COMMENT '证件类型'")
    private Long cardType;      // 证件类型

    @Column(columnDefinition = "VARCHAR(50) COMMENT '证件号'")
    private String cardId;      // 证件号
}
