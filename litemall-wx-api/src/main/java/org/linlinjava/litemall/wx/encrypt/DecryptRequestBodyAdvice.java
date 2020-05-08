//package org.linlinjava.litemall.wx.encrypt;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.linlinjava.litemall.wx.web.WxGoodsController;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.Map;
//
//
//@Component
//@ControllerAdvice(basePackages = "org.linlinjava.litemall.wx.web")
//public class DecryptRequestBodyAdvice implements RequestBodyAdvice {// 仅对requestBody生效
//    @Resource
//    private AESPk7 aesPk7;
//    private final Log logger = LogFactory.getLog(WxGoodsController.class);
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return true;
//    }
//
//    @Override
//    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
//        return inputMessage;
//    }
//
//    @Override
//    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        String decrypt = null;
//        try {
//   ObjectMapper objectMapper = new ObjectMapper();
//            JavaTimeModule timeModule = new JavaTimeModule();
//                    timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
//        objectMapper.registerModule(timeModule);
//            //解密操作
//            Map<String,String> dataMap = (Map)body;
//            String encrypt = dataMap.get("data");
//            decrypt = aesPk7.decrypt(encrypt);
//            logger.error("请求参数解密："+decrypt);
//        } catch (Exception e) {
//            logger.error("异常！", e);
//        }
//        return decrypt;
//    }
//
//
//    @Override
//    public Object handleEmptyBody(@Nullable Object var1, HttpInputMessage var2, MethodParameter var3, Type var4, Class<? extends HttpMessageConverter<?>> var5) {
//        logger.info("空参数");
//        return var1;
//    }
//
//
//}