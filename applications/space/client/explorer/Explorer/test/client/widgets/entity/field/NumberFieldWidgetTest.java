package client.widgets.entity.field;

import client.ApplicationTestCase;
import client.core.FieldBuilder;
import client.core.model.*;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.factory.EntityFactory;
import client.core.model.fields.NumberField;
import client.core.model.workmap.DelegationAction.Message;
import client.core.model.workmap.LineAction;
import client.core.system.MonetList;
import client.presenters.displays.entity.field.NumberFieldDisplay;
import client.services.*;
import client.services.callback.*;
import client.services.http.builders.EntityBuilder;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;

public class NumberFieldWidgetTest extends ApplicationTestCase {

	private static final String INTEGER_NUMBER_WITHOUT_FORMAT = "";
	private static final String INTEGER_NUMBER_WITH_FOUR_DIGITS = "0000";
	private static final String DOUBLE_NUMBER_WITHOUT_FORMAT = ".";
	private static final String DOUBLE_NUMBER_WITH_TWO_DECIMALS = ".00";
	private static final String DOUBLE_NUMBER_WITH_MAXIMUM_TWO_DECIMALS = ".##";

	private NumberFieldWidget widget;

	public void testCheckValue() {
		widget = new NumberFieldWidget(display(INTEGER_NUMBER_WITHOUT_FORMAT), "", translator());
		widget.update("12");
		assertEquals("12", widget.input.getText());
	}

	public void testCheckValueWhenNull() {
		widget = new NumberFieldWidget(display(INTEGER_NUMBER_WITHOUT_FORMAT), "", translator());
		widget.update(null);
		assertEquals("0", widget.input.getText());
	}

	public void testCheckValueWithLength() {
		widget = new NumberFieldWidget(display(INTEGER_NUMBER_WITH_FOUR_DIGITS), "", translator());
		widget.update("123");
		assertEquals("0123", widget.input.getText());
	}

	public void testCheckDoubleValue() {
		widget = new NumberFieldWidget(display(DOUBLE_NUMBER_WITHOUT_FORMAT), "", translator());
		widget.update("12.23");
		assertEquals("12.23", widget.input.getText());
	}

	public void testCheckDoubleValueWithExactNumberOfDecimals() {
		widget = new NumberFieldWidget(display(DOUBLE_NUMBER_WITH_TWO_DECIMALS), "", translator());
		widget.update("12.2323");
		assertEquals("12.23", widget.input.getText());

		widget.update("12.2");
		assertEquals("12.20", widget.input.getText());
	}

	public void testCheckDoubleValueWithMaximumNumberOfDecimals() {
		widget = new NumberFieldWidget(display(DOUBLE_NUMBER_WITH_MAXIMUM_TWO_DECIMALS), "", translator());
		widget.update("12.2323");
		assertEquals("12.23", widget.input.getText());

		widget.update("12.2");
		assertEquals("12.2", widget.input.getText());
	}

	public void testNumberWithSymbol() {
		widget = new NumberFieldWidget(display("##.## €"), "", translator());
		widget.update("12.2323");
		assertEquals("12.23 €", widget.input.getText());
	}





	private NumberFieldDisplay display(String format) {
		final NumberFieldDisplay display = new NumberFieldDisplay(null, field(format));
		display.inject(new Services() {
			@Override
			public SpaceService getSpaceService() {
				return new SpaceService() {
					@Override
					public void load(SpaceCallback callback) {

					}

					@Override
					public void loadDefinition(String definitionKey, Instance.ClassName definitionClassName, DefinitionCallback<EntityDefinition> callback) {

					}

					@Override
					public <T extends Entity> void loadDefinition(T entity, DefinitionCallback<EntityDefinition> callback) {

					}

					@Override
					public Space load() {
						return null;
					}

					@Override
					public EntityFactory getEntityFactory() {
						return new EntityBuilder<>();
					}
				};
			}

			@Override
			public NodeService getNodeService() {
				return new NodeService() {
					@Override
					public void open(String id, NodeCallback callback) {

					}

					@Override
					public void getLabel(Node node, Callback<String> callback) {

					}

					@Override
					public void add(Node parent, String code, NodeCallback callback) {

					}

					@Override
					public void delete(Node node, VoidCallback callback) {

					}

					@Override
					public void delete(Node[] nodes, VoidCallback callback) {

					}

					@Override
					public void saveNote(Node node, String name, String value, NoteCallback callback) {

					}

					@Override
					public void saveField(Node node, Field field, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void addField(Node node, MultipleField parent, Field field, int pos, VoidCallback callback) {

					}

					@Override
					public void deleteField(Node node, MultipleField parent, int pos, VoidCallback callback) {

					}

					@Override
					public void changeFieldOrder(Node node, MultipleField parent, int previousPos, int newPos, VoidCallback callback) {

					}

					@Override
					public void clearField(Node node, MultipleField parent, VoidCallback callback) {

					}

					@Override
					public void searchFieldHistory(Field field, String dataStore, String filter, int start, int limit, TermListCallback callback) {

					}

					@Override
					public void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback) {

					}

					@Override
					public void saveFile(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void savePicture(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void loadHelpPage(Node node, HtmlPageCallback callback) {
					}

					@Override
					public void executeCommand(Node node, Command command, NodeCommandCallback callback) {

					}

                    @Override
                    public void focusNodeField(Node node, Field field) {
                    }

                    @Override
                    public void focusNodeView(Node node) {
                    }

                    @Override
                    public void blurNodeField(Node node, Field field) {
                    }

                    @Override
                    public void blurNodeView(Node node) {
                    }

                    @Override
					public String getNodePreviewBaseUrl(String documentId) {
						return null;
					}

					@Override
					public String getDownloadNodeUrl(Node node) {
						return null;
					}

					@Override
					public String getDownloadNodeImageUrl(Node node, String imageId) {
						return null;
					}

					@Override
					public String getDownloadNodeThumbnailUrl(Node node, String imageId) {
						return null;
					}

					@Override
					public String getDownloadNodeFileUrl(Node node, String fileId) {
						return null;
					}
				};
			}

			@Override
			public TaskService getTaskService() {
				return null;
			}

			@Override
			public AccountService getAccountService() {
				return null;
			}

			@Override
			public SourceService getSourceService() {
				return null;
			}

			@Override
			public IndexService getIndexService() {
				return null;
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return null;
			}

            @Override
            public NotificationService getNotificationService() {
                return null;
            }
		});
		return display;
	}

	private NumberField field(final String format) {
		NumberField field = FieldBuilder.buildNumber("NumberField", "Campo numerico");
        field.setValue(new client.core.system.types.Number<>(0));
		field.setDefinition(new NumberFieldDefinition() {

			@Override
			public boolean is(String key) {
				return key.equals(getCode()) || key.equals(getName());
			}

			@Override
			public String getFormat() {
				return format;
			}

			@Override
			public RangeDefinition getRange() {
				return new RangeDefinition() {
					@Override
					public long getMin() {
						return 0;
					}

					@Override
					public long getMax() {
						return 200;
					}
				};
			}

			@Override
			public Edition getEdition() {
				return Edition.SLIDER;
			}

			@Override
			public boolean isMultiple() {
				return false;
			}

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
			public String getLabel() {
				return "Campo numerico";
			}

			@Override
			public String getDescription() {
				return "Campo numerico";
			}

			@Override
			public Instance.ClassName getClassName() {
				return NumberFieldDefinition.CLASS_NAME;
			}

			@Override
			public Template getTemplate() {
				return Template.INLINE;
			}

			@Override
			public FieldType getFieldType() {
				return FieldType.NORMAL;
			}

			@Override
			public boolean isCollapsible() {
				return false;
			}

			@Override
			public boolean isRequired() {
				return true;
			}

			@Override
			public boolean isReadonly() {
				return false;
			}

			@Override
			public boolean isExtended() {
				return false;
			}

			@Override
			public boolean isSuperField() {
				return false;
			}

			@Override
			public boolean isStatic() {
				return false;
			}

			@Override
			public boolean isUnivocal() {
				return false;
			}

			@Override
			public List<Display> getDisplays() {
				return new MonetList<>(
					new FieldDefinition.Display() {
						@Override
						public String getMessage() {
							return "23";
						}

						@Override
						public When getWhen() {
							return When.EMPTY;
						}
					},
					new FieldDefinition.Display() {
						@Override
						public String getMessage() {
							return "El campo tiene un valor incorrecto";
						}

						@Override
						public When getWhen() {
							return When.INVALID;
						}
					},
					new FieldDefinition.Display() {
						@Override
						public String getMessage() {
							return "El campo es de solo lectura";
						}

						@Override
						public When getWhen() {
							return When.READONLY;
						}
					},
					new FieldDefinition.Display() {
						@Override
						public String getMessage() {
							return "El campo es requerido";
						}

						@Override
						public When getWhen() {
							return When.REQUIRED;
						}
					}
				);
			}

			@Override
			public Display getDisplay(Display.When when) {
				for (Display display : getDisplays()) {
					if (display.getWhen() == when) return display;
				}
				return null;
			}

			@Override
			public String getCode() {
				return "Code";
			}

			@Override
			public String getName() {
				return "Campo numerico";
			}
		});

		return field;
	}

	private TranslatorService translator() {
		return new TranslatorService() {
			@Override
			public String getTaskAssignedToMeMessage() {
				return null;
			}

			@Override
			public String getTaskAssignedToMeBySenderMessage(User sender) {
				return null;
			}

			@Override
			public String getTaskAssignedToUserMessage(User Owner) {
				return null;
			}

			@Override
			public String getTaskAssignedToUserBySenderMessage(User owner, User sender) {
				return null;
			}

			@Override
			public String getTaskDateMessage(Date createDate, Date updateDate) {
				return null;
			}

			@Override
			public String getTaskNotificationsMessage(int count) {
				return null;
			}

			@Override
			public String getTaskDelegationMessage(Message message, Date failureDate, String roleTypeLabel) {
				return null;
			}

			@Override
			public String getTaskLineTimeoutMessage(Date timeout, LineAction.Stop timeoutStop) {
				return null;
			}

			@Override
			public String getTaskWaitMessage(Date dueDate) {
				return null;
			}

			@Override
			public String getTaskStateLabel(Task.State state) {
				return null;
			}

			@Override
			public String getTaskTypeIcon(Task.Type type) {
				return null;
			}

			@Override
			public String getCurrentLanguage() {
				return "es";
			}

			@Override
			public String translate(Object name) {
				return "";
			}

			@Override
			public String translateHTML(String html) {
				return "<table>        <tbody><tr>          <td vertical-align=\"middle\" width=\"36.5%\">            <div class=\"label\"></div>            <div class=\"description\"></div>            <span class=\"hint description-hint\"></span>            <div class=\"warning\"></div>            <span class=\"hint warning-hint\"></span>          </td>          <td vertical-align=\"bottom\">            <div class=\"relative\">                            <div class=\"component\">        <div class=\"input-panel\">          <div class=\"input\"></div>          <ul class=\"toolbar\">            <li>              <div class=\"clear\"></div>            </li>          </ul>          <span class=\"underline\"></span>        </div>      </div><div class=\"popup\">                <div class=\"toolbar\"><span class=\"close\">cerrar</span></div>                <div class=\"content dialog\">      <div class=\"slider\"></div>    </div>              </div>            </div>          </td>        </tr>      </tbody></table>      <div class=\"horizontal-separator\"></div>";
			}

			@Override
			public int dayOfWeekToNumber(DayOfWeek dayOfWeek) {
				return 1;
			}

			@Override
			public int monthToNumber(String month) {
				return 0;
			}

			@Override
			public String monthNumberToString(Integer month) {
				return null;
			}

			@Override
			public String[] getDateSeparators() {
				return new String[0];
			}

			@Override
			public String translateDateWithPrecision(Date date, DatePrecision precision) {
				return "";
			}

			@Override
			public String translateFullDate(Date date) {
				return null;
			}

			@Override
			public String translateFullDateByUser(Date date, String user) {
				return null;
			}

			@Override
			public String getLoadingLabel() {
				return "";
			}

			@Override
			public String getNoPhoto() {
				return null;
			}

			@Override
			public String getAddPhoto() {
				return null;
			}

			@Override
			public String translateSavedPeriodAgo(String message, Date date) {
				return null;
			}

			@Override
			public String translateSearchForCondition(String condition) {
				return null;
			}

			@Override
			public String translateCountDate(Date date) {
				return null;
			}

			@Override
			public String getCountLabel(int count) {
				return null;
			}

			@Override
			public String getSelectionCountLabel(int selectionCount) {
				return "";
			}
		};
	}
}


