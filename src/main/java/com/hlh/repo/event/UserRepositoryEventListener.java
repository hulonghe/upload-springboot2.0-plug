package com.hlh.repo.event;

import com.hlh.domain.entity.User;
import com.hlh.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;


/**
 * Spring Data REST Event
 */

@Component
public class UserRepositoryEventListener extends AbstractRepositoryEventListener<User> {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(User entity) {
        logger.info("/*==================== User onBeforeCreate ====================*/");
        calcAge(entity);
        super.onBeforeCreate(entity);
    }

    /**
     * Insert 之后
     *
     * @param entity
     */
    @Override
    protected void onAfterCreate(User entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== User onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(User entity) {
        logger.info("/*==================== User onBeforeSave ====================*/");
        calcAge(entity);
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(User entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== User onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(User entity) {
        logger.info("/*==================== User onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(User entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== User onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(User parent, Object linked) {
        logger.info("/*==================== User onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(User parent, Object linked) {
        logger.info("/*==================== User onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(User parent, Object linked) {
        logger.info("/*==================== User onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(User parent, Object linked) {
        logger.info("/*==================== User onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

    /**
     * 计算年龄
     *
     * @param entity
     */
    private void calcAge(User entity) {
        if (entity.getBirth() != null) {                    // 如果出生日期不为空，则计算年龄
            entity.setAge(DateUtil.calcAge(entity.getBirth()));
        } else {
            entity.setAge(entity.getAge() == null ? 0 : entity.getAge());
        }
    }
}
