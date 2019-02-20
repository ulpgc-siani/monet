package client.widgets.entity;

import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.components.FieldLabel;
import client.widgets.entity.field.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static client.core.model.definition.entity.FieldDefinition.Template;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.presenters.Presenter.Hook;

public abstract class FieldWidget<HookType extends Hook> extends CosmosHtmlPanel {

    protected final IsFieldDisplay display;
	protected final TranslatorService translator;
	protected FieldLabel label;
    protected WidgetState state;

    public FieldWidget(IsFieldDisplay display, String layout, TranslatorService translator) {
        super(getHtml(layout, translator));
        this.display = display;
        this.translator = translator;
        create();
        refresh();
        hook();
        setId(display.getCode());
    }

    public void removeLabel() {
        $(label).parent("td").remove();
    }

    private static String getHtml(String layout, TranslatorService translator) {
        return translator.translateHTML(layout);
    }

    protected void create() {
        addStyleNames();
	    createState();
        createField();
        bind();
    }

    protected void addStyleNames() {
        addStyleName(StyleName.FIELD);
    }

    protected void createField() {
	    createLabel();
        createComponent();
    }

    protected void createLabel() {
		label = new FieldLabel($(getElement()).find(toRule(StyleName.FIELD_LABEL)).html(), display.getLabel());
        setUpLabel();
	}

    private void setUpLabel() {
        if (display.getMessageWhenRequired() != null && !display.getMessageWhenRequired().isEmpty())
            label.isRequired();
        if (display.getDescription() != null && !display.getDescription().isEmpty())
            showDescriptionOnHover();
		if (display.isCollapsible()) {
			$(getElement()).find(toRule(StyleName.COMPONENT)).parent("tr").hide();
			label.addStyleName(StyleName.COLLAPSIBLE);
			label.addOnClickLabelHandler(new FieldLabel.LabelHandler() {
				@Override
				public void handle() {
					GQuery component = $(getElement()).find(toRule(StyleName.COMPONENT)).parent("tr");
					if (component.isVisible()) {
						label.removeStyleName(StyleName.OPENED);
						component.hide();
					} else {
						label.addStyleName(StyleName.OPENED);
						component.show();
					}
				}
			});
		}
		label.addOnClickDescriptionHandler(new FieldLabel.LabelHandler() {
			@Override
			public void handle() {
				display.showDescription();
			}
		});
    }

    public void setLabel(FieldLabel label) {
        removeLabel();
        this.label = label;
        setUpLabel();
    }

    private void showDescriptionOnHover() {
        addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                label.showDescription();
            }
        }, MouseOverEvent.getType());
        addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                label.hideDescription();
            }
        }, MouseOutEvent.getType());
    }

    protected abstract void createComponent();

	private void createState() {
		state = new WidgetState();
	}

    protected void bind() {
        bindKeepingStyles(label, toRule(StyleName.FIELD, StyleName.FIELD_LABEL));

        onAttach();
        RootPanel.detachOnWindowClose(this);
    }

    public void refresh() {
        refreshComponent();
        refreshMessages();
    }

    protected abstract void refreshComponent();

    protected void refreshMessages() {
        addMessageWhenEmpty(display.getMessageWhenEmpty());
        if (display.isReadonly()) setDesignWhenReadOnly();
    }

    private void hook() {
        display.onInvalidValue(new IsFieldDisplay.InvalidValueListener() {
            @Override
            public void update() {
                addMessageWhenInvalid(display.getInvalidValueCause());
            }
        });
        display.onValidValue(new IsFieldDisplay.ValidValueListener() {
            @Override
            public void update() {
                removeDesignWhenInvalid();
            }
        });
        display.addHook(createHook());
    }

    protected abstract HookType createHook();

    protected void addMessageWhenEmpty(String message) {
        if (message == null || $(this).find(StyleName.INPUT).length() == 0) return;

        $(this).find(StyleName.INPUT).get(0).setPropertyString("placeholder", message);
    }

    protected void addMessageWhenInvalid(String message) {
        addErrorMessage(message);
        addDesignWhenInvalid();
    }

    protected void addDesignWhenInvalid() {
        label.addStyleName(StyleName.INVALID);
        $(this).find(toRule(StyleName.COMPONENT)).addClass(StyleName.INVALID);
    }

    protected void removeDesignWhenInvalid() {
        label.removeStyleName(StyleName.INVALID);
        $(this).find(toRule(StyleName.COMPONENT)).removeClass(StyleName.INVALID);
        label.hideWarning();
    }

    protected void setDesignWhenReadOnly() {
        $(this).find(toRule(StyleName.COMPONENT)).addClass(StyleName.READ_ONLY);
    }

    protected void addErrorMessage(String message) {
        label.showWarning(message);
    }

    public interface StyleName {
		String COLLAPSIBLE = "collapsible";
		String COMPONENT = "component";
        String POPUP = "popup";
        String DIALOG = "dialog";
		String FIELD = "field";
		String FIELD_LABEL = "field-label";
        String HORIZONTAL_SEPARATOR = "horizontal-separator";
		String INPUT = "input";
        String INVALID = "invalid";
		String LABEL = "label";
        String MULTIPLE = "multiple";
        String READ_ONLY = "readonly";
        String REQUIRED = "required";
        String VALUES = "values";
		String OPENED = "opened";
    }

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		private static Builder instance;
		private final FieldLayoutHelper helper;

		public Builder() {
			this.helper = new FieldLayoutHelper();
		}

		@Override
		public boolean canBuild(Presenter presenter, String design) {
            register();
            return presenter.is(FieldDisplay.TYPE);
		}

		private void register() {
            registerNormalFields();
            registerPollFields();
        }

        private void registerNormalFields() {
            BooleanFieldWidget.Builder.register();
            CheckFieldWidget.Builder.register();
            CompositeFieldWidget.Builder.register();
            DateFieldWidget.Builder.register();
            FileFieldWidget.Builder.register();
            LinkFieldWidget.Builder.register();
            MemoFieldWidget.Builder.register();
            NumberFieldWidget.Builder.register();
            PictureFieldWidget.Builder.register();
            SelectFieldWidget.Builder.register();
            SerialFieldWidget.Builder.register();
            TextFieldWidget.Builder.register();
            UriFieldWidget.Builder.register();

            MultipleCompositeFieldWidget.Builder.register();
            MultipleDateFieldWidget.Builder.register();
            MultipleFileFieldWidget.Builder.register();
            MultipleLinkFieldWidget.Builder.register();
            MultipleMemoFieldWidget.Builder.register();
            MultipleNumberFieldWidget.Builder.register();
            MultiplePictureFieldWidget.Builder.register();
            MultipleSelectFieldWidget.Builder.register();
            MultipleSerialFieldWidget.Builder.register();
            MultipleTextFieldWidget.Builder.register();
        }

        private void registerPollFields() {
            client.widgets.entity.field_poll.BooleanFieldWidget.Builder.register();
            client.widgets.entity.field_poll.SelectFieldWidget.Builder.register();
            client.widgets.entity.field_poll.MultipleSelectFieldWidget.Builder.register();
        }

        @Override
		public Widget build(Presenter presenter, String design, String layout) {
            createInstance();
            if (!(presenter instanceof FieldDisplay)) return null;
            final FieldDisplay display = (FieldDisplay) presenter;

            FieldWidget.Builder childBuilder = getChildBuilder(display, display.getFieldType().toString(), layout, translator, theme);

            return childBuilder.build(display, design, helper.getFieldLayout(childBuilder.getComponentClass(display), childBuilder.getDialogClass(display), display.getTemplate()));
		}

		protected String getDialogClass(FieldDisplay display) {
			return null;
		}

		protected String getComponentClass(FieldDisplay display) {
			return null;
		}

		public static Builder get() {
			return instance;
		}

		private void createInstance() {
			if (instance != null) return;

			instance = new FieldWidget.Builder();
			instance.inject(translator);
			instance.inject(theme);
		}

		public class FieldLayoutHelper {
			private Map<String, String> fieldTemplates;
			private Map<String, String> componentTemplates;
			private Map<String, String> dialogTemplates;

			public FieldLayoutHelper() {
				this.fieldTemplates = null;
				this.componentTemplates = null;
				this.dialogTemplates = null;
			}

			public String getFieldLayout(String componentClass, String dialogClass, Template template) {
				if (fieldTemplates == null)
					createTemplates();
				return getFieldLayout(componentClass, dialogClass, new HTMLPanel(fieldTemplates.get(template.toString())));
			}

			protected void createTemplates() {
				createFieldTemplates();
				createComponentTemplates();
				createDialogTemplates();
			}

			private void createFieldTemplates() {
				HTMLPanel panel = new HTMLPanel(theme.getLayout("field-templates"));
				fieldTemplates = new HashMap<>();
				for (int i = 0; i < $(panel).children().size(); i++)
					fieldTemplates.put($(panel).children().get(i).getClassName(), translator.translateHTML($(panel).children().get(i).getInnerHTML()));
			}

			private void createComponentTemplates() {
				HTMLPanel panel = new HTMLPanel(theme.getLayout("field-component-templates"));
				componentTemplates = new HashMap<>();
				for (int i = 0; i < $(panel).children().size(); i++)
					addTemplates($(panel).children().get(i), componentTemplates);
			}

			private void createDialogTemplates() {
				HTMLPanel panel = new HTMLPanel(theme.getLayout("field-dialog-templates"));
				dialogTemplates = new HashMap<>();
				for (int i = 0; i < $(panel).children().size(); i++)
                    addTemplates($(panel).children().get(i), dialogTemplates);
			}

			private void addTemplates(Element element, Map<String, String> templates) {
				for (String className : getClasses(element))
					templates.put(className, translator.translateHTML(element.getInnerHTML()));
			}

			private List<String> getClasses(Element element) {
				return Arrays.asList(element.getClassName().split(" "));
			}

			private String getFieldLayout(String componentClass, String dialogClass, HTMLPanel template) {
				$(template).find(toRule(StyleName.COMPONENT)).replaceWith(componentTemplates.get(componentClass));

                if (dialogClass.isEmpty())
					$(template).find(toRule(StyleName.POPUP)).remove();
				else
					$(template).find(toRule(StyleName.POPUP)).find(toRule(StyleName.DIALOG)).html(dialogTemplates.get(dialogClass));

				return template.getElement().getInnerHTML();
			}

		}

    }

}
