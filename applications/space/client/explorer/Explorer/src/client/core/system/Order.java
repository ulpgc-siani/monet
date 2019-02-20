package client.core.system;

public class Order implements client.core.model.Order {
	private String name;
	private String label;
	private Mode mode;

	public Order() {
	}

	public Order(String name, String label) {
		this(name, label, Mode.ASC);
	}

	public Order(String name, String label, Mode mode) {
		this.name = name;
		this.label = label;
		this.mode = mode;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
}
