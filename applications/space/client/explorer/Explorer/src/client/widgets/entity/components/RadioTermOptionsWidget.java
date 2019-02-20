package client.widgets.entity.components;

import client.core.model.types.*;
import client.services.TranslatorService;
import client.widgets.toolbox.RadioTermListWidget;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.HashMap;
import java.util.Map;

public class RadioTermOptionsWidget extends VerticalPanel {

    private final TranslatorService translator;
    private final Map<Term, RadioTermListWidget.TermListItem> termToListItem;
    private RadioTermListWidget.SelectHandler selectHandler;
    private RadioTermListWidget currentPanel;
    private RadioTermListWidget firstPanel;

    public RadioTermOptionsWidget(TranslatorService translator) {
        this.translator = translator;
        termToListItem = new HashMap<>();
    }

    public void setSelectHandler(RadioTermListWidget.SelectHandler handler) {
        this.selectHandler = handler;
    }

    public void setTerms(TermList list) {
        clear();
        termToListItem.clear();
        addTerms(list);
    }

    public void select(Term term) {
        if (termToListItem.containsKey(term))
            termToListItem.get(term).check();
    }

    public void selectFirst() {
        if (firstPanel != null) firstPanel.selectFirst();
    }

    private void addTerms(TermList list) {
        for (Term term : list) {
            if (term.isLeaf())
                addTerm(term);
            else if (((CompositeTerm)term).isSelectable())
                addSuperTerm((SuperTerm) term);
            else
                addCategory((TermCategory) term);
        }
    }

    private void addTerm(Term term) {
        if (currentPanel == null) addNewPanel();
        termToListItem.put(term, (RadioTermListWidget.TermListItem) currentPanel.addItem(term));
    }

    private void addSuperTerm(SuperTerm term) {
        if (currentPanel == null) addNewPanel();
        termToListItem.put(term, (RadioTermListWidget.TermListItem) currentPanel.addItem(term));
        for (Term child : term.getTerms())
            termToListItem.put(child, (RadioTermListWidget.TermListItem) currentPanel.addItemWithLevel(child));
    }

    private void addCategory(TermCategory category) {
        final DisclosurePanel categoryPanel = new DisclosurePanel(category.getLabel());
        createPanel();
        categoryPanel.setContent(currentPanel);
        addTerms(category.getTerms());
        add(categoryPanel);
        currentPanel = null;
    }

    private void addNewPanel() {
        createPanel();
        add(currentPanel);
    }

    private void createPanel() {
        currentPanel = new RadioTermListWidget(translator);
        currentPanel.addStyleName(StyleName.TERM_LIST);
        currentPanel.addSelectHandler(selectHandler);
        if (firstPanel == null) firstPanel = currentPanel;
    }

    public interface StyleName {
        String TERM_LIST = "terms";
    }
}
