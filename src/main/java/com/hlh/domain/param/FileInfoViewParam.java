package com.hlh.domain.param;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.base.BaseMain;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 文件清单, 传回前端需要
 */

@Entity
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@EqualsAndHashCode(callSuper = true)
@Table(name = FinalPoolCfg.TB_FILEINFO_NAME)
@org.hibernate.annotations.Table(appliesTo = FinalPoolCfg.TB_FILEINFO_NAME, comment = "文件上传记录")
public class FileInfoViewParam extends BaseMain {
    private String parent;
    private Long parentId;
    private String url;
}
