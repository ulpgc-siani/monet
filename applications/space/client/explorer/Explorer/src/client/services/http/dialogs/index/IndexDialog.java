package client.services.http.dialogs.index;

import client.services.IndexService;
import client.services.http.dialogs.HttpDialog;

abstract class IndexDialog extends HttpDialog {

    protected void addScope(IndexService.Scope scope) {
        add("indexView", scope.getIndexView());

        if (scope instanceof IndexService.NodeScope)
            addNodeScope((IndexService.NodeScope) scope);
        else if (scope instanceof IndexService.FieldScope)
            addFieldScope((IndexService.FieldScope) scope);
    }

    private void addNodeScope(IndexService.NodeScope scope) {
        add("scope", "node");
        add("set", scope.getSet().getId());
        add("setView", scope.getSetView().getCode());
    }

    private void addFieldScope(IndexService.FieldScope scope) {
        add("scope", "field");
        add("field", scope.getFieldDefinition().getCode());
        add("singleton", scope.getSingleton());
    }

}
