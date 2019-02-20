package cosmos.gwt.presenters;

import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public interface PresenterBuilder {
	boolean canBuild(Presenter presenter, String design);
    Widget build(Presenter presenter, String design, String layout);
}
