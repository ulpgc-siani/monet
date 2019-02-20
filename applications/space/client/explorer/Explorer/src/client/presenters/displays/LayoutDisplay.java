package client.presenters.displays;

public class LayoutDisplay extends Display {
	private boolean reducedMode = false;

	public static final Type TYPE = new Type("LayoutDisplay", Display.TYPE);

	public boolean isReducedMode() {
		return this.reducedMode;
	}

	public void toggleMode() {
		notifyToggleMode();
	}

	public void reduceMode() {
		this.reducedMode = true;
		notifyReduceMode();
	}

	public void expandMode() {
		this.reducedMode = false;
		notifyExpandMode();
	}

	private void notifyToggleMode() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.toggleMode();
			}
		});
	}

	private void notifyReduceMode() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.reduceMode();
			}
		});
	}

	private void notifyExpandMode() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.expandMode();
			}
		});
	}

	private void notifyScroll(final int position) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.scroll(position);
			}
		});
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public void scroll(int position) {
		notifyScroll(position);
	}

	public interface Hook extends Display.Hook {
		void toggleMode();
		void reduceMode();
		void expandMode();
		void scroll(int position);
	}

}
