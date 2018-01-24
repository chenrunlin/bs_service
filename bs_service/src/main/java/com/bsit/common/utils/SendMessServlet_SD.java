package com.bsit.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * 短信平台接口
 * 
 * @author dev
 * 
 */
@SuppressWarnings({ "serial", "deprecation" })
public class SendMessServlet_SD extends HttpServlet {
	private static final Logger logger = Logger.getLogger(SendMessServlet_SD.class);
	
	public SendMessServlet_SD() {
		super();
	}

	public void destroy() {
		super.destroy();

	}

	public String doGet(String moble, String content) {
		try {
			if (content != null && moble != null && moble != ""
					&& !moble.equals("") && content != ""
					&& !content.equals("")) {
				content = URLEncoder.encode(content, "UTF-8");
				String md5 = "";
				String mm = "http://sdk8.interface.sudas.cn/z_mdsmssend.php?sn="
						+ Code.sn
						+ "&pwd="
						+ Code.pwd_SN
						+ "&Md5Sign="
						+ md5
						+ "&mobile="
						+ moble
						+ "&content="
						+ content
						+ "&rrid="
						+ Code.rrid
						+ "&stime="
						+ Code.Stime
						+ "&stype=1&ssafe=2&scode=1";
				URL url = new URL(mm); // 连接短信平台
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestProperty("User-Agent", "略");
				BufferedReader inn = new BufferedReader(new InputStreamReader(
						con.getInputStream(), "UTF-8"));

				char[] ch = new char[100];// 处理返回值，转化为数字
				int count = inn.read(ch);
				inn.close();
				String string = new String(ch, 0, count);
				return string;
			} else {
				return "2000";// 参数值为空或不正确
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "1000";// 抛出异常
		}
	}
	
	@SuppressWarnings("resource")
	public String doPost(String moble, String content) {
		try {
			if (!StringUtil.isEmpty(content) && !StringUtil.isEmpty(moble)) {
				content = URLEncoder.encode(content, "UTF-8");
				String url = "http://120.24.241.49/sms.aspx?action=send&userid="
						+ Code.USERID
						+ "&account="
						+ Code.ACCOUNT
						+ "&password="
						+ Code.PASSWORD
						+ "&mobile="
						+ moble
						+ "&content="
						+ content
						+ "&sendTime="
						+ Code.MStime
						+ "&extno=";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpost = new HttpPost(url);
				// 请求
				HttpResponse response = httpclient.execute(httpost);
				// 处理响应
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
					// 读取xml文档
					String result = EntityUtils.toString(response.getEntity());
					System.out.println(result);
					if (result != null && !result.equals("")) {
						InputStream respInfo = StringTOInputStream(result);
						try {
							// 获得pull解析器工厂
							XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
							// 获取XmlPullParser的实例
							XmlPullParser pullParser = pullParserFactory.newPullParser();
							// 设置需要解析的XML数据
							pullParser.setInput(respInfo, "UTF-8");
							int event = pullParser.getEventType();
							while (event != XmlPullParser.END_DOCUMENT) {
								switch (event) {
								case XmlPullParser.START_TAG:
								switch (pullParser.getName()) {
									case "returnstatus":
									return pullParser.nextText();
								}
							}
								event = pullParser.next();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					System.err.println("error");
				}
			}
	} catch (Exception e) {
		e.printStackTrace();
		return "1000";// 抛出异常
	}
		return "1000";
	}
	
	public static InputStream StringTOInputStream(String in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
		return is;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost() throws ServletException, IOException {
		URL url = new URL("http://sdk2.sudas.cn:8060/z_balance.aspx?sn="
				+ Code.sn + "&pwd=" + Code.pwd);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "略");
		BufferedReader inn = new BufferedReader(new InputStreamReader(
				con.getInputStream(), "utf-8"));

		char[] ch = new char[100];
		int count = inn.read(ch);
		inn.close();
		String string = new String(ch, 0, count);
		System.out.println(string);
	}

	public void init() throws ServletException {
	}

	public static void main(String[] args) throws ServletException, IOException {
//		SendMessServlet_SD mess = new SendMessServlet_SD();
//		mess.doGet("15319732919", "您好。您的验证码为395959。请及时操作！【索美科技】");
		
		SendMessServlet_SD mess = new SendMessServlet_SD();
		String result = mess.doPost("15319732919", "【北京倍胜】您好。您的验证码为395959。请及时操作！");
		System.out.println(result);
		
	}
	
	/**
	 * 手机发送短信
	 * 
	 * @param mobiles
	 *            //手机号
	 * @param contents
	 *            //发送短信内容(最多500个汉字或1000个纯英文，emay服务器程序能够自动分割 GBK2312；
	 * @return
	 */
	public BaseJsonObject sendMessCalls(String mobiles, String contents) {
		BaseJsonObject jsonObject = new BaseJsonObject();
		if (StringUtil.isEmpty(mobiles) || StringUtil.isEmpty(contents)) {
			jsonObject.setStatus(Status.COMMON_STATUS_ERROR_PARAMETER);
			return jsonObject;
		}
		try {
			long startTime = System.currentTimeMillis();
			// 发送短息,然后获取返回码
			String st = doPost(mobiles, contents);
			long endTime = System.currentTimeMillis();
			String messsTime = String.valueOf(endTime - startTime);
			logger.info("耗时：" + messsTime);
			if (st.equals("Success")) {
				//r = new RspMsg(logger, "1", contents);
				jsonObject.setStatus(Status.COMMON_STATUS_SEND_OK);
			} else {
				jsonObject.setStatus(Status.COMMON_STATUS_SEND_ERROR);
			}
		} catch (Exception e) {
			jsonObject.setStatus(Status.COMMON_STATUS_EXCEPRTION);
			e.printStackTrace();
			logger.info("error:" + e.getMessage());
		}
		logger.info("result:" + jsonObject);
		return jsonObject;
	}
	

}
