package client.core.model;

public interface TaskView extends View {

	enum Type {
		HISTORY, STATE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}
	}

	Key STATE_VIEW = new Key() {
		@Override
		public String getCode() {
			return "state";
		}

		@Override
		public String getName() {
			return "state";
		}
	};

	Task getScope();

}
