package cosmos.presenters;

public class Menu extends Presenter {

	public static final Type TYPE = new Type("Menu",Presenter.TYPE);

	@Override
	public Type getType() {
		return TYPE;
	}
}
