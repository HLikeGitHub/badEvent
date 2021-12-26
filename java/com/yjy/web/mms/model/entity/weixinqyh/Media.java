package com.yjy.web.mms.model.entity.weixinqyh;

/**
 * 媒体文件 
 * @author qxh
 *
 */
public class Media {
	//图片/语音/文件 媒体文件id，可以调用上传临时素材接口获取
    private String media_id;//素材上传得到media_id，该media_id仅三天内有效
    //上传文件类型
    private String createType;
    //上传时间戳
    private String createdAt;
    //标题
    /*private String title;
    //描述
    private String description;
    //点击后跳转的链接。
    private String url;
    //图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640320，小图8080。
    private String picurl;
    //文本内容
    private String content;
    //图文消息缩略图的media_id, 可以通过素材管理接口获得。
    //private String thumb_media_id;
    //图文消息的作者，不超过64个字节
    private String author;
    //图文消息点击“阅读原文”之后的页面链接
    private String content_source_url;
    //图文消息的描述，不超过512个字节，超过会自动截断
    private String digest;*/
    
	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/*public String getTitle() {
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
	
	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent_source_url() {
		return content_source_url;
	}

	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}*/
    
}
