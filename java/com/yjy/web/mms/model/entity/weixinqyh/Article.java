package com.yjy.web.mms.model.entity.weixinqyh;
/**
 * 文章
 * @author qxh
 *
 */
public class Article {
    //是    标题，不超过128个字节，超过会自动截断
    private String title;    
    //否    描述，不超过512个字节，超过会自动截断
    private String description;    
    //是    点击后跳转的链接。
    private String url;    
    //否    图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640320，小图8080。
    private String picurl;
    //图文消息缩略图的media_id, 可以通过素材管理接口获得。
    private String thumb_media_id;
    //图文消息的作者，不超过64个字节
    private String author;
    //图文消息的内容，支持html标签，不超过666 K个字节
    private String content;
    //图文消息的描述，不超过512个字节，超过会自动截断
    private String digest;
    //图文消息点击“阅读原文”之后的页面链接
    private String content_source_url;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPicurl() {
        return picurl;
    }
    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
	public String getThumb_media_id() {
		return thumb_media_id;
	}
	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getContent_source_url() {
		return content_source_url;
	}
	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}    
}