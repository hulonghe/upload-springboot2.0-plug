package com.hlh.repo.event;

import com.hlh.domain.entity.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * Spring Data REST Event
 */

@Component
public class TypeRepositoryEventListener extends AbstractRepositoryEventListener<Type> {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(Type entity) {
        logger.info("/*==================== Type onBeforeCreate ====================*/");
        entity.setParentIds(entity.getParentIds() == null ? new ArrayList<>() : entity.getParentIds());
        super.onBeforeCreate(entity);
    }

    /**
     * Insert 之后
     *
     * @param entity
     */
    @Override
    protected void onAfterCreate(Type entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Type onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(Type entity) {
        logger.info("/*==================== Type onBeforeSave ====================*/");
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Type entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Type onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(Type entity) {
        logger.info("/*==================== Type onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Type entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Type onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(Type parent, Object linked) {
        logger.info("/*==================== Type onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Type parent, Object linked) {
        logger.info("/*==================== Type onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Type parent, Object linked) {
        logger.info("/*==================== Type onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(Type parent, Object linked) {
        logger.info("/*==================== Type onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

}
