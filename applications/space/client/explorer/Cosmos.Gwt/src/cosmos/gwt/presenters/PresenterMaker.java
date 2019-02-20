package cosmos.gwt.presenters;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.model.Theme;
import cosmos.presenters.Presenter;
import cosmos.presenters.PresenterBodyMaker;
import cosmos.services.TranslatorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class PresenterMaker implements PresenterBodyMaker {
    private TranslatorService translator;
	private Theme theme;
	private static final String MAIN = "main";
    
	private List<PresenterBuilder> builderList = new ArrayList<>();
	private Map<Presenter, List<Widget>> presenterListOfPresenter = new HashMap<>(1000);
	private Map<Widget, List<PresenterHolder>> presenterHolderListOfPresenter = new HashMap<>(2000);

    public PresenterMaker(TranslatorService translator, Theme theme) {
        this.translator = translator;
	    this.theme = theme;
	    registerHolders(RootPanel.get(MAIN));
    }

    public void addBuilder(PresenterBuilder builder) {
        injectDependencies(builder);
        builderList.add(builder);
    }

    private void injectDependencies(PresenterBuilder builder) {

        if (!(builder instanceof cosmos.gwt.presenters.Presenter.Builder))
            return;

        cosmos.gwt.presenters.Presenter.Builder gwtBuilder = (cosmos.gwt.presenters.Presenter.Builder)builder;
        gwtBuilder.inject(translator);
        gwtBuilder.inject(theme);
    }

    @Override
    public void make(Presenter presenter) {
        for (Widget ownerWidget : getWidgetList(presenter.getOwner()))
            buildWidget(presenter, ownerWidget);
    }

	@Override
    public void dispose(Presenter presenter) {
		for (Widget ownerWidget : getWidgetList(presenter.getOwner()))
            disposeWidget(presenter, ownerWidget);
    }

	private Widget[] getWidgetList(Presenter presenter) {
		if (presenter == null) return new Widget[] {RootPanel.get(MAIN)};
		if (!presenterListOfPresenter.containsKey(presenter)) return new Widget[0];
		return presenterListOfPresenter.get(presenter).toArray(new Widget[0]);
	}

	private void buildWidget(Presenter presenter, Widget ownerWidget) {
		for (PresenterHolder presenterHolder : presenterHolderListOfPresenter.get(ownerWidget)) {
			if (presenterHolder.canHold(presenter))
				buildWidget(presenter, presenterHolder);
		}
	}

	private void buildWidget(Presenter presenter, PresenterHolder presenterHolder) {
		String design = presenterHolder.getDesign();
		String layout = presenterHolder.getLayout();

		PresenterBuilder presenterBuilder = getBuilder(presenter, design);
		if (presenterBuilder == null) return;

		Widget child = presenterBuilder.build(presenter, design, layout);
		presenterHolder.appendChild(child);

		if (child instanceof HoldAble)
			((HoldAble)child).onHold(presenterHolder);

		getWidgetListOfPresenter(presenter).add(child);
		registerHolders(child);
	}

	private void disposeWidget(Presenter presenter, Widget ownerWidget) {
		for (PresenterHolder presenterHolder : presenterHolderListOfPresenter.get(ownerWidget)) {
			if (presenterHolder.canHold(presenter))
				disposeWidget(presenter, presenterHolder);
		}
	}

	private void disposeWidget(Presenter presenter, PresenterHolder presenterHolder) {
		presenterHolder.clear();
	}

	private List<Widget> getWidgetListOfPresenter(Presenter presenter) {
		if (!presenterListOfPresenter.containsKey(presenter))
			presenterListOfPresenter.put(presenter, new ArrayList<Widget>());
		return presenterListOfPresenter.get(presenter);
	}

	private void registerHolders(Widget presenter) {
	    GQuery query = $(presenter.getElement()).not(toRule(PresenterHolder.STYLE, PresenterHolder.STYLE)).find(toRule(PresenterHolder.STYLE));
	    presenterHolderListOfPresenter.put(presenter, getWidgetHolderList(query));
    }

    private List<PresenterHolder> getWidgetHolderList(GQuery query) {
        List<PresenterHolder> holderList = new ArrayList<>();

        for (Element element : query.elements())
	        holderList.add(new PresenterHolder(element));

        return holderList;

    }

	private PresenterBuilder getBuilder(Presenter presenter, String design) {
		for (PresenterBuilder presenterBuilder : builderList)
			if (presenterBuilder.canBuild(presenter, design)) return presenterBuilder;
		return null;
	}

}
