package client.presenters.displays.entity.field;

import client.core.model.Source;
import client.core.model.types.TermList;
import client.services.SourceService;
import client.services.SourceService.Mode;
import client.services.callback.TermListCallback;

public class SourceTermsLoader {

    private static final int PAGE_SIZE = 10;
    private final Source source;
    private int currentPage;
    private SourceService sourceService;
    private String condition;
    private TermListCallback callback;
    private int totalTerms = -1;
    private String flatten;
    private String depth;

    public SourceTermsLoader(Source source) {
        this.source = source;
    }

    public void injectSourceService(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    public void loadPage(int pageNumber, String condition, TermPageCallback callback, String flatten, String depth) {
        this.flatten = flatten;
        this.depth = depth;
        if (!check(pageNumber)) return;
        callback.loading();
        this.callback = callback;
        this.condition = condition;
        currentPage = pageNumber;
        loadTerms();
    }

    public void nextPage(TermPageCallback callback) {
        loadPage(currentPage + 1, condition, callback, flatten, depth);
    }

    public void reloadPage(TermPageCallback callback) {
        loadPage(currentPage, condition, callback, flatten, depth);
    }

    public void reset() {
        currentPage = 0;
    }

    private boolean check(int page) {
        return page >= 0 && page < getPagesCount();
    }

    private int getPagesCount() {
        if (totalTerms <= 0)
            return 1;
        return Math.round(totalTerms / PAGE_SIZE) + ((totalTerms % PAGE_SIZE) > 0 ? 1 : 0);
    }

    private void loadTerms() {
        loadTerms(getStart(), getLimit());
    }

    private int getStart() {
        return currentPage * PAGE_SIZE;
    }

    private int getLimit() {
        return PAGE_SIZE;
    }

    private void loadTerms(int start, int limit) {
        if (condition == null || condition.isEmpty())
            sourceService.getTerms(source, Mode.FLATTEN, start, limit, flatten, depth, createCallback());
        else
            sourceService.searchTerms(source, Mode.FLATTEN, condition, start, limit, flatten, depth, createCallback());
    }

    private TermListCallback createCallback() {
        return new TermListCallback() {

            @Override
            public void success(TermList terms) {
                totalTerms = terms.getTotalCount();
                callback.success(terms);
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        };
    }

    public interface TermPageCallback extends TermListCallback {
        void loading();
    }
}
