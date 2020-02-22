package com.hlh.util;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.view.LoginInfoView;

import java.util.Map;

/**
 * 数据校验工具
 */

public class CheckUtil {
    public static final CheckUtil dao = new CheckUtil();

    private CheckUtil() {
    }

    /**
     * 校验ID
     *
     * @param id
     * @return
     */
    public boolean isId(Long id) {
        return null != id && String.valueOf(id).length() > 0;
    }

    public boolean isId(Long[] ids) {
        for (Long id : ids) {
            if (!isId(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 空校验
     *
     * @param obj
     * @return
     */
    public boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    public boolean isNull(Object... objs) {
        for (Object obj : objs) {
            if (this.isNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 非空校验
     *
     * @param obj
     * @return
     */
    public boolean isNotNull(Object obj) {
        if (obj == null) {
            return false;
        }
        return true;
    }

    public boolean isNotNull(Object... objs) {
        for (Object obj : objs) {
            if (this.isNull(obj)) {
                return false;
            }
        }
        return true;
    }

    public boolean isStrNotNull(String str) {
        return this.isNotNull(str) && str.length() > 0;
    }

    /**
     * 验证授权码是否符合规范
     *
     * @param act
     * @return
     */
    public boolean isAccessToken(String act) {
        if (!isStrNotNull(act)) {
            return false;
        }

        if (act.length() < 32) {
            return false;
        }

        return true;
    }

    /**
     * 账户信息空校验
     *
     * @param loginInfo
     * @return
     */
    public boolean isLoginInfoEnable(LoginInfoView loginInfo) {
        if (loginInfo == null || loginInfo.getLogin() == null) {
            return false;
        }

        return true;
    }

    /**
     * 登录授权参数校验
     *
     * @param map
     * @return
     */
    public boolean isLoginParam(Map map) {
        return map.containsKey(FinalPoolCfg.PARAM_USERNAME_NAME) && map.containsKey(FinalPoolCfg.PARAM_PASSWORD_NAME);
    }
}
