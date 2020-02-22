package com.hlh.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Yaml 文件操作
 */

public class YamlUtil {

    private static String bakDateFat = "";
    private static String bakSuffix = "bak";
    private static String ymalSuffix = "yaml";


    /**
     * 将Yaml文件内容全部转换为Map
     *
     * @param paths 文件地址
     * @return
     * @throws FileNotFoundException
     */
    public static HashMap<String, Object> load(String[] paths) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        HashMap<String, Object> res = new HashMap<>();
        for (String path : paths) {
            URL resource = YamlUtil.class.getClassLoader().getResource(path);
            if (resource != null) {
                res.putAll(yaml.load(new FileInputStream(resource.getFile())));
            }
        }

        return res;
    }

    /**
     * 根据Field内容获取相应的Yaml文件内容
     * field支持多段，如： mconfig.final.type
     *
     * @param path  文件地址
     * @param field 键链
     * @return
     * @throws FileNotFoundException
     */
    public static HashMap<String, Object> load(String path, String field) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        URL resource = YamlUtil.class.getClassLoader().getResource(path);
        assert resource != null && field != null && path.length() > 0 && field.length() > 0;
        HashMap<String, Object> map = yaml.load(new FileInputStream(resource.getFile()));
        return load(map, field);
    }

    /**
     * 根据Field内容获取相应的Map内容
     *
     * @param map   map数据
     * @param field 键链
     * @return
     * @throws FileNotFoundException
     */
    public static HashMap<String, Object> load(HashMap<String, Object> map, String field) {
        HashMap<String, Object> res = null;
        /* 点分 */
        String[] fields = field.split("\\.");
        for (String f : fields) {
            if (res == null && map.get(f) != null && map.get(f) instanceof LinkedHashMap) {
                res = (HashMap<String, Object>) map.get(f);
            } else if (map.get(f) != null && res.get(f) instanceof LinkedHashMap) {
                res = (HashMap<String, Object>) res.get(f);
            }
        }
        return res;
    }

    /**
     * 更新Yaml文件内容
     *
     * @param path 文件地址
     * @param map  更新后的内容
     * @return
     */
    public static void update(String path, HashMap<String, Object> map) throws FileNotFoundException {
        URL resource = YamlUtil.class.getClassLoader().getResource(path);

        /* 操作文件之前先备份源文件 */
        String date = DateUtil.getDate(new Date(), bakDateFat);
        FileUtil.copyFileUsingFileChannels(
                new File(resource.getFile()),
                new File(resource.getFile() + "." + date + "." + bakSuffix)
        );

        /* 准备更新 */
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);

        /* 更新 */
        Yaml yaml = new Yaml(dumperOptions);
        yaml.dump(map, new OutputStreamWriter(new FileOutputStream(resource.getFile())));
    }

    /**
     * 获取yaml文件完整名称
     *
     * @param ev 简要名
     * @return
     */
    public static String[] getFilePath(String ev) {
        String[] evs = ev.split(",");
        for (int i = 0; i < evs.length; i++) {
            evs[i] = "application-" + evs[i].trim() + "." + ymalSuffix;
        }
        return evs;
    }
}
