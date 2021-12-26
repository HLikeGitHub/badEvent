package com.yjy.web.comm.weixinqyh;

import java.io.IOException;

import com.yjy.web.mms.model.entity.weixinqyh.WxConfig;

import net.sf.json.JSONObject;

public class WxUtil {
    public static String accessToken="";
	public static String getAccessToken() {
		String access_token = "";
		if (WxConfig.isAccessTokenExpired(WxConfig.getExpiresTime())) {
			
			System.out.println("获取新的access_token开始！原有的token:"+WxConfig.ACCESS_TOKEN);
			//String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpId}&corpsecret={corpsecret}";
			String access_token_url = "http://192.168.6.223/wxapi/qyapi/cgi-bin/gettoken?corpid={corpId}&corpsecret={corpsecret}";
			String requestUrl = access_token_url.replace("{corpId}",
					WxConfig.CORP_ID).replace("{corpsecret}",
					WxConfig.CORP_SECRET);
			try {
				String accessTokenStr = HttpUtil.sendGet(requestUrl);
				JSONObject accessToken = JSONObject.fromObject(accessTokenStr);
				System.out.println("获取新的access_token："+access_token);
				access_token = accessToken.getString("access_token");
				//缓存token
				WxConfig.ACCESS_TOKEN = access_token;
				//设置token超时时间
				WxConfig.expiresTime = System.currentTimeMillis() +  1800 * 1000L;//7200
			} catch (IOException e) {
				e.printStackTrace();
				//强制过期token
				WxConfig.expiresTime = 0;
			}
		} else {
			System.out.println("使用原有的ACCESS_TOKEN:"+WxConfig.ACCESS_TOKEN);
			access_token = WxConfig.ACCESS_TOKEN;
		}
		return access_token;
	}

	public static String getJsapiTicket() {
		String jsapi_ticket = null;
		try {
			if (jsapi_ticket == null) {
				String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={AccessToken}&type=jsapi";
				String jsapiTicketUrl = jsapi_ticket_url.replace(
						"{AccessToken}", getAccessToken());
				String jsapiTicketStr = HttpUtil.sendGet(jsapiTicketUrl);
				JSONObject jsapiTicket = JSONObject.fromObject(jsapiTicketStr);
				System.out.println(jsapiTicket);
				jsapi_ticket = jsapiTicket.getString("ticket");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsapi_ticket;
	}
}
