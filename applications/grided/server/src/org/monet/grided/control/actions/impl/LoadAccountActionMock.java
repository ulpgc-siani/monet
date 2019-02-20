package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.model.FederationAccount;
import org.monet.grided.core.serializers.json.impl.JSONAccountSerializer;

import com.google.inject.Inject;

public class LoadAccountActionMock extends BaseAction {

    private JSONAccountSerializer serializer;

    @Inject
    public LoadAccountActionMock(JSONAccountSerializer serializer) {
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        FederationAccount account = new FederationAccount("username", "es", "username@mail.com");        
        sendResponse(response, this.serializer.serialize(account));
    }
}

