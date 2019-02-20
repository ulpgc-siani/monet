package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.index.IndexToolbarWidget;
import client.widgets.index.IndexWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public abstract class EntityViewWidget extends HTMLPanel {

    public EntityViewWidget(String html) {
        super(html);
    }

    public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {
	    public static EntityViewWidget.Builder viewBuilder;
	    public static FieldWidget.Builder fieldBuilder;
	    public static IndexWidget.Builder indexBuilder;
	    public static IndexToolbarWidget.Builder indexToolbarBuilder;

	    private static final String LAYOUT_DEFINED_BY_VIEW_DEFINITION = "_defined-by-view-definition_";

	    @Override
		public boolean canBuild(Presenter presenter, String design) {
		    register();
		    return presenter.is(ViewDisplay.TYPE);
		}

	    @Override
	    public Widget build(Presenter presenter, String design, String layout) {
		    createBuilders();

		    if (layout.equals(LAYOUT_DEFINED_BY_VIEW_DEFINITION)) {
			    ViewDefinition.Design viewDesign = ((ViewDisplay) presenter).getDesign();
			    layout = viewDesign != null ? viewDesign.toString() : ViewDefinition.Design.LIST.toString();
		    }

		    Builder builder = getChildBuilder(presenter, design, layout, translator, theme);
		    if (builder == null)
			    return null;

		    Widget widget = builder.build(presenter, design, layout);
		    if (layout != null && !layout.isEmpty())
			    widget.addStyleName(layout);

		    return widget;
	    }

	    public static FieldWidget.Builder getFieldBuilder() {
		    return fieldBuilder;
	    }

	    public static EntityViewWidget.Builder getViewBuilder() {
		    return viewBuilder;
	    }

	    public static IndexWidget.Builder getIndexBuilder() {
		    return indexBuilder;
	    }

	    public static IndexToolbarWidget.Builder getIndexToolbarBuilder() {
		    return indexToolbarBuilder;
	    }

	    protected String getLayout(String type, String layout) {
		    String layoutName = type + ((layout != null && !layout.isEmpty()) ? " " + layout : "");

		    if (!theme.existsLayout(layoutName))
			    layoutName = type;

		    return theme.getLayout(layoutName);
	    }

	    protected static void register() {
		    DesktopViewWidget.Builder.register();
			ContainerViewWidget.Builder.register();
		    FormViewWidget.Builder.register();
			DocumentViewWidget.Builder.register();
		    SetViewWidget.Builder.register();
		    SetViewDocumentWidget.Builder.register();
		    TaskListViewWidget.Builder.register();
		    TaskStateViewWidget.Builder.register();
		    TaskShortcutViewWidget.Builder.register();
	    }

	    private void createBuilders() {
		    if (fieldBuilder != null && viewBuilder != null && indexBuilder != null && indexToolbarBuilder != null) return;

		    fieldBuilder = setUpBuilder(new FieldWidget.Builder());
		    viewBuilder = setUpBuilder(new EntityViewWidget.Builder());
		    indexBuilder = setUpBuilder(new IndexWidget.Builder());
		    indexToolbarBuilder = setUpBuilder(new IndexToolbarWidget.Builder());
	    }

	    protected <T extends cosmos.gwt.presenters.Presenter.Builder> T setUpBuilder(T builder) {
		    builder.inject(translator);
		    builder.inject(theme);
		    return builder;
	    }
	}

}
