package client.widgets.toolbox;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

public class FluidListController<T> {

	public interface PageHandler {
		void nextPage();
		void reloadPage();
	}

	public FluidListController(HTMLListWidget<T> list, PageHandler handler) {
		this(list.getScrollPanel(), handler);
	}

	public FluidListController(ScrollPanel scrollPanel, PageHandler handler) {
		Window.addResizeHandler(new ListResizeHandler(handler));
		scrollPanel.addScrollHandler(new FluidScrollHandler(scrollPanel, handler));
	}

	private class ListResizeHandler implements ResizeHandler {

		private final Timer timer;

		public ListResizeHandler(final PageHandler handler) {
			timer = new Timer() {
				@Override
				public void run() {
					handler.reloadPage();
				}
			};
		}

		@Override
		public void onResize(ResizeEvent event) {
			timer.cancel();
			timer.schedule(200);
		}
	}

	private class FluidScrollHandler implements ScrollHandler {

		private final ScrollPanel scrollPanel;
		private final PageHandler handler;
		private int lastScrollPosition;

		public FluidScrollHandler(ScrollPanel scrollPanel, PageHandler handler) {
			this.scrollPanel = scrollPanel;
			this.handler = handler;
			this.lastScrollPosition = 0;
		}

		@Override
		public void onScroll(ScrollEvent event) {
			if (lastScrollPosition > scrollPanel.getMaximumVerticalScrollPosition())
				lastScrollPosition = 0;
			if (isScrollingUp() || !scrolledHalfOfContent()) return;
			lastScrollPosition = scrollPanel.getVerticalScrollPosition();
			handler.nextPage();
		}

		private boolean isScrollingUp() {
			return lastScrollPosition >= scrollPanel.getVerticalScrollPosition();
		}

		private boolean scrolledHalfOfContent() {
			return scrollPanel.getVerticalScrollPosition() >= (scrollPanel.getMaximumVerticalScrollPosition() - lastScrollPosition) / 2;
		}
	}
}
