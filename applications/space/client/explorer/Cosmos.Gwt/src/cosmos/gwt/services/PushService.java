package cosmos.gwt.services;

public interface PushService {
    void init();
    void onMessageReceived(String message);
}
