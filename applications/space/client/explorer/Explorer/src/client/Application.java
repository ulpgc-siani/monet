package client;

import client.services.Services;
import cosmos.presenters.RootDisplay;

public interface Application {
    void init(Services services);
	void start();
    RootDisplay getDisplay();
}
