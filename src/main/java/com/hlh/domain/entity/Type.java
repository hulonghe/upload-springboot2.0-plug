package com.hlh.domain.entity;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.base.BaseParent;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 类别信息
 */

@Entity
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor                          //无参构造
@AllArgsConstructor                         //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_TYPE_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_TYPE_NAME, comment = "类别信息")
public class Type extends BaseParent {

    @NotBlank(message = "分类名称不能为空")
    @Size(min = 2, max = 50, message = "分类名称长度应该在2-50之间")
    @Column(columnDefinition = "VARCHAR(50) COMMENT '分类名称'", nullable = false)
    private String name;                    // 分类名称

    @Column(columnDefinition = "VARCHAR(255) COMMENT '超链接地址'")
    private String hyperlink;               // 超链接地址

//    @Column(columnDefinition = "BIGINT(20) COMMENT '上一级目录'")
//    private Long parentId;                  // 上一级目录
//
//    @Column(columnDefinition = "TEXT COMMENT '目录索引'")
//    @Convert(converter = ListAndStringConvert.class)
//    private List<Long> parentIds;           // 目录索引
//
//    /**
//     * 增加关系树
//     *
//     * @param type
//     */
//    public void addParentIds(@NotNull Type type) {
//        if (type.parentIds == null) {
//            type.parentIds = new ArrayList();
//        }
//
//        if (this.parentIds == null) {
//            this.parentIds = new ArrayList();
//        }
//
//        // 直接关系
//        this.parentId = type.getId();
//        // 关系树
//        this.parentIds.addAll(0, type.parentIds);
//        this.parentIds.add(type.getId());
//    }
}
