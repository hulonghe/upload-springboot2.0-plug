package com.hlh.util.convert;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.util.HashMap;

/**
 * AttributeConverter: 此接口用于转化实体属性的
 * 将实体类中的Map与数据库中的String互转
 */

public class MapAndStringConvert implements AttributeConverter<HashMap, String> {

    private static Logger log = LoggerFactory.getLogger(MapAndStringConvert.class);

    @Override
    public String convertToDatabaseColumn(HashMap value) {
        try {
            if (null == value) {
                return null;
            }
            return JSON.toJSONString(value);
        } catch (Exception e) {
            log.error("实体属性转换到表字段", e);
        }
        return null;
    }

    @Override
    public HashMap convertToEntityAttribute(String value) {
        try {
            if (value == null) {
                return null;
            }
            return JSON.parseObject(value, HashMap.class);
        } catch (Exception e) {
            log.error("表字段转换到实体属性", e);
        }
        return null;
    }
}
