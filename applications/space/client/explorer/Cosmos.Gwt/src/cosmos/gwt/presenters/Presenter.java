package cosmos.gwt.presenters;

import cosmos.gwt.model.Theme;
import cosmos.services.TranslatorService;

import java.util.HashMap;
import java.util.Map;

public class Presenter {

	public abstract static class Builder<T extends TranslatorService> implements PresenterBuilder {
		private static Map<String, Builder> childrenBuilderFactory = new HashMap<>();
        protected T translator;
		protected Theme theme;

		public Builder() {
		}

        public void inject(T translator) {
            this.translator = translator;
        }

		public void inject(Theme theme) {
			this.theme = theme;
		}

		protected static void registerBuilder(String key, Builder builder) {
			childrenBuilderFactory.put(key, builder);
		}

		protected <T extends Builder> T getChildBuilder(cosmos.presenters.Presenter presenter, String design, String layout, TranslatorService translator, Theme theme) {
			Builder builder = findBuilder(presenter, design, layout);

			if (builder == null)
				return null;

			builder.inject(translator);
			builder.inject(theme);

			return (T)builder;
		}

		private Builder findBuilder(cosmos.presenters.Presenter presenter, String design, String layout) {
			String presenterType = presenter.getType().toString();
			String key = presenterType + design + layout;

			if (childrenBuilderFactory.containsKey(key))
				return childrenBuilderFactory.get(key);

			key = presenterType + design;
			if (childrenBuilderFactory.containsKey(key))
				return childrenBuilderFactory.get(key);

			return childrenBuilderFactory.containsKey(presenterType)?childrenBuilderFactory.get(presenterType):null;
		}

		public static class Design {
			private final String name;

			private Design(String name) {
				this.name = name;
			}

			@Override
			public String toString() {
				return this.name.toLowerCase();
			}
		}

	}

}
