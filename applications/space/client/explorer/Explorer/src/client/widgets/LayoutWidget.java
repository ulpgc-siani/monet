package client.widgets;

import client.presenters.displays.LayoutDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;

public class LayoutWidget extends HTMLPanel {
	private final LayoutDisplay display;
	private Timer scrollTimer;
	private int lastScroll = -1;

	public LayoutWidget(LayoutDisplay display) {
		super("");
		this.display = display;
		addBehaviours();
		hook();
		if (clientIsInWindows())
			getScrollTriggerPanel().addClassName(StyleName.WINDOWS);
	}

	private boolean clientIsInWindows() {
		return Window.Navigator.getPlatform().toLowerCase().contains("win");
	}

	private void addBehaviours() {
		addScrollEvent();
	}

	private void addScrollEvent() {
		scrollTimer = new Timer() {
			@Override
			public void run() {
				scroll();
			}
		};

		$(getScrollTriggerPanel()).scroll(new Function() {
			@Override
			public void f() {
				if (scrollTimer != null)
					scrollTimer.cancel();

				scrollTimer.schedule(100);
			}
		});
	}

	private void scroll() {
		if ($(getScrollTriggerPanel()).scrollTop() > scrollThresholdValue())
			reduceLayout();
		else
			expandLayout();
	}

	private int scrollThresholdValue() {
		return display.isReducedMode() ? 30 : 50;
	}

	private void reduceLayout() {
		if (!display.isReducedMode())
            display.reduceMode();
	}

	private void expandLayout() {
		if (display.isReducedMode())
            display.expandMode();
	}

	private void toggle() {
		if (display.isReducedMode())
			expand();
		else
			reduce();
	}

	public static Element getMain() {
		return $(RootPanel.get()).find(StyleRule.MAIN_PANEL).get(0);
	}

	public static Element getMainDisplay() {
		return $(RootPanel.get()).find(StyleRule.MAIN_DISPLAY_PANEL).get(0);
	}

	private Element getScrollTriggerPanel() {
		return getMain();
	}

	private void hook() {
		display.addHook(new LayoutDisplay.Hook() {
			@Override
			public void toggleMode() {
				LayoutWidget.this.toggle();
			}

			@Override
			public void reduceMode() {
				LayoutWidget.this.reduce();
			}

			@Override
			public void expandMode() {
				LayoutWidget.this.expand();
			}

			@Override
			public void scroll(int position) {
				LayoutWidget.this.scroll(getScrollTriggerPanel(), $(getScrollTriggerPanel()).scrollTop() + position);
			}
		});
	}

	private void scroll(final Element element, final int position) {
		int scroll = element.getScrollTop();
		scroll += Math.round((position - scroll) * 0.3);
		if (Math.abs(lastScroll - scroll) <= 10) return;
		lastScroll = scroll;
		if (Math.abs(scroll - position) <= 10) {
			element.setScrollTop(position);
			return;
		}
		element.setScrollTop(scroll);
		new Timer() {
			@Override
			public void run() {
				scroll(element, position);
			}
		}.schedule(70);
	}

	private void expand() {
		getLayout().removeClass(StyleName.LAYOUT_REDUCED).addClass(StyleName.LAYOUT_EXPANDED);
	}

	private void reduce() {
		getLayout().removeClass(StyleName.LAYOUT_EXPANDED).addClass(StyleName.LAYOUT_REDUCED);
	}

	private GQuery getLayout() {
		return $(RootPanel.get()).children(StyleRule.LAYOUT);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(LayoutDisplay.TYPE);
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new LayoutWidget((LayoutDisplay) presenter);
		}

	}

	private interface StyleRule {
		String LAYOUT = "#main";
		String MAIN_PANEL = ".design-panel.main";
		String MAIN_DISPLAY_PANEL = ".design-panel.main .visiting .main-display";
	}

	private interface StyleName {
		String LAYOUT_EXPANDED = "expanded";
		String LAYOUT_REDUCED = "reduced";
		String WINDOWS = "windows";
	}

}
