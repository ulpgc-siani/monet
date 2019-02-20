package client.presenters.operations;

import client.core.model.BusinessUnit;
import client.core.model.View;
import com.google.gwt.user.client.Window;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowBusinessUnitOperation extends ShowOperation<BusinessUnit, View> {

	public static final Type TYPE = new Type("ShowBusinessUnitOperation", Operation.TYPE);

	public ShowBusinessUnitOperation(Context context, BusinessUnit businessUnit) {
		super(context, businessUnit, null);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. BusinessUnit: " + entity.getLabel());

		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());
		Window.Location.assign(entity.getUrl());
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return view != null ? view.getLabel() : entity.getLabel();
	}

	@Override
	public String getDescription() {
		return entity.getLabel();
	}
}
