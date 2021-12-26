package com.yjy.web.mms.controller.forpub;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/forpub")
public class ForpubController {

	@RequestMapping(value="/poc-hsa-hgs-9001",method = RequestMethod.POST)
	@ResponseBody
	public String postdata(@RequestBody String  postdata) {//@RequestBody JSONObject postdata
//		System.out.println(postdata.toString());
		System.out.println("postdata："+postdata);
		return postdata;//postdata.toString();
	}
}
