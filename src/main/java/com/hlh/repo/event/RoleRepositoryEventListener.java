package com.hlh.repo.event;

import com.hlh.domain.entity.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Spring Data REST Event
 */

@Component
public class RoleRepositoryEventListener extends AbstractRepositoryEventListener<Role> {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(Role entity) {
        logger.info("/*==================== Role onBeforeCreate ====================*/");
        entity.setIdentity(entity.getIdentity() == null ? new HashMap<>() : entity.getIdentity());
        entity.setParentIds(entity.getParentIds() == null ? new ArrayList<>() : entity.getParentIds());
        super.onBeforeCreate(entity);
    }

    /**
     * Insert 之后
     *
     * @param entity
     */
    @Override
    protected void onAfterCreate(Role entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Role onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(Role entity) {
        logger.info("/*==================== Role onBeforeSave ====================*/");
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Role entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Role onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(Role entity) {
        logger.info("/*==================== Role onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Role entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Role onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(Role parent, Object linked) {
        logger.info("/*==================== Role onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Role parent, Object linked) {
        logger.info("/*==================== Role onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Role parent, Object linked) {
        logger.info("/*==================== Role onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(Role parent, Object linked) {
        logger.info("/*==================== Role onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

}
