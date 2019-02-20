package cosmos.presenters;

public interface PresenterBodyMaker {

    void make(Presenter presenter);
    void dispose(Presenter presenter);

}
