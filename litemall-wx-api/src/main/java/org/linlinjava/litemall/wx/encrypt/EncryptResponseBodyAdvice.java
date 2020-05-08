package org.linlinjava.litemall.wx.encrypt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.sf.json.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.wx.util.LocalDateTimeSerializer;
import org.linlinjava.litemall.wx.web.WxGoodsController;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;

@Component
@ControllerAdvice(basePackages = "org.linlinjava.litemall.wx.web")

public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Resource
    private AESPk7 aesPk7;
    private final Log logger = LogFactory.getLog(WxGoodsController.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (obj instanceof UrlResource || obj instanceof File) {
            return obj;
        }
        if (serverHttpRequest.getURI().getPath().contains("wx/storage/upload")){
            // 排除上传文件的返回结果,小程序上传是通过wx转发上传的，因此没统一解密
            return obj;
        }
        //通过 ServerHttpRequest的实现类ServletServerHttpRequest 获得HttpServletRequest
        ServletServerHttpRequest sshr = (ServletServerHttpRequest) serverHttpRequest;
        //此处获取到request 是为了取到在拦截器里面设置的一个对象 是我项目需要,可以忽略
        HttpServletRequest request = sshr.getServletRequest();
        String encryptResult = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //添加encry header，告诉前端数据已加密
            serverHttpResponse.getHeaders().add("encrypt", "true");
            JavaTimeModule timeModule = new JavaTimeModule();
            timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            objectMapper.registerModule(timeModule);


            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            //加密
            encryptResult = aesPk7.encrypt(result);
            logger.info("接口=" + request.getRequestURI() + ",原始数据=" + result + ",加密后数据=" + encryptResult);

        } catch (Exception e) {
            logger.error("异常！", e);
        }
        return encryptResult;
    }
}