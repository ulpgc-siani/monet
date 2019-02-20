package client.presenters.displays.entity;

import client.presenters.displays.EntityDisplay;
import cosmos.presenters.Presenter;

public interface EnvironmentDisplay {

	cosmos.presenters.Presenter.Type TYPE = new Presenter.Type("EnvironmentDisplay", EntityDisplay.TYPE);

	boolean isRoot();

}
