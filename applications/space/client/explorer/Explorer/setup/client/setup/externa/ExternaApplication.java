package client.setup.externa;

import client.core.*;
import client.core.model.*;
import client.core.model.Task.State;
import client.core.model.definition.Definition;
import client.core.model.definition.Dictionary;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.factory.EntityFactory;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.core.model.workmap.DelegationAction;
import client.core.model.workmap.EditionAction;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.core.system.MonetList;
import client.core.system.Report;
import client.services.*;
import client.services.callback.*;
import client.setup.SetupApplication;
import cosmos.types.Date;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Role.Type;

public class ExternaApplication extends SetupApplication {

	@Override
	protected String getTheme() {
		return "default";
	}

	@Override
	protected Node getRootNode() {
		return entornoExterna;
	}

	@Override
	protected Dictionary getDictionary() {
		List<Definition> definitions = new MonetList<>(new Definition[]{
			new client.core.system.definition.Definition("expcorr", "expcorr", "Expediente correctivo", "Expediente correctivo"),
			new client.core.system.definition.Definition("expprev", "expprev", "Expediente preventivo", "Expediente preventivo"),
			new client.core.system.definition.Definition("operario", "operario", "Operario", "Operario")
		});

		return EntityBuilder.buildDictionary(definitions);
	}

	/*
		@Override
		protected Presenter createOperationList(Account account, final ApplicationDisplay applicationDisplay) {
			OperationListDisplay operationListDisplay = new OperationListDisplay();
			final Operation.Context context = new Operation.Context() {
				@Override
				public Presenter getCanvas() {
					return applicationDisplay;
				}

				@Override
				public Operation getReferral() {
					return applicationDisplay.getVisitingDisplay() != null ? applicationDisplay.getVisitingDisplay().toOperation() : null;
				}
			};

			final client.presenters.operations.Operation operation1 = new client.presenters.operations.Operation(context) {
				@Override
				public String getDefaultLabel() {
					return "tramitar";
				}

				@Override
				public void execute() {
					Logger.getLogger("ApplicationLogger").log(Level.FINEST, "Se ha invocado a la operación de tramitar");
				}

				@Override
				public boolean disableButtonWhenExecute() {
					return false;
				}

				@Override
				public Type getType() {
					return Operation.TYPE;
				}

			};
			operation1.setEnabled(true);

			final client.presenters.operations.Operation operation2 = new client.presenters.operations.Operation(context) {
				@Override
				public String getDefaultLabel() {
					return "PDF";
				}

				@Override
				public void execute() {
					Logger.getLogger("ApplicationLogger").log(Level.FINEST, "Se ha invocado a la operación generar informe en PDF");
				}

				@Override
				public boolean disableButtonWhenExecute() {
					return false;
				}

				@Override
				public Type getType() {
					return Operation.TYPE;
				}
			};
			final client.presenters.operations.Operation operation3 = new client.presenters.operations.Operation(context) {
				@Override
				public String getDefaultLabel() {
					return "CSV";
				}

				@Override
				public void execute() {
					Logger.getLogger("ApplicationLogger").log(Level.FINEST, "Se ha invocado a la operación generar informe en CSV");
				}

				@Override
				public boolean disableButtonWhenExecute() {
					return false;
				}

				@Override
				public Type getType() {
					return Operation.TYPE;
				}
			};

			operationListDisplay.addChild(operation1);
			operationListDisplay.addChild(new CompositeOperation(context) {{
				setLabel("Herramientas...");
				addChild(new CompositeOperation(context) {{
					setLabel("Generar informe...");
					addChild(operation2);
					addChild(operation3);
				}});
				addChild(new Operation(context) {
					@Override
					public void execute() {
					}

					@Override
					public Type getType() {
						return Operation.TYPE;
					}
				});
			}});

			return operationListDisplay;
		}
	*/
	@Override
	protected Services createServices(String apiUrl) {

		return new Services() {
			private SpaceService spaceService;
			private NodeService nodeService;
			private TaskService taskService;
			private AccountService accountService;
			private SourceService sourceService;
			private IndexService indexService;

			@Override
			public SpaceService getSpaceService() {
				//return new client.services.NodeService(new Stub("http://localhost:8080/monet"));

				if (spaceService == null)
					spaceService = createModelService();

				return spaceService;
			}

			@Override
			public NodeService getNodeService() {
				//return new client.services.NodeService(new Stub("http://localhost:8080/monet"));

				if (nodeService == null)
					nodeService = createNodeService();

				return nodeService;
			}

			@Override
			public TaskService getTaskService() {

				if (taskService == null)
					taskService = createTaskListService();

				return taskService;
			}

			@Override
			public AccountService getAccountService() {
				//return new client.services.NodeService(new Stub("http://localhost:8080/monet"));

				if (accountService == null)
					accountService = createAccountService();

				return accountService;
			}

			@Override
			public SourceService getSourceService() {
				if (sourceService == null)
					sourceService = createSourceService();

				return sourceService;
			}

			@Override
			public IndexService getIndexService() {
				if (indexService == null)
					indexService = createIndexService();

				return indexService;
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return loadTranslator();
			}

            @Override
            public NotificationService getNotificationService() {
                return new NotificationService() {
                    @Override
                    public void registerSource(MessageSource source) {
                    }

                    @Override
                    public void notify(MessageType messageType, Message message) {
                    }

                    @Override
                    public void registerListener(UpdateTaskStateListener listener) {
                    }

                    @Override
                    public void registerListener(UpdateAccountListener listener) {
                    }

                    @Override
                    public void registerListener(UpdateFieldListener listener) {
                    }

                    @Override
                    public void registerListener(AddFieldListener listener) {
                    }

                    @Override
                    public void registerListener(DeleteFieldListener listener) {
                    }

                    @Override
                    public void registerListener(UpdateNodeListener listener) {
                    }

	                @Override
	                public void deregisterListener(UpdateTaskStateListener listener) {
	                }

	                @Override
	                public void deregisterListener(UpdateAccountListener listener) {
	                }

	                @Override
	                public void deregisterListener(UpdateFieldListener listener) {
	                }

	                @Override
	                public void deregisterListener(AddFieldListener listener) {
	                }

	                @Override
	                public void deregisterListener(DeleteFieldListener listener) {
	                }

	                @Override
	                public void deregisterListener(UpdateNodeListener listener) {
	                }
                };
            }

			private SpaceService createModelService() {
				final client.core.system.Space space = (client.core.system.Space) loadSpace();

				return new SpaceService() {
					@Override
					public void load(SpaceCallback callback) {
						callback.success(space);
					}

					@Override
					public void loadDefinition(String definitionKey, Instance.ClassName definitionClassName, DefinitionCallback<EntityDefinition> callback) {
					}

					@Override
					public <T extends Entity> void loadDefinition(T entity, DefinitionCallback<EntityDefinition> callback) {
						callback.success(entity.getDefinition());
					}

					@Override
					public Space load() {
						return space;
					}

					@Override
					public EntityFactory getEntityFactory() {
						return EntityBuilder.buildEntityFactory();
					}
				};
			}

			private NodeService createNodeService() {
				return new NodeService() {

					@Override
					public void open(String id, final NodeCallback callback) {
						if (id.equals("entornoExterna")) callback.success(entornoExterna);
						else if (id.equals("archivoExpedientes")) callback.success(archivoExpedientes);
						else if (id.equals("expediente1")) callback.success(expediente1);
						else if (id.equals("archivoIncidencias")) callback.success(archivoIncidencias);
						else if (id.equals("objetosActuacion")) callback.success(objetosActuacion);
						else if (id.equals("proveedores")) callback.success(proveedores);
						else if (id.equals("proveedor1")) callback.success(proveedor1);
						else if (id.equals("proveedor2")) callback.success(proveedor2);
						else if (id.equals("piezas")) callback.success(piezas);
						else if (id.equals("preferencias")) callback.success(preferencias);
						else if (id.equals("encargo")) callback.success(encargoDelegacion);
					}

					@Override
					public void getLabel(Node node, Callback<String> callback) {
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
					public void saveNote(Node node, final String name, final String value, final NoteCallback callback) {
						callback.success(new NoteCallback.Result() {
							@Override
							public String getName() {
								return name;
							}

							@Override
							public String getValue() {
								return value;
							}
						});
					}

					@Override
					public void add(Node parent, String code, final NodeCallback callback) {
						callback.success(entornoExterna);
					}

					@Override
					public void delete(Node node, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void delete(Node[] nodes, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void saveField(Node node, Field field, VoidCallback callback) {

					}

					@Override
					public void executeCommand(Node node, Command command, NodeCommandCallback callback) {
						callback.success(null);
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
						return "http://parques.dev.gisc.siani.es/pinfantiles/servlet/office.api?op=previewnode&id=" + documentId;
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

					private TermList history = TypeBuilder.buildTermList(new Term[] {
						TypeBuilder.buildTerm("Hola", "Hola"),
						TypeBuilder.buildTerm("Limpieza", "Limpieza"),
						TypeBuilder.buildTerm("Vaciado", "Vaciado"),
						TypeBuilder.buildTerm("Reparación 1", "Reparación 1"),
						TypeBuilder.buildTerm("Reparación 2", "Reparación 2"),
						TypeBuilder.buildTerm("Reparación 3", "Reparación 3"),
						TypeBuilder.buildTerm("Reparación 4", "Reparación 4"),
						TypeBuilder.buildTerm("Reparación 5", "Reparación 5"),
						TypeBuilder.buildTerm("Reparación 6", "Reparación 6")
					});

					@Override
					public void searchFieldHistory(Field field, String dataStore, String filter, int start, int limit, TermListCallback callback) {
						TermList callbackHistory = TypeBuilder.buildTermList();
						for (Term term : history) {
							if (term.getLabel().toLowerCase().startsWith(filter.toLowerCase()))
								callbackHistory.add(term);
						}
						callback.success(callbackHistory);
					}

					@Override
					public void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback) {
						callback.success(history);
					}

					@Override
					public void saveFile(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void savePicture(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void loadHelpPage(Node node, HtmlPageCallback callback) {
						callback.success(new client.core.system.HtmlPage("<div>Soy una página de ayuda</div>"));
					}
				};
			}

			private TaskService createTaskListService() {
				return new TaskService() {

					@Override
					public void open(String id, TaskCallback callback) {
						Task result;

						if (id.equals("delegation"))
							result = delegationTask;
						else if (id.equals("line"))
							result = lineTask;
						else if (id.equals("edition"))
							result = editionTask;
						else if (id.equals("wait"))
							result = waitTask;
						else
							result = lineTask;

						callback.success(result);
					}

					@Override
					public void saveUrgency(Task task, boolean value, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void saveOwner(Task task, User owner, String reason, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void abort(Task task, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void loadDelegationRoles(Task task, RoleListCallback callback) {
						callback.success(ListBuilder.buildRoleList(new Role[]{
							EntityBuilder.buildRole("1", Type.USER, "Role 1"),
							EntityBuilder.buildRole("2", Type.USER, "Role 2"),
							EntityBuilder.buildRole("3", Type.USER, "Role 3")
						}));
					}

					@Override
					public void selectDelegationRole(Task task, Role role, TaskCallback callback) {
						task.getWorkMap().getPlace().setAction(delegationActionInOrderStep);
						callback.success(task);
					}

					@Override
					public void setupDelegationOrder(Task task, TaskCallback callback) {
						task.getWorkMap().getPlace().setAction(delegationActionInSendingStep);
						callback.success(task);
					}

					@Override
					public void solveLine(Task task, LineAction.Stop stop, TaskCallback callback) {
						task.getWorkMap().getPlace().setAction(lineAction);
						callback.success(task);
					}

					@Override
					public void solveEdition(Task task, TaskCallback callback) {
						task.getWorkMap().getPlace().setAction(editionAction);
						callback.success(task);
					}

					@Override
					public void setupWait(Task task, WaitAction.Scale scale, int value, TaskCallback callback) {
						task.getWorkMap().getPlace().setAction(waitAction);
						callback.success(task);
					}

					@Override
					public void loadHistory(Task task, int start, int limit, HistoryCallback callback) {

					}

					@Override
					public void open(TaskListCallback callback) {
						callback.success(taskList);
					}

					@Override
					public void saveOwner(Task[] tasks, User owner, String reason, VoidCallback callback) {
					}

					private String getEntryLabel(String type, int pos) {
						return type + " " + pos;
					}

					private Task.Type getEntryType(int pos) {
						if (pos%2==0)
							return Task.Type.ACTIVITY;

						if (pos%3==0)
							return Task.Type.JOB;

						return Task.Type.SERVICE;
					}

					private User getEntryOwner(int pos) {
						if (pos%2==0)
							return EntityBuilder.buildUser("Mario Caballero", "mariocaballero@siani.com", null);

						if (pos%3==0)
							return EntityBuilder.buildUser("Jose Juan Hernández", "josejuanhernandez@siani.es", null);

						return null;
					}

					private User getEntrySender(int pos) {
						if (pos%2==0)
							return EntityBuilder.buildUser("Jose Juan Hernández", "josejuanhernandez@siani.es", null);

						if (pos%3==0)
							return EntityBuilder.buildUser("Mario Caballero", "mariocaballero@siani.com", null);

						return null;
					}

					private State getEntryState(int pos) {
						return State.PENDING;
					}

					private String getEntryDescription(int pos) {
						return pos%2==0?"Encargo de actuación iniciado. Esperando respuesta.":"Iniciando trabajo...";
					}

					private int getEntryMessagesCount(int pos) {
						return pos%2==0?0:10;
					}

					private String getEntryProgressImage(int pos) {
						return "file:///Users/mcaballero/Projects/labs/lab-cosmos/Sample/setup_resources/task-progress.png";
					}

					private Date getEntryCreateDate(int pos) {
						return new Date(2012, 10, 20);
					}

					private Date getEntryUpdateDate(int pos) {
						return new Date(2012, 10, 20);
					}

					private boolean getEntryUrgent(int pos) {
						return pos % 2 == 0;
					}

					@Override
					public void load(TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {
						final List<TaskListIndexEntry> entries = new MonetList<>();
						final Map<Integer, String> types = new HashMap<Integer, String>() {{
							put(0, "delegation"); put(1, "line"); put(2, "edition"); put(3, "wait");
							put(4, "line"); put(5, "line"); put(6, "line"); put(7, "line");
							put(8, "line"); put(9, "line");
						}};

						for (int i=0; i<10; i++) {
							final int pos = i;
							final String type = types.get(pos);
							entries.add(IndexBuilder.buildTaskListIndexEntry(new HashMap<String, Object>() {{
								put("entity", TaskBuilder.buildActivity(type, getEntryLabel(type, pos)));
								put("type", getEntryType(pos));
								put("owner", getEntryOwner(pos));
								put("sender", getEntrySender(pos));
								put("state", getEntryState(pos));
								put("description", getEntryDescription(pos));
								put("messages_count", getEntryMessagesCount(pos));
								put("progress_image", getEntryProgressImage(pos));
								put("create_date", getEntryCreateDate(pos));
								put("update_date", getEntryUpdateDate(pos));
								put("urgent", getEntryUrgent(pos));
							}}));
						}
						entries.setTotalCount(10);

						callback.success(entries);
					}

					@Override
					public void search(String condition, TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {
						load(situation, inbox, filters, orders, start, limit, callback);
					}

					@Override
					public void getFilterOptions(Filter filter, FilterOptionsCallback callback) {

					}

					@Override
					public void searchFilterOptions(Filter filter, String condition, FilterOptionsCallback callback) {

					}

				};
			}

			private AccountService createAccountService() {
				return new AccountService() {
					private int notificationsEmpty = 0;

					@Override
					public Account load() {
						return space.getAccount();
					}

                    @Override
                    public boolean userIsLogged(User user) {
                        return load().getUser().equals(user);
                    }

                    @Override
					public void load(AccountCallback callback) {
						callback.success(space.getAccount());
					}

					@Override
					public void loadNotifications(int start, int limit, NotificationListCallback callback) {

						callback.success(notificationsEmpty == 0 ? ListBuilder.buildNotificationList(new Notification[0]) : ListBuilder.buildNotificationList(new Notification[]{
							EntityBuilder.buildNotification("Ha llegado una orden de trabajo", new Date()),
							EntityBuilder.buildNotification("Ha llegado un parte de trabajo", new Date()),
							EntityBuilder.buildNotification("Sergio ha entregado el parte", new Date())
						}));

						notificationsEmpty = notificationsEmpty==0?1:0;
					}

					@Override
					public void loadBusinessUnits(BusinessUnitListCallback callback) {
						callback.success(ListBuilder.buildBusinessUnitList(new BusinessUnit[]{
							EntityBuilder.buildBusinessUnit(BusinessUnit.Type.MEMBER, "Coordinación", "http://www.google.es"),
							EntityBuilder.buildBusinessUnit(BusinessUnit.Type.PARTNER, "FCC", "http://www.google.es"),
							EntityBuilder.buildBusinessUnit(BusinessUnit.Type.MEMBER, "Ejecución", "http://www.google.es")
						}));
					}

					@Override
					public void logout(VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void saveProfilePhoto(String photoUrl) {
					}
				};
			}

			private SourceService createSourceService() {
				return new SourceService() {

                    private final TermList termList = TypeBuilder.buildTermList(new Term[] {
                        TypeBuilder.buildTerm("001", "Limpieza"),
                        TypeBuilder.buildTerm("002", "Vaciado"),
	                    TypeBuilder.buildTerm("003", "Reparación 1"),
	                    TypeBuilder.buildTerm("004", "Reparación 2"),
	                    TypeBuilder.buildTerm("005", "Reparación 3"),
	                    TypeBuilder.buildTerm("006", "Reparación 4"),
	                    TypeBuilder.buildTerm("007", "Reparación 5"),
	                    TypeBuilder.buildTerm("008", "Reparación 6")
                    });

					@Override
					public void open(String id, SourceCallback callback) {
						callback.success(EntityBuilder.buildSource(id, ""));
					}

					@Override
					public void locate(String key, String url, SourceCallback callback) {
						callback.success(EntityBuilder.buildSource(key, ""));
					}

					@Override
					public void getTerms(Source source, Mode mode, int start, int limit, String flatten, String depth, TermListCallback callback) {
						callback.success(termList);
					}

					@Override
					public void searchTerms(Source source, Mode mode, final String condition, int limit, int count, String flatten, String depth, TermListCallback callback) {
						List<Term> terms = new MonetList<>();

						for (Term term : termList) {
							if (term.getLabel().startsWith(condition))
								terms.add(term);
						}

						callback.success(TypeBuilder.buildTermList(terms.toArray(new Term[terms.size()])));
					}
				};
			}

			private IndexService createIndexService() {
				return new IndexService() {

					@Override
					public void open(String code, IndexCallback callback) {
						callback.success(indice);
					}

					@Override
					public void getEntries(final Index index, final Scope scope, List<Filter> filters, List<Order> orders, final int start, final int limit, NodeIndexEntriesCallback callback) {
						final String title = index == indiceProveedores?"Proveedor":"Expediente";
						List<NodeIndexEntry> entries = new MonetList<>();
						entries.setTotalCount(100);

						for (int pos = start; pos < start + limit; pos++) {
							final int finalPos = pos;
							NodeIndexEntry entry = IndexBuilder.buildNodeIndexEntry(new HashMap<String, Object>() {{
								put("entity", index == indiceProveedores?proveedor1:expediente1);
								put("label", title + " " + finalPos);
								put("picture", IndexBuilder.buildNodeIndexEntryAttribute("Imagen", "http://parques.dev.gisc.siani.es/pinfantiles/office/images/no-picture.jpg"));
								put("lines", new MonetList<NodeIndexEntry.Attribute>() {{
									add(IndexBuilder.buildNodeIndexEntryAttribute("Categoría", "Categoría de ejemplo"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Objeto de actuación", "00" + finalPos + " Objeto 2 - 00" + finalPos + ".001 componente 1"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Tipos de incidencia", "Desgastado, Oxidado"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Origen", "De parte"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Unidad destino", "Sin asignar"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Fecha de notificación", "agosto 2014"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Prioridad", "Media"));
									add(IndexBuilder.buildNodeIndexEntryAttribute("Creada por", "admin"));
								}});
								put("highlights", new MonetList<NodeIndexEntry.Attribute>() {{ add(IndexBuilder.buildNodeIndexEntryAttribute("estado", "Notificada")); }});
							}});
							entries.add(entry);
						}

						callback.success(entries);
					}

					@Override
					public void getEntry(Index index, Scope scope, Node entryNode, NodeIndexEntryCallback callback) {
					}

					@Override
					public void searchEntries(Index index, final Scope scope, String condition, List<Filter> filters, List<Order> orders, int start, int limit, NodeIndexEntriesCallback callback) {
						final List<NodeIndexEntry> entryListL = new MonetList() {{
							add(IndexBuilder.buildNodeIndexEntry(proveedor1, "Objeto 1"));
							setTotalCount(80);
						}};
						final List<NodeIndexEntry> entryListV = new MonetList() {{
							add(IndexBuilder.buildNodeIndexEntry(proveedor2, "Objeto 2"));
							setTotalCount(80);
						}};
						final List<NodeIndexEntry> entryListR = new MonetList() {{
							add(IndexBuilder.buildNodeIndexEntry(proveedor1, "Objeto 3"));
							setTotalCount(80);
						}};

						if (!condition.isEmpty() && condition.toLowerCase().startsWith("1"))
							callback.success(entryListL);
						else if (!condition.isEmpty() && condition.toLowerCase().startsWith("2"))
							callback.success(entryListV);
						else if (!condition.isEmpty() && condition.toLowerCase().startsWith("3"))
							callback.success(entryListR);
						else
							getEntries(index, scope, filters, orders, start, limit, callback);
					}

					@Override
					public void searchNodeEntries(Node container, String condition, int start, int limit, NodeIndexEntriesCallback callback) {
					}

					@Override
					public void getHistory(Index index, Scope scope, String dataStore, int start, int limit, NodeIndexEntriesCallback callback) {

					}

					@Override
					public void searchHistory(Index index, Scope scope, String dataStore, String filter, int start, int limit, NodeIndexEntriesCallback callback) {

					}

					@Override
					public void getFilterOptions(Index index, Scope scope, Filter filter, FilterOptionsCallback callback) {
						List<Filter.Option> options = null;

						if (filter.getName().equals("Categoría"))
							options = new MonetList<Filter.Option>() {{
								add(IndexBuilder.buildFilterOption("Limpieza", "Limpieza"));
								add(IndexBuilder.buildFilterOption("Rotura", "Rotura"));
								add(IndexBuilder.buildFilterOption("Extracción", "Extracción"));
							}};
						else
							options = new MonetList<Filter.Option>() {{
								add(IndexBuilder.buildFilterOption("es", "España"));
								add(IndexBuilder.buildFilterOption("hl", "Holanda"));
								add(IndexBuilder.buildFilterOption("ch", "Chile"));
								add(IndexBuilder.buildFilterOption("au", "Australia"));
								add(IndexBuilder.buildFilterOption("po", "Portugal"));
								add(IndexBuilder.buildFilterOption("ar", "Argentina"));
								add(IndexBuilder.buildFilterOption("br", "Brazil"));
								add(IndexBuilder.buildFilterOption("fr", "Francia"));
								add(IndexBuilder.buildFilterOption("be", "Belgica"));
								add(IndexBuilder.buildFilterOption("en", "Inglaterra"));
								add(IndexBuilder.buildFilterOption("it", "Italia"));
								add(IndexBuilder.buildFilterOption("ek", "Eslovaquia"));
								add(IndexBuilder.buildFilterOption("rc", "República Checa"));
								add(IndexBuilder.buildFilterOption("po", "Polonia"));
								add(IndexBuilder.buildFilterOption("ru", "Rusia"));
							}};

						callback.success(options);
					}

					@Override
					public void getFilterOptionsReport(Index index, Scope scope, Filter filter, ReportCallback callback) {
						callback.success(new Report());
					}

					@Override
					public void searchFilterOptions(Index index, Scope scope, Filter filter, final String condition, final FilterOptionsCallback callback) {
						getFilterOptions(index, scope, filter, new FilterOptionsCallback() {
							@Override
							public void success(List<Filter.Option> options) {
								List<Filter.Option> filteredOptions = filter(condition, options);
								callback.success(filteredOptions);
							}

							@Override
							public void failure(String error) {
								callback.failure(error);
							}

							private List<Filter.Option> filter(String condition, List<Filter.Option> options) {
								List<Filter.Option> result = new MonetList<>();

								for (Filter.Option option : options) {
									if (option.getValue().toLowerCase().contains(condition.toLowerCase()) || option.getLabel().toLowerCase().contains(condition.toLowerCase()))
										result.add(option);
								}

								return result;
							}

						});
					}
				};
			}

		};
	}

	// Delegation action
	static Field campoMotivo = FieldBuilder.buildText("motivo", "Motivo");
	static Field campoFechaInicio = FieldBuilder.buildDate("fechainicio", "Fecha de inicio");
	static Form encargoDelegacion = NodeBuilder.buildForm("encargo", "Encargo", "", ListBuilder.buildNodeViewList(new FormView[]{
		ViewBuilder.buildFormView("001", "Información general", null, true, new Field[]{campoMotivo, campoFechaInicio})
	}), new Field[] { campoMotivo, campoFechaInicio}, true);
	static DelegationAction delegationActionInRoleStep = TaskBuilder.buildDelegationActionInRoleStep("Iniciando encargo de actuación");
	static DelegationAction delegationActionInOrderStep = TaskBuilder.buildDelegationActionInOrderStep("Encargo de actuación iniciado. Configure el encargo", EntityBuilder.buildRole("1", Type.USER, "Role 1"), encargoDelegacion);
	static DelegationAction delegationActionInSendingStep = TaskBuilder.buildDelegationActionInSendingStep("Encargo de actuación configurado", EntityBuilder.buildRole("1", Type.USER, "Role 1"));
	static Task delegationTask = TaskBuilder.buildActivity("delegation", "Tarea con delegación", State.PENDING, TaskBuilder.buildWorkMap(delegationActionInRoleStep), ListBuilder.buildTaskViewList(new TaskView[]{
		ViewBuilder.buildTaskStateView("state", "Estado")
	}));

	// Line action
	static LineAction lineAction = TaskBuilder.buildLineAction("Seleccione una opción", new Date(), new LineAction.Stop[] {
		TaskBuilder.buildLineActionStop("001", "Opción 1"),
		TaskBuilder.buildLineActionStop("002", "Opción 2"),
		TaskBuilder.buildLineActionStop("003", "Opción 3")
	});
	static Task lineTask = TaskBuilder.buildActivity("line", "Tarea con línea", State.PENDING, TaskBuilder.buildWorkMap(lineAction), ListBuilder.buildTaskViewList(new TaskView[]{
		ViewBuilder.buildTaskStateView("state", "Estado")
	}));

	// Edition action
	static Field nombre = FieldBuilder.buildText("nombre", "Nombre");
	static Field apellidos = FieldBuilder.buildText("apellidos", "Apellidos");
	static Field direccion = FieldBuilder.buildText("direccion", "Dirección");
	static Form editionForm = NodeBuilder.buildForm("formulario", "Formulario", "", ListBuilder.buildNodeViewList(new FormView[]{
		ViewBuilder.buildFormView("001", "Información general", null, true, new Field[]{nombre, apellidos, direccion})
	}), new Field[]{ nombre, apellidos, direccion }, true);
	static EditionAction editionAction = TaskBuilder.buildEditionAction("Formulario de edición", editionForm);
	static Task editionTask = TaskBuilder.buildActivity("edition", "Tarea de edición", State.PENDING, TaskBuilder.buildWorkMap(editionAction), ListBuilder.buildTaskViewList(new TaskView[]{
		ViewBuilder.buildTaskStateView("state", "Estado")
	}));

	// Wait action
	static WaitAction waitAction = TaskBuilder.buildWaitAction("Configurando tiempo de espera", new Date());
	static Task waitTask = TaskBuilder.buildActivity("wait", "Tarea de espera", State.PENDING, TaskBuilder.buildWorkMap(waitAction), ListBuilder.buildTaskViewList(new TaskView[]{
		ViewBuilder.buildTaskStateView("state", "Estado")
	}));

	static Index indice = IndexBuilder.buildIndex("indice", new client.core.model.Index.Attribute[] {
		IndexBuilder.buildIndexAttribute("categoria", "Categoría"),
		IndexBuilder.buildIndexAttribute("objetoactuacion", "Objeto de actuación"),
		IndexBuilder.buildIndexAttribute("tiposincidencia", "Tipos de incidencia"),
		IndexBuilder.buildIndexAttribute("origen", "De parte"),
		IndexBuilder.buildIndexAttribute("unidaddestino", "Unidad destino"),
		IndexBuilder.buildIndexAttribute("fechanotificación", "Fecha de notificación"),
		IndexBuilder.buildIndexAttribute("prioridad", "Prioridad")
	});

	static Index indiceProveedores = IndexBuilder.buildIndex("proveedores", new client.core.model.Index.Attribute[] {
		IndexBuilder.buildIndexAttribute("categoria", "Categoría"),
		IndexBuilder.buildIndexAttribute("objetoactuacion", "Objeto de actuación"),
		IndexBuilder.buildIndexAttribute("tiposincidencia", "Tipos de incidencia"),
		IndexBuilder.buildIndexAttribute("origen", "De parte"),
		IndexBuilder.buildIndexAttribute("unidaddestino", "Unidad destino"),
		IndexBuilder.buildIndexAttribute("fechanotificación", "Fecha de notificación"),
		IndexBuilder.buildIndexAttribute("prioridad", "Prioridad")
	});

	static Index indiceArchivoExpedientes = IndexBuilder.buildIndex("expedientes", new client.core.model.Index.Attribute[] {
		IndexBuilder.buildIndexAttribute("categoria", "Categoría"),
		IndexBuilder.buildIndexAttribute("objetoactuacion", "Objeto de actuación"),
		IndexBuilder.buildIndexAttribute("tiposincidencia", "Tipos de incidencia"),
		IndexBuilder.buildIndexAttribute("origen", "De parte"),
		IndexBuilder.buildIndexAttribute("unidaddestino", "Unidad destino"),
		IndexBuilder.buildIndexAttribute("fechanotificación", "Fecha de notificación"),
		IndexBuilder.buildIndexAttribute("prioridad", "Prioridad")
	});

	static List<Filter> filters = new MonetList<>(IndexBuilder.buildFilter("categoria", "Categoría"),
			IndexBuilder.buildFilter("lugar", "Lugar"));

	static List<Order> orders = new MonetList<>(IndexBuilder.buildOrder("fechacreacion", "Fecha de creación", Order.Mode.ASC),
			IndexBuilder.buildOrder("title", "Título", Order.Mode.ASC));

	// NODOS DE PRUEBA
	static Collection archivoExpedientes = NodeBuilder.buildCollection("archivoExpedientes", "Archivo de expedientes", "Expedientes de coordinación de trabajos", ListBuilder.buildNodeViewList(new CollectionView[]{
		ViewBuilder.buildCollectionView("001", "En ejecución", null, true),
		ViewBuilder.buildCollectionView("002", "Resueltos", null, true),
		ViewBuilder.buildCollectionView("003", "Cancelados", null, true),
		ViewBuilder.buildCollectionView("004", "Todos", null, true)
	}), indice, ListBuilder.buildCommandList(new Command[] {
		EntityBuilder.buildCommand("tramitar", "tramitar", false),
		EntityBuilder.buildGroupCommand("descargar", "descargar...", true, ListBuilder.buildCommandList(new Command[]{
			EntityBuilder.buildCommand("descargar-pdf", "en PDF", true),
			EntityBuilder.buildCommand("descargar-pdf", "en CSV", true)
		}))
	}), new MonetList<Ref>() {{ add(new client.core.system.definition.Ref("expcorr", "Expediente correctivo")); add(new client.core.system.definition.Ref("expprev", "Expediente preventivo")); }}, filters, orders, false);

	static Form informacionGeneral = NodeBuilder.buildForm("exp1InformacionGeneral", "Información General", "", true);
	static Collection encargos = NodeBuilder.buildCollection("exp1Encargos", "Encargos", "", true);
	static Container expediente1 = NodeBuilder.buildContainer("expediente1", "Expediente 1", "Expediente del sistema de información", ListBuilder.buildNodeViewList(new NodeView[]{
		ViewBuilder.buildFormView("001", "Información general", informacionGeneral, true, new Field[0]),
		ViewBuilder.buildCollectionView("002", "Encargos", encargos, true)
	}), false);

	static {
		expediente1.setAncestors(new MonetList() {{ add(archivoExpedientes); }});
		informacionGeneral.setAncestors(new MonetList() {{ add(expediente1); }});
		encargos.setAncestors(new MonetList() {{ add(expediente1); }});
	}

	static Collection archivoIncidencias = NodeBuilder.buildCollection("archivoIncidencias", "Archivo de incidencias", "Todas las incidencias que se han producido", false);
	static Collection objetosActuacion = NodeBuilder.buildCollection("objetosActuacion", "Objetos de actuación", "Conjunto de objetos de actuación", false);
	static Collection proveedores = NodeBuilder.buildCollection("proveedores", "Proveedores", "", ListBuilder.buildNodeViewList(new CollectionView[]{
		ViewBuilder.buildCollectionView("001", "Proveedores", null, true)
	}), indiceProveedores, ListBuilder.EmptyCommandList, ListBuilder.EmptyRefList, filters, orders, false);

	static Form proveedor1 = NodeBuilder.buildForm("proveedor1", "Proveedor 1", "", false);
	static Form proveedor2 = NodeBuilder.buildForm("proveedor2", "Proveedor 2", "", false);

	static {
		proveedor1.setAncestors(new MonetList() {{ add(proveedores); }});
		proveedor1.setOwner(proveedores);
		proveedor2.setAncestors(new MonetList() {{ add(proveedores); }});
		proveedor2.setOwner(proveedores);
	}

	static Collection piezas = NodeBuilder.buildCollection("piezas", "Piezas", "", false);
	static Form preferencias = NodeBuilder.buildForm("preferencias", "Preferencias", "", false);

	static Desktop entornoExterna = NodeBuilder.buildDesktop("entornoExterna", "Limpieza de playas", "", ListBuilder.buildNodeViewList(new NodeView[]{
		ViewBuilder.buildDesktopView("c001", "Gestión", null, true, new Entity[]{archivoExpedientes, archivoIncidencias, objetosActuacion}),
		ViewBuilder.buildDesktopView("c002", "Maestros", null, false, new Entity[]{proveedores, piezas, preferencias})
	}), ListBuilder.buildCommandList(new Command[] {
		EntityBuilder.buildCommand("tramitar", "tramitar", true),
		EntityBuilder.buildGroupCommand("herramientas", "Herramientas", true, ListBuilder.buildCommandList(new Command[] {
			EntityBuilder.buildGroupCommand("generarinforme", "Generar Informe", true, ListBuilder.buildCommandList(new Command[] {
				EntityBuilder.buildCommand("pdf", "PDF", true),
				EntityBuilder.buildCommand("csv", "CSV", true)
			}))
		})),
	}));

}
