package com.yjy.web.mms.controller.weixinqyh;

import com.yjy.web.comm.weixinqyh.WxUtil;
import com.yjy.web.mms.model.entity.weixinqyh.*;
import com.yjy.web.mms.services.weixinqyh.SendMessageService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MessageController {
	
	private SendMessageService sendMessageService;
	private org.slf4j.Logger logger =org.slf4j.LoggerFactory.getLogger(getClass());
	public MessageController() {
		sendMessageService=new SendMessageService();
	}
	
	// 发送文本消息
	public void sendTextMessage(String toUser, String toParty, String toTag,
			String content) throws ParseException, IOException {
		TextMessage message = new TextMessage();
		if (toUser != null) {
			message.setTouser(toUser);
		}
		if (toParty != null) {
			message.setToparty(toParty);
		}
		if (toTag != null) {
			message.setTotag(toTag);
		}
		message.setMsgtype("text");
		message.setAgentid(WxConfig.AGENT_ID);
		Text text = new Text();
		text.setContent(content);
		message.setText(text);
		logger.info("获取新的access_token开始！原有的token:"+WxConfig.ACCESS_TOKEN);
		String access_token = WxUtil.getAccessToken();
		logger.info("新的token:"+access_token);
		//暂时屏蔽消息推送功能
		sendMessageService.sendMessage(access_token, message);//WxConfig.ACCESS_TOKEN

	}

	// 发送图片消息,语音消息,文件消息
	public void sendMessage(String toUser, String toParty, String toTag,
			String media_id, String type) throws ParseException, IOException {
		Message message = new Message();
		Media media = new Media();
		media.setMedia_id(media_id);
		if (toUser != null) {
			message.setTouser(toUser);
		}
		if (toParty != null) {
			message.setToparty(toParty);
		}
		if (toTag != null) {
			message.setTotag(toTag);
		}
		// 图片消息
		if (type.equals("image")) {
			message.setImage(media);
			message.setMsgtype("image");
		}
		// 语音消息
		if (type.equals("voice")) {
			message.setVoice(media);
			message.setMsgtype("voice");
		}
		// 文件消息
		if (type.equals("file")) {
			message.setFile(media);
			message.setMsgtype("file");
		}
		message.setAgentid(WxConfig.AGENT_ID);
		sendMessageService.sendMessage(WxConfig.ACCESS_TOKEN, message);
	}

	// 发送视频消息
	public void sendVideoMessage(String toUser, String toParty, String toTag,
			String media_id, String title, String description)
			throws ParseException, IOException {
		VideoMessage message = new VideoMessage();
		Video video = new Video();
		if (toUser != null) {
			message.setTouser(toUser);
		}
		if (toParty != null) {
			message.setToparty(toParty);
		}
		if (toTag != null) {
			message.setTotag(toTag);
		}
		message.setMsgtype("video");
		message.setAgentid(WxConfig.AGENT_ID);
		if (description != null) {
			video.setDescription(description);
		}
		if (title != null) {
			video.setTitle(title);
		}
		video.setMedia_id(media_id);
		message.setVideo(video);
		sendMessageService.sendMessage(WxConfig.ACCESS_TOKEN, message);
	}

	// 文本卡片
	public void sengCardMessage(String toUser, String toParty, String toTag,
			String title, String description, String url)
			throws ParseException, IOException {
		TextcardMessage message = new TextcardMessage();
		Textcard text = new Textcard();
		message.setAgentid(WxConfig.AGENT_ID);
		if (toUser != null) {
			message.setTouser(toUser);
		}
		if (toParty != null) {
			message.setToparty(toParty);
		}
		if (toTag != null) {
			message.setTotag(toTag);
		}
		text.setDescription(description);
		text.setTitle(title);
		text.setUrl(url);
		message.setMsgtype("textcard");
		message.setTextcard(text);

		logger.info("获取新的access_token开始！原有的token:"+WxConfig.ACCESS_TOKEN);
		String access_token = WxUtil.getAccessToken();
		logger.info("新的token:"+access_token);
		//暂时屏蔽消息推送功能
		sendMessageService.sendMessage(access_token, message);
	}

	// 图文消息
	public void newsMessage(String toUser, String toParty, String toTag,
			String title, String description, String url, String picurl,
			String media_id, String author, String content, String digest,
			String content_source_url, String type) throws ParseException,
			IOException {
		NewsMessage message = new NewsMessage();
		News news = new News();
		Article article = new Article();
		message.setAgentid(WxConfig.AGENT_ID);
		message.setSafe(0);
		article.setTitle(title);
		if (toUser != null) {
			message.setTouser(toUser);
		}
		if (toParty != null) {
			message.setToparty(toParty);
		}
		if (toTag != null) {
			message.setTotag(toTag);
		}
		if (type.equals("news")) {
			article.setDescription(description);
			article.setPicurl(picurl);
			article.setUrl(url);
			List<Article> articles = new ArrayList<Article>();
			articles.add(article);
			news.setArticles(articles);
			message.setNews(news);
		}
		if (type.equals("mpnews")) {
			article.setAuthor(author);
			article.setContent_source_url(content_source_url);
			article.setThumb_media_id(media_id);
			article.setDigest(digest);
			article.setContent(content);
			List<Article> articles = new ArrayList<Article>();
			articles.add(article);
			news.setArticles(articles);
			message.setMpnews(news);
		}

		sendMessageService.sendMessage(WxConfig.ACCESS_TOKEN, message);

	}
}
