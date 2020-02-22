package com.hlh.domain.param;

import com.hlh.config.FinalPoolCfg;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 文件清单,可编辑体
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Table(name = FinalPoolCfg.TB_FILEINFO_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_FILEINFO_NAME, comment = "文件上传记录")
public class FileInfoEditParam {
    @Id
    private Long id;                    // 主键

    private Boolean flag;               // 状态

    private String parent;

    private Long parentId;
}
