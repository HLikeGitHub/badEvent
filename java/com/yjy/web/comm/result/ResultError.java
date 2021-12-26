package com.yjy.web.comm.result;

import com.alibaba.fastjson.JSONObject;
import com.yjy.web.comm.utils.JSONUtil;
import com.yjy.web.comm.utils.RegexUtil;
import com.yjy.web.comm.utils.SenDetUtil;
import com.yjy.web.comm.utils.TextUtil;
import javafx.util.Pair;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误结果
 * @info ResultError
 * @author rwx
 * @mail aba121mail@qq.com
 */
public class ResultError {

    private final Map<ResultCode, Object> errorMap;
    private ResultError(Builder builder){
        errorMap = builder.errorMap;
    }

    public static final String UID_KEY = "uid_key";
    public static final String USER_KEY = "user_key";
    public static final String YXW_SERVICE_KEY = "yxw_service_key";
    public static final String ORDER_KEY = "order_key";
    public Pair<Boolean, Object> getError(String logPrefix, Logger logger){
        Map<String, Object> result = new HashMap<>();

        //参数缺失
        if(errorMap.containsKey(ResultCode.ERROR_PARAMS_MISS)){
            String[] params = (String[]) errorMap.get(ResultCode.ERROR_PARAMS_MISS);
            if(TextUtil.isNullOrEmpty(params)){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_PARAMS_MISS, null));
            }
        }

        //验证次数已达上限
        if(errorMap.containsKey(ResultCode.ERROR_SMS_CHECK_MAX)){
            boolean isSmsNumMax = (boolean) errorMap.get(ResultCode.ERROR_SMS_CHECK_MAX);
            if(isSmsNumMax){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_SMS_CHECK_MAX, null));
            }
        }

        //token失效
        if(errorMap.containsKey(ResultCode.ERROR_TOKEN_EXPIRE)){
            Object id = errorMap.get(ResultCode.ERROR_TOKEN_EXPIRE);
            if(id == null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_TOKEN_EXPIRE, null));
            }
            result.put(UID_KEY, id.toString());
        }

        //验证码超时
        Object serverSmsCode = null;
        if(errorMap.containsKey(ResultCode.ERROR_SMS_EXPIRE)){
            serverSmsCode = errorMap.get(ResultCode.ERROR_SMS_EXPIRE);
            if(serverSmsCode == null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_SMS_EXPIRE, null));
            }
        }

        //验证码错误
        if(errorMap.containsKey(ResultCode.ERROR_SMS_VERIFY)){
            Object clientSmsCode = errorMap.get(ResultCode.ERROR_SMS_VERIFY);
            if(serverSmsCode == null || clientSmsCode == null || !serverSmsCode.toString().equals(clientSmsCode.toString())){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_SMS_VERIFY, null));
            }
        }

        //手机号码不匹配
        if(errorMap.containsKey(ResultCode.ERROR_PHONE_ERROR)){
            Object isMatch = errorMap.get(ResultCode.ERROR_PHONE_ERROR);
            if(isMatch == null || !((boolean)isMatch)){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_PHONE_ERROR, null));
            }
        }

        //名字不匹配
        if(errorMap.containsKey(ResultCode.ERROR_NAME_ERROR)){
            Object isMatch = errorMap.get(ResultCode.ERROR_NAME_ERROR);
            if(isMatch == null || !((boolean)isMatch)){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_NAME_ERROR, null));
            }
        }

        //手机号码非法
        if(errorMap.containsKey(ResultCode.ERROR_PHONE_ILLEGAL)){
            Object phone = errorMap.get(ResultCode.ERROR_PHONE_ILLEGAL);
            if(phone == null || !RegexUtil.isCellPhone(phone.toString())){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_PHONE_ILLEGAL, null));
            }
        }

        //手机号码账户不存在
        if(errorMap.containsKey(ResultCode.ERROR_LOGIN_PHONE_NO_EXISTS)){
            Object appUser = errorMap.get(ResultCode.ERROR_LOGIN_PHONE_NO_EXISTS);
            if(appUser == null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_LOGIN_PHONE_NO_EXISTS, null));
            }
            result.put(USER_KEY, appUser);
        }

        //手机号码已经注册过
        if(errorMap.containsKey(ResultCode.ERROR_REG_PHONE_EXISTS)){
            Object appUser = errorMap.get(ResultCode.ERROR_REG_PHONE_EXISTS);
            if(appUser != null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_REG_PHONE_EXISTS, null));
            }
        }

        //认证信息已经被绑定过
        if(errorMap.containsKey(ResultCode.ERROR_AUTH_HAD_BIND)){
            Object appUser = errorMap.get(ResultCode.ERROR_AUTH_HAD_BIND);
            if(appUser != null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_AUTH_HAD_BIND, null));
            }
        }

        //认证信息已经被绑定过
        if(errorMap.containsKey(ResultCode.ERROR_ADVICE_LENGTH)){
            String advice = errorMap.get(ResultCode.ERROR_ADVICE_LENGTH).toString();
            if(advice.length() > 200){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_ADVICE_LENGTH, null));
            }
        }

        //提交的内容包含敏感词汇
        if(errorMap.containsKey(ResultCode.ERROR_ADVICE_SEN_WORD)){
            String advice = errorMap.get(ResultCode.ERROR_ADVICE_SEN_WORD).toString();
            String word = SenDetUtil.getInstance().isPornographic(advice);
            if(word != null){
                Map<String, String> data = new HashMap<>();
                data.put("advice", advice);
                data.put("word", word);
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_ADVICE_SEN_WORD, data));
            }
        }

        //无法获取YxwService
        if(errorMap.containsKey(ResultCode.ERROR_SERVICE_YXW_ERR0R)){
            Object yxwService = errorMap.get(ResultCode.ERROR_SERVICE_YXW_ERR0R);
            if(yxwService == null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_SERVICE_YXW_ERR0R, null));
            }
            result.put(YXW_SERVICE_KEY, yxwService);
        }

        //接口返回数据解析出问题，或者接口调用不正确
        if(errorMap.containsKey(ResultCode.ERROR_WEB_SERVICE_ERR0R)){
            Object jsonObj = errorMap.get(ResultCode.ERROR_WEB_SERVICE_ERR0R);
            //接口返回数据解析出问题
            if(jsonObj == null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_WEB_SERVICE_ERR0R, null));
            }

            //接口调用不正确
            JSONObject jsonObject = (JSONObject) jsonObj;
            if(!"0".equals(jsonObject.getString("resultCode"))){
                String resultMessage = jsonObject.getString("resultMessage");
                if(TextUtil.isNullOrEmpty(resultMessage)){
                    resultMessage = ResultCode.ERROR_WEB_SERVICE_ERR0R.getMsg();
                }
                String resultJson = JSONUtil.toJSON(new Result(ResultCode.ERROR_WEB_SERVICE_ERR0R.getCode(), resultMessage));
                logger.error(logPrefix + resultJson);
                return new Pair<>(true, resultJson);
            }
        }

        if(errorMap.containsKey(ResultCode.ERROR_GEN_ORDER_FAIL)){
            Object orderObj = errorMap.get(ResultCode.ERROR_GEN_ORDER_FAIL);
            if(orderObj == null){
                return logResult(logPrefix, logger, genResult(ResultCode.ERROR_GEN_ORDER_FAIL, null));
            }
            result.put(ORDER_KEY, orderObj);
        }

        return new Pair<>(false, result);
    }

    private Result genResult(ResultCode code, Map<String, String> data){
        return new Result(code, data);
    }

    /**
     * 打印错误日志，返回错误信息
     * @param logPrefix
     * @param logger
     * @param result
     * @return
     */
    private Pair<Boolean, Object> logResult(String logPrefix, Logger logger, Result result){
        String jsonResult = JSONUtil.toJSON(result);
        logger.error(logPrefix + jsonResult);
        return new Pair<>(true, jsonResult);
    }

    /**
     * 错误结果构造器
     */
    public static class Builder{
        private final Map<ResultCode, Object> errorMap;
        public Builder(){
            errorMap = new HashMap<>();
        }

        public Builder with(ResultCode code, Object obj){
            errorMap.put(code, obj);
            return this;
        }

        public ResultError build(){
            return new ResultError(this);
        }

    }

}