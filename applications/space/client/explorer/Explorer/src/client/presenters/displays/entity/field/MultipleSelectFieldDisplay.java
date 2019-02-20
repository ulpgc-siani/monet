package client.presenters.displays.entity.field;

import client.core.constructors.SelectFieldTermConstructor;
import client.core.model.Source;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.Node;
import client.core.model.fields.MultipleSelectField;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.services.callback.TermListCallback;
import client.services.callback.SourceCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultipleSelectFieldDisplay extends MultipleFieldDisplay<MultipleSelectField, SelectFieldDefinition, Term> implements IsMultipleSelectFieldDisplay {

	public static final Type TYPE = new Type("MultipleSelectFieldDisplay", MultipleFieldDisplay.TYPE);
    private static final int MIN_SEARCH_LENGTH = 2;
    private SelectFieldDisplay.HistoryOptions historyOptions;
    private SourceTermsLoader termsLoader;
    private boolean loading = false;
    private boolean executingSearch = false;
    private String pendingFilter;
    private int lastFilterLength = 0;

    public MultipleSelectFieldDisplay(Node node, MultipleSelectField field) {
		super(node, field);
	}

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        historyOptions = new MultipleHistoryOptions();
    }

    @Override
	public Type getType() {
		return TYPE;
	}

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public String getValueFormatted() {
        return null;
    }

    @Override
    public void nextPage() {
        if (termsLoader == null || loading) return;
        termsLoader.nextPage(new SourceTermsLoader.TermPageCallback() {
            @Override
            public void success(final TermList options) {
                updateHooks(new Notification<Hook>() {
                    @Override
                    public void update(Hook hook) {
                        hook.page(options);
                    }
                });
                loading = false;
            }

            @Override
            public void failure(String error) {
                notifyOptionsFailure(error);
                loading = false;
            }

            @Override
            public void loading() {
                notifyLoading();
            }
        });
    }

    @Override
    public void reloadPage() {
        if (termsLoader != null)
            termsLoader.reloadPage(new SourceTermsLoader.TermPageCallback() {
                @Override
                public void success(final TermList options) {
                    updateHooks(new Notification<Hook>() {
                        @Override
                        public void update(Hook hook) {
                            hook.page(options);
                        }
                    });
                }

                @Override
                public void failure(String error) {
                    notifyOptionsFailure(error);
                }

                @Override
                public void loading() {
                    notifyLoading();
                }
            });
    }

    @Override
    protected boolean shouldUpdateValue(Term oldValue, Term term) {
        return isEmbedded() || super.shouldUpdateValue(oldValue, term);
    }

    @Override
    public Term createTerm(String value, String label) {
        return getTypeFactory().createTerm(value, label);
    }

    @Override
    public void loadOptions() {
        if (termsLoader != null) termsLoader.reset();
        if (getDefinition().getSource() != null) {
            if (getEntity().getSource() == null)
                services.getSourceService().locate(getDefinition().getSource(), null, createSourceCallback(null));
            else
                createSourceCallback(null).success(getEntity().getSource());
        }
        else
            notifyOptions(SelectFieldTermConstructor.constructList(getDefinition().getTerms()));
    }

    @Override
    public void loadOptions(final String filter) {
        if (filter.isEmpty() || filter.trim().isEmpty() || filter.length() < MIN_SEARCH_LENGTH) {
            if (lastFilterLength >= MIN_SEARCH_LENGTH) {
                loadOptions();
            }
            lastFilterLength = 0;
            return;
        }
        if (executingSearch) {
            pendingFilter = filter;
            return;
        }
        if (termsLoader != null) termsLoader.reset();
        lastFilterLength = filter.length();
        if (getDefinition().getSource() != null) {
            executingSearch = true;
            if (getEntity().getSource() == null)
                services.getSourceService().locate(getDefinition().getSource(), null, createSourceCallback(filter));
            else
                createSourceCallback(filter).success(getEntity().getSource());
        }
        else
            notifyOptions(SelectFieldTermConstructor.constructList(getDefinition().getTerms()).filter(filter));
    }

    @Override
    public boolean isTermSelected(Term option) {
        for (Term term : getAllValues())
            if (term.equals(option)) return true;
        return false;
    }

    @Override
    public boolean isEmbedded() {
        return getDefinition().getSelect().isEmbedded();
    }

    public boolean showCode() {
        return getDefinition().allowKey();
	}

    @Override
    public boolean allowHistory() {
        return getDefinition().allowHistory();
    }

    @Override
    public void loadHistory() {
        services.getNodeService().getFieldHistory(getEntity(), getAllowHistory().getDataStore(), 0, SelectFieldDisplay.MAX_HISTORY_ITEMS, createHistoryCallback(null));
    }

    @Override
    public void loadHistory(String filter) {
        services.getNodeService().searchFieldHistory(getEntity(), getAllowHistory().getDataStore(), filter, 0, SelectFieldDisplay.MAX_HISTORY_ITEMS, createHistoryCallback(filter));
    }

    @Override
    public boolean allowOther() {
        return getDefinition().allowOther();
    }

    @Override
    public boolean allowSearch() {
        return getDefinition().allowSearch();
    }

    @Override
    public String getValueForTermOther() {
        return getDefinition().getValueForTermOther();
    }

    @Override
    public void toggle(Term term) {
        if (isTermSelected(term))
            delete(getValueIndex(term));
        else
            add(term);
    }

    @Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

    private SelectFieldDefinition.AllowHistoryDefinition getAllowHistory() {
        return getDefinition().getAllowHistory();
    }

    private SourceCallback createSourceCallback(final String filter) {
        return new SourceCallback() {
            @Override
            public void success(Source object) {
                if (termsLoader == null) {
                    termsLoader = new SourceTermsLoader(object);
                    termsLoader.injectSourceService(services.getSourceService());
                }
                getEntity().setSource(object);
                termsLoader.loadPage(0, filter, createOptionsCallback(filter), getFlatten(), getDepth());
            }

            @Override
            public void failure(String error) {
                notifySourceFailure();
            }
        };
    }

    private String getFlatten() {
        if (getDefinition().getSelect() == null)
            return SelectFieldDefinition.SelectDefinition.Flatten.ALL.toString();
        return getDefinition().getSelect().getFlatten().toString();
    }

    private String getDepth() {
        if (getDefinition().getSelect() == null)
            return "-1";
        return String.valueOf(getDefinition().getSelect().getDepth());
    }

    private SourceTermsLoader.TermPageCallback createOptionsCallback(final String filter) {
        return new SourceTermsLoader.TermPageCallback() {
            @Override
            public void success(final TermList options) {
                notifyOptions(options);
                loading = false;
                executingSearch = false;

                if (pendingFilter != null && !pendingFilter.equals(filter))
                    loadOptions(pendingFilter);

                pendingFilter = null;
            }

            @Override
            public void failure(String error) {
                notifyOptionsFailure(filter);
                loading = false;
                executingSearch = false;
            }

            @Override
            public void loading() {
                notifyLoading();
                loading = true;
            }
        };
    }

    private TermListCallback createHistoryCallback(final String filter) {
        return new TermListCallback() {
            @Override
            public void success(TermList options) {
                notifyHistory(options);
            }

            @Override
            public void failure(String error) {
                notifyHistoryFailure(filter);
            }
        };
    }

    private void notifyHistory(final TermList history) {
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                historyOptions.setHistory(history);
                hook.historyOptions(historyOptions);
            }
        });
    }

    private void notifyHistoryFailure(String filter) {
        Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load source " + getAllowHistory().getDataStore() + " options for condition " + filter);
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.optionsFailure();
            }
        });
    }

    private void notifyOptions(final TermList options) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
                historyOptions.setOptions(options);
				hook.historyOptions(historyOptions);
			}
		});
	}

	private void notifyOptionsFailure(String filter) {
        Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load source " + getEntity().getSource().getId() + " options for condition " + filter);
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.optionsFailure();
			}
		});
	}

    private void notifySourceFailure() {
        Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not get source " + getDefinition().getSource());
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.optionsFailure();
            }
        });
    }

    private void notifyLoading() {
        loading = true;
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.loading();
            }
        });
    }

    public interface Hook extends MultipleFieldDisplay.Hook, SelectFieldDisplay.Hook { }

    public class MultipleHistoryOptions extends SelectFieldDisplay.HistoryOptions {

        public MultipleHistoryOptions() {
            super(getEntityFactory());
        }

        @Override
        public TermList getOptions() {
            return unselectedTerms(super.getOptions());
        }

        @Override
        public TermList getHistory() {
            return unselectedTerms(super.getHistory());
        }

        private TermList unselectedTerms(TermList selectedTerms) {
            TermList terms = getEntityFactory().createTermList();
            for (Term term : selectedTerms)
                if (!getAllValues().contains(term)) terms.add(term);
            return terms;
        }
    }
}
