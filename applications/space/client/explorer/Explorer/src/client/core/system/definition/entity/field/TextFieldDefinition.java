package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class TextFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.TextFieldDefinition {
	private client.core.model.definition.entity.field.TextFieldDefinition.AllowHistoryDefinition allowHistory;
	private client.core.model.definition.entity.field.TextFieldDefinition.LengthDefinition length;
	private client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition edition;
	private List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition> patterns;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.TextFieldDefinition.CLASS_NAME;
	}

	@Override
	public boolean allowHistory() {
		return allowHistory != null;
	}

	@Override
	public client.core.model.definition.entity.field.TextFieldDefinition.AllowHistoryDefinition getAllowHistory() {
		return allowHistory;
	}

	public void setAllowHistory(client.core.model.definition.entity.field.TextFieldDefinition.AllowHistoryDefinition allowHistory) {
		this.allowHistory = allowHistory;
	}

	@Override
	public client.core.model.definition.entity.field.TextFieldDefinition.LengthDefinition getLength() {
		return length;
	}

	public void setLength(client.core.model.definition.entity.field.TextFieldDefinition.LengthDefinition length) {
		this.length = length;
	}

	@Override
	public client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition getEdition() {
		return edition;
	}

	public void setEdition(client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition edition) {
		this.edition = edition;
	}

	@Override
	public List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition> getPatterns() {
		return this.patterns;
	}

	public void setPatterns(List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition> patterns) {
		this.patterns = patterns;
	}

	public static class LengthDefinition implements client.core.model.definition.entity.field.TextFieldDefinition.LengthDefinition {
		private int min;
		private int max;

		@Override
		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		@Override
		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}
	}

	public static class EditionDefinition implements client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition {
		private Mode mode;

		@Override
		public Mode getMode() {
			return mode;
		}

		public void setMode(Mode mode) {
			this.mode = mode;
		}
	}

	public static class PatternDefinition implements client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition {
		private String regExp;
		private List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition> metaList;

		@Override
		public String getRegExp() {
			return regExp;
		}

		public void setRegExp(String regExp) {
			this.regExp = regExp;
		}

		@Override
		public List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition> getMetaList() {
			return metaList;
		}

		public void setMetaList(List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition> metaList) {
			this.metaList = metaList;
		}

		public static class MetaDefinition implements client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition {
			private long position;
			private String indicator;

			@Override
			public long getPosition() {
				return position;
			}

			public void setPosition(long position) {
				this.position = position;
			}

			@Override
			public String getIndicator() {
				return indicator;
			}

			public void setIndicator(String indicator) {
				this.indicator = indicator;
			}
		}
	}

	public static class AllowHistoryDefinition implements client.core.model.definition.entity.field.TextFieldDefinition.AllowHistoryDefinition {
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
