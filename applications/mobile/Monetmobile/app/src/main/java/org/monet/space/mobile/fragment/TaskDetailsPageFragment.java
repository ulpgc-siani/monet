package org.monet.space.mobile.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.DateUtils;
import org.monet.space.mobile.helpers.FontUtils;
import org.monet.space.mobile.helpers.ImageProvider;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskDetails.Attachment;
import org.monet.space.mobile.mvp.fragment.Fragment;
import org.monet.space.mobile.presenter.TaskDetailPagePresenter;
import org.monet.space.mobile.view.TaskDetailPageView;
import org.monet.space.mobile.widget.Timeline;

import roboguice.inject.InjectView;

import java.util.Date;

public class TaskDetailsPageFragment extends Fragment<TaskDetailPageView, TaskDetailPagePresenter, Void> implements TaskDetailPageView, OnClickListener, OnCheckedChangeListener {

	private static final String KEY_TASK_DETAILS = "key_task_details";

	@InjectView(R.id.main)
	RelativeLayout mainView;

	@InjectView(R.id.text_label)
	TextView labelText;

	@InjectView(R.id.text_source_label)
	TextView sourceLabelText;

	@InjectView(R.id.timeline)
	Timeline timeline;

	@InjectView(R.id.text_description)
	TextView descriptionText;

	@InjectView(R.id.view_urgent)
	View urgentView;

	@InjectView(R.id.view_geolocated)
	View geolocatedView;

	@InjectView(R.id.text_suggested_start_date)
	TextView suggestedStartDateText;
	@InjectView(R.id.text_suggested_end_date)
	TextView suggestedEndDateText;

	@InjectView(R.id.text_start_date)
	TextView startDateText;
	@InjectView(R.id.text_end_date)
	TextView endDateText;

	@InjectView(R.id.progress_text)
	TextView progressText;

	@InjectView(R.id.progress_bar)
	ProgressBar progressBar;

	@InjectView(R.id.header_comments)
	View commentsHeader;

	@InjectView(R.id.text_comments)
	TextView commentsText;

	@InjectView(R.id.attachments_header)
	View attachmentsHeaderView;

	@InjectView(R.id.attachments)
	LinearLayout attachmentsLayout;

	@InjectView(R.id.picture_attachments)
	LinearLayout pictureAttachmentsLayout;

	@InjectView(R.id.dates_details)
	View datesDetails;

	@InjectView(R.id.time_to_finish)
	TextView timeToFinish;

	@InjectView(R.id.show_dates_details)
	ToggleButton showDatesDetails;

	@InjectView(R.id.tool_bar_holder)
	LinearLayout toolBarHolder;

	private LinearLayout toolBar = null;

	private TaskDetails mTaskDetails;

	public static TaskDetailsPageFragment build(TaskDetails taskDetails) {
		TaskDetailsPageFragment details = new TaskDetailsPageFragment();
		details.mTaskDetails = taskDetails;
		return details;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_taskdetails_details_page, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if ((savedInstanceState != null) && (savedInstanceState.containsKey(KEY_TASK_DETAILS)))
			mTaskDetails = savedInstanceState.getParcelable(KEY_TASK_DETAILS);

		showDatesDetails.setOnCheckedChangeListener(this);
		timeline.setOnClickListener(this);

		FontUtils.setRobotoFont(this.getActivity(), this.getView());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (mTaskDetails != null)
			outState.putParcelable(KEY_TASK_DETAILS, mTaskDetails);
		//TODO nacho
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		this.presenter.resume();

		this.refreshView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.setHasOptionsMenu(true);

		this.presenter.initialize(mTaskDetails);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() != R.id.show_dates_details) return;
		datesDetails.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.timeline:
				showDatesDetails.toggle();
				break;
			case R.id.btn_continue:
				presenter.doContinue();
				break;
			case R.id.btn_display:
				presenter.doDisplay();
				break;
			case R.id.btn_unassign:
				new AlertDialog.Builder(getActivity())
						.setMessage(R.string.unassign_dialog_message).setTitle(R.string.unassign_dialog_title)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								presenter.doAbandon();
							}
						})
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						})
						.create().show();
				break;

			case R.id.btn_assign:
				presenter.doAssign();
			default:
				break;
		}
	}


	public void refresh(TaskDetails value) {
		this.presenter.refresh(value);
		mTaskDetails = value;
		refreshView();
	}

	public void refreshView() {
		if (mTaskDetails == null)
			return;

		this.labelText.setText(mTaskDetails.label);
		this.sourceLabelText.setText(mTaskDetails.sourceLabel);
		this.descriptionText.setText(mTaskDetails.description);

		if (!mTaskDetails.urgent)
			this.urgentView.setVisibility(View.GONE);

		if (mTaskDetails.position == null)
			this.geolocatedView.setVisibility(View.GONE);

		if (mTaskDetails.suggestedStartDate != null) {
			this.timeline.setSuggestStartDate(mTaskDetails.suggestedStartDate);
			this.suggestedStartDateText.setText(DateUtils.formatAsDate(this.getActivity(), mTaskDetails.suggestedStartDate));
		}

		if (mTaskDetails.suggestedEndDate != null) {
			this.timeline.setSuggestEndDate(mTaskDetails.suggestedEndDate);
			this.suggestedEndDateText.setText(DateUtils.formatAsDate(this.getActivity(), mTaskDetails.suggestedEndDate));

			if (mTaskDetails.endDate == null) {
				refreshEndDateView();
			}
		}

		if (mTaskDetails.startDate != null) {
			this.timeline.setStartDate(mTaskDetails.startDate);
			this.startDateText.setText(DateUtils.formatAsDate(this.getActivity(), mTaskDetails.startDate));
		}

		if (mTaskDetails.endDate != null) {
			this.timeline.setEndDate(mTaskDetails.endDate);
			this.endDateText.setText(DateUtils.formatAsDate(this.getActivity(), mTaskDetails.endDate));
		} else {
		}

		switch (mTaskDetails.tray) {
			case TaskDetails.TASK_TRAY_ASSIGNED:
				this.showToolbarAssigned();
				break;

			case TaskDetails.TASK_TRAY_FINISHED:
				this.showToolbarFinished();
				break;

			case TaskDetails.TASK_TRAY_UNASSIGNED:
				this.showToolbarUnassigned();
				break;
		}

		int progress;
		if (mTaskDetails.tray == TaskDetails.TASK_TRAY_FINISHED)
			progress = 100;
		else
			progress = (int) ((((float) mTaskDetails.step) / mTaskDetails.stepCount) * 100);
		this.progressBar.setProgress(progress);
		this.progressText.setText(progress + getString(R.string.progress_text));

		if (mTaskDetails.comments != null && mTaskDetails.comments.length() > 0) {
			this.commentsHeader.setVisibility(View.VISIBLE);
			this.commentsText.setVisibility(View.VISIBLE);
			this.commentsText.setText(mTaskDetails.comments);
		}

		refreshAttachmentsView();
	}

	private void showToolbarAssigned() {
		if (this.toolBar != null) {
			this.toolBarHolder.removeView(this.toolBar);
		}

		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.toolBar = (LinearLayout) inflater.inflate(R.layout.assigned_bar, null);

		this.toolBarHolder.addView(this.toolBar);

		this.toolBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.toolBarHolder.setVisibility(View.VISIBLE);

		this.toolBar.findViewById(R.id.btn_unassign).setOnClickListener(this);
		this.toolBar.findViewById(R.id.btn_continue).setOnClickListener(this);
	}

	private void showToolbarFinished() {
		if (this.toolBar != null) {
			this.toolBarHolder.removeView(this.toolBar);
		}

		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.toolBar = (LinearLayout) inflater.inflate(R.layout.finished_bar, null);

		this.toolBarHolder.addView(this.toolBar);
		this.toolBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.toolBarHolder.setVisibility(View.VISIBLE);

		this.toolBar.findViewById(R.id.btn_display).setOnClickListener(this);
	}

	private void showToolbarUnassigned() {
		if (this.toolBar != null) {
			this.toolBarHolder.removeView(this.toolBar);
		}

		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.toolBar = (LinearLayout) inflater.inflate(R.layout.unassigned_bar, null);

		this.toolBarHolder.addView(this.toolBar);
		this.toolBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.toolBarHolder.setVisibility(View.VISIBLE);

		this.toolBar.findViewById(R.id.btn_assign).setOnClickListener(this);
	}

	private void refreshAttachmentsView() {
		if (mTaskDetails.attachments.length == 0)
			return;

		LayoutInflater layoutInflater = this.getLayoutInflater(null);
		int position = 0;
		int positionPicture = 0;
		this.pictureAttachmentsLayout.removeAllViews();
		this.attachmentsLayout.removeAllViews();
		for (Attachment attachment : mTaskDetails.attachments) {
			if (attachment.contentType.startsWith("image")) {
				View view = layoutInflater.inflate(R.layout.attachment_picture_list_item, this.pictureAttachmentsLayout, false);
				view.setTag(attachment);
				view.setOnClickListener(this.attachmentClickListener);
				this.pictureAttachmentsLayout.addView(view, positionPicture++);
				LoadImageTask.execute((ImageView) view, mTaskDetails.id);
			} else {
				View view = layoutInflater.inflate(R.layout.attachment_list_item, this.attachmentsLayout, false);
				view.setTag(attachment);
				((TextView) view).setText(attachment.label);
				view.setOnClickListener(this.attachmentClickListener);
				this.attachmentsLayout.addView(view, position++);
			}
		}
		if (position > 0) {
			this.attachmentsHeaderView.setVisibility(View.VISIBLE);
			this.attachmentsLayout.setVisibility(View.VISIBLE);
		}
		if (positionPicture > 0) {
			this.pictureAttachmentsLayout.setVisibility(View.VISIBLE);
			if (positionPicture == 1) {
				ImageView view = ((ImageView) this.pictureAttachmentsLayout.getChildAt(0));
				this.pictureAttachmentsLayout.removeView(view);
				ViewGroup scrollView = ((ViewGroup) this.pictureAttachmentsLayout.getParent());
				ViewGroup parent = (ViewGroup) scrollView.getParent();
				if (parent != null) {
					int index = parent.indexOfChild(scrollView);
					parent.removeView(scrollView);
					LayoutParams params = view.getLayoutParams();
					params.width = LayoutParams.MATCH_PARENT;

					parent.addView(view, index);
				}
			}
		}
	}

	private void refreshEndDateView() {
		int timeToFinishResId;
		long time = new Date().getTime() - mTaskDetails.suggestedEndDate.getTime();
		long diffInSeconds = Math.abs(time) / 1000;

		long diff[] = new long[]{0, 0, 0};
	/* min */
		diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours */
		diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days */
		diff[0] = (diffInSeconds = (diffInSeconds / 24));

		StringBuilder builder = new StringBuilder();
		if (diff[0] > 0) {
			builder.append(getResources().getQuantityString(R.plurals.days, (int) diff[0], diff[0]));
			builder.append(" ");
		}
		if (diff[1] > 0) {
			builder.append(getResources().getQuantityString(R.plurals.hours, (int) diff[1], diff[1]));
			builder.append(" ");
		}
		if (diff[0] == 0 && diff[1] < 12 && diff[2] > 0) {
			builder.append(getResources().getQuantityString(R.plurals.minutes, (int) diff[2], diff[2]));
			builder.append(" ");
		}
		if (diff[0] == 0 && diff[1] == 0 && diff[2] == 0)
			builder.append(getResources().getString(R.string.seconds));

		if (time > 0) {
			timeToFinishResId = R.string.delayed_message;
		} else {
			timeToFinishResId = R.string.pending_message;
		}
		this.timeToFinish.setText(String.format(getString(timeToFinishResId), builder.toString().trim()));
		this.timeToFinish.setVisibility(View.VISIBLE);
	}

	private OnClickListener attachmentClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Attachment attachment = (Attachment) v.getTag();
			presenter.openAttachment(attachment);
		}

	};

	private static class LoadImageTask extends AsyncTask<String, Void, Drawable> {

		private ImageView view;
		private long jobId;
		private String imageId;

		private LoadImageTask(ImageView view, long jobId) {
			this.view = view;
			this.jobId = jobId;
			this.imageId = ((Attachment) view.getTag()).path;
		}

		public static void execute(ImageView view, long jobId) {
			new LoadImageTask(view, jobId).execute();
		}

		@Override
		protected Drawable doInBackground(String... params) {
			return ImageProvider.loadImageThumbnail(String.valueOf(this.jobId), this.imageId, this.view.getContext(), true);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			if (result != null) {
				this.view.setImageDrawable(result);
			}
		}
	}

}
