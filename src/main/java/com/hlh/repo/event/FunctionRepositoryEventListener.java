package com.hlh.repo.event;

import com.hlh.domain.entity.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * Spring Data REST Event
 */

@Component
public class FunctionRepositoryEventListener extends AbstractRepositoryEventListener<Function> {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(Function entity) {
        logger.info("/*==================== Function onBeforeCreate ====================*/");
        entity.setRoles(entity.getRoles() == null ? new ArrayList<>() : entity.getRoles());
        entity.setUsers(entity.getUsers() == null ? new ArrayList<>() : entity.getUsers());
        entity.setParentIds(entity.getParentIds() == null ? new ArrayList<>() : entity.getParentIds());
        super.onBeforeCreate(entity);
    }

    /**
     * Insert 之后
     *
     * @param entity
     */
    @Override
    protected void onAfterCreate(Function entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Function onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(Function entity) {
        logger.info("/*==================== Function onBeforeSave ====================*/");
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Function entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Function onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(Function entity) {
        logger.info("/*==================== Function onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Function entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Function onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(Function parent, Object linked) {
        logger.info("/*==================== Function onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Function parent, Object linked) {
        logger.info("/*==================== Function onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Function parent, Object linked) {
        logger.info("/*==================== Function onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(Function parent, Object linked) {
        logger.info("/*==================== Function onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

}
