package com.yjy.web.comm.result;

/**
 * HTTP返回结果数据编码和消息枚举
 * @info ResultCode
 * @author rwx
 * @email aba121mail@qq.com
 */
public enum ResultCode {

    SUCCESS(0, "成功"),

    ERROR(-1, "错误"),
    ERROR_UNKNOWN(-2, "未知错误"),
    ERROR_PARAMS(-3, "参数错误"),
    ERROR_CHECK(-4, "验证失败"),
    ERROR_SMS_CHECK_MAX(-5, "24小时内验证次数已达上限"),
    ERROR_SMS_EXPIRE(-6, "验证码已失效"),
    ERROR_SMS_VERIFY(-7, "验证码错误"),
    ERROR_OPE_EXPIRE(-8, "操作超时"),
    ERROR_PARAMS_MISS(-9, "提交信息不完整"),
    ERROR_SUBMIT_ERR0R(-10, "提交失败，请稍后再试"),
    ERROR_PHONE_ILLEGAL(-11, "手机号码非法"),
    ERROR_REG_PHONE_EXISTS(-12, "该号码已注册过"),
    ERROR_LOGIN_PHONE_NO_EXISTS(-13, "账号不存在"),
    ERROR_LOGIN_PWD_MISS(-14, "密码错误"),
    ERROR_PHONE_ERROR(-15, "手机号码错误"),
    ERROR_NAME_ERROR(-16, "姓名错误"),
    ERROR_AUTH_NO_FOUND(-17, "未查到实名认证信息，请在微信公众号上实名登记"),
    ERROR_TOKEN_EXPIRE(-18, "登录超时，请重新登录"),
    ERROR_OPENID_NO_EXISTS(-19, "ID不存在"),
    ERROR_AUTH_HAD_BIND(-20, "绑定失败，认证信息已被绑定过"),
    ERROR_LOGIN_PWD_NOT_SET(-21, "请设置密码"),
    ERROR_ADVICE_LENGTH(-22, "提交失败，内容超过200个字符"),
    ERROR_ADVICE_SEN_WORD(-23, "提交的内容包含敏感词汇"),
    ERROR_GEN_ORDER_FAIL(-24, "生成订单失败"),

    ERROR_FILE_UPLOAD(-400, "上传文件失败，请稍后再试"),
    ERROR_SERVICE_ERR0R(-500, "服务调用失败，请稍后再试"),
    ERROR_WEB_SERVICE_ERR0R(-501, "第三方服务调用失败，请稍后再试"),
    ERROR_SERVICE_YXW_ERR0R(-502, "服务调用失败，请稍后再试");

    /**
     * 结果编码
     */
    private int code;
    /**
     * 结果信息
     */
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
