package com.future.link.utils;

import java.util.Date;

import com.future.link.wx.model.TextMessage;
import com.sun.xml.internal.ws.util.xml.XmlUtil;

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
}
