package client.presenters.displays;

import client.presenters.operations.Operation;
import cosmos.presenters.Display;
import cosmos.presenters.Presenter;

import java.util.ArrayList;

public class OperationListDisplay extends Display {
	public static final Type TYPE = new Type("OperationListDisplay", Display.TYPE);

	public OperationListDisplay() {
        super();
	}

	public Operation[] getOperations() {
		ArrayList<Operation> result = new ArrayList<>();

		for (Presenter child : this) {
			if (child instanceof Operation)
				result.add((Operation)child);
		}

		return result.toArray(new Operation[result.size()]);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

}
