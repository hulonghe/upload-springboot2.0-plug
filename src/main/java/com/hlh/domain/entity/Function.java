package com.hlh.domain.entity;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.base.BaseParent;
import com.hlh.util.convert.ListAndStringConvert;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 功能信息
 */

@Entity
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor                          //无参构造
@AllArgsConstructor                         //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_FUNCTION_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_FUNCTION_NAME, comment = "功能信息")
public class Function extends BaseParent {

    @NotBlank(message = "名称不能为空")
    @Size(min = 2, max = 50, message = "名称长度应该在2-50之间")
    @Column(columnDefinition = "VARCHAR(50) COMMENT '名称'", nullable = false)
    private String name;                    // 名称

    @Column(columnDefinition = "VARCHAR(255) COMMENT '超链接地址'")
    private String hyperlink;               // 超链接地址
//
//    @Column(columnDefinition = "BIGINT(20) COMMENT '上一级目录'")
//    private Long parentId;                  // 上一级目录
//
//    @Column(columnDefinition = "TEXT COMMENT '目录索引'")
//    @Convert(converter = ListAndStringConvert.class)
//    private List<Long> parentIds;           // 目录索引

    @Column(columnDefinition = "TEXT COMMENT '所属角色'")
    @Convert(converter = ListAndStringConvert.class)
    private List<Long> roles;                // 所属角色

    @Column(columnDefinition = "TEXT COMMENT '所属用户'")
    @Convert(converter = ListAndStringConvert.class)
    private List<Long> users;                // 所属用户
//
//    /**
//     * 增加关系树
//     *
//     * @param type
//     */
//    public void addParentIds(@NotNull Function type) {
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
