package team.glhf.salus.controller;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.ServletOutputStream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.glhf.salus.dto.common.LocationReq;
import team.glhf.salus.dto.common.LocationRes;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.CommonException;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.CommonService;

import java.io.IOException;
import java.io.InputStream;

/**
 * Common controller for web application
 *
 * @author Felix
 * @since 2023/11/4
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/{fileName}")
    public void getStatic(@PathVariable String fileName, ServletOutputStream outputStream) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("/static/" + fileName);
            InputStream inputStream = resources[0].getInputStream();
            IoUtil.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new CommonException(HttpCodeEnum.REQUEST_ERROR);
        }
    }

    @GetMapping("/geometry")
    public Result<LocationRes> geometry(@Validated LocationReq locationReq) {
        return Result.okResult(commonService.getLocation(locationReq.getPosition()));
    }
}
