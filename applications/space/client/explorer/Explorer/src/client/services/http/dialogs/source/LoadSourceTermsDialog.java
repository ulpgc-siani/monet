package client.services.http.dialogs.source;

import client.core.model.Source;
import client.services.SourceService;
import client.services.http.dialogs.HttpDialog;

public class LoadSourceTermsDialog extends HttpDialog {
    private Source source;

    public LoadSourceTermsDialog(Source source, SourceService.Mode mode, int start, int limit, String flatten, String depth) {
        this.source = source;

        add("mode", mode.toString());
        add("start", start);
        add("limit", limit);
        add("flatten", flatten);
        add("depth", depth);
    }

    @Override
    public String getOperation() {
        return "source$terms";
    }

    @Override
    public String getEntityId() {
        return source.getId();
    }
}
