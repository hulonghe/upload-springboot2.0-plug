package com.hlh.domain.base;


import com.hlh.config.FinalPoolCfg;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * 每个实体类,必须继承它
 */

@MappedSuperclass
public abstract class BaseMain implements Base {
    @Id
    @GeneratedValue(generator = "snow-flake-id")
    @GenericGenerator(name = "snow-flake-id", strategy = FinalPoolCfg.CFG_GENERIC_GENERATOR_ID_PAG_PATH)
    @Column(columnDefinition = "BIGINT(20) NOT NULL COMMENT '唯一键'")
    private Long id;                                         // 主键

    @Column(columnDefinition = "BIT DEFAULT 1 COMMENT '是否可用'")
    private boolean flag = true;                            // 是否可用

    @Column(columnDefinition = "VARCHAR(255) COMMENT '错误提示消息'")
    private String errorMsg;                                // 错误提示消息

    @Column(columnDefinition = "BIT DEFAULT 0 COMMENT '是否已删除'")
    private boolean isDel = false;                          // 是否已删除

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMain baseMain = (BaseMain) o;
        return flag == baseMain.flag &&
                isDel == baseMain.isDel &&
                Objects.equals(id, baseMain.id) &&
                Objects.equals(errorMsg, baseMain.errorMsg);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, flag, errorMsg, isDel);
    }

    @Override
    public String toString() {
        return "BaseMain{" +
                "id='" + id + '\'' +
                ", flag=" + flag +
                ", errorMsg='" + errorMsg + '\'' +
                ", isDel=" + isDel +
                '}';
    }
}
