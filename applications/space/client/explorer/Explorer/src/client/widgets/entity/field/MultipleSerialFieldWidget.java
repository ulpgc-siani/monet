package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleSerialFieldDisplay;
import client.presenters.displays.entity.field.MultipleSerialFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.toolbox.SortableHTMLListController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class MultipleSerialFieldWidget extends FieldWidget<MultipleSerialFieldDisplay.Hook> {

    private HTMLListWidget<String> listWidget;
    private AbsolutePanel values;

    public MultipleSerialFieldWidget(IsMultipleSerialFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
        addStyleName(StyleName.SERIAL);
    }

    @Override
    protected void createComponent() {
        createList();
    }

    @Override
    protected void bind() {
        bind(values, toRule(StyleName.VALUES));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        listWidget.clear();
        refreshValuesVisibility();
        for (String value : multipleSerialFieldDisplay().getAllValues())
            listWidget.addItem(value);
    }

    @Override
    protected MultipleSerialFieldDisplay.Hook createHook() {
        return new MultipleFieldDisplay.Hook() {
            @Override
            public void values() {
            }

            @Override
            public void value() {
            }

            @Override
            public void error(String error) {

            }
        };
    }

    private void createList() {
        listWidget = new HTMLListWidget<>(new HTMLListWidget.ReadOnlyListItem.Builder(translator), translator);
        listWidget.addStyleName(FileFieldWidget.StyleName.VALUES);
        values = boundaryPanel();
    }

    private AbsolutePanel boundaryPanel() {
        AbsolutePanel boundaryPanel = new AbsolutePanel();
        final SortableHTMLListController sortableHTMLListController = new SortableHTMLListController<>(boundaryPanel, listWidget);
        sortableHTMLListController.addChangePositionEvent(new SortableHTMLListController.ChangePositionEvent<HTMLListWidget.ReadOnlyListItem<String>>() {
            @Override
            public void onPositionChange(HTMLListWidget.ReadOnlyListItem<String> item, int newPosition) {
                multipleSerialFieldDisplay().changeOrder(item.getValue(), newPosition);
            }
        });
        boundaryPanel.add(listWidget);
        return boundaryPanel;
    }

    private void refreshValuesVisibility() {
        values.setVisible(!multipleSerialFieldDisplay().isEmpty());
        if (multipleSerialFieldDisplay().isEmpty())
            $(getElement()).find(toRule(StyleName.HORIZONTAL_SEPARATOR)).first().hide();
        else
            $(getElement()).find(toRule(StyleName.HORIZONTAL_SEPARATOR)).first().show();
    }

    private IsMultipleSerialFieldDisplay multipleSerialFieldDisplay() {
        return (IsMultipleSerialFieldDisplay) display;
    }

    public interface StyleName extends SerialFieldWidget.StyleName { }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleSerialFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleSerialFieldWidget((IsMultipleSerialFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-serial-multiple";
	    }

    }
}
