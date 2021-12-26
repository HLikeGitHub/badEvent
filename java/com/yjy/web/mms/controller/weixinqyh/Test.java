package com.yjy.web.mms.controller.weixinqyh;

import java.io.IOException;
import java.text.ParseException;

public class Test {

	public static void main(String[] args) {
		TestMessage test =new TestMessage();
		try {
			test.send();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
