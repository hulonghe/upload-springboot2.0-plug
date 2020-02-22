package com.hlh.config;

import com.hlh.domain.entity.Type;
import com.hlh.repo.TypeRepo;
import com.hlh.util.CheckUtil;
import com.hlh.util.SpringContextUtil;
import com.hlh.util.YamlUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 项目启动运行配置
 */

@Component
public class AfterStartDoCfg implements CommandLineRunner {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private TypeRepo typeRepo;

    @Autowired
    private FinalPoolCfg finalPoolCfg;

    private CheckUtil checkUtil = CheckUtil.dao;

    @Override
    public void run(String... args) throws Exception {
        logger.info("/*==================== 初始化开始 ====================*/");
        init();
        initTypeEntity();
        setOneIndexedParameters();
        logger.info("/*==================== 完成初始化 ====================*/");
    }

    private void init() throws FileNotFoundException {
        logger.info("/*==================== 正在初始化基础数据 ====================*/");

        String[] filePaths = YamlUtil.getFilePath(finalPoolCfg.getEnvironment());
        VarPool.cfgMap = YamlUtil.load(filePaths);

        logger.info("/*==================== 完成基础数据初始化 ====================*/");
    }


    /**
     * 初始化分类表
     */
    private void initTypeEntity() {
        logger.info("/*==================== 正在初始化分类数据 ====================*/");

        /* 顶级分类内容 */
        HashMap<String, Object> map = YamlUtil.load(VarPool.cfgMap, FinalPoolCfg.CFG_INIT_TYPE_KEY);
        if (map == null || map.size() == 0) {
            return;
        }

        map.forEach((key, value) -> {
            Type type = initTypeEntityFirstRate(key);
            nextInitType(value, type);
        });

        logger.info("/*==================== 完成分类数据初始化 ====================*/");
    }

    /**
     * 创建一级分类
     *
     * @param name
     * @return
     */
    private Type initTypeEntityFirstRate(String name) {
        Type firstByNameAndParentIdIsNullAndParentIdsIsNull = typeRepo.findFirstByNameAndParentIdIsNullAndParentIdsIsNull(name);
        /* 如果分类不存在 ，则创建 */
        if (firstByNameAndParentIdIsNullAndParentIdsIsNull == null) {
            logger.info("/* initTypeEntityFirstRate -> In creation -> " + name + "*/");
            Type articleType = Type.builder().name(name).build();
            return typeRepo.save(articleType);
        } else {
            return firstByNameAndParentIdIsNullAndParentIdsIsNull;
        }
    }

    /**
     * 创建下级分类
     *
     * @param name
     * @param parent
     * @return
     */
    private Type initTypeEntityLowerLevel(String name, Type parent) {
        Type firstByNameAndParentId = typeRepo.findFirstByNameAndParentId(name, parent.getId());
        /* 如果分类不存在 ，则创建 */
        if (firstByNameAndParentId == null) {
            logger.info("/* initTypeEntityLowerLevel -> In creation -> " + parent.getName() + " -> " + name + "*/");
            Type articleType = Type.builder().name(name).build();
            articleType.addParentIds(parent);
            return typeRepo.save(articleType);
        } else {
            return firstByNameAndParentId;
        }
    }

    /**
     * 递归取出每一个关联信息
     *
     * @param next 下一个节点
     * @param type 父级信息
     */
    private void nextInitType(Object next, Type type) {
        if (next instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) next;
            map.forEach((key, value) -> {
                Type lowerLevel = initTypeEntityLowerLevel(key, type);
                nextInitType(value, lowerLevel);
            });
        }
    }

    /**
     * 设置默认分页从从1开始
     */
    private void setOneIndexedParameters() {
        PageableHandlerMethodArgumentResolver bean = SpringContextUtil.getBean(PageableHandlerMethodArgumentResolver.class);
        bean.setOneIndexedParameters(true);
    }
}
