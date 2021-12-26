package com.yjy.web.mms.model.entity.weixinqyh;
/**
 * 图片语音文件消息共用类
 * @author qxh
 *
 */
public class Message extends BaseMessage{
	//图片消息
    private Media image ;
    //语音消息
    private Media voice;
    //文件消息
    private Media file;
    //是否安全  0否   1是
    private int safe;
    //文章列表
    
    public Media getImage() {
        return image;
    }
    public void setImage(Media image) {
        this.image = image;
    }
    public Media getVoice() {
		return voice;
	}
	public void setVoice(Media voice) {
		this.voice = voice;
	}
	public Media getFile() {
		return file;
	}
	public void setFile(Media file) {
		this.file = file;
	}
	public int getSafe() {
		return safe;
	}
	public void setSafe(int safe) {
		this.safe = safe;
	}
	
    
}
