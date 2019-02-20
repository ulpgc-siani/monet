package org.monet.space.mobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.squareup.otto.Subscribe;
import org.monet.space.mobile.events.ChatMessageReceivedEvent;
import org.monet.space.mobile.helpers.FontUtils;
import org.monet.space.mobile.helpers.NotificationHelper;
import org.monet.space.mobile.model.ChatItem;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.fragment.ListFragment;
import org.monet.space.mobile.presenter.TaskChatPagePresenter;
import org.monet.space.mobile.view.TaskChatPageView;
import org.monet.space.mobile.widget.EmojiTextView;
import org.monet.space.mobile.R;

import roboguice.inject.InjectView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TaskChatPageFragment extends ListFragment<TaskChatPageView, TaskChatPagePresenter, Void> implements TaskChatPageView, OnClickListener, OnEditorActionListener {

	@InjectView(android.R.id.list)
	ListView mChatListView;

	@InjectView(R.id.send)
	View mSendButton;
	@InjectView(R.id.new_message)
	TextView mNewMessageView;

	private TaskDetails mTaskDetails;
	private List<ChatItem> mChatItems;
	private ArrayAdapter<ChatItem> mArrayAdapter;

	public static TaskChatPageFragment build(TaskDetails taskDetails) {
		TaskChatPageFragment details = new TaskChatPageFragment();
		details.mTaskDetails = taskDetails;
		details.mChatItems = taskDetails.chatItems;
		return details;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_taskdetails_chat_page, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSendButton.setOnClickListener(this);
		mNewMessageView.setOnEditorActionListener(this);

		FontUtils.setRobotoFont(this.getActivity(), this.getView());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.send:
				onSendMessage();
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
			case EditorInfo.IME_ACTION_SEND:
				onSendMessage();
				break;
			default:
				break;
		}
		return false;
	}

	private void onSendMessage() {
		String newMessage = mNewMessageView.getText().toString();
		if (newMessage.length() == 0)
			return;

		mNewMessageView.setText("");
		this.presenter.send(mTaskDetails, newMessage);
	}

	@Subscribe
	public void onChatMessageReceived(final ChatMessageReceivedEvent event) {
		if (event.TaskId == mTaskDetails.id) {
			event.Captured = true;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.refreshView();
	}

	public void refreshView() {
		if (mChatItems == null || getActivity() == null)
			return;

		mArrayAdapter = new ArrayAdapter<ChatItem>(getActivity(), R.layout.chat_item, -1, mChatItems) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ChatItem item = getItem(position);

				if (convertView == null)
					convertView = getLayoutInflater(null).inflate(R.layout.chat_item, mChatListView, false);

				int messageId;
				int dateId;
				if (item.IsOut) {
					messageId = R.id.message_out;
					dateId = R.id.date_out;
					convertView.findViewById(R.id.in).setVisibility(View.GONE);
					convertView.findViewById(R.id.out).setVisibility(View.VISIBLE);
				} else {
					messageId = R.id.message_in;
					dateId = R.id.date_in;
					convertView.findViewById(R.id.in).setVisibility(View.VISIBLE);
					convertView.findViewById(R.id.out).setVisibility(View.GONE);
				}

				((EmojiTextView) convertView.findViewById(messageId)).setEmojiText(item.Message);

				String dateText = DateUtils.formatSameDayTime(item.DateTime.getTime(), new Date().getTime(), DateFormat.SHORT, DateFormat.SHORT).toString();
				((TextView) convertView.findViewById(dateId)).setText(dateText);
				return convertView;
			}
		};
		this.setListAdapter(mArrayAdapter);

		NotificationHelper.cancelChatNotifications(this.getActivity(), mTaskDetails.id);
	}

	@Override
	public void addChatItem(ChatItem item) {
		mArrayAdapter.add(item);
	}

	public void refresh(TaskDetails value) {
		if (mArrayAdapter != null) {
			mArrayAdapter.clear();
			for (ChatItem chatItem : value.chatItems)
				mArrayAdapter.add(chatItem);
		} else {
			this.refreshView();
		}
	}

}
