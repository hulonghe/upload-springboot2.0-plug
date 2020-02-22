package com.hlh.domain.base;

import com.hlh.util.convert.ListAndStringConvert;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限树
 */

@ToString(callSuper = true)
@MappedSuperclass
public class BaseParent extends BaseDate implements ParentIdsOp {

    @Column(columnDefinition = "BIGINT(20) COMMENT '上级用户外键'")
    private Long parentId;                                  // 上级

    @Column(columnDefinition = "TEXT COMMENT '上级目录索引'")
    @Convert(converter = ListAndStringConvert.class)
    private List<Long> parentIds;                           // 上级目录索引

    @Override
    public void addParentIds(BaseParent parent) {
        if (parent.parentIds == null) {
            parent.parentIds = new ArrayList();
        }

        if (this.parentIds == null) {
            this.parentIds = new ArrayList();
        }

        // 直接关系
        this.parentId = parent.getId();
        // 关系树
        this.parentIds.addAll(0, parent.parentIds);
        this.parentIds.add(parent.getId());
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Long> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<Long> parentIds) {
        this.parentIds = parentIds;
    }
}
