package com.yjy.web.mms.controller.weixinqyh;

import com.yjy.web.comm.utils.XmlUtil;
import com.yjy.web.comm.weixinqyh.WxUtil;
import com.yjy.web.mms.model.entity.weixinqyh.*;
import com.yjy.web.mms.services.weixinqyh.MediaService;
import com.yjy.web.mms.services.weixinqyh.SendMessageService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TestMessage {

	public static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MediaService.class);
	SendMessageService sms = new SendMessageService();
   
	public static void main(String[] args) {
//		String access_token = WxUtil.getAccessToken();
//		System.out.println(access_token);
		String content = "测试推送消息，如果打扰请见谅！";
		TextMessage message = new TextMessage();
		message.setTouser("185006");//@all
		message.setMsgtype("text");
		message.setAgentid(WxConfig.AGENT_ID);

	}
	
	
	public void getToken() {
		for (int i = 0; i < 10; i++) {
			String access_token = WxUtil.getAccessToken();
			System.out.println(access_token);
		}
	}

	// 文本消息测试
	
	public void send() throws ParseException, IOException {
		String content = "测试推送消息，如果打扰请见谅！";
		TextMessage message = new TextMessage();
		message.setTouser("185006");//@all
		message.setMsgtype("text");
		message.setAgentid(WxConfig.AGENT_ID);
		Text text = new Text();
		text.setContent(content);
		message.setText(text);
		System.out.println(XmlUtil.obj2Xml(message));
		String access_token = WxUtil.getAccessToken();
		sms.sendMessage(access_token, message);
	}

	// 发送图片消息,语音消息,文件消息
	
	public void sendMessage() throws ParseException, IOException, java.text.ParseException {
		Message message = new Message();
		message.setTouser("@all");
		message.setMsgtype("file");
		message.setAgentid(WxConfig.AGENT_ID);
		Media media = new Media();
		media.setMedia_id("3rV118w5wn9qw0CSFo5LCjIAGFEZyU33Pf3WMwcloFX8");
		message.setFile(media);
		SendMessageService sms = new SendMessageService();
		String access_token = WxUtil.getAccessToken();
		sms.sendMessage(access_token, message);
	}

	// 视频消息
	
	public void videoMessage() throws ParseException, IOException {
		VideoMessage message = new VideoMessage();
		Video video = new Video();
		message.setMsgtype("video");
		message.setTouser("@all");
		message.setToparty("");
		message.setTotag("");
		message.setAgentid(WxConfig.AGENT_ID);
		video.setDescription("欢迎收看欢迎收看欢迎收看欢迎收看");
		video.setTitle("欢迎收看");
		video.setMedia_id("3sdPMjsfRlE1JQTeWVjtp9N1ksm2IJes_RM27_LH_bDNjXpxy-_dFslWHWa9MK_0z");
		message.setVideo(video);
		String access_token = WxUtil.getAccessToken();
		sms.sendMessage(access_token, message);
	}

	// 文本卡片消息

	public void sengCardMessage() throws ParseException, IOException, java.text.ParseException {
		TextcardMessage message = new TextcardMessage();
		Textcard text = new Textcard();
		message.setAgentid(WxConfig.AGENT_ID);
		message.setMsgtype("textcard");
		message.setTouser("@all");
		message.setToparty("");
		message.setTotag("");
		text.setDescription("<div class=\"gray\">2016年9月26日</div> <div class=\"normal\">恭喜你抽中iPhone 7一台，领奖码：xxxx</div><div class=\"highlight\">请于2016年10月10日前联系行政同事领取</div>");
		text.setTitle("领奖通知");
		text.setUrl("https://www.baidu.com/");
		message.setTextcard(text);
		String access_token = WxUtil.getAccessToken();
		sms.sendMessage(access_token, message);
	}

	// 图文消息

	public void newsMessage() throws ParseException, IOException, java.text.ParseException {
		NewsMessage message = new NewsMessage();
		Article article = new Article();
		message.setAgentid(WxConfig.AGENT_ID);
		message.setMsgtype("mpnews");
		message.setTouser("@all");
		message.setToparty("");
		message.setTotag("");
		article.setAuthor("qxh");
		article.setContent_source_url("http://work.weixin.qq.com/api/doc#10167");
		article.setThumb_media_id("3RgHidkBDEXkon0__Gg7ZgCvBHOypIn3KbkiwJL7d_o0wAIFPX4b3vCkILzicCUn9");
		article.setDigest("这是一个很特别的描述");
		article.setContent("企业微信开发api");
		article.setTitle("青年文摘");
		/*
		 * article.setDescription("这是一个很特别的描述"); article.setPicurl(
		 * "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"
		 * ); article.setUrl("http://work.weixin.qq.com/api/doc#10167");
		 */
		List<Article> articles = new ArrayList<Article>();
		articles.add(article);
		News news = new News();
		news.setArticles(articles);
		message.setMpnews(news);
		String access_token = WxUtil.getAccessToken();
		sms.sendMessage(access_token, message);
	}

	// 上传多媒体文件

	public void upload() {
		MediaService ms = new MediaService();
		String access_token = WxUtil.getAccessToken();
		File file = new File("F://hahh.mp4");
		// 地址
		Media media = ms.Upload(access_token, "video", file);
		// media_id
		System.out.println("media_id：" + media.getMedia_id());
		// 类型
		System.out.println("类型：" + media.getCreateType());
		// 时间戳
		System.out.println("时间戳：" + media.getCreatedAt());
	}

}
