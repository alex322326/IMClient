package com.example.administrator.imclient.activity;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.imclient.R;
import com.example.administrator.imclient.activity.service.ChatService;
import com.example.administrator.imclient.activity.utils.MyTime;

public class ChatActivity extends BaseActivity {
	private TextView title;
	private EditText content;
	private ListView list;
	private ChatService service;
//	private Chat chat;
//	private ChatManager chatManager;
//	private Message message;
//	private ChatManagerListener listenter;
	private String userID;
private MyAdapter adapter;
//	private  class MyMessageListener implements MessageListener{
//		public void processMessage(Chat chat, final Message message) {
//			Log.d("test", message.getBody()+"�յ���Ϣ");
//			handler.post(new Runnable() {
//				@Override
//				public void run() {
//					Toast.makeText(getApplicationContext(), message.getBody(), 0).show();
//				}
//			});
//			
//		}
//		
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		title = (TextView) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.input);
		list = (ListView) findViewById(R.id.listview);
		String nick = getIntent().getStringExtra("nick");
		userID = getIntent().getStringExtra("userID");
		service = myapp.getChatService();
		Uri uri = Uri.parse("content://itheima.imclientxmpp");
		getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()){
			public void onChange(boolean selfChange) {
				refreshList();
			};
			
			public void onChange(boolean selfChange, Uri uri) {
				refreshList();
			};
		} );
		String sql = "select * from  SMS where  SESSION_ID=? and  SESSION_NAME=? order by TIME ASC ;";
		// List
		Cursor cursor = myapp.getDb().rawQuery(sql, new String[] { userID, myapp.getMyAccout()});
		adapter = new MyAdapter(getApplicationContext(), cursor, true);
		list.setAdapter(adapter);
		if(cursor.getCount()>3){
			list.setSelection(cursor.getCount()-1);
		}
		
//		new Thread(){
//			public void run() {
//				chatManager = connection.getChatManager();
//				chatManager.addChatListener(new ChatManagerListener() {
//					@Override
//					public void chatCreated(Chat chat, boolean createdLocally) {
//						if(!createdLocally){
//							chat.addMessageListener(new MyMessageListener());
//						}
//						
//					}
//				});
//				chat = chatManager.createChat(userID, new MyMessageListener());
//			};
//		}.start();
		
	}
	/**
	 * ���������¼�б��б�
	 */
	private void refreshList() {
		String sql = "select * from  SMS where  SESSION_ID=? and  SESSION_NAME=? order by TIME ASC ;";
		// List
		Cursor cursor = myapp.getDb().rawQuery(sql, new String[] { userID, myapp.getMyAccout()});
		//adapter.swapCursor(cursor);
		adapter.changeCursor(cursor);
		list.setSelection(cursor.getCount()-1);
	}
	/**
	 * ������Ϣ
	 * @param v
	 */
	public void send(View v){
		String body = content.getText().toString();
		//��������ȡ�������� ����Message����
		final Message message = new Message();
		message.setBody(body);
		message.setType(Type.chat);
		message.setTo(userID);
		message.setFrom(myapp.getMyAccout());
		service.sendMessage(userID, message);
//		new Thread(){
//			public void run() {
//				try {
//					chat.sendMessage(message);
//				} catch (XMPPException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};
//		}.start();
		
	}
	
	class MyAdapter extends CursorAdapter{

		public MyAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getItemViewType(int position) {
			//��ȡ���α�
			Cursor cursor = getCursor();
			cursor.moveToPosition(position);
			String from = cursor.getString(cursor.getColumnIndex("FROM_ID"));
			if(from.equals(myapp.getMyAccout())){
				//���from_ID �͵�ǰ�û���ͬ ˵�����Լ����͵���Ϣ
				return 0;
			}
			return 1;
		}
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			if (type == 0) {
				int layoutId = R.layout.item_chat_send;
				return setMessage(position, convertView, layoutId);
			} else {
				int layoutId = R.layout.item_chat_receive;
				return setMessage(position, convertView, layoutId);
			}
		}
		
		private View setMessage(int position, View convertView, int layoutId) {
			// view =====start
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				view = View.inflate(getBaseContext(), layoutId, null);
				holder.content = (TextView) view.findViewById(R.id.content);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.head = (ImageView) view.findViewById(R.id.head);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			// view =====end

			// ���
			Cursor cursor = getCursor();
			cursor.moveToPosition(position);

			String body = cursor.getString(cursor.getColumnIndex("BODY"));
			long time = cursor.getLong(cursor.getColumnIndex("TIME"));
			String timeString = MyTime.geTime(time);

			holder.time.setText(timeString);
			holder.content.setText(body);
			holder.head.setImageResource(R.mipmap.ic_launcher);
			return view;
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return null;
//			ViewHolder holder = new ViewHolder();
//			View view;
//			if(getItemViewType(cursor.getPosition()) == 1){
//				view = View.inflate(getBaseContext(), R.layout.item_chat_send, null);
//			}else{
//				view = View.inflate(getBaseContext(), R.layout.item_chat_receive, null);
//			}
//			
//			holder.content = (TextView) view.findViewById(R.id.content);
//			holder.time = (TextView) view.findViewById(R.id.time);
//			holder.head = (ImageView) view.findViewById(R.id.head);
//			view.setTag(holder);
//			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
//			ViewHolder holder = (ViewHolder) view.getTag();
//			holder.content.setText(cursor.getString(cursor.getColumnIndex("body")));
//			holder.time.setText(MyTime.geTime(cursor.getLong(cursor.getColumnIndex("time"))));
//			
		}
		class ViewHolder {
			TextView time;
			TextView content;
			ImageView head;
		}
	}
}
