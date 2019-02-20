package client.widgets.view.workmap;

import client.presenters.displays.entity.workmap.TaskStateActionDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class TaskStateActionWidget {

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			register();
			return presenter.is(TaskStateActionDisplay.TYPE);
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {

			Builder builder = getChildBuilder(presenter, design, layout, translator, theme);
			if (builder == null)
				return null;

			Widget widget = builder.build(presenter, design, layout);
			if (!layout.isEmpty())
				widget.getElement().addClassName(layout);

			return widget;
		}

		protected static void register() {
			TaskStateDelegationActionWidget.Builder.register();
//		    TaskStateSendJobActionWidget.Builder.register();
		    TaskStateLineActionWidget.Builder.register();
		    TaskStateEditionActionWidget.Builder.register();
//		    TaskStateEnrollActionWidget.Builder.register();
		    TaskStateWaitActionWidget.Builder.register();
		}

	}
}
