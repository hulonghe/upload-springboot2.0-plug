package com.hlh.domain.entity;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.base.BaseDate;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 文章信息
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_ARTICLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_ARTICLE_NAME, comment = "文章信息")
public class Article extends BaseDate {

    @NotBlank(message = "标题不能为空")
    @Size(min = 2, max = 50, message = "标题长度应该在2-50之间")
    @Column(columnDefinition = "VARCHAR(50) COMMENT '标题'", nullable = false)
    private String title;                           // 标题

    @NotBlank(message = "内容不能为空")
    @Column(columnDefinition = "TEXT COMMENT '作者'", nullable = false)
    private String content;                         // 内容

    @NotNull(message = "类别不能为空")
    @Column(columnDefinition = "BIGINT(20) COMMENT '类别外键'", nullable = false)
    private Long type;                              // 类别

    @Column(columnDefinition = "VARCHAR(50) COMMENT '作者'")
    private String author;                          // 作者

    @Column(columnDefinition = "VARCHAR(255) COMMENT '摘录'")
    private String excerpt;                         // 摘录

    @Column(columnDefinition = "INT(2) DEFAULT 1 COMMENT '是否超链接'")
    private Integer kind;                           // 种类，如：超链接1，视频2，文章0

    @Column(columnDefinition = "BIT DEFAULT 1 COMMENT '是否开启评论'")
    private Boolean commentStatus;                  // 是否开启评论

    @Column(columnDefinition = "VARCHAR(128) COMMENT '文章密码'")
    private String password;                        // 文章密码

    @Column(columnDefinition = "INT(11) DEFAULT 0 COMMENT '评论数'")
    private Integer commentCount;                   // 评论数

    @Column(columnDefinition = "INT(11) DEFAULT 0 COMMENT '阅读数'")
    private Integer reading;                        // 阅读数

    @Column(columnDefinition = "BIGINT(20) COMMENT '发布者'")
    private Long publisher;                         // 发布者

    @Column(columnDefinition = "BIT DEFAULT 1 COMMENT '是否置顶'")
    private Boolean isTop;                          // 是否置顶
}
