package com.yjy.web.comm.utils.crypt.aes;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author rwx
 * @info desc
 * @mail aba121mail@qq.com
 */
@ControllerAdvice
public class EncryptRespondBodyAdvice implements ResponseBodyAdvice {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Value("${my.profiles.aes}")
    private String aesKey;

    private String encoding = "UTF-8";

    @Override
    public boolean supports(MethodParameter methodParameter, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter parameter, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(CryptoAnnotationUtil.need2Encrypt(parameter)){
            try{
//                logger.info("encrypt data--->>>plain="+body.toString());
                JSONObject jsonObject = JSONObject.parseObject(body.toString());
                if(jsonObject != null && jsonObject.containsKey("data")){
                    String strData = jsonObject.getString("data");
                    String cipherData = AESUtil.encrypt(strData, aesKey);
                    jsonObject.replace("data", cipherData);
                    body = jsonObject.toString();
                    logger.info("encrypt data--->>>cipher="+body.toString());
                }
            }catch (Exception e){
                logger.error("encrypt data--->>>error="+e.getClass());
                e.printStackTrace();
            }
            return body;
        }

        return body;
    }
}