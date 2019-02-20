package client.core.model;

public interface Instance {

	ClassName getClassName();

	class ClassName {
		private String name;

		public ClassName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name.toString().toLowerCase();
		}
	}

}
