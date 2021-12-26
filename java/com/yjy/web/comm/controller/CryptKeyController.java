package com.yjy.web.comm.controller;

import com.yjy.web.comm.result.Result;
import com.yjy.web.comm.result.ResultCode;
import com.yjy.web.comm.utils.JSONUtil;
import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.comm.utils.crypt.RSAUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @info CryptKeyController
 * @author rwx
 * @email aba121mail@qq.com
 */
@Controller
@RequestMapping(value = "comm/crypt")
public class CryptKeyController {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Value("${my.profiles.aes}")
    private String aesKey;

//    @Value("${my.rsa.pri}")
//    private String rsaPrivateKey;

    @RequestMapping(value = "aes/burrow")
    @ResponseBody
    public String getAesKeyCipherText(@RequestParam Map<String, String> param){
        final String logPrefix = "comm/crypt/aes/burrow -> ";

        String clientPubKey = param.get("clientPubKey");
        logger.info(logPrefix+"clientPubKey="+clientPubKey);

        if(TextUtil.isNullOrEmpty(clientPubKey)){
            return JSONUtil.toJSON(new Result(ResultCode.ERROR_PARAMS_MISS));
        }

        //使用客户端的RSA公钥加密AES的KEY传给客户端
        String aesCipherText = "";
        try{
            aesCipherText = RSAUtil.encrypt(aesKey, clientPubKey);
        }catch (Exception e){
            logger.error(logPrefix + e.getClass());
            e.printStackTrace();
        }

        //加密失败
        if(TextUtil.isNullOrEmpty(aesCipherText)){
            String result = JSONUtil.toJSON(new Result(ResultCode.ERROR_SERVICE_ERR0R));
            logger.error(logPrefix + result);
            return result;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("cipher", aesCipherText);
        String result = JSONUtil.toJSON(new Result<Map<String, Object>>(ResultCode.SUCCESS, data));
        logger.info(logPrefix + result);
        return result;
    }
}
