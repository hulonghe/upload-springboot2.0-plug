package com.hlh.util.aliyunsms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: AliyunSmsSenderServiceImpl
 * @Author: Laohu
 * @Description: 阿里云短信服务实现
 * @Date: 2019/8/6 10:07
 * @Version: 1.0
 */

@Component
public class AliyunSmsSenderUtil {

    @Resource
    private AliyunSMSConfig smsConfig;

    private Logger logger = LogManager.getLogger(this.getClass());

    /**
     * 对接阿里云短信服务实现短信发送
     * 发送验证码类的短信时，每个号码每分钟最多发送一次，每个小时最多发送5次。其它类短信频控请参考阿里云
     *
     * @param phoneNumbers:      手机号
     * @param templateParamJson: 模板参数json {"sellerName":"123456","orderSn":"123456"}
     * @param templateCode:      阿里云短信模板code
     *                           发送验证码类的短信时，每个号码每分钟最多发送一次，每个小时最多发送5次。其它类短信频控请参考阿里云
     * @return
     */
    public SendSmsResponse sendSms(String phoneNumbers, String templateParamJson, String templateCode) {

        // 封装短信发送对象
        AliyunSMS sms = new AliyunSMS();
        sms.setPhoneNumbers(phoneNumbers);
        sms.setTemplateParam(templateParamJson);
        sms.setTemplateCode(templateCode);

        // 获取短信发送服务机
        IAcsClient acsClient = getClient();

        //获取短信请求
        SendSmsRequest request = getSmsRequest(sms);
        SendSmsResponse sendSmsResponse = new SendSmsResponse();

        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            logger.error("发送短信发生错误:错误代码是 %s，错误消息是 %s，错误请求ID是 %s，错误Msg是 %s，错误类型是 %s",
                    e.getErrCode(), e.getMessage(), e.getRequestId(), e.getErrMsg(), e.getErrorType());
        }
        return sendSmsResponse;
    }

    /**
     * 查询发送短信的内容
     *
     * @param bizId:       短信对象的对应的bizId
     * @param phoneNumber: 手机号
     * @param pageSize:    分页大小
     * @param currentPage: 当前页码
     * @return
     */
    public QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber, Long pageSize, Long currentPage) {

        // 查询实体封装
        AliyunSMSQuery smsQuery = new AliyunSMSQuery();
        smsQuery.setBizId(bizId);
        smsQuery.setPhoneNumber(phoneNumber);
        smsQuery.setCurrentPage(currentPage);
        smsQuery.setPageSize(pageSize);
        smsQuery.setSendDate(new Date());

        // 获取短信发送服务机
        IAcsClient acsClient = getClient();
        QuerySendDetailsRequest request = getSmsQueryRequest(smsQuery);
        QuerySendDetailsResponse querySendDetailsResponse = null;
        try {
            querySendDetailsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            logger.error("查询发送短信发生错误:错误代码是 %s，错误消息是 %s，错误请求ID是 %s，错误Msg是 %s，错误类型是 %s",
                    e.getErrCode(), e.getMessage(), e.getRequestId(), e.getErrMsg(), e.getErrorType());
        }
        return querySendDetailsResponse;
    }

    /**
     * 封装查询阿里云短信请求对象
     *
     * @param smsQuery
     * @return
     */
    private QuerySendDetailsRequest getSmsQueryRequest(AliyunSMSQuery smsQuery) {
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        request.setPhoneNumber(smsQuery.getPhoneNumber());
        request.setBizId(smsQuery.getBizId());
        SimpleDateFormat ft = new SimpleDateFormat(smsConfig.getDateFormat());
        request.setSendDate(ft.format(smsQuery.getSendDate()));
        request.setPageSize(smsQuery.getPageSize());
        request.setCurrentPage(smsQuery.getCurrentPage());
        return request;
    }

    /**
     * 获取短信请求
     *
     * @param sms: 短信发送实体
     * @return
     */
    private SendSmsRequest getSmsRequest(AliyunSMS sms) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(sms.getPhoneNumbers());
        request.setSignName(smsConfig.getSignName());
        request.setTemplateCode(sms.getTemplateCode());
        request.setTemplateParam(sms.getTemplateParam());
        request.setOutId(sms.getOutId());
        return request;
    }

    /**
     * 获取短信发送服务机
     *
     * @return
     */
    private IAcsClient getClient() {
        IClientProfile profile = DefaultProfile.getProfile(
                smsConfig.getRegionId(),
                smsConfig.getAccessKeyId(),
                smsConfig.getAccessKeySecret()
        );

        DefaultProfile.addEndpoint(
                smsConfig.getRegionId(),
                smsConfig.getProduct(),
                smsConfig.getDomain()
        );
        return new DefaultAcsClient(profile);
    }
}
