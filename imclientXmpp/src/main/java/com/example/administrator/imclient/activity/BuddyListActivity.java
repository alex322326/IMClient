package com.example.administrator.imclient.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.imclient.R;

public class BuddyListActivity extends BaseActivity {
	private XMPPConnection connection;
	private ArrayList<RosterGroup> groups;
	private ExpandableListView listView;
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buddy_list);
		listView = (ExpandableListView) findViewById(R.id.listview);
		connection = myapp.getConnection();
		refreshRoaster();
		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				if(adapter!=null){

					RosterEntry entry = (RosterEntry) adapter.getChild(groupPosition, childPosition);
					String nick = entry.getName();
					String userID = entry.getUser();
					Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
					intent.putExtra("nick", nick);
					intent.putExtra("userID", userID);
					startActivity(intent);
				}
				return true;
			}
		});
	}
	
	private void refreshRoaster(){
		new Thread(){
			public void run() {
				//���еĺ��� ��ͨ��roster������ ��ȡ��roster���Ի�ȡ�����еķ�����Ϣ
				Roster roster = connection.getRoster();
				roster.addRosterListener(new RosterListener() {
					@Override
					public void presenceChanged(Presence presence) {
						refreshRoaster();
					}
					
					@Override
					public void entriesUpdated(Collection<String> addresses) {
						Log.d("test", "entriesUpdated");
						refreshRoaster();
					}
					
					@Override
					public void entriesDeleted(Collection<String> addresses) {
						Log.d("test", "entriesDeleted");
						refreshRoaster();
					}
					
					@Override
					public void entriesAdded(Collection<String> addresses) {
						Log.d("test", "entriesAdded");
						refreshRoaster();
					}
				});
				//��ȡ�����еķ�����Ϣ
				Collection<RosterGroup> tempGroups=roster.getGroups();
				//��ȡ�����б����ļ���
				groups = new ArrayList<>(tempGroups);
				for(RosterGroup group:groups){
					//ͨ����ѷ��� ���Ի�ȡ�������ڵ����к��ѵ���Ϣ Collection<RosterEntry>
					Collection<RosterEntry> tempEntries = group.getEntries();
					//ͨ��Collection<RosterEntry>��������list
					ArrayList<RosterEntry> entries = new ArrayList<>(tempEntries);
					//�������list ���Ի�ȡ��ÿһ������ RosterEntry
					for(RosterEntry entry:entries){
						Log.d("test", entry.getName()+"name"+entry.getUser());
					}
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(adapter == null){
							adapter = new MyAdapter();
							listView.setAdapter(adapter);	
						}else{
							adapter.notifyDataSetChanged();
						}
					
					}
				});
			};
		}.start();
	}
	class MyAdapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition).getEntryCount();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			 RosterGroup group = groups.get(groupPosition);
			 ArrayList<RosterEntry> entries = new ArrayList<>(group.getEntries());
			return entries.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View view = null;
			ViewHoder holder = null;
			if (convertView == null) {
				holder = new ViewHoder();
				view = View.inflate(getBaseContext(), R.layout.item_buddy_group, null);
				holder.group = (TextView) view.findViewById(R.id.group);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHoder) view.getTag();
			}
			// ---end

			RosterGroup group = (RosterGroup) getGroup(groupPosition);
			holder.group.setText(group.getName());
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			View view = null;
			ViewHoder holder = null;
			if (convertView == null) {
				view = View.inflate(getBaseContext(), R.layout.item_buddy_list, null);
				holder = new ViewHoder();
				holder.head = (ImageView) view.findViewById(R.id.head);
				holder.name = (TextView) view.findViewById(R.id.name);
				holder.status = (TextView) view.findViewById(R.id.status);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHoder) view.getTag();
			}
			// view--end

			RosterEntry person = (RosterEntry) getChild(groupPosition, childPosition);

			holder.name.setText(person.getName());
			holder.status.setText(person.getUser());
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		class ViewHoder {
			// ---group
			TextView group;
			// ----child
			TextView name;
			TextView status;
			ImageView head;
		}
	}
}
