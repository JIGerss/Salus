import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.region.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.glhf.salus.properties.CosProperty;

import java.io.File;
import java.util.UUID;

/**
 * @author Felix
 * @since 2023/11/1
 */
@SuppressWarnings("all")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CosTest.class)
public class CosTest {
    private final CosProperty cosProperty;

    public CosTest(CosProperty cosProperty) {
        this.cosProperty = cosProperty;
    }

    public COSClient initCOSClient(){
        COSCredentials cred = new BasicCOSCredentials(cosProperty.secretId, cosProperty.secretKey);
        Region region = new Region(cosProperty.region);
        ClientConfig clientConfig = new ClientConfig(region);
        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    /**
     * 上传文件
     */
    @Test
    public void upLoad(){
        try {
            String filePath = "D:\\Photos\\winter.png";
            // 指定要上传的文件
            File localFile = new File("D:\\Photos\\winter.png");
            // 指定要上传到 COS 上对象键
            String key = getFileKey(filePath);
            System.out.println(key + "key----------------------");
            System.out.println("url------------" + cosProperty.url + "/"+ key);
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }


    /**
     * 生成文件路径
     *
     */
    private String getFileKey(String originalfileName) {
        String filePath = "test/";
        String fileType = originalfileName.substring(originalfileName.lastIndexOf("."));
        return filePath + UUID.randomUUID()+fileType;
    }

}
