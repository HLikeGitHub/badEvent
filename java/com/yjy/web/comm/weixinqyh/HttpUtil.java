package com.yjy.web.comm.weixinqyh;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;


import net.sf.json.JSONObject;

public class HttpUtil {

	private static HttpClient httpClient = getHttpClient();
	private static HttpClient postClient = null;
	private static HttpResponse httpResponse = null;

	public static DefaultHttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
		HttpProtocolParams.setUseExpectContinue(params, true);

		final int REQUEST_TIMEOUT = 15 * 1000; // 设置请求超时2秒钟
		final int SO_TIMEOUT = 15 * 1000; // 设置等待数据超时时间2秒钟

		HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				REQUEST_TIMEOUT);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK,
				true);
		SchemeRegistry schreg = new SchemeRegistry();
		schreg.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schreg.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(
				schreg);
		pccm.setMaxTotal(200);
		pccm.setDefaultMaxPerRoute(200);

		DefaultHttpClient httpClient = new DefaultHttpClient(pccm, params);
		httpClient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
						0, false));
		return httpClient;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String sendGet(String url) throws IOException {
		int code = 0;
		HttpGet get = new HttpGet(url);
		try {
			String cont = null;
			if (httpClient == null) {
				httpClient = getHttpClient();
			}

			httpResponse = httpClient.execute(get);

			code = httpResponse.getStatusLine().getStatusCode();
			if (code >= 200 && code < 300) {

				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					cont = EntityUtils.toString(entity);
					System.out.println(cont);
					return cont;
				} else {
					return Integer.toString(code);
				}

			} else {
				return Integer.toString(code);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				EntityUtils.consume(httpResponse.getEntity());
			}
		}
		return Integer.toString(code);

	}

	/**
	 * 通过post发送!
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String post(String url, JSONObject json, String token)
			throws ParseException, IOException {
		HttpPost post = new HttpPost(url);
		int code = 0;
		try {

			if (postClient == null) {
				postClient = getClient(true);
			}
			StringEntity s = new StringEntity(json.toString(),Charset.forName("utf-8"));
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json;charset=UTF-8");
			if (token != null) {
				post.addHeader("Authorization", "Bearer " + token);
			}
			post.setEntity(s);
			post.setHeader("Content-Type","application/json;charset=UTF-8");  

			httpResponse = postClient.execute(post);
			code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("postcode:" + code);
			if (code >= 200 && code < 300) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					String charset = EntityUtils.toString(entity);
					System.out.println("post返回的东西:" + charset);
					return charset;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				EntityUtils.consume(httpResponse.getEntity());
			}
			if (post != null) { // 不要忘记释放，尽量通过该方法实现，

				post.releaseConnection();

			}
		}
		return null;

	}

	/**
	 * 
	 * 
	 * @param isSSL
	 * @return
	 */
	public static HttpClient getClient(boolean isSSL) {

		HttpClient httpClient = getHttpClient();
		if (isSSL) {
			X509TrustManager xtm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			try {
				SSLContext ctx = SSLContext.getInstance("TLS");
				ctx.init(null, new TrustManager[] { xtm }, null);
				SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
				httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		return httpClient;
	}
}
