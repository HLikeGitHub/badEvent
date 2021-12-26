package com.yjy.web.mms.services.weixinqyh;

import com.yjy.web.comm.weixinqyh.ImgUtil;
import com.yjy.web.mms.model.entity.weixinqyh.Media;
import net.sf.json.JSONObject;

import java.io.File;

public class MediaService {


	public static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MediaService.class);
	
	public  Media Upload(String accessToken, String type, File  file) { 
		 Media media=new Media();
		 String uploadMediaUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";  
		 String url = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);  
		 JSONObject jsonObject;  
	        try {
	            ImgUtil post = new ImgUtil(url);
	            post.addParameter("media", file);  
	            String str = post.send(); 
	            jsonObject = JSONObject.fromObject(str); 
	            if (jsonObject.containsKey("media_id")) {  
	            	media=new Media();  
	            	media.setMedia_id(jsonObject.getString("media_id"));  
	            	media.setCreateType(jsonObject.getString("type"));  
	            	media.setCreatedAt(jsonObject.getString("created_at"));  
	            } else { 
	            	logger.info("errcode:{} errmsg:{}"+jsonObject.getInt("errcode")+jsonObject.getString("errmsg"));  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return media;  
	  } 
}
