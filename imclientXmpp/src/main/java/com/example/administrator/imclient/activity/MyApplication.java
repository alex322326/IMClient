package com.example.administrator.imclient.activity;



import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.imclient.activity.dao.DaoMaster;
import com.example.administrator.imclient.activity.dao.DaoSession;
import com.example.administrator.imclient.activity.dao.SmsDao;
import com.example.administrator.imclient.activity.service.ChatService;

import org.jivesoftware.smack.XMPPConnection;

public class MyApplication extends Application {
	private XMPPConnection connection;
	private String myAccout;
	private ChatService chatService;
	private SmsDao smsDao;
	private SQLiteDatabase db;
	public XMPPConnection getConnection() {
		return connection;
	}
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}
	public String getMyAccout() {
		return myAccout;
	}
	public void setMyAccout(String myAccout) {
		this.myAccout = myAccout;
	}
	public ChatService getChatService() {
		return chatService;
	}
	public void setChatService(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		db = new DaoMaster.DevOpenHelper(getApplicationContext(),"im.db",null).getWritableDatabase();
		DaoMaster master = new DaoMaster(db);
		DaoSession session = master.newSession();
		smsDao = session.getSmsDao();
	}
	public SmsDao getSmsDao() {
		return smsDao;
	}
	public void setSmsDao(SmsDao smsDao) {
		this.smsDao = smsDao;
	}
	
	public SQLiteDatabase getDb(){
		return this.db;
	}
}
