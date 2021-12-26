package com.yjy.web.mms.services.weixinqyh;

import com.yjy.web.comm.weixinqyh.HttpUtil;
import com.yjy.web.mms.model.entity.weixinqyh.BaseMessage;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

public class SendMessageService{

    public org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SendMessageService.class);
	
	//private  static  String sendMessage_url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={ACCESS_TOKEN}";
	private  static  String ACCESS_TOKEN="";
	private  static  String sendMessage_url="http://192.168.6.223/wxapi/qyapi/cgi-bin/message/send?access_token=";//{ACCESS_TOKEN}
	
	
	public void sendMessage(String accessToken, BaseMessage message) throws ParseException, IOException {
		//获取字符串 将message对象转换为json字符串    
        JSONObject jsonMessage=JSONObject.fromObject(message);
        System.out.println("jsonTextMessage:"+jsonMessage);
        //2.获取请求的url  
       // sendMessage_url=sendMessage_url.replace("{ACCESS_TOKEN}", accessToken);
//        sendMessage_url=
        //3.调用接口,发送消息
        //String sendMessageStr=HttpRequest.sendPost(sendMessage_url, jsonMessage.toString());
        logger.info("sendMessage_url:"+sendMessage_url+accessToken);
        String sendMessageStr= HttpUtil.post(sendMessage_url+accessToken, jsonMessage, accessToken);
        JSONObject jsonObject = JSONObject.fromObject(sendMessageStr);
        logger.info("jsonObject:"+jsonObject.toString());

        //4.错误消息处理
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                logger.info("errcode:{} errmsg:{}"+jsonObject.getInt("errcode")+jsonObject.getString("errmsg"));  
            }  
        }  
		
	}

}
