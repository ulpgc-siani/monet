package client.widgets.popups.dialogs.link;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.Order;
import client.presenters.displays.LinkFieldIndexDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexFilterToolsWidget;
import client.widgets.popups.PopUpWidget;
import client.widgets.toolbox.CheckListWidget;
import client.widgets.toolbox.IndexFilterItemWidget;
import client.widgets.toolbox.IndexFilterWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.dom.client.Style.Unit.PX;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class LinkFieldIndexHeaderDialog extends IndexFilterItemWidget {

    private final Order.Mode[] orders = new Order.Mode[]{null, Order.Mode.ASC, Order.Mode.DESC};
    private final String[] arrows = new String[]{StyleName.ARROW, StyleName.ARROW_UP, StyleName.ARROW_DOWN};
    private final LinkFieldIndexDisplay display;
    private int currentOrder;

    public LinkFieldIndexHeaderDialog(final LinkFieldIndexDisplay display, LinkFieldIndexDisplay.Header header, TranslatorService translator, IndexFilterToolsWidget.LayoutHelper layoutHelper) {
        super(header.getFilter(), translator, new IndexFilterWidget.Delegate() {
            @Override
            public void loadFilterOptions(Filter filter, String condition) {
                display.loadFilterOptions(filter, condition);
            }
        }, layoutHelper);
        this.display = display;
        init();
        create(header);
        currentOrder = 0;
    }

    public void addClickHandler(final ClickHandler handler) {
        addDomHandler(handler, ClickEvent.getType());
    }

    public Order.Mode getOrder() {
        return orders[currentOrder];
    }

    @Override
    protected void createDialog() {
        dialog = new Dialog(filter, optionsPanel);
        setUpDialog();
        dialog.setSizeCalculator(new PopUpWidget.SizeCalculator() {
            @Override
            public int getWidth() {
                return (int) (getParentDialog().getOffsetWidth() / 1.5);
            }

            @Override
            public int getHeight() {
                return getParentDialog().getOffsetHeight() / 2;
            }
        });
    }

    private void create(LinkFieldIndexDisplay.Header header) {
        createHandler();
        checkIsFilterable(header);
        addStyleName(StyleName.HEADER);
    }

    private void createHandler() {
        setHandler(new Handler() {
            @Override
            public void activateFilter() {
                activate();
            }

            @Override
            public void deactivateFilter() {
                deactivate();
            }

            @Override
            public void onSelectOptions(List<Filter.Option> options) {
                filter.setOptions(options);
                display.select(filter);
            }

            @Override
            public void onClearOptions() {
            }
        });
    }

    private void checkIsFilterable(LinkFieldIndexDisplay.Header header) {
        label.addStyleName(StyleName.LABEL);
        label.addStyleName(StyleName.HEADER);
        if (!header.isFilterable())
            removeFilterOptions();
        else
            addLabelWithArrow();
    }

    private void removeFilterOptions() {
        addStyleName("no-filter");
        $(this).find(StyleName.ANCHOR).remove();
        $(this).find(toRule(StyleName.DIALOG)).remove();
        $(selectedOptionsPanel).addClass("no-filter");
    }

    private void addLabelWithArrow() {
        label.add(createArrow());
        label.setTitle(filter.getLabel());
    }

    private Widget createArrow() {
        final InlineHTML arrow = new InlineHTML();
        arrow.setStyleName(arrows[currentOrder]);
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                currentOrder = (currentOrder + 1) % orders.length;
                arrow.setStyleName(arrows[currentOrder]);
            }
        });
        return arrow;
    }

    private Element getParentDialog() {
        return $(getElement()).parents(toRule(LinkFieldIndexHeaderDialog.StyleName.DIALOG)).get(0);
    }

    public interface StyleName extends IndexFilterItemWidget.StyleName {
        String ANCHOR = "a";
        String ARROW = "arrow";
        String ARROW_DOWN = "arrow down";
        String ARROW_UP = "arrow up";
        String HEADER = "header";
        String FILTER_OPTIONS = "filter-options";
    }

    private class Dialog extends IndexFilterWidget.Dialog {

        public Dialog(Filter filter, CheckListWidget options) {
            super(filter, options);
            addStyleName(LinkFieldIndexHeaderDialog.StyleName.FILTER_OPTIONS);
        }

	    @Override
        public void show() {
            super.show();
            Element parent = getParentDialog();
            if (parent.getAbsoluteLeft() + parent.getOffsetWidth() < getElement().getAbsoluteLeft() + getElement().getOffsetWidth()) {
	            Style style = getElement().getStyle();
	            style.setRight(0, PX);
	            style.clearLeft();
            }
        }

        @Override
        public void hide() {
            super.hide();
            $(getElement()).parent(toRule(LinkFieldIndexHeaderDialog.StyleName.ACTIVE)).removeClass(LinkFieldIndexHeaderDialog.StyleName.ACTIVE);
        }
    }
}
