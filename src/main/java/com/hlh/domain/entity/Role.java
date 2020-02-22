package com.hlh.domain.entity;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.base.BaseParent;
import com.hlh.util.convert.MapAndStringConvert;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 角色信息
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                                          //无参构造
@AllArgsConstructor                                         //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_ROLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_ROLE_NAME, comment = "角色信息")
public class Role extends BaseParent implements Serializable {

    @NotNull(message = "发布者不能为空")
    @Column(columnDefinition = "BIGINT(20) COMMENT '发布者'", nullable = false)
    private Long publisher;                                    // 发布者

    @NotBlank(message = "名称不能为空")
    @Size(min = 2, max = 50, message = "名称长度应该在2-50之间")
    @Column(columnDefinition = "VARCHAR(50) COMMENT '名称'", nullable = false, unique = true)
    private String name;                                    // 名称

    @Column(columnDefinition = "TEXT COMMENT '权限组'")
    @Convert(converter = MapAndStringConvert.class)
    private HashMap<String, Boolean> identity;              // 权限组

//    @Column(columnDefinition = "BIGINT(20) COMMENT '上级'")
//    private Long parentId;                                  // 上级
//
//    @Column(columnDefinition = "TEXT COMMENT '上级目录索引'")
//    @Convert(converter = ListAndStringConvert.class)
//    private List<Long> parentIds;                           // 上级目录索引
//
//    /**
//     * 增加关系树
//     *
//     * @param role
//     */
//    public void addParentIds(@NotNull Role role) {
//        if (role.parentIds == null) {
//            role.parentIds = new ArrayList();
//        }
//
//        if (this.parentIds == null) {
//            this.parentIds = new ArrayList();
//        }
//
//        // 直接关系
//        this.parentId = role.getId();
//        // 关系树
//        this.parentIds.addAll(0, role.parentIds);
//        this.parentIds.add(role.getId());
//    }
}
