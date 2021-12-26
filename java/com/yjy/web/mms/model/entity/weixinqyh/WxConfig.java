package com.yjy.web.mms.model.entity.weixinqyh;

/**
 * 通用配置
 * 
 * @author qxh
 * 
 */
public class WxConfig {
	// 与开发模式接口配置信息中的Token保持一致
	public static final String CORP_ID = "wx7f8fb40ae642bfbd";
	// 开发者ID
	public static final String CORP_SECRET = "_4vjhcxsHIfcDpfqEJYu3cCzr5KHl4swSgrqRMTkH1Y";

	public static long expiresTime = 0;

	public static String ACCESS_TOKEN;

	//应用id
	public static final int AGENT_ID = 1000003;

	public static boolean isAccessTokenExpired(Long expiresTime) {
		return System.currentTimeMillis() > expiresTime;
	}

	public static long getExpiresTime() {
		return expiresTime;
	}

}
