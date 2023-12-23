package team.glhf.salus.service;

import org.springframework.web.multipart.MultipartFile;
import team.glhf.salus.dto.common.LocationRes;
import team.glhf.salus.enumeration.FilePathEnum;

import java.util.Collection;
import java.util.List;

/**
 * @author Felix
 * @since 2023/11/4
 */
public interface CommonService {
    /**
     * 发送邮件验证码
     */
    void sendEmailCode(String to, String code);

    /**
     * 发送手机验证码
     */
    void sendSmsCode(String phone, String code);

    /**
     * 上传一张图片
     */
    String uploadImage(MultipartFile file, FilePathEnum pathEnum);

    /**
     * 批量图片
     */
    List<String> uploadAllImages(Collection<MultipartFile> files, FilePathEnum pathEnum);

    /**
     * 经纬度逆向地址获取
     */
    LocationRes getLocation(String position);
}
