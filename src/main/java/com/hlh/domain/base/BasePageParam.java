package com.hlh.domain.base;

import com.hlh.config.FinalPoolCfg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础参数对象
 * 所有参数对象都必须继承此类
 */

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class BasePageParam implements Base {

    @Autowired
    private FinalPoolCfg finalPoolCfg;

    private int page = finalPoolCfg.getPage();              // 查询页
    private int size = finalPoolCfg.getSize();              // 页大小
    private int count;                                      // 总计

    private String startDate;                               // 开始时间
    private String endDate;                                 // 结束时间
    private Boolean isDel;                                  // 被删除
    private Boolean flag;                                   // 状态位
    private String value;
}
