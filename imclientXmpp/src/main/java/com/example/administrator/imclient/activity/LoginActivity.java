package com.example.administrator.imclient.activity;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.imclient.R;
import com.example.administrator.imclient.activity.service.ChatService;

public class LoginActivity extends BaseActivity {
	private EditText et_pwd;
	private EditText et_accout;
	private XMPPConnection connection;
	private boolean isLoginSuccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_pwd = (EditText) findViewById(R.id.pwd);
		et_accout = (EditText) findViewById(R.id.account);
		new Thread(){

			public void run() {
				ConnectionConfiguration config = new ConnectionConfiguration("10.0.2.2", 5222, "itheima.com");
				//���ÿ��Ե���  ������logcat��������˺Ϳͻ���֮��ͨѶ��xmpp��Ϣ������
				config.setDebuggerEnabled(true);
				connection = new XMPPConnection(config);
				try {
					connection.connect();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
		
	}
	
	public void login(View v){
		final String password = et_pwd.getText().toString();
		final String account = et_accout.getText().toString();
		if(password!=null && !TextUtils.isEmpty(password) && account!=null && !TextUtils.isEmpty(account)){
			new Thread(){
				public void run() {
					try {
						connection.login(account, password);
						//��¼�ɹ�
						isLoginSuccess = true;
					} catch (XMPPException e) {
						//��¼ʧ�ܻ����쳣 
						isLoginSuccess = false;
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							if(isLoginSuccess){
								//��¼�ɹ� ��ת�������б�ҳ��
								myapp.setConnection(connection);
								myapp.setMyAccout(account);
								startService(new Intent(getApplicationContext(),ChatService.class));
								Toast.makeText(getApplicationContext(), "��¼�ɹ�!", Toast.LENGTH_SHORT).show();
								startActivity(new Intent(getApplicationContext(),BuddyListActivity.class));
							}else{
								//��¼ʧ��
								Toast.makeText(getApplicationContext(), "��¼ʧ��!", Toast.LENGTH_SHORT).show();
							}
							
						}
					});
				};
			}.start();
			
		}
	}
}
