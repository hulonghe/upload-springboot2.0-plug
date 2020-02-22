package com.hlh.web;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.entity.FileInfo;
import com.hlh.domain.param.FileInfoEditParam;
import com.hlh.domain.view.LoginInfoView;
import com.hlh.repo.FileInfoRepo;
import com.hlh.util.*;
import com.hlh.util.longjing.FileHelper;
import com.hlh.util.rep.RepCode;
import com.hlh.util.rep.RepUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 文件上传接口
 */

@RestController
public class UploadController extends BaseServiceUtil {
    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private FinalPoolCfg finalPool;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private FileInfoRepo fileInfoRepo;

    @Autowired
    private FileHelper fileHelper;

    private RandomUtil randomUtil = RandomUtil.dao;

    /**
     * 单文件上传
     *
     * @param fileInfoEditParam
     * @return
     */
    @PostMapping(value = "/upload")
    public Object upload(
            HttpServletRequest request,
            HttpServletResponse response,
            FileInfoEditParam fileInfoEditParam
    ) {
        Map map = null;
        MultipartFile file = null;
        MultipartHttpServletRequest multipartRequest = null;
        List<MultipartFile> files = null;
        if (request instanceof MultipartHttpServletRequest) {
            multipartRequest = (MultipartHttpServletRequest) (request);
            files = multipartRequest.getFiles("file");
        }

        if (files != null && files.size() > 0) {
            file = files.get(0);
        }

        if ((null != file) && !file.isEmpty()) {
            map = saveFile(request, response, fileInfoEditParam, file, map);
        } else {
            return error(request, RepCode.CODE_UPLOAD_NULL);
        }

        return map;
    }

    /**
     * 单文件上传(用于Layui)
     *
     * @param fileInfoEditParam
     * @return
     */
    @PostMapping("/upload/layui")
    public Object uploadLayui(
            HttpServletRequest request,
            HttpServletResponse response,
            FileInfoEditParam fileInfoEditParam
    ) {
        Map map = null;
        MultipartFile file = null;
        MultipartHttpServletRequest multipartRequest = null;
        List<MultipartFile> files = null;
        if (request instanceof MultipartHttpServletRequest) {
            multipartRequest = (MultipartHttpServletRequest) (request);
            files = multipartRequest.getFiles("file");
        }

        if (files != null && files.size() > 0) {
            file = files.get(0);
        }

        if (null != file && !file.isEmpty()) {
            map = saveFile(request, response, fileInfoEditParam, file, map);
        } else {
            return error(request, RepCode.CODE_UPLOAD_NULL);
        }

        Map<String, Object> map2 = new HashMap<>();
        map2.put("src", map.get("data"));
        return RepUtil.post(map2);
    }

    /**
     * 多文件具体上传时间，主要是使用了MultipartHttpServletRequest和MultipartFile
     *
     * @param request
     * @return
     */
    @PostMapping("/uploads")
    public String uploads(HttpServletRequest request, HttpServletResponse response) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (null != file && !file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream =
                            new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " =>" + e.getMessage();
                }
            } else {
                return "You failed to upload " + i + " becausethe file was empty.";
            }
        }

        return "upload successful";
    }

    /**
     * 保存
     *
     * @param request           HttpServletRequest 请求体
     * @param response          HttpServletResponse 响应体
     * @param fileInfoEditParam FileInfoEditParam 附加参数
     * @param file              MultipartFile 附件
     * @param map               Map 返回参数
     * @return
     */
    private Map saveFile(HttpServletRequest request, HttpServletResponse response, FileInfoEditParam fileInfoEditParam, MultipartFile file, Map map) {
        try {
            // 文件保存根路径
            String uploadPath = fileHelper.getUploadRootPath();
            // 具体的日期文件夹
            String date = DateUtil.getToday() + "//";
            // 文件后缀
            String fileExtention = ImageUtil.getFileExtention(Objects.requireNonNull(file.getOriginalFilename()));
            // 创建文件名
            String fileName = randomUtil.getFileName(fileExtention);
            // 上传文件保存的完整路径
            String filepath = uploadPath + date + fileName;
            // 视频文件缩略图保存路径
            String videoImgPath = null;
            // 如果文件夹不存在,则创建
            FileUtil.isDirExists(new File(uploadPath + date));
            // 新的文件句柄
            File newFile = new File(filepath);
            // 如果文件不存在则创建文件
            FileUtil.isFileExists(newFile);
            // 将上传的文件保存至本地(newFile)中
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
            out.write(file.getBytes());
            out.flush();
            out.close();

            /**
             * 文件处理及上传记录保存
             */
            if (imageUtil.isImage(fileExtention) && file.getSize() > finalPool.getImageMoreScale().toBytes()) {
                List<String> thumbnails = imageUtil.generateThumbnail2Directory(
                        finalPool.getImageScale(),
                        uploadPath + date,
                        filepath
                );

                if (thumbnails.size() > 0) {
                    // 成功则返回压缩图片的访问路径
                    map = RepUtil.post(finalPool.getFileServerUrl() + date + imageUtil.appendSuffix(fileName, finalPool.getImagesSuffix()));
                } else {
                    // 失败则返回源文件的访问路径
                    map = RepUtil.post(finalPool.getFileServerUrl() + date + fileName);
                }
            } else {
                //不是图片,返回源文件路径
                map = RepUtil.post(finalPool.getFileServerUrl() + date + fileName);
            }

            /* 数据持久化 */
            if (fileInfoEditParam.getParent() == null) {
                fileInfoEditParam.setParent("default");
            }
            if (fileInfoEditParam.getParentId() == null) {
                fileInfoEditParam.setParentId(-1L);
            }

            /* 记录信息 */
            LoginInfoView sessionLogin = getSessionLogin(request, null);
            long userid = -1L;
            if (sessionLogin != null && sessionLogin.getLogin() != null) {
                userid = sessionLogin.getLogin().getId();
            } else {
                userid = NetUtil.ipToLong(NetUtil.getIpAddr(request));
            }

            FileInfo build = FileInfo.builder()
                    .sourceName(file.getOriginalFilename())
                    .parent(fileInfoEditParam.getParent())
                    .parentId(fileInfoEditParam.getParentId())
                    .path(filepath)
                    .url(String.valueOf(map.get("data")))
                    .size(file.getSize())
                    .type("image")
                    .publisher(userid)
                    .build();

            map = RepUtil.post(saveFileInfo(build));
        } catch (IOException e) {
            e.printStackTrace();
            map = RepUtil.post(RepCode.CODE_UPLOAD_ERR);
        }
        return map;
    }

    public FileInfo saveFileInfo(FileInfo build) {
        FileInfo save = fileInfoRepo.save(build);
        return save;
    }
}
