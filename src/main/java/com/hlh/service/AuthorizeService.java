package com.hlh.service;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.entity.Login;
import com.hlh.domain.entity.Role;
import com.hlh.domain.view.LoginInfoView;
import com.hlh.repo.LoginRepo;
import com.hlh.repo.RoleRepo;
import com.hlh.util.BaseServiceUtil;
import com.hlh.util.CheckUtil;
import com.hlh.util.RandomUtil;
import com.hlh.util.convert.PwdEncryptionConvert;
import com.hlh.util.rep.RepCode;
import com.hlh.util.rep.RepUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * 登录、注册、授权
 */

@Service
public class AuthorizeService extends BaseServiceUtil {

    private CheckUtil checkUtil = CheckUtil.dao;

    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private RoleRepo roleRepo;

    public Object login(HttpServletRequest request, HttpServletResponse response, Map param) {
        boolean isLoginParam = checkUtil.isLoginParam(param);
        if (!isLoginParam) {
            return RepUtil.post(RepCode.CODE_VALIDATOR_ERR);
        }

        /* 验证码校验 */

        String username = (String) param.get(FinalPoolCfg.PARAM_USERNAME_NAME);
        String password = (String) param.get(FinalPoolCfg.PARAM_PASSWORD_NAME);

        Login login = loginRepo.findFirstByUsernameOrPhoneOrEmail(username, username, username);
        /* 账户不存在 */
        if (login == null) {
            return RepUtil.post(RepCode.CODE_NO_USER);
        }
        /* 密码错误 */
        if (!PwdEncryptionConvert.passwordEncrypt(password).equals(login.getPassword())) {
            return RepUtil.post(RepCode.CODE_PWD_ERR);
        }
        /* 账户不可用 */
        if (login.isDel() || !login.isFlag() || login.getRole() == null) {
            return RepUtil.post(RepCode.CODE_USER_STOP);
        }

        Optional<Role> roleOptional = roleRepo.findById(login.getRole());
        /* 角色不存在 */
        if (!roleOptional.isPresent()) {
            return RepUtil.post(RepCode.CODE_ROLE_NO);
        }
        Role role = roleOptional.get();
        /* 角色不可用 */
        if (role.isDel() || !role.isFlag()) {
            return RepUtil.post(RepCode.CODE_ROLE_STOP);
        }

        /* 保存登录，授权 */
        LoginInfoView loginInfo = LoginInfoView.builder().login(login).role(role).build();
        boolean setSessionLogin = setSessionLogin(request, response, loginInfo);
        if (setSessionLogin) {
            return RepUtil.post(RepCode.CODE_SUCCESS);
        } else {
            return RepUtil.post(RepCode.CODE_LOGIN_ERR);
        }
    }

    public Object register(HttpServletRequest request, HttpServletResponse response, Map param) {
        return null;
    }

    /**
     * 登出
     *
     * @param request
     * @param ak
     */
    public void loginOut(HttpServletRequest request, String ak) {
        if (CheckUtil.dao.isAccessToken(ak)) {
            removeSossionLogin(request, RandomUtil.dao.urlEncodeAK(ak.trim()));
        }
    }
}
