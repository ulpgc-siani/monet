package client.presenters.displays.entity.field;

import client.core.constructors.SelectFieldTermConstructor;
import client.core.model.Node;
import client.core.model.Source;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.factory.TypeFactory;
import client.core.model.fields.SelectField;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.services.SourceService.Mode;
import client.services.callback.SourceCallback;
import client.services.callback.TermListCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectFieldDisplay extends FieldDisplay<SelectField, SelectFieldDefinition, Term> implements IsSelectFieldDisplay {

	public static final Type TYPE = new Type("SelectFieldDisplay", FieldDisplay.TYPE);
    protected static final int MAX_HISTORY_ITEMS = 5;
    private static final int MIN_SEARCH_LENGTH = 2;
    private SourceTermsLoader termsLoader;
    private int lastFilterLength = 0;

    private HistoryOptions historyOptions;
    private boolean loading = false;
    private boolean executingSearch = false;
    private String pendingFilter;

    public SelectFieldDisplay(Node node, SelectField field) {
		super(node, field);
	}

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        historyOptions = new HistoryOptions(getEntityFactory());
    }

    @Override
	public Type getType() {
		return TYPE;
	}

    @Override
    public String getValueAsString() {
        if (!hasValue()) return "";
        if (showCode())
            return getValue().getValue() + " - " + getValue().getLabel();
        return getValue().getLabel();
    }

    @Override
    public String getValueFormatted() {
        return (showCode() ? getValue().getValue() + " - " : "") + getValue().getLabel();
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
                        loading = false;
                    }
                });
            }

            @Override
            public void failure(String error) {
                loading = false;
                notifyOptionsFailure(error);
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
        } else
            notifyOptions(SelectFieldTermConstructor.constructList(getDefinition().getTerms()));
	}

    @Override
    protected boolean shouldUpdateValue(Term oldValue, Term term) {
        return isEmbedded() || super.shouldUpdateValue(oldValue, term);
    }

    @Override
	public void loadOptions(String filter) {
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
    public boolean isTermSelected(Term term) {
        return getValue().equals(term);
    }

    @Override
    public boolean isEmbedded() {
        return getDefinition().getSelect() != null && getDefinition().getSelect().isEmbedded();
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
        services.getNodeService().getFieldHistory(getEntity(), getAllowHistory().getDataStore(), 0, MAX_HISTORY_ITEMS, createHistoryCallback(null));
    }

    @Override
    public void loadHistory(String filter) {
        services.getNodeService().searchFieldHistory(getEntity(), getAllowHistory().getDataStore(), filter, 0, MAX_HISTORY_ITEMS, createHistoryCallback(filter));
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
    protected Term format(Term term) {
        return term;
    }

    private SourceCallback createSourceCallback(final String filter) {
        return new SourceCallback() {
            @Override
            public void success(Source source) {
                if (isEmbedded())
                    loadAll(source);
                else
                    loadFirstPage(source, filter);
            }

            @Override
            public void failure(String error) {
                notifySourceFailure();
            }
        };
    }

    private void loadAll(Source source) {
        services.getSourceService().getTerms(source, Mode.TREE, 0, -1, getFlatten(), getDepth(), createOptionsCallback(null));
    }

    private void loadFirstPage(Source object, String filter) {
        if (termsLoader == null) {
            termsLoader = new SourceTermsLoader(object);
            termsLoader.injectSourceService(services.getSourceService());
        }
        getEntity().setSource(object);
        termsLoader.loadPage(0, filter, createOptionsCallback(filter), getFlatten(), getDepth());
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
            public void success(TermList options) {
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
            public void success(final TermList options) {
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

    private SelectFieldDefinition.AllowHistoryDefinition getAllowHistory() {
        return getDefinition().getAllowHistory();
    }

	private void notifyOptions(final TermList termList) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
                historyOptions.setOptions(termList);
				hook.historyOptions(historyOptions);
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

	private void notifyOptionsFailure(String filter) {
		final SelectField field = getEntity();
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load source " + field.getSource().getId() + " options for condition " + (filter == null ? "" : filter));
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

    @Override
    public TypeFactory getTypeFactory() {
        return getEntityFactory();
    }

	public static class HistoryOptions {
        private final TypeFactory factory;
        private TermList history;
        private TermList options;

        public HistoryOptions(TypeFactory termListFactory) {
            factory = termListFactory;
            history = factory.createTermList();
            options = factory.createTermList();
        }

        public TermList getHistory() {
            return history;
        }

        public TermList getOptions() {
            return options;
        }

        public void setHistory(TermList history) {
            this.history = history;
        }

        public void setOptions(TermList options) {
            this.options = options;
        }
    }

    public interface Hook extends FieldDisplay.Hook {
        void historyOptions(HistoryOptions historyOptions);
        void optionsFailure();
        void page(TermList options);
        void loading();
    }
}
