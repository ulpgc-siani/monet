package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class MemoFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.MemoFieldDefinition {
	private client.core.model.definition.entity.field.MemoFieldDefinition.AllowHistoryDefinition allowHistory;
	private client.core.model.definition.entity.field.MemoFieldDefinition.LengthDefinition length;
	private client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition edition;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.MemoFieldDefinition.CLASS_NAME;
	}

	@Override
	public boolean allowHistory() {
		return allowHistory != null;
	}

	@Override
	public client.core.model.definition.entity.field.MemoFieldDefinition.AllowHistoryDefinition getAllowHistory() {
		return allowHistory;
	}

	public void setAllowHistory(client.core.model.definition.entity.field.MemoFieldDefinition.AllowHistoryDefinition allowHistory) {
		this.allowHistory = allowHistory;
	}

	@Override
	public client.core.model.definition.entity.field.MemoFieldDefinition.LengthDefinition getLength() {
		return length;
	}

	public void setLength(client.core.model.definition.entity.field.MemoFieldDefinition.LengthDefinition length) {
		this.length = length;
	}

	@Override
	public client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition getEdition() {
		return edition;
	}

	public void setEdition(client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition edition) {
		this.edition = edition;
	}

	public static class LengthDefinition implements client.core.model.definition.entity.field.MemoFieldDefinition.LengthDefinition {
		private int max;

		@Override
		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}
	}

	public static class EditionDefinition implements client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition {
		private Mode mode;

		@Override
		public Mode getMode() {
			return mode;
		}

		public void setMode(Mode mode) {
			this.mode = mode;
		}
	}

	public static class AllowHistoryDefinition implements client.core.model.definition.entity.field.MemoFieldDefinition.AllowHistoryDefinition {
		private String dataStore;

		@Override
		public String getDataStore() {
			return dataStore;
		}

		public void setDataStore(String dataStore) {
			this.dataStore = dataStore;
		}
	}

}
