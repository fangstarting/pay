package com.fzipp.pay.controller.file;

import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.StaticResourceConfig;
import com.fzipp.pay.common.file.MyFileUtils;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.myService.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;


@Controller
@CrossOrigin
public class LoadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadController.class);

    @Autowired
    private UploadService uploadService;

    /**
     * 文件上传-头像 && 删除服务源头像文件
     * @param multipartFile
     * @return data:url
     */
    @PostMapping("/load/up/profile")
    @ResponseBody
    public Result uploadProfile(@RequestParam(value = "file",required = false) MultipartFile multipartFile){
        Result result = new Result();
        if (multipartFile==null||multipartFile.isEmpty()){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.UPLOAD_PRO_NULL);
            return result;
        }
        return uploadService.uploadImg(multipartFile);
    }

    @Autowired
    private HttpServletResponse response;


    @Value("${file.dri.profile}")
    private String driProfile;


    /**
     * 头像加载 公开API
     * @return
     */
    @GetMapping(value = StaticResourceConfig.MAPPING_STATIC_RESOURCE_PROFILE+"{filename}")
    public void staticResourceProfile(@PathVariable String filename){
        try {
            MyFileUtils.download(driProfile+filename,null, MyFileUtils.Disposition.inline,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("${file.dri.root}")
    private String driRoot;

    /**
     * 资源加载 公开API
     * @return
     */
    @GetMapping(value = StaticResourceConfig.MAPPING_STATIC_RESOURCE) //  "/load/static/resource/
    public void staticResource(String path){
        try {
            MyFileUtils.download(driRoot+path,null, MyFileUtils.Disposition.inline,response);
        } catch (IOException e) {
            return;
        }
    }

    @Value("${file.dri.root}")
    private String rootPath;

    /**
     * 文件下载 非公开API
     * @param filePath
     * @return
     */
    @GetMapping("/load/down")
    public void downloadProfile(@RequestParam("filePath") String filePath){
        try {
            String downloadPath = rootPath+ filePath;
            String downFileName = null;
            MyFileUtils.download(downloadPath,downFileName,MyFileUtils.Disposition.attachment,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件下载 非公开API
     * @param filename 文件相对于root的服务器绝对路径名
     * @return
     * @throws IOException
     */
    @GetMapping("/load/download")
    @ResponseBody
    public ResponseEntity download(String filename){
        try {
            //下载文件的路径(这里绝对路径)
            String filepath= rootPath+filename;
            File file =new File(filepath);
            return MyFileUtils.commonExport(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private ResponseEntity test(String filename) {
        try {
            //下载文件的路径(这里绝对路径)
            String filepath= rootPath+filename;
            File file =new File(filepath);

            //*******************11111111111111111111

            //创建字节输入流，这里不实用Buffer类
//            InputStream in = new FileInputStream(file);
            //available:获取输入流所读取的文件的最大字节数
//            byte[] body = new byte[in.available()];
            //把字节读取到数组中
//            in.read(body);
            //设置请求头
//            MultiValueMap<String, String> headers = new HttpHeaders();
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment;filename=" + file.getName());
//        headers.add("ContentType","image/png");
//            headers.add("Content-Type","application/octet-stream");
//            headers.add("Content-Length",String.valueOf(file.length()));
            //设置响应状态
//            HttpStatus statusCode = HttpStatus.OK;
//            in.close();
//            ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
//            return entity;//返回
//            return ResponseEntity
//                    .ok()
//                    .header(String.valueOf(headers))
//                    .contentLength(file.length())
//                    .contentType(MediaType.parseMediaType("application/octet-stream"))
//                    .body(new FileSystemResource(file));


            //*******************2222222222222222222222

            InputStream inputStream = new FileInputStream(file);
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Access-Control-Expose-Headers", "Content-Disposition"); // 暴露 Content-Disposition
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(file.getName(),"UTF-8")));
            headers.add("Pragma", "no-cache");
            headers.add("Content-Length",String.valueOf(file.length()));
            headers.add("Expires", "0");
            headers.add("Content-Type","application/vnd.ms-excel;charset=utf-8");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(inputStreamResource);

            //***********************33333333333333333333

            //Excel下载设置contentType为vnd.ms-excel
//            headers.add("Content-Type","application/vnd.ms-excel;charset=utf-8");
//            headers.add("Content-Type","application/octet-stream");
//            //创建字节输入流，这里不实用Buffer类
//            InputStream in = new FileInputStream(file);
//            //available:获取输入流所读取的文件的最大字节数
//            byte[] body = new byte[in.available()];
//            //把字节读取到数组中
//            in.read(body);
//            return new ResponseEntity<byte[]>(body,headers, HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;//返回
    }

}
