package com.hlh.repo.event;

import com.hlh.domain.entity.FileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;


/**
 * Spring Data REST Event
 */

@Component
public class FileInfoRepositoryEventListener extends AbstractRepositoryEventListener<FileInfo> {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(FileInfo entity) {
        logger.info("/*==================== FileInfo onBeforeCreate ====================*/");

        super.onBeforeCreate(entity);
    }

    /**
     * Insert 之后
     *
     * @param entity
     */
    @Override
    protected void onAfterCreate(FileInfo entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== FileInfo onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(FileInfo entity) {
        logger.info("/*==================== FileInfo onBeforeSave ====================*/");
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(FileInfo entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== FileInfo onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(FileInfo entity) {
        logger.info("/*==================== FileInfo onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(FileInfo entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== FileInfo onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(FileInfo parent, Object linked) {
        logger.info("/*==================== FileInfo onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(FileInfo parent, Object linked) {
        logger.info("/*==================== FileInfo onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(FileInfo parent, Object linked) {
        logger.info("/*==================== FileInfo onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(FileInfo parent, Object linked) {
        logger.info("/*==================== FileInfo onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

}
