package com.example.administrator.imclient.activity.utils;

import com.example.administrator.imclient.R;
import com.example.administrator.imclient.activity.dao.Sms;

import org.jivesoftware.smack.packet.Message;




public class BeanUtils {

	public static Sms MessageToSms(Message msg){
		Sms smsinfo = new Sms();
		String fromAccount = getPrefAccount(msg.getFrom());
		smsinfo.setFrom_id(fromAccount);
		smsinfo.setFrom_avatar(R.mipmap.ic_launcher);
		smsinfo.setFrom_nick(msg.getSubject());// getSubject 保存 nick
		smsinfo.setBody(msg.getBody());
		smsinfo.setType(msg.getType().toString());
		smsinfo.setTime(System.currentTimeMillis());
		return smsinfo;
	}
	public static String getPrefAccount(String account){
		if(account.contains("/Spark")){
			account = account.split("/Spark")[0];
		}
		return account;
	}
}
