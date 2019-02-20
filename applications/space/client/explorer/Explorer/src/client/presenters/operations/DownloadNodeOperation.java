package client.presenters.operations;

import client.core.model.Node;
import client.services.TranslatorService;
import com.google.gwt.user.client.Window;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadNodeOperation extends Operation {
	private Node node;

	public static final Type TYPE = new Type("DownloadNodeOperation", Operation.TYPE);

	public DownloadNodeOperation(Context context, Node node) {
		super(context);
		this.node = node;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Entity: " + node.getLabel());
		Window.Location.assign(services.getNodeService().getDownloadNodeUrl(node));
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.Label.DOWNLOAD);
	}
}
