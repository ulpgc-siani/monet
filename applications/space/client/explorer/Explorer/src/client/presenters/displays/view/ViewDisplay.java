package client.presenters.displays.view;

import client.core.model.Entity;
import client.core.model.Key;
import client.core.model.View;
import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.EntityDisplay;

public abstract class ViewDisplay<EntityType extends Entity, ViewType extends View> extends EntityDisplay<EntityType, ViewType> {

	public static final Type TYPE = new Type("ViewDisplay", EntityDisplay.TYPE);

	public ViewDisplay(EntityType entity, ViewType view) {
        super(entity, view);
	}

	public Key getKey() {
		return getView().getKey();
	}

	public String getLabel() {
		return getView().getLabel();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public ViewDefinition.Design getDesign() {
		ViewDefinition definition = (ViewDefinition)getView().getDefinition();
		return definition != null ? definition.getDesign() : null;
	}

	public static class Builder<T extends Entity, V extends View> extends client.presenters.displays.Display.Builder {

		protected static void register() {
			CatalogViewDisplay.Builder.register();
			CollectionViewDisplay.Builder.register();
			ContainerViewDisplay.Builder.register();
			DesktopViewDisplay.Builder.register();
			FormViewDisplay.Builder.register();
			DocumentViewDisplay.Builder.register();
			TaskListViewDisplay.Builder.register();
			TaskStateViewDisplay.Builder.register();
			TaskShortcutViewDisplay.Builder.register();
		}

        public ViewDisplay build(T entity, V view) {
	        register();

			Builder builder = (Builder) getBuilder(entity.getClassName(), view.getClassName());
	        if (builder == null)
		        return null;

	        return builder.build(entity, view);
        }

	}

}
