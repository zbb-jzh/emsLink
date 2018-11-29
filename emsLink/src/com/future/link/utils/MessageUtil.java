package com.future.link.utils;

import java.util.Date;

import com.future.link.wx.model.TextMessage;

/*
 * ��Ϣ��������
 */
public class MessageUtil {
	public static final String MSGTYPE_EVENT = "event";//��Ϣ����--�¼�
	public static final String MESSAGE_SUBSCIBE = "subscribe";//��Ϣ�¼�����--�����¼�
	public static final String MESSAGE_UNSUBSCIBE = "unsubscribe";//��Ϣ�¼�����--ȡ�������¼�
	public static final String MESSAGE_TEXT = "text";//��Ϣ����--�ı���Ϣ
	
	/*
	 * ��װ�ı���Ϣ
	 */
	public static String textMsg(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return XmlTextParse.textMsgToxml(text);
	}
	
	/*
	 * ��Ӧ�����¼�--�ظ��ı���Ϣ
	 */
	public static String subscribeForText(String toUserName,String fromUserName){
		return textMsg(toUserName, fromUserName, "欢迎关注本公众号");
	}
	
	/*
	 * ��Ӧȡ�������¼�
	 */
	public static String unsubscribe(String toUserName,String fromUserName){
		//TODO ���Խ���ȡ�غ����������ҵ����
		System.out.println("欢迎"+ fromUserName +"下次光临");
		return "";
	}
	
	/**
	 * 发送模板信息
	 */
	public static void pushTemplateMessage(String content) {
		
		String token = WXUtils.getToken();
		
		//设置url
        String pushMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
        
        //String json = "{\"template_id\":\""+templateId+"\",\"touser\":\""+openId+"\",\"data\":{\"result\":{\"value\":\"关注成功\",\"color\":\"#173177\"}}}";
       
        String result = HttpsGetUtil.postData(pushMessageUrl, content , "utf-8");
        
	}
}
