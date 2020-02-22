package com.hlh.util.convert;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AttributeConverter: 此接口用于转化实体属性的
 * 将实体类中的List与数据库中的String互转
 */

public class ListAndStringConvert implements AttributeConverter<List, String> {
    private String split = "\\|";

    private static Logger log = LoggerFactory.getLogger(ListAndStringConvert.class);

    @Override
    public String convertToDatabaseColumn(List value) {
        try {
            if (null == value) {
                return null;
            }
            String res = StringUtils.join(value, "|");
            return res;
        } catch (Exception e) {
            log.error("实体属性转换到表字段", e);
        }
        return null;
    }

    @Override
    public List convertToEntityAttribute(String value) {
        try {
            if (value == null) {
                return null;
            }
            String[] res = value.split(this.split);
            return new ArrayList(Arrays.asList(res));
        } catch (Exception e) {
            log.error("表字段转换到实体属性", e);
        }
        return null;
    }
}
