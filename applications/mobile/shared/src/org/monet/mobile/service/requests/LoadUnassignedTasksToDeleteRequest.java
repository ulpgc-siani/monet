package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

import java.util.List;

public class LoadUnassignedTasksToDeleteRequest extends Request {

    public List<String> Ids;

    public LoadUnassignedTasksToDeleteRequest() {
        super(ActionCode.LoadUnassignedTasksToDelete);
    }

    public LoadUnassignedTasksToDeleteRequest(List<String> ids) {
        super(ActionCode.LoadUnassignedTasksToDelete);
        this.Ids = ids;
    }

}
