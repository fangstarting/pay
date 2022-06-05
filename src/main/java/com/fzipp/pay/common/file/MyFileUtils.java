package com.fzipp.pay.common.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 文件处理通用工具类<br>
 * 依赖：
 * <dependency>
 * <groupId>commons-fileupload</groupId>
 * <artifactId>commons-fileupload</artifactId>
 * <version>1.4</version>
 * </dependency>
 * 在文件操作中，不用/或者\最好，推荐使用File.separator根据不同系统进行获取文件目录分隔符
 *
 * @author FengFang
 * @since 2022-5-12
 */
@Component
public class MyFileUtils {

    /**
     * buffer size in bytes
     */
    private final static int BUFFER_SIZE = 8192;

    private static final Logger LOGGER = LoggerFactory.getLogger(MyFileUtils.class);


    /**
     * 响应返回文件的方式
     */
    public enum Disposition {
        /**
         * 在线打开
         */
        inline,
        /**
         * 附件方式下载
         */
        attachment
    }

    /**
     * 通用文件导出
     * @param file
     * @return
     * @throws Exception
     */
    public static ResponseEntity commonExport(File file) throws Exception {
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
    }



    /**
     * 文件上传处理
     *
     * @param multipartFile 源文件
     * @param uploadDir     上传目录 例如--> "D:/data/temp/profile/"
     * @param fileName      文件名称(不含后缀) 例如--> "name123"; Null:源文件名
     *                      //     * @return 上传成功为: true ,否则为: false
     * @throws IOException
     */
    public static void upload(@NotNull MultipartFile multipartFile, @NotNull String uploadDir, String fileName) throws IOException {
        LOGGER.info("文件上传处理>>>");
        // 获取源文件名
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String extension = "." + FilenameUtils.getExtension(originalFilename).toLowerCase();
        // 新的文件名称
        String newFileName;
        if (fileName == null || "".equals(fileName)) {
            newFileName = originalFilename;
        } else {
            newFileName = fileName + extension;
        }
        // 获取文件大小
        long size = multipartFile.getSize();
        // 根据上传路径生成目录
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();// 若目录不存在则创建目录--mkdirs()可建立多级文件夹/mkdir()只建立一级文件夹
        // 处理上传
        multipartFile.transferTo(new File(dir, newFileName));
        LOGGER.info("文件上传处理完毕>>>上传路径：" + uploadDir + newFileName + ",文件大小：" + size);
    }

    /**
     * 文件下载处理
     *
     * @param downloadPath 下载路径 例如--> "D:/data/temp/profile/user.txt"
     * @param downFileName 下载文件的默认名称(不含后缀) 例如--> "name123"; Null:源文件名
     * @param disposition  响应返回文件的方式：attachment表示以附件方式下载；inline表示在线打开
     * @param response
     * @throws IOException
     */
    public static void download(@NotNull String downloadPath, String downFileName, @NotNull Disposition disposition, @NotNull HttpServletResponse response) throws IOException {
        LOGGER.info("文件下载处理>>>下载路径：" + downloadPath);
        // 获取下载的文件对象
        File file = new File(downloadPath);
        // 判断抽象文件路径是否为file
        if (!file.isFile()) {
            LOGGER.info("文件路径无效");
            return;
        }
        // 下载文件名
        if (downFileName == null || "".equals(downFileName)) {
            downFileName = file.getName();
        } else {
            // 获取文件后缀名
            String extension = "." + FilenameUtils.getExtension(file.getName()).toLowerCase();
            // 拼接下载文件名
            downFileName = downFileName + extension;
        }
        // 将文件写入输入流
        FileInputStream inputStream = new FileInputStream(file);
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        // Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        // attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("content-disposition", disposition + ";filename=" + URLEncoder.encode(downFileName, "UTF-8"));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
//        response.setContentType("application/octet-stream");
        // 获取响应输出流
        ServletOutputStream outputStream = response.getOutputStream();
        // 文件拷贝
        try {
            IOUtils.copy(inputStream, outputStream);  //正常Error：java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
        }catch (Exception e){
            LOGGER.error("文件拷贝异常>>>已被System中止拷贝文件");
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            return;
        }
        // 关闭流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        LOGGER.info("文件下载处理完毕>>>");
    }

    /**
     * 下载网络文件到本地
     * 功能描述: 网络文件下载到服务器本地
     *
     * @param path       下载后的文件路径和名称
     * @param netAddress 文件所在网络地址
     */
    public static void downloadNet(String netAddress, String path) throws IOException {
        URL url = new URL(netAddress);
        URLConnection conn = url.openConnection();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path));
        int copySize;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((copySize = bufferedInputStream.read(buffer)) > 0) {
            bufferedOutputStream.write(buffer, 0, copySize);
        }
        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

    /**
     * 本地图片转换成base64字符串
     *
     * @param imageName 图片名称
     * @reture 图片Base64
     * @author Byr
     * @dateTime 2019-03-07
     */
    public static String ImageToBase64ByLocal(String imageName) throws IOException {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            //获取图片路径
            Resource resource = new ClassPathResource("/static/imageSeal/" + imageName + ".png");
            File file = resource.getFile();
            in = new FileInputStream(file.getPath());

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 删除文件，可以是单个文件或文件夹
     *
     * @param filePath 待删除的文件名
     * @return 文件删除成功返回true, 否则返回false
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("删除文件失败：" + filePath + "文件不存在");
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(filePath);
            } else {
                return deleteDirectory(filePath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
            LOGGER.info("删除单个文件" + filePath + "成功！");
            return true;
        } else {
            LOGGER.info("删除单个文件" + filePath + "失败！");
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true, 否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            LOGGER.info("删除目录失败" + dir + "目录不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
            }
            // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
            }
            if (!flag) {
                break;
            }
        }

        if (!flag) {
            LOGGER.info("删除目录失败");
            return false;
        }

        // 删除当前目录
        if (dirFile.delete()) {
            LOGGER.info("删除目录" + dir + "成功！");
            return true;
        } else {
            LOGGER.info("删除目录" + dir + "失败！");
            return false;
        }
    }

    // 删除文件夹
    // param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除指定文件夹下所有文件
    // param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
