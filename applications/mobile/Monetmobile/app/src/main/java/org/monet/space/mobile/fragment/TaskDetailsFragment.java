package org.monet.space.mobile.fragment;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.ChatMessageReceivedEvent;
import org.monet.space.mobile.events.DetailsPageChangedEvent;
import org.monet.space.mobile.helpers.FontUtils;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.fragment.Fragment;
import org.monet.space.mobile.presenter.TaskDetailsPresenter;
import org.monet.space.mobile.view.TaskDetailsView;

import roboguice.inject.InjectView;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;
import com.viewpagerindicator.TabPageIndicator;

public class TaskDetailsFragment extends Fragment<TaskDetailsView, TaskDetailsPresenter, Void> implements TaskDetailsView, OnPageChangeListener {

	private static final int PAGE_DETAILS_ID = 0;
	private static final int PAGE_CHAT_ID = 1;

	@InjectView(R.id.page_loader)
	LinearLayout pageLoader;

	@InjectView(R.id.tab_bar)
	TabPageIndicator tabBar;

	@InjectView(R.id.viewpager)
	ViewPager viewPager;

	private Handler mHandler;
	private TaskDetails mTaskDetails;

	public static TaskDetailsFragment build(long taskId, boolean showChatPage) {
		TaskDetailsFragment details = new TaskDetailsFragment();
		Bundle args = new Bundle();
		args.putLong(RouterHelper.ID, taskId);
		args.putBoolean(RouterHelper.SHOW_CHAT_PAGE, showChatPage);
		details.setArguments(args);
		return details;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mHandler = new Handler();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_taskdetails, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		tabBar.setOnPageChangeListener(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			this.viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

		FontUtils.setRobotoFont(this.getActivity(), this.getView());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		presenter.initialize();
	}

	@Subscribe
	public void onChatMessageReceived(final ChatMessageReceivedEvent event) {
		this.mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (mTaskDetails != null && event.TaskId == mTaskDetails.id && viewPager.getCurrentItem() != PAGE_CHAT_ID) {
					mTaskDetails.notReadChats++;
					refreshNotReadChats();
				}
			}

		});
	}

	private void refreshNotReadChats() {
		tabBar.notifyDataSetChanged();
	}

	@Override
	public void showChatPage() {
		this.viewPager.setCurrentItem(PAGE_CHAT_ID, false);
	}

	@Override
	public void setTaskDetails(TaskDetails value) {
		if (value != null) {
			if (mTaskDetails == null || mTaskDetails.id != value.id || viewPager.getAdapter() == null) {
				PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager(), value);
				this.viewPager.setAdapter(pageAdapter);
				this.tabBar.setViewPager(viewPager);

				Animation outAnimation = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out);
				outAnimation.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						pageLoader.setVisibility(View.GONE);
					}
				});
				pageLoader.startAnimation(outAnimation);

				viewPager.setVisibility(View.VISIBLE);
				Animation inAnimation = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in);
				viewPager.startAnimation(inAnimation);

				if (value.serverId == null)
					this.tabBar.setVisibility(View.GONE);
				else {
					this.tabBar.setVisibility(View.VISIBLE);
					tabBar.startAnimation(inAnimation);
				}
			} else {
				((PageAdapter) this.viewPager.getAdapter()).update(value);
			}
			mTaskDetails = value;
		} else {
			viewPager.setVisibility(View.GONE);
			tabBar.setVisibility(View.GONE);
			pageLoader.setVisibility(View.VISIBLE);
		}

	}

	public class PageAdapter extends FragmentStatePagerAdapter {

		private TaskDetails mTaskDetails;
		private int count;
		private TaskChatPageFragment mChatPageFragment;
		private TaskDetailsPageFragment mDetailsPageFragment;

		public PageAdapter(FragmentManager fm, TaskDetails taskDetails) {
			super(fm);
			this.mTaskDetails = taskDetails;
			this.count = mTaskDetails.serverId == null ? 1 : 2;
			this.mChatPageFragment = TaskChatPageFragment.build(mTaskDetails);
			this.mDetailsPageFragment = TaskDetailsPageFragment.build(mTaskDetails);
		}

		public void update(TaskDetails value) {
			this.mTaskDetails = value;
			this.mChatPageFragment.refresh(value);
			this.mDetailsPageFragment.refresh(value);
		}

		@Override
		public int getCount() {
			return this.count;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case PAGE_CHAT_ID:
					String value = getResources().getString(R.string.chat_tab);
					if (mTaskDetails.notReadChats > 0) {
						value += String.format(" (%1d)", mTaskDetails.notReadChats);
					}
					return value;

				case PAGE_DETAILS_ID:
					return getString(R.string.details_tab);
			}
			return null;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			switch (position) {
				case PAGE_CHAT_ID:
					return mChatPageFragment;
				case PAGE_DETAILS_ID:
					return mDetailsPageFragment;
			}
			return null;
		}

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ZoomOutPageTransformer implements ViewPager.PageTransformer {

		private static float MIN_SCALE = 0.85f;
		private static float MIN_ALPHA = 0.5f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					view.setTranslationX(horzMargin - vertMargin / 2);
				} else {
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}

				// Scale the page down (between MIN_SCALE and 1)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int id) {
		if (id == PAGE_CHAT_ID) {
			presenter.viewChats();
			refreshNotReadChats();
		}
		BusProvider.get().post(new DetailsPageChangedEvent(id == PAGE_CHAT_ID));
	}

	public long getTaskId() {
		return this.mTaskDetails != null ? this.mTaskDetails.id : -1;
	}

}
