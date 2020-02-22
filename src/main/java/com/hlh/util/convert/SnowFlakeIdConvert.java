package com.hlh.util.convert;


import com.hlh.util.CheckUtil;
import com.hlh.util.SnowflakeUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 主键生成机制
 */

public class SnowFlakeIdConvert implements IdentifierGenerator {

    public SnowFlakeIdConvert() {
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object entity) throws HibernateException {
        try {
            Class<?> aClass = entity.getClass();                        // 获取类类型
            Method getId = aClass.getMethod("getId");            // 获取getId方法
            Object id = getId.invoke(entity);                           // 执行方法获取返回结果
            if (id == null || !(id instanceof Long)) {                  // 如果结果为null，或者不是Long类型
                return SnowflakeUtils.dao.nextId();                         // 重新生成id
            } else {                                                    // 如果结果存在
                long idd = (Long) id;
                if (CheckUtil.dao.isId(idd)) {                          // 如果结果是ID格式
                    return (Long) id;                                       // 返回此ID
                } else {                                                // 反之
                    return SnowflakeUtils.dao.nextId();                     // 重新生成
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return SnowflakeUtils.dao.nextId();
    }
}
