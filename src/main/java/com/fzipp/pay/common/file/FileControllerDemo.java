package com.fzipp.pay.common.file;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName FileController
 * @Description 文件上传下载
 * @Author 24k
 * @Date 2021/12/31 14:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileControllerDemo {
    private static final Logger logger = LoggerFactory.getLogger(FileControllerDemo.class);


    @RequestMapping("/form")
    public String uploadFile(@RequestParam(value = "imgTitle", required = false) String imgTitle, @RequestParam(value = "imgContent", required = false) String imgContent, @RequestParam(value = "file", required = false) MultipartFile[] multipartFiles, final HttpServletResponse response, final HttpServletRequest request) {

        System.out.println("文字："+imgTitle+","+imgContent);

        //在文件操作中，不用/或者\最好，推荐使用File.separator根据不同系统进行获取文件目录分隔符
        String rootPath = null;
        try {
            rootPath = ResourceUtils.getURL("classpath:").getPath()+"static/upload/profile/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(rootPath);
        File fileDir = new File(rootPath);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                for (int i = 0; i < multipartFiles.length; i++) {
                    try {
                        //以原来的名称命名,覆盖掉旧的
                        String storagePath = rootPath + multipartFiles[i].getOriginalFilename();
                        logger.info("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                + "，保存的路径为：" + storagePath);
                        Streams.copy(multipartFiles[i].getInputStream(), new FileOutputStream(storagePath), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            return "error";
        }
        return "ok";
    }
}
