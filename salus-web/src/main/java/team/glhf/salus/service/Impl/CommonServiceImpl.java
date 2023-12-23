package team.glhf.salus.service.Impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.glhf.salus.dto.common.LocationRes;
import team.glhf.salus.dto.common.location.AddressComponent;
import team.glhf.salus.dto.common.location.LocationResult;
import team.glhf.salus.dto.common.location.Regeocode;
import team.glhf.salus.enumeration.FilePathEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.CommonException;
import team.glhf.salus.exception.EmailCodeException;
import team.glhf.salus.exception.PlaceException;
import team.glhf.salus.exception.SmsCodeException;
import team.glhf.salus.properties.CosProperty;
import team.glhf.salus.properties.EmailCodeProperty;
import team.glhf.salus.properties.MapProperty;
import team.glhf.salus.properties.SmsCodeProperty;
import team.glhf.salus.service.CommonService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Felix
 * @since 2023/11/3
 */
@Service
@SuppressWarnings("DuplicatedCode")
public class CommonServiceImpl implements CommonService {
    private final COSClient cosClient;
    private final SmsClient smsClient;
    private final List<String> mailTemplate;
    private final JavaMailSender javaMailSender;
    private final CosProperty cosProperties;
    private final MapProperty mapProperties;
    private final SmsCodeProperty smsProperties;
    private final EmailCodeProperty emailProperties;

    public CommonServiceImpl(EmailCodeProperty emailProperties, CosProperty cosProperties, MapProperty mapProperties, COSClient cosClient, SmsClient smsClient, JavaMailSender javaMailSender, List<String> mailTemplate, SmsCodeProperty smsProperties) {
        this.emailProperties = emailProperties;
        this.cosProperties = cosProperties;
        this.mapProperties = mapProperties;
        this.cosClient = cosClient;
        this.smsClient = smsClient;
        this.javaMailSender = javaMailSender;
        this.mailTemplate = mailTemplate;
        this.smsProperties = smsProperties;
    }

    @Override
    public void sendEmailCode(String to, String code) {
        // generating a new email
        String textHtml = mailTemplate.get(0)
                .concat(to.substring(0, to.lastIndexOf('@')))
                .concat(mailTemplate.get(1))
                .concat(code)
                .concat(mailTemplate.get(2));
        // sending the email
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject(emailProperties.subject);
            helper.setFrom(emailProperties.from);
            helper.setText(textHtml, true);
            helper.setTo(to);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailCodeException(HttpCodeEnum.EMAIL_SEND_ERROR);
        }
    }

    @Override
    public void sendSmsCode(String phone, String code) {
        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setSmsSdkAppId(smsProperties.appId);
            request.setSignName(smsProperties.signName);
            request.setTemplateId(smsProperties.templateId);
            request.setTemplateParamSet(new String[]{code});
            request.setPhoneNumberSet(new String[]{"+86" + phone});
            smsClient.SendSms(request);
        } catch (TencentCloudSDKException e) {
            throw new SmsCodeException(HttpCodeEnum.SMS_SEND_ERROR);
        }
    }

    @Override
    public String uploadImage(MultipartFile file, FilePathEnum pathEnum) {
        try (InputStream inputStream = file.getInputStream()) {
            // 根据文件前64个字节判断文件类型
            String type = FileTypeUtil.getType(file.getInputStream());
            // 常见图片类型
            switch (type) {
                case "jpg", "apng", "png", "gif", "bmp", "tiff", "webp" -> {
                    String filePath = pathEnum.getPath() + UUID.randomUUID() + "." + type;
                    cosClient.putObject(new PutObjectRequest(cosProperties.bucketName, filePath, inputStream, null));
                    return cosProperties.url + "/" + filePath;
                }
                default -> throw new CommonException(HttpCodeEnum.PARAM_IMAGE_FORMAT_ERROR);
            }
        } catch (IOException e) {
            throw new CommonException(HttpCodeEnum.UPLOAD_ERROR);
        }
    }

    @Override
    public List<String> uploadAllImages(Collection<MultipartFile> files, FilePathEnum pathEnum) {
        return files.stream().map(file -> uploadImage(file, pathEnum)).toList();
    }

    @Override
    public LocationRes getLocation(String position) {
        if (null == position) {
            return null;
        }
        if (!position.matches("^[0-9]{1,4}.?[0-9]{0,6},[0-9]{1,4}.?[0-9]{0,6}$")) {
            throw new PlaceException(HttpCodeEnum.GET_LOCATION_ERROR);
        }
        // 发送外部请求
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("location", position);
        urlVariables.put("key", mapProperties.key);
        urlVariables.put("poitype", mapProperties.poi);
        urlVariables.put("radius", mapProperties.radius);
        urlVariables.put("extensions", "base");
        urlVariables.put("roadlevel", 1);
        try {
            String result = HttpUtil.get("https://restapi.amap.com/v3/geocode/regeo", urlVariables);
            LocationResult positionResult = JSONUtil.toBean(result, LocationResult.class);
            Regeocode regeocode = positionResult.getRegeocode();
            AddressComponent component = regeocode.getAddressComponent();
            return LocationRes.builder()
                    .formattedAddress(regeocode.getFormattedAddress())
                    .city(component.getCity())
                    .cityCode(component.getCitycode())
                    .country(component.getCountry())
                    .district(component.getDistrict())
                    .province(component.getProvince())
                    .township(component.getTownship())
                    .build();
        } catch (Exception e) {
            throw new CommonException(HttpCodeEnum.GET_LOCATION_ERROR);
        }
    }
}
