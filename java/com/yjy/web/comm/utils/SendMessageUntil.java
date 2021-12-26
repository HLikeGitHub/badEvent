package com.yjy.web.comm.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class SendMessageUntil {
	
    public static void main(String[] args) {
    	getDXFSDX("18823061040","");//【江门市中心医院】王先生，您好！测试短信！
    }
    /*
     * 调用电信短信平台
	*/
	public static String  getDXFSDX(String phone,String content)  {
		Map<String,String> map=new HashMap<String,String>();
		  String sm;
		try {
		  sm = URLEncoder.encode(content, "GBK");
		  System.setProperty("http.proxyHost","192.168.61.103");
		  System.setProperty("http.proxyPort","8089");
		  URI uri = new URI("http://webdx.1608.com/send/?Uid=jmbjch&Key=1428C225563206E0A3E0C510A70AB9A3&smsMob="+phone+"&smsText="+sm);
		  RestTemplate restTemplate=new RestTemplate();
		  String data=restTemplate.getForObject(uri,String.class);
		  System.out.println(data);
		  System.setProperty("http.proxyHost","");
		  System.setProperty("http.proxyPort","");
		  return data;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "fail";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "fail";
		}

		  
		}

	
      /*
		       * 欧一短信平台
       */
	   public boolean  getDXFSOY(String phone,String content) {
			 String bm="";
			//用户名  实际使用时应该从session中获取
			String username = "400012";
			//密码 实际使用时应该从session中得到的用户名中获取
			String password = "0809412";
			//手机号码  单一内容测试时使用
			String phoneid = phone;//"18823061040";
			//原地址(扩展码 可不填 前端获取 测试时为方便定义)
			String source_address = "扩展码";
			//外部编码 (有需求是可获取)
			long external_id = 0x1L;
			//发送短信方法
			String mt = "send";//mt
			//以下参数均由前端页面传来
			String result = "";
			//String content =  content;//"【测试】这是一条测试短信aaa123";
			//因做测试 故原地址与外部编码不参与测试 实际情况有需要从前端定义参数获取
			try {
				//做URLEncoder - UTF-8编码
				String sm = URLEncoder.encode(content, "utf8");
				//将参数进行封装
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("un", username);
				paramMap.put("pw", password);
				
				//单一内容时群发  将手机号用;隔开
				paramMap.put("da", phoneid);
				paramMap.put("sm", sm);
				//发送POST请求
				result = SendPOST(mt,paramMap);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
	   } 
	
	
	
  //欧一短信服务接口
	//定义常量
	//dc 数据类型
	private static final int DATACODING = 15; //定死
	//rf 响应格式
	private static final int REPSPONSEFORMAT = 2; //定死
	//rd 是否需要状态报告
	private static final int REPORTDATA = 1; //定死
	//tf 短信内容的传输编码
	private static final int TRANSFERENCODING = 3; //定死
	//serviceaddress service端地址 //变量可根据实际情况改变
	public static String serviceaddress ="http://webdx.1608.com/"; //"http://61.129.57.231:7891/mt";
			
	
	public static String SendPOST(String function, Map<String, Object> paramMap) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(serviceaddress+function);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// conn.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			paramMap.put("dc", DATACODING);
			paramMap.put("rf", REPSPONSEFORMAT);
			paramMap.put("rd", REPORTDATA);
			paramMap.put("tf", TRANSFERENCODING);
			// 设置请求属性
			String param = "";
			if (paramMap != null && paramMap.size() > 0) {
				Iterator<String> ite = paramMap.keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();// key
					Object value = paramMap.get(key);
					param += key + "=" + value + "&";
				}
				param = param.substring(0, param.length() - 1);
				System.out.println(param);
			}

			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.err.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	
  //JSON方法输出
	public static void writeJson(String array,HttpServletResponse resp) {
		resp.setContentType("text/json;charset=utf-8");
		PrintWriter pw;
		try {
			pw = resp.getWriter();
			pw.write(array);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
  
}
