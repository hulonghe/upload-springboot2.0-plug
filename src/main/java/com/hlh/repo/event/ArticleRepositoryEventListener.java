package com.hlh.repo.event;

import com.hlh.domain.entity.Article;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;


/**
 * Spring Data REST Event
 */

@Component
public class ArticleRepositoryEventListener extends AbstractRepositoryEventListener<Article> {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(Article entity) {
        logger.info("/*==================== Article onBeforeCreate ====================*/");

        entity.setIsTop(entity.getIsTop() == null ? false : entity.getIsTop());
        entity.setKind(entity.getKind() == null ? 0 : entity.getKind());
        entity.setCommentStatus(entity.getCommentStatus() == null ? true : entity.getCommentStatus());
        entity.setCommentCount(0);
        entity.setReading(0);

        super.onBeforeCreate(entity);
    }

    /**
     * Insert 之后
     *
     * @param entity
     */
    @Override
    protected void onAfterCreate(Article entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Article onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(Article entity) {
        logger.info("/*==================== Article onBeforeSave ====================*/");
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Article entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Article onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(Article entity) {
        logger.info("/*==================== Article onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Article entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Article onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(Article parent, Object linked) {
        logger.info("/*==================== Article onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Article parent, Object linked) {
        logger.info("/*==================== Article onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Article parent, Object linked) {
        logger.info("/*==================== Article onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(Article parent, Object linked) {
        logger.info("/*==================== Article onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

}
