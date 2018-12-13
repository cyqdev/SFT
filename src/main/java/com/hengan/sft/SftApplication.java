package com.hengan.sft;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@SpringBootApplication
public class SftApplication {

	private static String SECRET_KEY = "#rSgRKVunlLUMopFWbEevOy9";
	/**
	 * 访问善付通
	 * @param model
	 * @return
	 */
//	@RequestMapping(value = "/sft/center",method = RequestMethod.GET)
	public Model decode(String payKey,Model model) {
//		String payKey = request.getParameter("payKey");
		System.out.println("第1行：" + payKey);
		System.out.println("第2行：" + SECRET_KEY);
		payKey = EncryptUtil.decryptBase64(payKey, SECRET_KEY);
		//String payKey = "MUZBMDE2OTVBODQ1NzU2RDU3QjIwRTJGNEIzNEZDRUQ1RjQwQkFFOEYyNDlERDhCRTAyOEJBM0JGOUU3NTNDQ0EzQjhFRENGMDIzMDRDQTY5RkM0Nzc5OUM5MDU4NTFGRkFDMkI2OTkzMTBDODdEMzZBNkNBMzM1N0Y5NTk3NDBCRTM1REFGOUNCMDc2QkJGQ0M0RUNGNTUzQUM3RkMyNkMzM0Q1QTIwODU1N0VFQjI0MzFDQzY1MDk5NUU1RUVDMDlCNEVFMjdDODJGODk1N0JBRTBDRUY3RkI3MTk1NTU4MDM0Q0RFQTMzQjEyRjU3NUEzMEYzQjhENDI2MTdBOTBEOTM1OTY5RUYwMDk1OEE5NDcyOTMxMjIzMDgzMzNGREIwNEZCMTE0MDI4OURDMUY2RUJFQzQ0NDBGOTdCRjBFMjg3RDlDRkI0NTA2QkMzQThCMEUzQzFENzFFNTQyNURBQTdEOENFNzIyODJCMEU1Q0ZENkM3RDA5NkIxNDBCMjNDRDRENUQyQUZBNjU3MjYxQTExMzdBQUYzQkUwNjYwM0I1OTc2NzE2MzZFNEM0QzVCQzg2MzcxQkMyMENBNkI5MjA0Njc4NjFCMUMxNjM0MzU3MjNEMTkwMUNGRDZEMjYyM0E5QUNDOTJBM0U0MTkwRDc2MjA4REJBQ0UzNTQ2N0E5REM2MDQzODhBREQ2NDVDNEE1MzI0NkUwQzE5NDMyREEzQTM2ODBDMDlEOTNEQTk5MkRFNjI0MjZGNUYzMENFMzE0QzY1RjAxM0M1NUMxNDdFRjZCMjhEM0NDNDk2OTUzM0Q5MTkzMjQ2OUQwMjc4RDU5Qjk1M0E5MTQ2NDI5NkU1Rjg4NDA5Mzk3QkFDNDYzOTA5NjExMjkxNTJDMDU1Q0FFQkYzNTU5ODk2MjIxMjAzMzgzRUY5Q0Q1MjEyMkNCQTBCRkIyQkYxOEQwNkU2REY4NDI5RDBBQkZEM0MzNTVCMUI1Q0M0QTA3MUFDMEZBMDY5RjU0QTU4MUJGRTA3NjA4OUQzRkU2RjgyRDc2MkVGRUVGMjk0QTkxMzc2N0E5REM2MDQzODhBREQ2NkIwNkQxMDkwRkFDRDJBMjhGNjJCMTU1QTlDMDAyNjEwMzJFNEI0RDhGNzVDNjIyRjNGQ0I1Njk5MTc3MTBENkE4OURBREIwOUZFQjZGM0E2NDY3OTREQzhFNDZFQTFCNzc4NjU0MjQ2QUE1Njc4M0RCMDRGQjExNDAyODlEQzEwNEREMjhBODQwOTlEQjhEOERBNEY4OThDODEzRkU2OTM4QkRGNEY1RDBDQjA0NTU1QjY2QTBDOTQ3NDdERUExRDUwMzk1RkZEOTM0RjJCOTQzQUM4NzBBOTEzN0Q1NjgxNkNCRkVCNTBGMjAwQkFGQ0EyMzM1MERDNEI4QjJFRDZEQTBFRkVGMTEyNkIzQUU1QzQ4RDhBNDE2RDgzNjZBMjQ4REM0ODlEMkNGNkUzODVEMjk5RUU0NTgzOTNGOTQzNDU0MzdDMDQwNDUwMUQwOUY4QTRCRjU1RTM3MjI0QTRBMThCNzU2MzA2MjA2RjJFNTBCREJEM0IxQkM3Mjc1QkE0MUM0MzI2MDFFOTI2QzhDOUU0N0UxRDJBQjFGNTU5NjcwRUJBQzJCQzVGRDU5MzI1MTQ4OTMyMEJGRUUxQUFFREVEQTUyQTUwQzIwRDgyNjIzQTlBQ0M5MkEzRTQxOTBENzYyMDhEQkFDRTM1NDY3QTlEQzYwNDM4OEFERDY0NUM0QTUzMjQ2RTBDMTk0MzJEQTNBMzY4MEMwOUQ5M0UyNTA3NDgzREQ4QjQ1Qzk5N0NDQ0VDMDM1RDg5OTcyMDVBRUEzRDEwQUY4MTFEOEExMDBFRkMzNUQwQUFDQkI2QjVEQkY5QzFDRjVGRTlBRDM2RkFBQURCQzVBRkUyQjhFQkJBOUJCMzU5Q0FERjBFRUM3M0ZERjk3RjFGRDBEMEIyODYyQzg3RDI0NkY5RDM3MUZENzU0RTJERDRDN0VDMThBNEIyREU1N0RFMzU0QzcxNjQ2MkMzOEJFOEUwQzA3NDhDNTE5ODJBRkE1RDczQjkwNzM1M0QzQTI5MTI1OEVGNkRDNkU4MkI5NzVGNjY1MDM1Njg5REVFRjJBQzFBNUU4QkIwODg4QUY2NDIzMTBENkNERUYyMDA0REUyMEM5M0JCNEM1QUY3RDE1RjlCQjJEN0M1MDQ3NUU1RjQ3ODc5MzlENUMwMjFDM0UyRDBEM0YwRTA4MTREOTBGQTVBM0NFMDdCNDRCNTEzQ0Y2RUE0NjI4MTNEMUYyMTlFMENEMkE2NDBGMEIxQjA1MkE4RkFBQzdBMTQyRDY4MkUyNkNENzU3NjU1MDc1MkUwNjQ1MzM1MEU4MTkxRDNFMDY1NjgzMEQ0N0EyRUUzRUEzNzhDMzA4MThFNUIxMEI2ODc2QjZDQjMyNUMzNUY3OTYyOTZFNUY4ODQwOTM5N0JBMEJBOUEzRjUzQjU1QkUwMzhBQjE4MjdFNjFBRDVCMTQ0OTQxRTA3MEYwN0YzQzZDRjczRkM2QUFFQUU3NEYwRjg4MEQxNUQzMUE3M0FGREQxODg0QjRDNzdDMzVGRTg3MDQ3OEM5MjI3N0EwM0RGRDJDMDAzMEQxNjM2RkZFQzU5Q0EwMEI0ODdBOTU1MTQ3RDk0NTIzMDBCOTgwNUQwOTRBNzg5ODE0NzNCNzNFNjEzMUIyMjFGNkRDQ0IyRTk1M0YzQUJCMzIyQUE1QzU5MjdDQTU3OURBQjUzRkNFOEI0OUE5NkUyQTdFNUEyRTQ1MUZFMzNGMjg3NDM4QzM5RUMwMEI4MkJENTM5RDBDQ0MwQjkxMTU2Q0QyREVFQkQ4QzUzMDdFRkUzNDIyMkJGMkRGRDAyMjMzMUJEODJDQzMwQzJBMDMxRkNEQzBDMDRDN0YyRjAxNTRCQTM0RUEwMzVBNDI3MTU5NjkyMTk4RDI1NzM4Q0EyMEREQzg5RDRCQTcwMDQ5QjAzOUMwMkUwODlFQUJCNDJCMTk2NzI1RTJDMjQ0OEFDNjJFOUQ0NEQwQjM5QzZEOTc5NDhFNkM2Qzk1RkEzMzExQjVEQkY3REU2NDZEQTQ1MTA4NUFGMDBBMzBCQjU5NDI0NUVDMEE3RjhENUNBMTFBOTAxOUEwNzg5OUNGMjZBOTQ5MTdDQzI4NjVCQkRDMjE3NTUxRERGRjM5NEVDQzBCQ0Y3NTBDQTk3QTUyMzI2QkY3Q0U2ODcwQzNCQUIyREVGOTY2NTAzMDRBNDAxQjlDODQ4RkY5OEM1NjQ4ODExN0M0QjI4NDNFN0VENzhENTFFQzkwMEVFQkZBOTZFQzJCODUxNDFBRjlCNTUzN0IzMkEzMUYwNEI1MTk2ODhCMTZEOTM0QTc4NjU1QjY5QTlCMzMwNDhGMEU2RjFGODU3MEVBREY3QzExMEVDODE3MjgwMDk5NzQ5REVDQTBFM0YwNTZCRDdCRjg5QzA0NTRCNjdGMTU5NkZFMjBCNTc3M0YwQjI1MDRCRTcwNEQ2RkQxMDkxMDVEMDdDN0IwQTVDNDMzMThCNzMyMjU2OTk5QzA3NUVBODAxNDY5OTg2NzA0MTQ0NjkyOTIyRTkxOEIxREM2Mjc1RkIyMUIwNTRBRkMxNTA1RTIwMkE5QUZGMTI0QUMzNkE4MzI1Qjg2RDYzMjMyNTFFNUM4RTIyQ0JBRkJDRTNEMzk0RUU5NDdDNDQzRTJCQTI4RTFGODkzRTM3NkRFMkQyRDIwNzREREIxNENDNUJEOEJCMzI1MUY0RjZEM0I4NTVCQjU5RTRDMDQ2Qzg1MUE1QkI3MENFMUNDQTU0QzI0NUQ4NzM5NTc0Q0MxNEIwRkVDRTJCMUM1Mjg0OTlEN0YzRjMyNTY4REVBMzlCODE0NTk0QzJBNzk2MjQyQzkxNTJDQzY3RjI0RkRDNDkzMjAyQTM5NDJCQ0QxNkUyREU3QjJGMEU1M0Y1QTU0MkIxMDUyRjgwQUE4N0M0NkM0NEFFQUExNDFDMTU1MjZDRjUzRjEwMDJFMEI4NkE2QzJCQUQzMzAzRjY1QzhDMjE4REQyQTZDQ0ZGMUE2NkYyNDcwRUYwRjkwQjI5MUFENDgyNjAxNTFBMUQxMUI4OTA4NzFCQzE1QkJFM0YwNDgwRkRCMjY2NEYxMDc0MDgxRjI3OEJCNENEMkQ0ODg0QTRFOEUxMkQ3QTA3NDlEMEE5QTZCOEVEN0NDNDE0QTM2RUEzOUE1OUU5MkQyMkY3MDBERTQ2Mjk5MzI4NTQ5NEVEQjk4MDY1QTA5Nzc3RDQzNUIwRDE5ODAxRUY0RkYzOTU2QzM4RUYwMUQxMTRGQ0RGODkxRDg1QUZCMTQ3NDg4RkFGRTI2OURBN0I4QTAyQ0I3QkFBMUQwRDZDRDc5RTdBREFCMzVCREZDMzdDQUUxRkQ5RTE1ODE3Q0UyMTdCMERFODFCNDcyMzgyQUNGM0Y0NzIyRDkwOTQyNEUyN0FGOTM2RDY1NDFENjc4RjBCMzM3MzkxNkJEQ0Y0NTg0NzcwNzU1MzY4QkUzMDU4NEE0RUMwRUU0RUI0MzYxOEE3QThGOTg4MjVCQTNDOUIwOUYzNDQ1MkM4QUQxQzM3RTBEQjBENzJGNzgyQjY0Q0JCNzYwQTU0REQzOTMyNkIzM0YyQTcwOEYwNTU4OTlDQTY1MTZBQkRGMjVEQTg0NzE3OUM0RDBFRTc5MzFCMjIxRjZEQ0NCMkU5NTNGM0FCQjMyMkFBNUM1OTI3Q0E1NzlEQUI1M0ZDRThCNDlBOTZFMkE3RTVBMkU0NTFGRTMzRjI4NzQzOEMzOUVDMDBCODJCRDUzOUQwQ0NDMEI5MTE1NkNEMkRFRUJEOEM1MzA3RUZFMzQyMjJCRjJERkQwMjIzMzFCRDgyQ0MzMEMyQTAzMUZDREMwQzA0QzdGMkYwMTU0QkEzNEVBMDNFQzlEMjZBQjI1MTJEODg5QTQzQkE3OENGM0RGREY3NTNFNzAwQjUwODI4NTMzRTE5N0VFNjAyRjNCOERGNjQ5RDg1REE0MDJFRjgxQjQyM0IzMUVEMTYyMDg5QTUyMjUwQTYxNTFBREMwOEQ3OUZDODA4NzI0N0MxMDlGQTFCRENDMDM0RTI4MkU0ODUyODEyMjczMzlEMzFFNTJFQ0E4MjJGMzhCQzBBMzNCQzlEOENFNUEyOEZDN0FGRDAwMUQwMzA3MDAzODBBOTVBOTRDNjZDMDQ2QkFBQkI5Nzg0RkU1NUE0NjU0NDcwMjQxQkY1QzI5Njg2QTY3MzFBNTYyMEEzNzc5OUJDMEQ2OEJENzQ1N0Q3NUJFMTRGOUY4NzlDNzgzQTBDMDQ3NEIwM0JEREFBN0Q4Q0U3MjI4MkIwRTVDRkQ2QzdEMDk2QjE0MEIyM0NENEQ1RDJBRkE2NTcyNjE3M0Q3MzJFNjkyRjMyNzExNjkyOUNCNjJDMTk3NEYyODA5QTdFRjk5NjhCRTE1MDMxODE3RjhCNkNERUQ1MzY2NDJFRDJEQUUzRkM4QzEzRTVGODczMjVGNkI4RkVBMjRCN0UyMjY4OTBGQUQ3MjIyNzVDRDBGQkJERUQ0RjhGOTFCQkE0OUZERTk5OTlFRkJGNzgxQTgxNTE3RTMxOTdFRkE3MzVGNDI1RkU4RUUyN0IwQTQ0NjA0MjFCMDZBOEM1NzNBQUZBMTFCQzQ4Nw==";
		//payKey = EncryptUtil.decryptBase64(payKey, SECRET_KEY);
		System.out.println("第3行：" + payKey);
		JSONObject jsonObject = JSONObject.parseObject(payKey);
		String thirdSysId = jsonObject.getString("ThirdSysID");
		System.out.println("第4行：" + thirdSysId);
		String txCode = jsonObject.getString("TxCode");
		System.out.println("第五行：" + txCode);
		String requestType = jsonObject.getString("RequestType");
		System.out.println("第6行：" + requestType);
		String data = jsonObject.getString("Data");
		System.out.println("第7行：" + data);
		String auth = jsonObject.getString("Auth");
		System.out.println("第8行：" + auth);
		String url = jsonObject.getString("Url");
		System.out.println("第9行：" + url);
		model.addAttribute("thirdSysId", thirdSysId);
		model.addAttribute("txCode", txCode);
		model.addAttribute("requestType", requestType);
		model.addAttribute("data", data);
		model.addAttribute("auth", auth);
		model.addAttribute("url", url);
		return model;
	}
	@RequestMapping(value = "/sft/center",method = RequestMethod.GET)
	public String hello1(HttpServletRequest request, Model model) throws IOException {
		String orderId = request.getParameter("orderId");
		String payId = request.getParameter("payId");
		String payCode = request.getParameter("payCode");
		String authKey = request.getParameter("authKey");
		String url = "http://39.108.99.164/payment/unifiedOrder?authKey="+authKey;
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("payId", payId);
		map.put("payCode", payCode);
		String body = SftApplication.sendPostDataByJson(url, JSON.toJSONString(map), "utf-8");
		System.out.println(body);
		JSONObject json = JSON.parseObject(body);
		String payKey = json.getJSONObject("data").getString("payKey");
		System.out.println(payKey);
		model = decode(payKey,model);
		model.addAttribute("body", payKey);
		return "center";
	}

	public static String sendPostDataByJson(String url, String json, String encoding) throws ClientProtocolException, IOException {
		String result = "";

		// 创建httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		// 设置参数到请求对象中
		StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
		stringEntity.setContentEncoding("utf-8");
		httpPost.setEntity(stringEntity);

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = httpClient.execute(httpPost);

		// 获取结果实体
		// 判断网络连接状态码是否正常(0--200都数正常)
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(response.getEntity(), "utf-8");
		}
		// 释放链接
		response.close();

		return result;
	}

	public static void main(String[] args) {
		SpringApplication.run(SftApplication.class, args);
	}
}
