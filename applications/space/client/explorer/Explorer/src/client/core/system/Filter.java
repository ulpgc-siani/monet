package client.core.system;

import client.core.model.List;

import static client.core.model.Instance.ClassName;

public class Filter implements client.core.model.Filter {
	private String name;
	private String label;
	private List<client.core.model.Filter.Option> options;

	public Filter() {
	}

	public Filter(String name, String label) {
		this.name = name;
		this.label = label;
		this.options = new MonetList<>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public List<client.core.model.Filter.Option> getOptions() {
		return this.options;
	}

	public void setOptions(List<client.core.model.Filter.Option> options) {
		this.options = options;
	}

	public static class Option implements client.core.model.Filter.Option {
		private String value;
		private String label;

		public Option() {
		}

		public Option(String value, String label) {
			this.value = value;
			this.label = label;
		}

		@Override
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}

	@Override
	public final ClassName getInstanceClass() {
		return client.core.model.Filter.CLASS_NAME;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof String)
			return getName().equals(object);

		if (!(object instanceof Filter))
			return false;

		return getName().equals(((Filter)object).getName());
	}

}
