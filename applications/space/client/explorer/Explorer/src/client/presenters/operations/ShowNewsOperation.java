package client.presenters.operations;

import client.core.model.News;
import client.core.model.View;
import client.presenters.displays.ApplicationDisplay;
import client.presenters.displays.ExplorationDisplay;
import client.services.TranslatorService;
import client.services.callback.NewsCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowNewsOperation extends ShowOperation<News, View> {

	public static final Type TYPE = new Type("ShowNewsOperation", Operation.TYPE);

	public ShowNewsOperation(Context context) {
		super(context, null, null);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called.");
		NewsCallback callback = new NewsCallback() {
			@Override
			public void success(News object) {
				entity = object;
				refreshExplorationDisplay();
			}

			@Override
			public void failure(String error) {
			}
		};

		if (entity == null) {
			services.getNewsService().open(callback);
			return;
		}

		callback.success(entity);
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getMenuLabel(boolean fullLabel) {
		TranslatorService service = services.getTranslatorService();
		return service.translate(TranslatorService.Label.NEWS);
	}

	@Override
	public String getDefaultLabel() {
		TranslatorService service = services.getTranslatorService();
		return service.translate(TranslatorService.OperationLabel.SHOW_NEWS);
	}

	@Override
	public String getDescription() {
		return null;
	}

	private void refreshExplorationDisplay() {
		ApplicationDisplay display = this.context.getCanvas();
		ExplorationDisplay explorationDisplay = display.getExplorationDisplay();

		explorationDisplay.clearChildren();
		explorationDisplay.setRootOperation(explorationDisplay.get(this));
		explorationDisplay.activate(this);
	}

}
