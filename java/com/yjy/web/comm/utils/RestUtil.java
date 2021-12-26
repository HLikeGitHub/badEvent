package com.yjy.web.comm.utils;

import com.yjy.web.comm.result.Result;
import com.yjy.web.comm.result.ResultCode;
import com.yjy.web.comm.utils.crypt.aes.AESUtil;
import com.yjy.web.comm.utils.properties.AppPropUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @info RestUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class RestUtil {

    /**
     * 调用服务
     * @param logger
     * @param logPrefix
     * @param url
     * @param responseEntity 外部测试可以传入
     * @return
     */
    public static Result svrCallGet(Logger logger, String logPrefix, String url, ResponseEntity<String> responseEntity){
        logger.info(logPrefix + "req-url=" + url);

        if(responseEntity == null){
            try {
                RestTemplate restTemplate = BeanUtil.getBean(RestTemplate.class);
                responseEntity = restTemplate.getForEntity(url, String.class);
            } catch (Exception e) {
                logger.error(logPrefix + e.getClass());
                e.printStackTrace();
            }
        }

        return getResult(logger, logPrefix, responseEntity, false);
    }

    /**
     * 获取结果
     * @param logger
     * @param logPrefix
     * @param responseEntity
     * @param toDecrypt
     * @return
     */
    private static Result getResult(Logger logger, String logPrefix, ResponseEntity<String> responseEntity, boolean toDecrypt){
        //如果不是测试的调用生成，生成后还需要判断是否null
        if(responseEntity != null){
            int statusCode = responseEntity.getStatusCodeValue();
            logger.info(logPrefix + "statusCode=" + statusCode);
            logger.info(logPrefix + "headers=" + responseEntity.getHeaders());
            String body = responseEntity.getBody();
            logger.info(logPrefix + "body=" + body);

            if (statusCode == 200) {
                //调用成功
                try {
                    Result result = JSONUtil.toObject(body, Result.class);
                    if(toDecrypt){
                        Object obj = result.getData();
                        if(obj != null){
                            String data = obj.toString();
                            data = AESUtil.decrypt(data, AppPropUtil.getInstance().getAes());
                            result.setData(data);
                        }
                    }
                    return result;
                } catch (Exception e) {
                    logger.error(logPrefix + e.getClass());
                    e.printStackTrace();
                }
            }
        }
        return new Result(ResultCode.ERROR_SERVICE_ERR0R);
    }

    public static Result svrCallPostBody(Logger logger, String logPrefix, String url, boolean toEncrypt, String json){
        logger.info(logPrefix + "req-url=" + url+", body="+json);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        if(toEncrypt){
            try{
                json = AESUtil.encrypt(json, AppPropUtil.getInstance().getAes());
            }catch (Exception e){
                logger.error(logPrefix + "error, e="+e.getClass());
                e.printStackTrace();
                return new Result(ResultCode.ERROR_SERVICE_ERR0R);
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(json, headers);

        ResponseEntity<String> responseEntity = null;
        try{
            RestTemplate restTemplate = BeanUtil.getBean(RestTemplate.class);
            responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return getResult(logger, logPrefix, responseEntity, toEncrypt);
    }

}
