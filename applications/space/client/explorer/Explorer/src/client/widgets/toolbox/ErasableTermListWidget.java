package client.widgets.toolbox;

import client.core.model.types.Term;
import client.services.TranslatorService;

public class ErasableTermListWidget extends ErasableListWidget<Term> {

	public ErasableTermListWidget(TranslatorService translator, boolean showCode) {
		super(new TermListItem.Builder(translator, showCode), translator);
	}

	public static class TermListItem extends ListItem<Term> {

		private final boolean showCode;

		public TermListItem(Term value, boolean showCode, TranslatorService translator) {
			super(value, translator, false);
			this.showCode = showCode;
			init();
		}

		@Override
		protected String getLabel() {
			if (showCode) {
				return value.getValue() + " - " + value.getLabel();
			}
			return value.getLabel();
		}

        public static class Builder extends ListItem.Builder<Term> {

			private final boolean showCode;

			public Builder(TranslatorService translator, boolean showCode) {
				super(translator);
				this.showCode = showCode;
			}

			@Override
			public HTMLListWidget.ListItem build(Term value, Mode mode) {
				return new TermListItem(value, showCode, translator);
			}

        }

    }

}