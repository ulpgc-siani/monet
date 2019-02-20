package client.presenters.displays;

import client.core.model.Entity;
import client.core.model.HtmlPage;
import client.core.model.Node;
import client.presenters.operations.HideHelperOperation;
import client.services.callback.HtmlPageCallback;

public class HelperDisplay extends Display {
	private HtmlPage page;
	private Listener listener;

	public static final Type TYPE = new Type("HelperDisplay", Display.TYPE);

	public HelperDisplay() {
		page = null;
		addHideOperation();
	}

	public HtmlPage getPage() {
		return page;
	}

	public boolean isVisible() {
        return getPage() != null && !getPage().getContent().isEmpty();
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void setEntity(Entity entity) {
		this.page = null;

		if (entity instanceof Node)
			services.getNodeService().loadHelpPage((Node) entity, new HtmlPageCallback() {
				@Override
				public void success(HtmlPage page) {
					setPage(page);
				}

				@Override
				public void failure(String error) {
					notifyPageError(error);
				}
			});
		else
			notifyPage();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	protected void onInjectServices() {
	}

	private void setPage(HtmlPage page) {
		this.page = page;
		notifyPage();
	}

	private void notifyPage() {

		if (listener != null)
			listener.onChangePage(page);

		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.page();
			}
		});
	}

	private void notifyPageError(final String error) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageError(error);
			}
		});
	}

	private void addHideOperation() {
		addChild(new HideHelperOperation(getOperationContext()));
	}

	public interface Hook extends Display.Hook {
		void page();
		void pageError(String error);
	}

	public interface Listener {
		void onChangePage(HtmlPage page);
	}

}
