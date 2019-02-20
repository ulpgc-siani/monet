package client.presenters.displays;

import client.presenters.operations.Operation;

public abstract class VisitingDisplay<T extends Display> extends Display {
	protected final cosmos.presenters.Operation from;

	public static final Type TYPE = new Type("VisitingDisplay", Display.TYPE);

	public VisitingDisplay(T display, cosmos.presenters.Operation from) {
		this.from = from;
		addDisplay(display);
	}

	public T getDisplay() {
		if (childrenCount() <= 0)
			return null;

		return (T)this.getChild(0);
	}

	public cosmos.presenters.Operation getFrom() {
		return from;
	}

	public abstract Operation toOperation();
	public abstract String getLabel();
    public abstract void back();

	@Override
	public Type getType() {
		return TYPE;
	}

	private void addDisplay(T display) {
		if (display != null)
			this.addChild(display);
	}

	public interface Hook extends Display.Hook {
		void label();
		void labelFailure(String error);
	}

}
