package com.hlh.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hlh.config.FinalPoolCfg;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 文章视图
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                                          //无参构造
@AllArgsConstructor                                         //有参构造
@Table(name = FinalPoolCfg.VIEW_ARTICLE_INFO_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.VIEW_ARTICLE_INFO_NAME, comment = "文章视图")
public class ArticleInfoView{
    @Id
    private Long id;                                        // 主键

    private boolean flag = true;                            // 是否可用

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;                                // 开始创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;                               // 最后更新时间

    private String author;                                  // 作者
    private String content;                                 // 内容
    private String title;                                   // 标题
    private String excerpt;                                 // 摘录
    private String type;                                    // 文章类别
    private Boolean commentStatus;                          // 是否开启评论
    private String password;                                // 文章密码
    private Integer commentCount;                           // 评论数
}
