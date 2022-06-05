package com.fzipp.pay.service.myService;

import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.LogNotes;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.file.MyFileUtils;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.LocalHostUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.Mess;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.MessService;
import com.fzipp.pay.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    @Value("${path.down.profile}")
    private String pathDownProfile;
    @Value("${path.down.profileLast}")
    private String pathDownProfileList;

    @Value("${file.dri.profile}")
    private String driProfile;

    @Autowired
    private UserService userService;

    @Autowired
    private MessService messService;

    @Autowired
    private MultipartProperties multipartProperties;

    @Autowired
    private RequestUtil requestUtil;


    /**
     * 头像图片上传 && 删除服务源头像文件
     *
     * @param multipartFile
     * @return java.lang.String
     * @Discription: MultipartFile对象是一个SpringMVC提供的文件上传接收类
     * 其底层会自动去与HttpServletRequest request中的request.getInputStream()融合，从而实现文件上传
     * 所以：文件上传底层原理就是  request.getInputStream()
     */
    public Result uploadImg(MultipartFile multipartFile) {
        Result result = new Result();
        String type = multipartFile.getContentType();
        if (!(type.equals(SysProp.PROFILE_TYPE_JPEG) || type.equals(SysProp.PROFILE_TYPE_PNG))) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.UPLOAD_PRO_TYPE_NO);
            return result;
        }
        String originalFilename = multipartFile.getOriginalFilename();//获取源文件名
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(SysProp.DECIMAL_POINT)).toLowerCase();//截取文件后缀类型

//        String rootPath = multipartProperties.getLocation() + SysProp.PROFILE_PATH;//获取头像文件存储路径
        String rootPath = driProfile;//获取头像文件存储路径

        String yyyyMMddHHmmss = DateUtil.getStrDateFormat(new Date(), "yyyyMMddHHmmss");
        String proName = requestUtil.getUsername() + yyyyMMddHHmmss + fileSuffix;//文件名：username+文件后缀
        //最终存储路径
        String storagePath = rootPath + proName;
        File targetFile = new File(storagePath);
        try {
            if (!targetFile.exists()) {//若目录不存在则创建目录--mkdirs()可建立多级文件夹/mkdir()只建立一级文件夹
                targetFile.mkdirs();
            }
            multipartFile.transferTo(targetFile);
            logger.info("文件上传成功，上传路径：" + storagePath);
            //删除服务器源文件
            String profile = userService.getById(requestUtil.getUserId()).getProfile();
            String filePath = driProfile+profile;
            if(!SysProp.DEFAULT_USER_PROFILE.equalsIgnoreCase(profile)){
                boolean b = MyFileUtils.deleteFile(filePath);
                result.setSign(b);
            }else {
                result.setSign(true);
            }
            result.setError(ErrorCode.CORRECT);

            String localIP = LocalHostUtil.getLocalIP();
            localIP = SysProp.HTTP_TOP+localIP+pathDownProfileList;
//            result.setData(pathDownProfile + proName);
            result.setData(localIP + proName);
            result.setMessage(Messages.UPLOAD_PRO_Y);
            this.optionSql(proName);//更新数据库信息
        } catch (IOException e) {
            e.printStackTrace();
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.UPLOAD_PRO_N);
        }
        return result;
    }

    private void optionSql(String proName) {
        User user1 = requestUtil.getUser();
        User user = new User();
        user.setUserId(user1.getUserId());
        user.setProfile(proName);
        user.setUpdatetime(new Date());
        userService.updateById(user);
        Mess mess = new Mess();
        mess.setUserId(user1.getUserId());
        mess.setStatus(SysProp.MESS_STATUS_NO);
        mess.setIsSystem(SysProp.MESS_IS_SYSTEM_YES);
        mess.setUpdatetime(new Date());
        mess.setTitle(LogNotes.MESS_SYS_UP_PRO_TITLE);
        mess.setMatter(LogNotes.MESS_SYS_UP_PRO_MATTER(user1.getRealname(), user1.getSex()));
        messService.save(mess);
    }
}
