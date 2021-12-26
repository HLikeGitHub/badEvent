package com.yjy.web.comm.utils.crypt.aes;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author rwx
 * @info DecryptRequestBodyAdvice
 * @email aba121mail@qq.com
 */
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Value("${my.profiles.aes}")
    private String aesKey;

    private String encoding = "UTF-8";


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if(CryptoAnnotationUtil.need2Decrypt(parameter)){
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    String content = "";
                    List<String> lines = IOUtils.readLines(inputMessage.getBody(), encoding);
                    if(lines != null && lines.size() != 0){
                        for(String line : lines){
                            content += line;
                        }
                    }

                    try{
                        content = AESUtil.decrypt(content, aesKey);
                    }catch (Exception e){
                        logger.error("decrypt body error, e="+e.getClass().getName());
                        e.printStackTrace();
                    }

                    return new ByteArrayInputStream(content.getBytes(encoding));
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        }

        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
