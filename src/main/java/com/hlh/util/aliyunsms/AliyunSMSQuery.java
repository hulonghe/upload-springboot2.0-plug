package com.hlh.util.aliyunsms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 短信查询实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliyunSMSQuery {
    private String bizId;
    private String phoneNumber;
    private Date sendDate;
    private Long pageSize;
    private Long currentPage;
}
