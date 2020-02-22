package com.hlh;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.entity.*;
import com.hlh.domain.view.ArticleInfoView;
import com.hlh.repo.*;
import com.hlh.repo.view.ArticleInfoViewRepo;
import com.hlh.util.httpclient.HttpResult;
import com.hlh.util.httpclient.HttpUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

@SpringBootTest
class AuthorizeControllerTests {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testInsertLogin() {
        Login login = loginRepo.save(Login.builder()
                .username("huxiaomei")
                .password("Hlh967484")
                .phone("17378777831")
                .email("2815888042@qq.com")
                .role(1213463237765500928L)
                .build());

        System.out.println(login.getId());

        User user = User.builder().build();
        User save = userRepo.save(user);
    }

    @Test
    public void testSelectLogin() {
        Login firstById = loginRepo.findFirstById(2L);
        System.out.println(firstById);
    }

    @Autowired
    private RoleRepo roleRepo;

    @Test
    public void testInsertRole() {
        HashMap<String, Boolean> identity = new HashMap<>();
        identity.put("/**", true);
        roleRepo.save(Role.builder()
                .publisher(1L)
                .name("超级管理员")
                .identity(identity)
                .build());
    }

    @Autowired
    private TypeRepo typeRepo;

    @Test
    public void testTypeInsert() {
        Optional<Type> byId = typeRepo.findById(1214437035855712256L);
        if (byId.isPresent()) {
            Type type1 = Type.builder().name("默认子类").build();
            type1.addParentIds(byId.get());
            typeRepo.save(type1);
        }
    }

    @Test
    public void testTypeQuery() {
        Optional<Type> byId = typeRepo.findById(1214437035855712256L);
        if (byId.isPresent()) {
            System.out.println(byId.get());
        }
    }

    @Autowired
    private ArticleRepo articleRepo;

    @Test
    public void testArticleInsert() {
        Article article = Article.builder()
                .author("胡隆河")
                .content("测试内容！")
                .excerpt("摘录")
                .title("测试")
                .type(1214437035855712256L)
                .build();
        Article save = articleRepo.save(article);
        System.out.println(save);
    }

    @Autowired
    private ArticleInfoViewRepo articleInfoViewRepo;

    @Test
    public void testArticleQuery() {
        Optional<ArticleInfoView> byId = articleInfoViewRepo.findById(1214446851403681792L);
        if (byId.isPresent()) {
            System.out.println(byId.get());
        } else {
            System.out.println("No Data!");
        }
    }

    @Autowired
    private FinalPoolCfg finalPoolCfg;

    @Test
    public void testGetConfigFile() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        URL resource = this.getClass().getClassLoader().getResource("application-" + finalPoolCfg.getEnvironment() + ".yaml");
        assert resource != null;
        HashMap<String, Object> map = yaml.load(new FileInputStream(resource.getFile()));
        map = (HashMap<String, Object>) map.get("mconfig");
        map = (HashMap<String, Object>) map.get("final");
        map = (HashMap<String, Object>) map.get("initTypeEntity");

        map.forEach((key, value) -> {
            System.out.println("一级： " + key);
            nextType(value, key);
        });
    }

    private void nextType(Object next, String parent) {
        if (next instanceof Boolean) {
            System.out.println(parent + " -> 无下级");
        } else if (next instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) next;
            map.forEach((key, value) -> {
                System.out.println("父级： " + parent + " -> 下级: " + key);
                nextType(value, key);
            });
        } else {
            System.out.println("异常： " + parent + " -> " + next);
        }
    }

    @Test
    public void testYamlUpdate() throws FileNotFoundException {
//        URL resource = this.getClass().getClassLoader().getResource("application-" + finalPoolCfg.getEnvironment() + ".yaml");
//        System.out.println(resource.getFile());
//        HashMap<String, Object> load = YamlUtil.load(VarPool.cfgMap, "mconfig.final");
//        load.put("initTypeEntity", true);
//        YamlUtil.update("application-" + finalPoolCfg.getEnvironment() + ".yaml", VarPool.cfgMap);
    }



    @Autowired
    private HttpUtil httpUtil;

    @Test
    public void testHttp() throws Exception {
        Map<String, Object> params = new HashMap<>();
        HttpResult httpResult = httpUtil.doPost("http://127.0.0.1:30002/test/testPassengerInfo?mobileNo=测试&userId=测试1", params);
        System.out.println(httpResult);
    }
}