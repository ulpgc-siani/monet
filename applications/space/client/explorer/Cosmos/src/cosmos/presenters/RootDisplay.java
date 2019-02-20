package cosmos.presenters;


public abstract class RootDisplay extends Display {

	public static final Type TYPE = new Type("RootDisplay", Display.TYPE);

    private PresenterBodyMaker[] makers;

    public RootDisplay(PresenterBodyMaker[] makers) {
        this.makers = makers;
    }

    @Override
    public void presenterAdded(Presenter presenter) {
	    super.presenterAdded(presenter);
        for (PresenterBodyMaker maker : makers)
            maker.make(presenter);
    }
    
    @Override
    public void presenterRemoved(Presenter presenter) {
	    super.presenterRemoved(presenter);
        for (PresenterBodyMaker maker : makers)
            maker.dispose(presenter);
    }

}
