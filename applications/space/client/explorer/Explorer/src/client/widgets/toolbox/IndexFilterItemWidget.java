package client.widgets.toolbox;

import client.core.model.Filter;
import client.services.TranslatorService;
import client.widgets.index.IndexFilterToolsWidget.LayoutHelper;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import cosmos.utils.StringUtils;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class IndexFilterItemWidget extends IndexFilterWidget {
    protected LayoutHelper helper;
    protected HTMLPanel label;

    public IndexFilterItemWidget(Filter filter, TranslatorService translator, IndexFilterWidget.Delegate delegate, LayoutHelper helper) {
        super(filter, translator, delegate);
        this.helper = helper;
        init();
    }

    @Override
    protected void init() {
        getElement().setInnerHTML(helper.getFilterLayout());
        addStyleName(StyleName.FILTER);

        createAnchor();
        createLabel();
        addSelectedOptions();
        createOptions();
        createDialog();
    }

    protected void createLabel() {
        label = new HTMLPanel(StringUtils.shortContent(filter.getLabel(), 26));
        label.setTitle(filter.getLabel());
        bindWidgetToElement(label, $(this).find(toRule(StyleName.LABEL)).get(0));
    }

    private void addSelectedOptions() {
        selectedOptionsPanel = new FlowPanel();
        selectedOptionsPanel.addStyleName(StyleName.SELECTED_OPTIONS);
        bindWidgetToElement(selectedOptionsPanel, $(getElement()).find(toRule(StyleName.SELECTED_OPTIONS)).get(0));
        selectedOptionsPanel.add(new Label(translator.translate(TranslatorService.ListLabel.ALL)));
    }

    public static class Builder extends IndexFilterWidget.Builder {

        public static final String DESIGN = "filter-tools";
        private final LayoutHelper helper;

        public Builder(LayoutHelper helper) {
            this.helper = helper;
        }

        @Override
        public IndexFilterItemWidget build(Filter filter, TranslatorService translator, Delegate delegate) {
            return new IndexFilterItemWidget(filter, translator, delegate, helper);
        }
    }

    public interface StyleName extends IndexFilterWidget.StyleName {
        String FILTER = "filter";
        String LABEL = "label";
        String SELECTED_OPTIONS = "selected-options";
    }
}
