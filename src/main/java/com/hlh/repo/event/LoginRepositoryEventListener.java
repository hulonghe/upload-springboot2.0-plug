package com.hlh.repo.event;

import com.hlh.domain.entity.Login;
import com.hlh.domain.entity.User;
import com.hlh.repo.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


/**
 * Spring Data REST Event
 */

@Component
public class LoginRepositoryEventListener extends AbstractRepositoryEventListener<Login> {

    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private UserRepo userRepo;

    /**
     * Insert 之前
     *
     * @param entity
     */
    @Override
    protected void onBeforeCreate(Login entity) {
        logger.info("/*==================== Login onBeforeCreate ====================*/");
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
    protected void onAfterCreate(Login entity) {
        logger.info("/*" + entity.toString() + "*/");

        if (entity.getId() != null) {
            Optional<User> userRepoById = userRepo.findById(entity.getId());
            if (!userRepoById.isPresent()) {
                User user = User.builder().id(entity.getId()).age(0).build();
                User save = userRepo.save(user);
                logger.info("/* create user -> " + save.toString() + "*/");
            }
        }

        logger.info("/*==================== Login onAfterCreate ====================*/");
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(Login entity) {
        logger.info("/*==================== Login onBeforeSave ====================*/");
        super.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Login entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Login onAfterSave ====================*/");
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeDelete(Login entity) {
        logger.info("/*==================== Login onBeforeDelete ====================*/");
        super.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Login entity) {
        logger.info("/*" + entity.toString() + "*/");
        logger.info("/*==================== Login onAfterDelete ====================*/");
        super.onAfterDelete(entity);
    }

    @Override
    protected void onBeforeLinkSave(Login parent, Object linked) {
        logger.info("/*==================== Login onBeforeLinkSave ====================*/");
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Login parent, Object linked) {
        logger.info("/*==================== Login onAfterLinkSave ====================*/");
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Login parent, Object linked) {
        logger.info("/*==================== Login onBeforeLinkDelete ====================*/");
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(Login parent, Object linked) {
        logger.info("/*==================== Login onAfterLinkDelete ====================*/");
        super.onAfterLinkDelete(parent, linked);
    }

}
