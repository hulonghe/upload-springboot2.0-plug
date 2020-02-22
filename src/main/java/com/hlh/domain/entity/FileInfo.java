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
 * 文件清单
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_FILEINFO_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_FILEINFO_NAME, comment = "文件信息")
public class FileInfo extends BaseDate {

    @NotBlank(message = "源文件名不能为空")
    @Size(min = 2, max = 255, message = "源文件名应该在2-50之间")
    @Column(columnDefinition = "VARCHAR(255) COMMENT '源文件名'", nullable = false)
    private String sourceName;                           // 源文件名

    @NotBlank(message = "名称不能为空")
    @Size(min = 1, max = 255, message = "名称长度应该在1-255之间")
    @Column(nullable = false, columnDefinition = "VARCHAR(255) COMMENT '名称'")
    private String url;                                 // 名称,这里指文件访问路径

    @NotBlank(message = "保存路径不能为空")
    @Size(min = 1, max = 255, message = "保存路径长度应该在1-255之间")
    @Column(nullable = false, columnDefinition = "VARCHAR(255) COMMENT '保存路径'")
    private String path;                                // 保存路径,这里指文件保存路径

    @NotBlank(message = "类型不能为空")
    @Size(min = 1, max = 10, message = "类型长度应该在1-10之间")
    @Column(nullable = false, columnDefinition = "VARCHAR(10) COMMENT '类型'")
    private String type;                                // 类型

    @NotNull(message = "文件大小不能为空")
    @Column(columnDefinition = "BIGINT(20) DEFAULT 0 COMMENT '大小'")
    private Long size;                                  // 大小

    @NotBlank(message = "所属者上级不能为空")
    @Size(min = 1, max = 50, message = "所属者上级应该在1-50之间")
    @Column(nullable = false, columnDefinition = "VARCHAR(50) COMMENT '所属者上级,这里指某一张表'")
    private String parent;                              // 所属者的上级,这里指某一张表

    @NotNull(message = "所属者不能为空")
    @Column(nullable = false, columnDefinition = "BIGINT(20) COMMENT '所属者,这里指表中主键'")
    private Long parentId;                              // 所属者

    @NotNull(message = "上传者不能为空")
    @Column(nullable = false, columnDefinition = "BIGINT(20) COMMENT '上传者主键'")
    private Long publisher;                             // 上传者
}
