package cosmos.presenters;

public interface Holder {

	Presenter.Type getPresenterType();
	boolean canHold(Presenter presenter);

}
