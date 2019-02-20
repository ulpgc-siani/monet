package client.presenters.operations;

import cosmos.presenters.Presenter;

import java.util.ArrayList;
import java.util.List;

public class CompositeOperation extends Operation {

	public static final Type TYPE = new Type("CompositeOperation", Operation.TYPE);

	public CompositeOperation(Context context) {
		super(context);
	}

	public List<Operation> getOperations() {
		List<Operation> operations = new ArrayList<>();

		for (Presenter child : this) {
			if (child instanceof Operation)
				operations.add((Operation)child);
		}

		return operations;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return "composite";
	}

}
