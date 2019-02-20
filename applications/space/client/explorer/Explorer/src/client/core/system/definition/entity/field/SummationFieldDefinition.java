package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class SummationFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.SummationFieldDefinition {
	private List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> terms;
	private String source;
	private client.core.model.definition.entity.field.SummationFieldDefinition.SelectDefinition select;
	private String format;
	private client.core.model.definition.entity.field.SummationFieldDefinition.RangeDefinition range;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.SummationFieldDefinition.CLASS_NAME;
	}

	@Override
	public List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> getTerms() {
		return terms;
	}

	public void setTerms(List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> terms) {
		this.terms = terms;
	}

	@Override
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public client.core.model.definition.entity.field.SummationFieldDefinition.SelectDefinition getSelect() {
		return select;
	}

	public void setSelect(client.core.model.definition.entity.field.SummationFieldDefinition.SelectDefinition select) {
		this.select = select;
	}

	@Override
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public client.core.model.definition.entity.field.SummationFieldDefinition.RangeDefinition getRange() {
		return range;
	}

	public void setRange(client.core.model.definition.entity.field.SummationFieldDefinition.RangeDefinition range) {
		this.range = range;
	}

	public static class SummationItemDefinition implements client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition {
		private String key;
		private String label;
		private Type type;
		private boolean multiple;
		private boolean negative;
		private List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> terms;

		@Override
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		@Override
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		@Override
		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		@Override
		public boolean isMultiple() {
			return multiple;
		}

		public void setMultiple(boolean multiple) {
			this.multiple = multiple;
		}

		@Override
		public boolean isNegative() {
			return negative;
		}

		public void setNegative(boolean negative) {
			this.negative = negative;
		}

		@Override
		public List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> getTerms() {
			return terms;
		}

		public void setTerms(List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> terms) {
			this.terms = terms;
		}
	}

	public static class SelectDefinition implements client.core.model.definition.entity.field.SummationFieldDefinition.SelectDefinition {
		private Flatten flatten;
		private int depth;

		@Override
		public Flatten getFlatten() {
			return flatten;
		}

		public void setFlatten(Flatten flatten) {
			this.flatten = flatten;
		}

		@Override
		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

	}

	public static class RangeDefinition implements client.core.model.definition.entity.field.SummationFieldDefinition.RangeDefinition {
		private long min;
		private long max;

		@Override
		public long getMin() {
			return min;
		}

		public void setMin(long min) {
			this.min = min;
		}

		@Override
		public long getMax() {
			return max;
		}

		public void setMax(long max) {
			this.max = max;
		}
	}
}