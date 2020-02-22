package com.hlh.util.longjing;

import com.hlh.config.FinalPoolCfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileHelper {

    @Autowired
    private FinalPoolCfg finalPool;

    /**
     * 获取上传文件保存根路径
     *
     * @return
     */
    public String getUploadRootPath() {
        return System.getProperty("os.name")
                .toLowerCase().startsWith("win")
                ? finalPool.getUploadSavePathWin()
                : finalPool.getUploadSavePathLinux();
    }

    /**
     * 临时文件夹路径
     *
     * @param rootPath
     * @return
     */
    public String getTempDirPath(String rootPath) {
        return rootPath + "temp//" + System.currentTimeMillis() + "//";
    }

    /**
     * 创建文件名
     *
     * @param fr 前缀
     * @param ex 后缀
     * @return
     */
    public String createFileName(String fr, String ex) {
        return fr + "-" + System.currentTimeMillis() + "." + ex;
    }

    /**
     * 获取上一级目录名称
     *
     * @param file
     * @return
     */
    public String getFileParentName(File file) {
        return new File(file.getParent()).getName();
    }

    /**
     * 获取LOGO文件地址
     *
     * @return
     */
    public String getLogoFilePath() {
        return System.getProperty("os.name")
                .toLowerCase().startsWith("win")
                ? finalPool.getLogoFilePathWin()
                : finalPool.getLogoFilePathLinux();
    }

}
