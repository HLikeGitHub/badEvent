package com.yjy.web.mms.controller.weixinqyh;

import java.io.File;

import com.yjy.web.mms.services.weixinqyh.MediaService;
import com.yjy.web.comm.weixinqyh.WxUtil;

public class MediaController {
	private MediaService mediaService;

	public void uploadMedia(File file, String mediatype) {
		mediaService.Upload(WxUtil.getAccessToken(), mediatype, file);
	}

}
