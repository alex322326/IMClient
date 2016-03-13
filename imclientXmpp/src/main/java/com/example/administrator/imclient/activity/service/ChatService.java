package com.example.administrator.imclient.activity.service;

import java.io.Serializable;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.imclient.R;
import com.example.administrator.imclient.activity.ChatActivity;
import com.example.administrator.imclient.activity.MyApplication;
import com.example.administrator.imclient.activity.dao.Sms;
import com.example.administrator.imclient.activity.dao.SmsDao;
import com.example.administrator.imclient.activity.utils.BeanUtils;

public class ChatService extends Service {
	//ÿһ��chat���󶼶�Ӧ���һ�����������
	private Chat chat;
	//�������������������
	private ChatManager chatManager;
	//����ʱ������Ϣ��javabean
	private Message message;
	private ChatManagerListener listenter;
	private XMPPConnection connection;
	private MyApplication myApp;
	private SmsDao smsDao;

	private  class MyMessageListener implements MessageListener{
		public void processMessage(Chat chat, final Message message) {
			Log.d("test", message.getBody()+"�յ���Ϣ");
			Sms sms = BeanUtils.MessageToSms(message);
			sms.setSession_id(BeanUtils.getPrefAccount(message.getFrom()));
			sms.setSession_name(myApp.getMyAccout());
			//���յ�����Ϣ�������
			smsDao.insert(sms);
			Uri uri = Uri.parse("content://itheima.imclientxmpp");
			getContentResolver().notifyChange(uri, null);
			sendNotification(message);
		}
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		myApp = (MyApplication) getApplication();
		myApp.setChatService(this);
		connection = myApp.getConnection();
		chatManager = connection.getChatManager();
		smsDao = myApp.getSmsDao();
		chatManager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if(!createdLocally){
					chat.addMessageListener(new MyMessageListener());
				}
				
			}
		});
	}
	
	/**
	 * ������Ϣ�ķ���
	 * @param userJID
	 * @param msg
	 */
	public void sendMessage(final String userJID,final Message msg){
		new Thread(){
			public void run() {
				//�ѷ��͵���Ϣ���뵽��ݿ���
				Sms sms = BeanUtils.MessageToSms(msg);
				sms.setSession_id(userJID);
				sms.setSession_name(myApp.getMyAccout());
				smsDao.insert(sms);
				Uri uri = Uri.parse("content://itheima.imclientxmpp");
				getContentResolver().notifyChange(uri, null);
				Chat chat = chatManager.createChat(userJID, new MyMessageListener());
				try {
					chat.sendMessage(msg);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	@SuppressLint("NewApi")
	private void sendNotification(Message msg){
		Builder builder = new Builder(getApplicationContext());
		builder.setContentTitle(BeanUtils.getPrefAccount(msg.getFrom())+"������Ϣ");
		builder.setContentText(msg.getBody());
		//���֮���Զ���ʧ
		builder.setAutoCancel(true);
		builder.setTicker(msg.getFrom()+"������Ϣ:"+msg.getBody());
		builder.setSmallIcon(R.mipmap.ic_launcher);
		Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
		intent.putExtra("userID", BeanUtils.getPrefAccount(msg.getFrom()));
		intent.putExtra("nick", msg.getSubject());
//		PendingIntent pintent = PendingIntent.
		builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(0, notification);
	}

}
