package client.services.http.dialogs.source;

import client.core.model.Source;
import client.services.SourceService;

public class SearchSourceTermsDialog extends LoadSourceTermsDialog {

    public SearchSourceTermsDialog(Source source, SourceService.Mode mode, String condition, int start, int limit, String flatten, String depth) {
        super(source, mode, start, limit, flatten, depth);

        add("condition", condition);
    }
}
