package com.future.link.wx.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;

import com.alibaba.fastjson.JSONObject;
import com.future.link.common.Result;
import com.future.link.delivery.service.SendDeliveryService;
import com.future.link.user.model.WxUser;
import com.future.link.utils.Constant;
import com.future.link.utils.HttpsGetUtil;
import com.future.link.utils.MessageUtil;
import com.future.link.utils.PayCommonUtil;
import com.future.link.utils.ToolDateTime;
import com.future.link.utils.WXUtils;
import com.future.link.utils.XMLUtil;
import com.future.link.utils.XmlTextParse;
import com.future.link.wx.sevice.WXService;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class WXController extends Controller {
	
	/**
	 * 微信公众号验证，主动发送请求
	 * @throws IOException 
	 */
	public void doFirstAuth() throws IOException {
		
		Boolean isGet = this.getRequest().getMethod().equalsIgnoreCase("GET");

        if(isGet){

        	System.out.println("开始校验签名");
            /**
             * 接收微信服务器发送请求时传递过来的4个参数
             */
            String signature = getPara("signature");//微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
            String timestamp = getPara("timestamp");//时间戳
            String nonce = getPara("nonce");//随机数
            String echostr = getPara("echostr");//随机字符串
            Prop prop = PropKit.use("WxConfig.properties");
    		
            //排序
            String sortString = WXUtils.sort(prop.get("token").trim(), timestamp, nonce);
            //加密
            String mySignature = WXUtils.sha1(sortString);
            //校验签名
            if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
                System.out.println("签名校验通过。");
                //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
                //response.getWriter().println(echostr);
                this.getResponse().getWriter().write(echostr);
            } else {
                System.out.println("签名校验失败.");
            }

        }else{

        	this.getRequest().setCharacterEncoding("UTF-8");
    		this.getResponse().setCharacterEncoding("UTF-8");
    		PrintWriter out = this.getResponse().getWriter();
    		String message = "success";
    		try {
    			//把微信返回的xml信息转义成map
    			Map<String, String> map = XmlTextParse.xmlToMap(this.getRequest());
    			String fromUserName = map.get("FromUserName");//消息来源用户标识
    			String toUserName = map.get("ToUserName");//消息目的用户标识
    			String msgType = map.get("MsgType");//消息类型
    			String content = map.get("Content");//消息内容
    			
    			String eventType = map.get("Event");
    			if(MessageUtil.MSGTYPE_EVENT.equals(msgType)){//如果为事件类型
    				if(MessageUtil.MESSAGE_SUBSCIBE.equals(eventType)){//处理订阅事件
    					message = MessageUtil.subscribeForText(toUserName, fromUserName);
    					String json = "{\"template_id\":\"5-DADbr1h5lAxclKN0qheN_zi3nAyIEeeKD7gdpd3ow\",\"touser\":\""+fromUserName+"\",\"data\":{\"result\":{\"value\":\"关注成功\",\"color\":\"#173177\"}}}";
    					MessageUtil.pushTemplateMessage(json);
    					WxUser user = new WxUser();
    					user.setOpenId(fromUserName);
    					WXService.service.addWXUser(user);
    				}else if(MessageUtil.MESSAGE_UNSUBSCIBE.equals(eventType)){//处理取消订阅事件
    					message = MessageUtil.unsubscribe(toUserName, fromUserName);
    				}
    				this.getResponse().getWriter().write(message);
    			}
    		} catch (DocumentException e) {
    			e.printStackTrace();
    		}finally{
    			out.println(message);
    			if(out!=null){
    				out.close();
    			}
    		}

        }
        
		
	}
	
	/**
	 * 获取微信用户信息
	 * @return
	 * @throws Exception
	 */
	public void getWxUserInfo() throws Exception{
		WxUser user=(WxUser) this.getRequest().getSession().getAttribute("wxuser");
		String pageUrl = this.getPara("url");
		if(null == user) {
			Prop prop = PropKit.use("WxConfig.properties");
			

	        //String redirectUri = "http://fccd104a.ngrok.io/link/wx/auth";

	        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + prop.get("appid") +"&redirect_uri=" + prop.get("redirecturi") +"&response_type=code&scope=snsapi_userinfo&state="+ pageUrl +"&connect_redirect=1#wechat_redirect";
	        this.getResponse().sendRedirect(url);
		}else {
			redirect(pageUrl);
		}
    }
	
	/**
	 * 授权回调
	 * @throws ServletException
	 * @throws IOException
	 */
    public void auth()throws ServletException, IOException {
    	
    	Prop prop = PropKit.use("WxConfig.properties");

            //拼接
            String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                    + "appid="
                    + prop.get("appid")
                    + "&secret="
                    + prop.get("appsecret")
                    + "&code=CODE&grant_type=authorization_code";
            String get_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
            this.getRequest().setCharacterEncoding("UTF-8");
            this.getResponse().setCharacterEncoding("UTF-8");
            String code = this.getRequest().getParameter("code");
            if(code == null || code.equals("")){
              
                return;
            }
            System.out.println("******************code=" + code);
            get_access_token_url = get_access_token_url.replace("CODE", code);
            String json = HttpsGetUtil.doHttpsGetJson(get_access_token_url);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String access_token = "", openid = "", refresh_token = "";

        if(this.getRequest().getSession().getAttribute("code") != null && code.equals(this.getRequest().getSession().getAttribute("code"))){
            access_token = (String) this.getRequest().getSession().getAttribute("access_token");
            openid = (String) this.getRequest().getSession().getAttribute("openid");
            refresh_token = (String) this.getRequest().getSession().getAttribute("refresh_token");
        }else {
            access_token = jsonObject.getString("access_token");
            openid = jsonObject.getString("openid");
            refresh_token = jsonObject.getString("refresh_token");
        }

        this.getRequest().getSession().setAttribute("code", code);
        this.getRequest().getSession().setAttribute("access_token", access_token);
        this.getRequest().getSession().setAttribute("refresh_token", refresh_token);
        this.getRequest().getSession().setAttribute("openid", openid);

        //第五步验证access_token是否失效；展示都不需要
        String chickUrl="https://api.weixin.qq.com/sns/auth?access_token="+access_token+"&openid="+openid;
        String chickjson = HttpsGetUtil.doHttpsGetJson(chickUrl);
        JSONObject chickjsonObject = JSONObject.parseObject(chickjson);
        if(!"0".equals(chickjsonObject.getString("errcode"))){
            // 第三步：刷新access_token（如果需要）-----暂时没有使用,参考文档https://mp.weixin.qq.com/wiki，
            String refreshTokenUrl="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+prop.get("appid")+"&grant_type=refresh_token&refresh_token="+refresh_token;
            String refreshjson = HttpsGetUtil.doHttpsGetJson(refreshTokenUrl);
            JSONObject refreshInfo = JSONObject.parseObject(refreshjson);

            System.out.println(refreshInfo.toString());
            access_token = refreshInfo.getString("access_token");
            this.getRequest().getSession().setAttribute("access_token", access_token);
        }

            get_userinfo = get_userinfo.replace("ACCESS_TOKEN", access_token);
            get_userinfo = get_userinfo.replace("OPENID", openid);
            String userInfoJson = HttpsGetUtil.doHttpsGetJson(get_userinfo);
            JSONObject userInfoJO = JSONObject.parseObject(userInfoJson);
            String user_openid = userInfoJO.getString("openid");
            String user_nickname = userInfoJO.getString("nickname");
            String user_sex = userInfoJO.getString("sex");
            String user_province = userInfoJO.getString("province");
            String user_city = userInfoJO.getString("city");
            String user_country = userInfoJO.getString("country");
            String user_headimgurl = userInfoJO.getString("headimgurl");

            String user_privilege = userInfoJO.getString("privilege");
//            String user_unionid = userInfoJO.getString("unionid");

            WxUser user = new WxUser();
            user.setCity(user_city);
            user.setCountry(user_country);
            user.setHeadimgUrl(user_headimgurl);
            user.setNickName(URLEncoder.encode(user_nickname, "utf-8"));
            user.setOpenId(user_openid);
            user.setPrivilege(user_privilege);
            user.setProvince(user_province);
            user.setSex(user_sex);
//            customerInfo.setUnionId(user_unionid);
            
            WxUser wxuser = WXService.service.addWXUser(user);
            wxuser.setNickName(URLDecoder.decode(user_nickname, "utf-8"));
            this.getRequest().getSession().setAttribute("wxuser", wxuser);
            String pageUrl = this.getPara("state");
            redirect(pageUrl);
    }
    
    /**
     * 微信支付
     * @throws Exception
     */
    public void wxPayH5() throws Exception {
		
    	SortedMap<Object, Object> result = new TreeMap<Object, Object>();
		
		Prop prop = PropKit.use("wxconfig.properties");
		
		try {
			// 付款金额，必填
			String total_fee = this.getPara("totalFee");
			// 充值类型：1 积分 2 现金
			//czstyle = Integer.valueOf(request.getParameter("czstyle"));
			// ip地址获取
			String basePath = this.getRequest().getServerName() + ":" + this.getRequest().getServerPort();
			// 账号信息
			String appid = prop.get("appid"); // appid
			String mch_id = prop.get("partner"); // 商业号
			String key = prop.get("apikey"); // key 注：key为商户平台设置的密钥key
 
			String currTime = PayCommonUtil.getCurrTime();
			String strTime = currTime.substring(8, currTime.length());
			String strRandom = PayCommonUtil.buildRandom(4) + "";
			String nonce_str = strTime + strRandom;
			// 价格 注意：价格的单位是分
			String order_price = new BigDecimal(total_fee).multiply(new BigDecimal(100)).toString().split("\\.")[0];
			// 自己网站上的订单号
			String out_trade_no = this.getPara("orderId");
			// 获取发起电脑 ip
			String spbill_create_ip = HttpsGetUtil.getRealIp(this.getRequest());
			
			// 回调接口
			String notify_url = prop.get("notifyUrl");
			// 页面跳转同步通知页面路径
			String trade_type = "JSAPI";
 
			// 设置package订单参数
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			packageParams.put("appid", appid);
			packageParams.put("body", "快递服务费");
			packageParams.put("mch_id", mch_id);
			// 生成签名的时候需要你自己设置随机字符串
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("notify_url", notify_url);
			packageParams.put("out_trade_no", out_trade_no);
			
			packageParams.put("spbill_create_ip", spbill_create_ip);
			packageParams.put("total_fee", order_price);
			packageParams.put("trade_type", trade_type);
			
			//packageParams.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://www.xxxx.com\",\"wap_name\": \"学易资源分享平台\"}}");
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			//String sign =  WXUtils.getSign(packageParams, "utf-8", key);
			packageParams.put("sign", sign);
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpsGetUtil.postData(prop.get("unifiedorderurl"), requestXML);
			Map map = XMLUtil.doXMLParse(resXml);
			String prepay_id = (String) map.get("prepay_id");
			//确认支付过后跳的地址,需要经过urlencode处理
			/*String urlString = URLEncoder.encode("http://www.xxxx.com/xxxxx/my_waRecord", "GBK");
			String mweb_url = map.get("mweb_url")+"&redirect_url="+urlString;
			
			this.getResponse().sendRedirect(mweb_url);*/
			result.put("appid", appid);
			result.put("nonceStr", nonce_str);
			result.put("package", "prepay_id="+prepay_id);
			result.put("timeStamp", ToolDateTime.getDateByTime());
			result.put("signType", "MD5");
			result.put("paySign", PayCommonUtil.createSign("UTF-8", result, key));
			
			renderJson(new Result(Constant.SUCCESS, result));
		} catch (Exception e) {
			
			result.put("errormsg", e.getMessage());
		}
	}
    
    

	/**
	 * 执行回调 确认支付后处理事件 例如添加金额到数据库等操作
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void payNotify()throws Exception {
		System.out.println("进入支付h5回调=====================");
		String xmlMsg = readData(this.getRequest());
		System.out.println("pay notice---------"+xmlMsg);
		Map params = XMLUtil.doXMLParse(xmlMsg);
//		String appid  = params.get("appid");
//		//商户号
//		String mch_id  = params.get("mch_id");
		//String result_code  = params.get("result_code")+"";
//		String openId      = params.get("openid");
//		//交易类型
//		String trade_type      = params.get("trade_type");
//		//付款银行
//		String bank_type      = params.get("bank_type");
//		// 总金额
//		String total_fee     = params.get("total_fee");
//		//现金支付金额
//		String cash_fee     = params.get("cash_fee");
//		// 微信支付订单号
//		String transaction_id      = params.get("transaction_id");
//		// 商户订单号
//		String out_trade_no      = params.get("out_trade_no");
//		// 支付完成时间，格式为yyyyMMddHHmmss
//		String time_end      = params.get("time_end");
		
		/////////////////////////////以下是附加参数///////////////////////////////////
		
		//String attach      = params.get("attach")+"";
//		String fee_type      = params.get("fee_type");
//		String is_subscribe      = params.get("is_subscribe");
//		String err_code      = params.get("err_code");
//		String err_code_des      = params.get("err_code_des");
		
		String return_code=params.get("return_code")+"";  
        String result_code=params.get("result_code")+"";  
        String out_trade_no = params.get("out_trade_no")+"";
        String resXml = "";
        if("SUCCESS".equals(return_code)&&"SUCCESS".equals(result_code)){  
            //表示支付成功  
            //sign进行验签，确保消息的真伪  
            String sign = params.get("sign")+"";//sign不参与验签  
            
            Prop prop = PropKit.use("wxconfig.properties");
    		
    		String key = prop.get("apikey"); // key 注：key为商户
            String reSign = WXUtils.getSign(params, key, "utf-8");  
            if(sign.equals(reSign)){  
                //验签成功，进行结算  
                System.out.println("验签成功");
                
                SendDeliveryService.service.updateOrderPayStatus(Integer.valueOf(out_trade_no));
                //----待修改，结算时，加锁加事务，验证订单是否有效，判断金额是否正确  
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }else {
            	resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[签名错误]]></return_msg>" + "</xml> ";
            }
        }else {
        	resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[签名错误]]></return_msg>" + "</xml> ";
        }
        
        BufferedOutputStream out = new BufferedOutputStream(this.getResponse().getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();
		
		/*try {
 
			// 过滤空 设置 TreeMap
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String parameter = (String) it.next();
				String parameterValue = params.get(parameter)+"";
				String v = "";
				if (null != parameterValue) {
					v = parameterValue.trim();
				}
				System.out.println("value==============="+v);
				packageParams.put(parameter, v);
			}
			// 查看回调参数
			// 账号信息
			String resXml = "";
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
				
				try {
				} catch (Exception e) {
				}
				////////// 执行自己的业务逻辑////////////////return_msg
				// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[充值失败]]></return_msg>" + "</xml> ";
			}
			BufferedOutputStream out = new BufferedOutputStream(this.getResponse().getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
 
	}
	
	public static String readData(HttpServletRequest request) {
		BufferedReader br = null;
		try {
			StringBuilder result = new StringBuilder();
			br = request.getReader();
			for (String line; (line=br.readLine())!=null;) {
				if (result.length() > 0) {
					result.append("\n");
				}
				result.append(line);
			}
 
			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (br != null)
				try {br.close();} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	

}
