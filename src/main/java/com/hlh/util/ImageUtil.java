package com.hlh.util;


import com.hlh.config.FinalPoolCfg;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ProjectName: 宇石科技智能控制软件后端
 * @Package: com.yskj.mf.util
 * @ClassName: ImageUtil
 * @Author: Laohu
 * @Description: 图片处理
 * @Date: 2019/7/27 11:49
 * @Version: 1.0
 */

/**
 * 图片压缩工具类
 **/
@Component
public class ImageUtil {

    @Autowired
    private FinalPoolCfg finalPool;

    // 图片默认缩放比率
    private static final double DEFAULT_SCALE = 0.8d;

    /**
     * 生成缩略图到指定的目录
     *
     * @param path  目录
     * @param files 要生成缩略图的文件列表
     * @throws IOException
     */
    public List<String> generateThumbnail2Directory(String path, String... files) throws IOException {
        return generateThumbnail2Directory(DEFAULT_SCALE, path, files);
    }

    /**
     * 生成缩略图到指定的目录
     *
     * @param scale    图片缩放率
     * @param pathname 缩略图保存目录
     * @param files    要生成缩略图的文件列表
     * @throws IOException
     */
    public List<String> generateThumbnail2Directory(double scale, String pathname, String... files) throws IOException {
        Thumbnails.of(files)
                // 图片缩放率，不能和size()一起使用
                .scale(scale)
                // 缩略图保存目录,该目录需存在，否则报错
                .toFiles(new File(pathname), Rename.SUFFIX_HYPHEN_THUMBNAIL);

        List<String> list = new ArrayList<>(files.length);
        for (String file : files) {
            list.add(appendSuffix(file, finalPool.getImagesSuffix()));
        }

        return list;
    }

    /**
     * 将指定目录下所有图片生成缩略图
     *
     * @param pathname 文件目录
     */
    public void generateDirectoryThumbnail(String pathname) throws IOException {
        generateDirectoryThumbnail(pathname, DEFAULT_SCALE);
    }

    /**
     * 将指定目录下所有图片生成缩略图
     *
     * @param pathname 文件目录
     */
    public void generateDirectoryThumbnail(String pathname, double scale) throws IOException {
        File[] files = new File(pathname).listFiles();
        compressRecurse(files, pathname);
    }

    /**
     * 文件追加后缀
     *
     * @param fileName 原文件名
     * @param suffix   文件后缀
     * @return
     */
    public String appendSuffix(String fileName, String suffix) {
        String newFileName = "";

        int indexOfDot = fileName.lastIndexOf('.');

        if (indexOfDot != -1) {
            newFileName = fileName.substring(0, indexOfDot);
            newFileName += suffix;
            newFileName += fileName.substring(indexOfDot);
        } else {
            newFileName = fileName + suffix;
        }

        return newFileName;
    }


    private void compressRecurse(File[] files, String pathname) throws IOException {
        for (File file : files) {
            // 目录
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                compressRecurse(subFiles, pathname + File.separator + file.getName());
            } else {
                // 文件包含压缩文件后缀或非图片格式，则不再压缩
                String extension = getFileExtention(file.getName());
                if (!file.getName().contains(finalPool.getImagesSuffix()) && isImage(extension)) {
                    generateThumbnail2Directory(pathname, file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 根据文件扩展名判断文件是否图片格式
     *
     * @param extension 文件扩展名
     * @return
     */
    public boolean isImage(String extension) {
        if (null == finalPool.getImageExtension() || "".equals(finalPool.getImageExtension()) || "*".equals(finalPool.getImageExtension())) {
            return true;
        }

        String[] imageExtension = finalPool.getImageExtension().split(",");

        for (String e : imageExtension) {
            if (extension.toLowerCase().equals(e)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileExtention(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }

    /**
     * 计算两个颜色的相似度
     *
     * @param a
     * @param b
     * @return
     */
    public int colorAberration(Color a, Color b) {
        return (a.getRed() - b.getRed()) ^ 2 + (a.getGreen() - b.getGreen()) ^ 2 + (a.getBlue() - b.getBlue()) ^ 2;
    }
}