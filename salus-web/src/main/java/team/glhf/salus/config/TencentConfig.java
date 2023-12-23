package team.glhf.salus.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.glhf.salus.properties.CosProperty;
import team.glhf.salus.properties.SmsCodeProperty;

/**
 * @author Felix
 * @since 2023/11/4
 */
@Data
@Configuration
public class TencentConfig {
    /**
     * Cos client generator
     */
    @Bean
    public COSClient cosClient(CosProperty cosProperty){
        COSCredentials cred = new BasicCOSCredentials(cosProperty.secretId, cosProperty.secretKey);
        Region region = new Region(cosProperty.region);
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    /**
     * Sms client generator
     */
    @Bean
    public SmsClient smsClient(SmsCodeProperty property) {
        Credential cred = new Credential(property.secretId, property.secretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        httpProfile.setEndpoint("sms.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, "ap-guangzhou", clientProfile);
    }
}
