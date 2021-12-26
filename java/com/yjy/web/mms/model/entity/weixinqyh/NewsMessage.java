package com.yjy.web.mms.model.entity.weixinqyh;

/**
 * 图文消息
 * @author qxh
 *
 */
public class NewsMessage extends BaseMessage {  
	//文章列表
    private News news;
    private News mpnews;
    private int safe;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public int getSafe() {
		return safe;
	}

	public void setSafe(int safe) {
		this.safe = safe;
	}

	public News getMpnews() {
		return mpnews;
	}

	public void setMpnews(News mpnews) {
		this.mpnews = mpnews;
	}
	
	

}  
