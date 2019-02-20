package org.monet.space.office.presentation.user.renders;

import org.monet.space.office.configuration.Configuration;
import org.monet.space.kernel.model.UserInfo;

public class SuggestionPageRender extends OfficeRender {

	public SuggestionPageRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	@Override
	protected void init() {
		UserInfo info = this.account.getUser().getInfo();

		loadCanvas("suggestion");

		addMark("space", Configuration.getInstance().getUrl());
		addMark("subject", this.getParameterAsString("subject"));
		addMark("fullname", info.getFullname());
		addMark("email", info.getEmail());
		addMark("message", this.getParameterAsString("message"));
	}

}
