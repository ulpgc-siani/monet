package client.setup.micv;

import client.core.*;
import client.core.model.*;
import client.core.model.definition.Definition;
import client.core.model.definition.Dictionary;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.IndexDefinition;
import client.core.model.definition.views.IndexViewDefinition;
import client.core.model.definition.views.ViewDefinition;
import client.core.model.factory.EntityFactory;
import client.core.model.fields.*;
import client.core.model.types.Link;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.core.system.MonetList;
import client.core.system.Report;
import client.services.*;
import client.services.callback.*;
import client.services.http.HttpInstance;
import client.services.http.builders.SpaceBuilder;
import client.setup.SetupApplication;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.user.client.Timer;
import cosmos.types.Date;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MiCVApplication extends SetupApplication {

	@Override
	protected String getTheme() {
		return "micv";
	}

	@Override
	protected Node getRootNode() {
		return entornoCurriculumVitae;
	}

	@Override
	protected List<Space.Tab> getTabs() {
		java.util.List<Space.Tab> result = new MonetList<>();
		Node rootNode = getRootNode();

		for (NodeView view : (ViewList<NodeView>) rootNode.getViews())
			result.add(createTab(rootNode, view.getLabel(), Space.Tab.Type.ENVIRONMENT, view, view.isDefault()));

		return new MonetList(result.toArray(new Space.Tab[result.size()]));
	}

	@Override
	protected Dictionary getDictionary() {
		List<Definition> definitions = new MonetList<>(new Definition[]{
			new client.core.system.definition.Definition("ic00", "ic00", "Situación profesional actual", "Situación profesional actual"),
			new client.core.system.definition.Definition("ic01", "ic01", "Cargos y actividades desempeñadas con anterioridad", "Cargos y actividades desempeñadas con anterioridad"),
			new client.core.system.definition.Definition("ic02", "ic02", "Diplomaturas, licenciaturas e ingenierías, grados y másteres", "Diplomaturas, licenciaturas e ingenierías, grados y másteres"),
			new client.core.system.definition.Definition("ic03", "ic03", "Doctorados", "Doctorados"),
			new client.core.system.definition.Definition("ic04", "ic04", "Conocimiento de idiomas", "Conocimiento de idiomas"),
			new client.core.system.definition.Definition("ic05", "ic05", "Dirección de tesis doctorales y/o proyectos fin de carrera", "Dirección de tesis doctorales y/o proyectos fin de carrera"),
			new client.core.system.definition.Definition("ic06", "ic06", "Participación en proyectos de I+D+i financiados en convocatorias competitivas de Administraciones o entidades públicas y privadas", "Participación en proyectos de I+D+i financiados en convocatorias competitivas de Administraciones o entidades públicas y privadas"),
			new client.core.system.definition.Definition("ic07", "ic07", "Participación en contratos, convenios o proyectos de I+D+i no competitivos con Administraciones o entidades públicas o privadas", "Participación en contratos, convenios o proyectos de I+D+i no competitivos con Administraciones o entidades públicas o privadas"),
			new client.core.system.definition.Definition("ic08", "ic08", "Propiedad intelectual e industrial. Know-how y secretos industriales", "Propiedad intelectual e industrial. Know-how y secretos industriales"),
			new client.core.system.definition.Definition("ic09", "ic09", "Publicaciones, documentos científicos y técnicos", "Publicaciones, documentos científicos y técnicos"),
			new client.core.system.definition.Definition("ic10", "ic10", "Trabajos presentados en congresos nacionales o internacionales\n", "Trabajos presentados en congresos nacionales o internacionales\n"),
			new client.core.system.definition.Definition("ic12", "ic12", "Comités científicos asesores, sociedades científicas\n", "Comités científicos asesores, sociedades científicas\n"),
			new client.core.system.definition.Definition("ic13", "ic13", "Experiencia en organización de actividades de I+D+i\n", "Experiencia en organización de actividades de I+D+i\n"),
			new client.core.system.definition.Definition("ic14", "ic14", "Experiencia de gestión de I+D+i", "Experiencia de gestión de I+D+i"),
			new client.core.system.definition.Definition("ic15", "ic15", "Estancias en centros de I+D+i públicos o privados", "Estancias en centros de I+D+i públicos o privados"),
			new client.core.system.definition.Definition("ic16", "ic16", "Actividad sanitaria en instituciones de la UE", "Actividad sanitaria en instituciones de la UE"),
			new client.core.system.definition.Definition("ic17", "ic17", "Actividad sanitaria en la OMS", "Actividad sanitaria en la OMS"),
			new client.core.system.definition.Definition("ic18", "ic18", "Actividad sanitaria en otros organismos internacionales", "Actividad sanitaria en otros organismos internacionales"),
			new client.core.system.definition.Definition("ic19", "ic19", "Actividad en cooperación practicando la atención de salud en países en desarrollo", "Actividad en cooperación practicando la atención de salud en países en desarrollo"),
			new client.core.system.definition.Definition("ic20", "ic20", "Actividad en cooperación sanitaria en países en desarrollo", "Actividad en cooperación sanitaria en países en desarrollo"),
			new client.core.system.definition.Definition("ic21", "ic21", "Actividad de atención de salud en otros países extranjeros\n", "Actividad de atención de salud en otros países extranjeros\n"),
			new client.core.system.definition.Definition("ic22", "ic22", "Actividad sanitaria en países extranjeros", "Actividad sanitaria en países extranjeros"),
			new client.core.system.definition.Definition("ic23", "ic23", "Tutorías/supervisión de actividades de atención de salud", "Tutorías/supervisión de actividades de atención de salud"),
			new client.core.system.definition.Definition("ic24", "ic24", "Cursos y seminarios impartidos orientados a la mejora de la atención de salud para profesionales sanitarios", "Cursos y seminarios impartidos orientados a la mejora de la atención de salud para profesionales sanitarios"),
			new client.core.system.definition.Definition("ic25", "ic25", "Elaboración de protocolos y otros materiales de atención de salud", "Elaboración de protocolos y otros materiales de atención de salud"),
			new client.core.system.definition.Definition("ic26", "ic26", "Participación en proyectos de innovación sanitaria", "Participación en proyectos de innovación sanitaria"),
			new client.core.system.definition.Definition("ic27", "ic27", "Participación en proyectos para la planificación/mejora de la sanidad", "Participación en proyectos para la planificación/mejora de la sanidad"),
			new client.core.system.definition.Definition("ic28", "ic28", "Participación en congresos, cursos y seminarios orientados a la atención de salud", "Participación en congresos, cursos y seminarios orientados a la atención de salud"),
			new client.core.system.definition.Definition("ic29", "ic29", "Otras actividades/méritos no incluidos en la relación anterior", "Otras actividades/méritos no incluidos en la relación anterior")
		});

		return EntityBuilder.buildDictionary(definitions);
	}

	@Override
	protected Services createServices(String apiUrl) {

		return new Services() {
			private SpaceService spaceService;
			private NodeService nodeService;
			private TaskService taskService;
			private AccountService accountService;
			private SourceService sourceService;
			private IndexService indexService;
			private PushService pushService;

			@Override
			public SpaceService getSpaceService() {
				//return new client.services.NodeService(new Stub("http://localhost:8080/monet"));

				if (spaceService == null)
					spaceService = createSpaceService();

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

			private SpaceService createSpaceService() {
				final client.core.system.Space space = (client.core.system.Space) loadSpace();

				return new SpaceService() {
					@Override
					public void load(SpaceCallback callback) {
						callback.success(space);
					}

					@Override
					public void loadDefinition(String definitionKey, Instance.ClassName definitionClassName, DefinitionCallback<EntityDefinition> callback) {
						callback.success(new IndexDefinition() {
							@Override
							public ReferenceDefinition getReference() {
								return new ReferenceDefinition() {
									@Override
									public List<AttributeDefinition> getAttributes() {
										List<AttributeDefinition> attributes = new MonetList<>();
										attributes.add(new AttributeDefinition() {
											@Override
											public String getCode() {
												return "001";
											}

											@Override
											public String getName() {
												return "001";
											}

											@Override
											public String getLabel() {
												return "Header 1";
											}

											@Override
											public String getDescription() {
												return null;
											}

											@Override
											public Type getType() {
												return null;
											}

											@Override
											public boolean is(Type type) {
												return getType() == type;
											}

											@Override
											public Precision getPrecision() {
												return null;
											}

											@Override
											public Ref getSource() {
												return null;
											}
										});
										attributes.add(new AttributeDefinition() {
											@Override
											public String getCode() {
												return "002";
											}

											@Override
											public String getName() {
												return "002";
											}

											@Override
											public String getLabel() {
												return "Header 2";
											}

											@Override
											public String getDescription() {
												return null;
											}

											@Override
											public Type getType() {
												return Type.PICTURE;
											}

											@Override
											public boolean is(Type type) {
												return getType() == type;
											}

											@Override
											public Precision getPrecision() {
												return null;
											}

											@Override
											public Ref getSource() {
												return null;
											}
										});
										attributes.add(new AttributeDefinition() {
											@Override
											public String getCode() {
												return "003";
											}

											@Override
											public String getName() {
												return "003";
											}

											@Override
											public String getLabel() {
												return "Header 3";
											}

											@Override
											public String getDescription() {
												return null;
											}

											@Override
											public Type getType() {
												return null;
											}

											@Override
											public boolean is(Type type) {
												return getType() == type;
											}

											@Override
											public Precision getPrecision() {
												return null;
											}

											@Override
											public Ref getSource() {
												return null;
											}
										});
										return attributes;
									}

									@Override
									public AttributeDefinition getAttribute(String key) {
										for (AttributeDefinition definition : getAttributes()) {
											if (definition.getCode().equals(key)) return definition;
										}
										return null;
									}
								};
							}

							@Override
							public List<IndexViewDefinition> getViews() {
								MonetList<IndexViewDefinition> views = new MonetList<>();
								views.add(new IndexViewDefinition() {
									@Override
									public ShowDefinition getShow() {
										return null;
									}

									@Override
									public List<Ref> getAttributes() {
										List<Ref> refs = new MonetList<>();
										refs.add(new Ref() {
											@Override
											public String getValue() {
												return "001";
											}

											@Override
											public String getDefinition() {
												return "Def";
											}
										});
										refs.add(new Ref() {
											@Override
											public String getValue() {
												return "002";
											}

											@Override
											public String getDefinition() {
												return "Def";
											}
										});
										refs.add(new Ref() {
											@Override
											public String getValue() {
												return "003";
											}

											@Override
											public String getDefinition() {
												return "Def";
											}
										});
										return refs;
									}

									@Override
									public boolean isDefault() {
										return true;
									}

									@Override
									public Design getDesign() {
										return null;
									}

									@Override
									public String getCode() {
										return "001";
									}

									@Override
									public String getName() {
										return null;
									}

									@Override
									public String getLabel() {
										return null;
									}

									@Override
									public String getDescription() {
										return null;
									}

									@Override
									public Instance.ClassName getClassName() {
										return null;
									}
								});
								views.add(new IndexViewDefinition() {
									@Override
									public ShowDefinition getShow() {
										return null;
									}

									@Override
									public List<Ref> getAttributes() {
										return null;
									}

									@Override
									public boolean isDefault() {
										return false;
									}

									@Override
									public Design getDesign() {
										return null;
									}

									@Override
									public String getCode() {
										return "002";
									}

									@Override
									public String getName() {
										return null;
									}

									@Override
									public String getLabel() {
										return null;
									}

									@Override
									public String getDescription() {
										return null;
									}

									@Override
									public Instance.ClassName getClassName() {
										return null;
									}
								});
								views.add(new IndexViewDefinition() {
									@Override
									public ShowDefinition getShow() {
										return null;
									}

									@Override
									public List<Ref> getAttributes() {
										return null;
									}

									@Override
									public boolean isDefault() {
										return false;
									}

									@Override
									public Design getDesign() {
										return null;
									}

									@Override
									public String getCode() {
										return "003";
									}

									@Override
									public String getName() {
										return null;
									}

									@Override
									public String getLabel() {
										return null;
									}

									@Override
									public String getDescription() {
										return null;
									}

									@Override
									public Instance.ClassName getClassName() {
										return null;
									}
								});
								return views;
							}

							@Override
							public IndexViewDefinition getDefaultView() {
								for (IndexViewDefinition view : getViews()) {
									if (view.isDefault()) return view;
								}
								return null;
							}

							@Override
							public IndexViewDefinition getView(String key) {
								for (IndexViewDefinition definition : getViews()) {
									if (definition.getCode().equals(key)) return definition;
								}
								return null;
							}

							@Override
							public String getCode() {
								return null;
							}

							@Override
							public String getName() {
								return null;
							}

							@Override
							public String getLabel() {
								return null;
							}

							@Override
							public String getDescription() {
								return null;
							}

							@Override
							public Instance.ClassName getClassName() {
								return null;
							}
						});
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
						if (id.equals("entornoCurriculumVitae")) callback.success(entornoCurriculumVitae);
						else callback.success(nodes.get(id));
					}

					@Override
					public void getLabel(Node node, Callback<String> callback) {

					}

					@Override
					public void addField(Node node, MultipleField parent, Field field, int pos, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void deleteField(Node node, MultipleField parent, int pos, VoidCallback callback) {
						callback.success(null);
					}

					@Override
					public void changeFieldOrder(Node node, MultipleField parent, int previousPos, int newPos, VoidCallback callback) {

					}

					@Override
					public void clearField(Node node, MultipleField parent, VoidCallback callback) {
						callback.success(null);
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
						callback.success(nodes.get("1"));
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
						TypeBuilder.buildTerm("Hola","Hola"),
						TypeBuilder.buildTerm("Limpieza","Limpieza"),
						TypeBuilder.buildTerm("Vaciado","Vaciado"),
						TypeBuilder.buildTerm("Reparación 1","Reparación 1"),
						TypeBuilder.buildTerm("Reparación 2","Reparación 2"),
						TypeBuilder.buildTerm("Reparación 3","Reparación 3"),
						TypeBuilder.buildTerm("Reparación 4","Reparación 4"),
						TypeBuilder.buildTerm("Reparación 5","Reparación 5"),
						TypeBuilder.buildTerm("Reparación 6","Reparación 6")
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
						callback.success(TaskBuilder.buildActivity("activity", "Actividad"));
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
					}

					@Override
					public void loadDelegationRoles(Task task, RoleListCallback callback) {

					}

					@Override
					public void selectDelegationRole(Task task, Role role, TaskCallback callback) {

					}

					@Override
					public void setupDelegationOrder(Task task, TaskCallback callback) {

					}

					@Override
					public void solveLine(Task task, LineAction.Stop stop, TaskCallback callback) {

					}

					@Override
					public void solveEdition(Task task, TaskCallback callback) {

					}

					@Override
					public void setupWait(Task task, WaitAction.Scale scale, int value, TaskCallback callback) {

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

					@Override
					public void load(TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {
					}

					@Override
					public void search(String condition, TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {

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
						callback.success(ListBuilder.buildNotificationList(new Notification[] {
							EntityBuilder.buildNotification("Ha llegado una orden de trabajo", new Date()),
							EntityBuilder.buildNotification("Ha llegado un parte de trabajo", new Date()),
							EntityBuilder.buildNotification("Jose ha entregado el proyecto a tiempo", new Date())
						}));
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
		                /*TypeBuilder.buildTerm("006", "Reparación 4"),
                        TypeBuilder.buildTerm("007", "Reparación 5"),
                        TypeBuilder.buildTerm("008", "Reparación 6")*/
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

					private List<NodeIndexEntry> createSearchResults(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("1"), "English"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("7"), "European Accounting Strategy: adopting the International Standards"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("2"), "Ciencias empresariales"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("3"), "Profesor asociado a tiempo completo"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("11"), "Centro de Estudios de Literaturas y Civilizaciones del Rio de la Plata"));
						}}, start, count);
					}

					private List<NodeIndexEntry> createIndiceSituacionProfesional(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("1"), "English"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("2"), "Ciencias empresariales"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("3"), "Profesor asociado a tiempo completo"));
						}}, start, count);
					}

					private List<NodeIndexEntry> createIndiceFormacionAcademicaRecibida(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("4"), "Contabilidad de las organizaciones no lucrativas españolas"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("5"), "Contabilidad mecanizada"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("6"), "Elementos técnicos y cuentas anuales"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("7"), "European Accounting Strategy: adopting the International Standards"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("8"), "Islas Canarias: más que sol y playa"));
						}}, start, count);
					}

					private List<NodeIndexEntry> createIndiceActividadDocente(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
						}}, start, count);
					}

					private List<NodeIndexEntry> createIndiceExperienciaCientificaTecnologica(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("9"), "Reseñas en revistas científicas"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("10"), "Comisión de investigación sobre lenguas criollas"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("11"), "Centro de Estudios de Literaturas y Civilizaciones del Rio de la Plata"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("12"), "I Premio de Investigación otorgado por los Círculos Científicos Estudiantiles de la Universidad de Bucarest por el estudio: ¿Existen ideas erasmistas en Lazarillo de Tormes?"));
						}}, start, count);
					}

					private List<NodeIndexEntry> createIndiceActividadCientificaTecnologica(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("13"), "Sector de la construcción"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("14"), "Accounting guides for social economy organisations"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("15"), "Análisis económico-financiero de las empresas de la provincia de Las Palmas (1992-1994)"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("16"), "Análisis económico-financiero de las empresas de la provincia de Las Palmas (1996-1997)"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("17"), "Aspectos económicos y contables del voluntariado en las ONGs"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("18"), "Contaduría y Administración"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("19"), "Contabilidad financiera. Volumen I"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("20"), "Contabilidad financiera. Volumen I (2ª edición)"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("21"), "Contabilidad financiera. Volumen II"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("22"), "Contabilidad financiera. Volumen III"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("23"), "Resultado contable y mantenimiento del capital en el ámbito no lucrativo"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("24"), "Una reseña de la incidencia contable de la introducción del euro"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("25"), "¿Responde el urbanismo a los requerimientos de un desarrollo sostenible?"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("26"), "4th International Conference of the Iberoamerican Academy of Management"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("27"), "5th International Conference of the Iberoamerican Academy of Management"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("28"), "6th International Conference of the Iberoamerican Academy of Management"));
						}}, start, count);
					}

					private List<NodeIndexEntry> createIndiceActividadCampoSanidad(final int start, final int count) {
						return page(new MonetList<NodeIndexEntry>() {{
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("29"), "Actividad sanitaria en instituciones de la UE"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("30"), "Actividad sanitaria en la OMS"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("31"), "Actividad sanitaria en otros organismos internacionales"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("32"), "Actividad en cooperación practicando la atención de salud en países en desarrollo"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("33"), "Actividad en cooperación sanitaria en países en desarrollo"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("34"), "Actividad de atención de salud en otros países extranjeros"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("35"), "Actividad sanitaria en países extranjeros"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("36"), "Tutorías/supervisión de actividades de atención de salud"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("37"), "Cursos y seminarios impartidos orientados a la mejora de la atención de salud para profesionales sanitarios"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("38"), "Elaboración de protocolos y otros materiales de atención de salud"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("39"), "Participación en proyectos de innovación sanitaria"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("40"), "Participación en proyectos para la planificación/mejora de la sanidad"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("41"), "Participación en congresos, cursos y seminarios orientados a la atención de salud"));
							add(IndexBuilder.buildNodeIndexEntry(nodes.get("42"), "Otras actividades/méritos no incluidos en la relación anterior"));
						}}, start, count);
					}

					private List<NodeIndexEntry> page(java.util.List<NodeIndexEntry> entryList, int start, int count) {
						List<NodeIndexEntry> result = new MonetList<>();
						int current = 0;
						for (NodeIndexEntry entry : entryList) {
							if (current < count)
								result.add(entry);
							else
								return result;
							current++;
						}
						return result;
					}

					@Override
					public void open(String code, IndexCallback callback) {
						callback.success(indiceActividadCampoSanidad);
					}

					@Override
					public void getEntries(Index index, Scope scope, List<Filter> filters, List<Order> orders, final int start, final int limit, final NodeIndexEntriesCallback callback) {
						List<NodeIndexEntry> nodeIndexEntryList = new MonetList<>();

						if (index == null)
							nodeIndexEntryList = createSearchResults(start, limit);
						else if (index.getId().equals(indiceSituacionProfesional.getId()))
							nodeIndexEntryList = createIndiceSituacionProfesional(start, limit);
						else if (index.getId().equals(indiceFormacionAcademicaRecibida.getId()))
							nodeIndexEntryList = createIndiceFormacionAcademicaRecibida(start, limit);
						else if (index.getId().equals(indiceActividadDocente.getId()))
							nodeIndexEntryList = createIndiceActividadDocente(start, limit);
						else if (index.getId().equals(indiceExperienciaCientificaTecnologica.getId()))
							nodeIndexEntryList = createIndiceExperienciaCientificaTecnologica(start, limit);
						else if (index.getId().equals(indiceActividadCientificaTecnologica.getId()))
							nodeIndexEntryList = createIndiceActividadCientificaTecnologica(start, limit);
						else if (index.getId().equals(indiceActividadCampoSanidad.getId()))
							nodeIndexEntryList = createIndiceActividadCampoSanidad(start, limit);
						else {
							MonetList<NodeIndexEntry> list = new MonetList<>();
							for (int i = start; i < start + limit; i++) {
								final client.core.system.NodeIndexEntry.Attribute footer = new client.core.system.NodeIndexEntry.Attribute("Hola " + i);
								final int iValue = i;
								list.add(new client.core.system.NodeIndexEntry() {{
									setEntity(new Node() {
										@Override
										public Type getType() {
											return null;
										}

										@Override
										public String getLabel() {
											return null;
										}

										@Override
										public void setLabel(String label) {
										}

										@Override
										public Map<String, String> getNotes() {
											return null;
										}

										@Override
										public String getNote(String name) {
											return null;
										}

										@Override
										public void setNotes(Map notes) {

										}

										@Override
										public void addNote(String name, String value) {

										}

										@Override
										public List<Command> getCommands() {
											return null;
										}

										@Override
										public ViewList<NodeView> getViews() {
											return null;
										}

										@Override
										public NodeView getView(Key key) {
											return null;
										}

										@Override
										public NodeView getView(String key) {
											return null;
										}

										@Override
										public String getDefinitionClass() {
											return null;
										}

										@Override
										public boolean isContainer() {
											return false;
										}

										@Override
										public boolean isCollection() {
											return false;
										}

										@Override
										public boolean isCatalog() {
											return false;
										}

										@Override
										public boolean isSet() {
											return false;
										}

										@Override
										public boolean isForm() {
											return false;
										}

										@Override
										public boolean isDesktop() {
											return false;
										}

										@Override
										public boolean isDocument() {
											return false;
										}

										@Override
										public boolean isComponent() {
											return false;
										}

										@Override
										public boolean isEnvironment() {
											return false;
										}

										@Override
										public boolean isEditable() {
											return true;
										}

										@Override
										public boolean isLocked() {
											return false;
										}

										@Override
										public Link toLink() {
											return new client.core.system.types.Link("00" + iValue, "Link " + iValue);
										}

										@Override
										public String getId() {
											return null;
										}

										@Override
										public Key getKey() {
											return null;
										}

										@Override
										public String getDescription() {
											return null;
										}

										@Override
										public ClassName getClassName() {
											return null;
										}

										@Override
										public Activity getOwner() {
											return null;
										}

										@Override
										public void setOwner(Entity owner) {

										}

										@Override
										public List<Entity> getAncestors() {
											return null;
										}

										@Override
										public void setAncestors(List ancestors) {

										}

										@Override
										public EntityDefinition getDefinition() {
											return null;
										}

										@Override
										public void setDefinition(EntityDefinition definition) {

										}

										@Override
										public String[] search(String query) {
											return new String[0];
										}
									});
									setTitle(new client.core.system.NodeIndexEntry.Attribute("Hola " + Math.random()));
									MonetList<NodeIndexEntry.Attribute> footers = new MonetList<>();
									footers.add(footer);
									setFooters(footers);
									setIcon(new client.core.system.NodeIndexEntry.Attribute("/themes/default/images/addphoto.png"){{
										setDefinition(new IndexDefinition.ReferenceDefinition.AttributeDefinition() {
											@Override
											public String getCode() {
												return null;
											}

											@Override
											public String getName() {
												return null;
											}

											@Override
											public String getLabel() {
												return null;
											}

											@Override
											public String getDescription() {
												return null;
											}

											@Override
											public Type getType() {
												return Type.PICTURE;
											}

											@Override
											public boolean is(Type type) {
												return getType() == type;
											}

											@Override
											public Precision getPrecision() {
												return null;
											}

											@Override
											public Ref getSource() {
												return null;
											}
										});
									}});
								}});
							}
							for (final Order order : orders) {
								Collections.sort(list, new Comparator<NodeIndexEntry>() {
									@Override
									public int compare(NodeIndexEntry o1, NodeIndexEntry o2) {
										if (order.getMode() == Order.Mode.ASC)
											return o1.getTitle().getValue().compareTo(o2.getTitle().getValue());
										return o2.getTitle().getValue().compareTo(o1.getTitle().getValue());
									}
								});
							}
							list.setTotalCount(30);
							callback.success(list);
						}

						final List<NodeIndexEntry> finalNodeIndexEntryList = nodeIndexEntryList;

						new Timer() {
							@Override
							public void run() {
								callback.success(finalNodeIndexEntryList);
							}
						}.schedule(2000);
					}

					@Override
					public void getEntry(Index index, Scope scope, Node entryNode, NodeIndexEntryCallback callback) {
					}

					@Override
					public void searchEntries(Index index, Scope scope, String condition, List<Filter> filters, List<Order> orders, int start, int limit, NodeIndexEntriesCallback callback) {
						getEntries(index, scope, filters, orders, start, limit, callback);
					}

					@Override
					public void searchNodeEntries(Node container, String condition, int start, int limit, NodeIndexEntriesCallback callback) {
						getEntries(null, null, filters, orders, start, limit, callback);
					}

					@Override
					public void getHistory(Index index, Scope scope, String dataStore, int start, int limit, NodeIndexEntriesCallback callback) {

					}

					@Override
					public void searchHistory(Index index, Scope scope, String dataStore, String filter, int start, int limit, NodeIndexEntriesCallback callback) {

					}

					@Override
					public void getFilterOptions(Index index, Scope scope, Filter filter, FilterOptionsCallback callback) {
						List<Filter.Option> options;

						if (filter.getName().equals("Categoría"))
							options = new MonetList<Filter.Option>() {{
								add(IndexBuilder.buildFilterOption("limpieza", "Limpieza"));
								add(IndexBuilder.buildFilterOption("rotura", "Rotura"));
								add(IndexBuilder.buildFilterOption("extraccion", "Extracción"));
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
						Map<String, Integer> values = new HashMap<String, Integer>() {{
							put("ic00", 0); put("ic01", 1); put("ic02", 2); put("ic03", 3); put("ic04", 4); put("ic05", 5);
							put("ic06", 6); put("ic07", 7); put("ic08", 8); put("ic09", 9); put("ic10", 10); put("ic11", 11);
							put("ic12", 12); put("ic13", 13); put("ic14", 14); put("ic15", 15); put("ic16", 16); put("ic17", 17);
							put("ic18", 18); put("ic19", 19); put("ic20", 20); put("ic21", 21); put("ic22", 22); put("ic23", 23);
							put("ic24", 24); put("ic25", 25); put("ic26", 26); put("ic27", 27); put("ic28", 28); put("ic29", 29);
						}};
						callback.success(new Report(values));
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

	static Map<String, Node> nodes = new HashMap<>();

	private static void createForm(final String id, Node parent, final String title) {
		TextField F001 = FieldBuilder.buildText("F001", "Título");
		SelectField F002 = FieldBuilder.buildSelect("F002", "Categoría");
		SelectField F003 = FieldBuilder.buildSelect("F003", "Modalidad de contrato");
		SelectField F004 = FieldBuilder.buildSelect("F004", "Tipo de dedicación");
		SelectField F005 = FieldBuilder.buildSelect("F005", "Estado");

		BooleanField F006 = FieldBuilder.buildBoolean("F006", "Test boolean");
		NumberField F007 = FieldBuilder.buildNumber("F007", "Test number");
		CompositeField F008 = FieldBuilder.buildComposite("F008", "Test composite");
		DateField F009 = FieldBuilder.buildDate("F009", "Test date");
		FileField F010 = FieldBuilder.buildFile("F010", "Test file");
		MemoField F011 = FieldBuilder.buildMemo("F011", "Test memo");


		Form form = NodeBuilder.buildForm(id, title, "", ListBuilder.buildNodeViewList(new NodeView[]{
			ViewBuilder.buildFormView("001", "Vista por defecto", null, true, new Field[]{F001, F002, F003, F004, F005, F006, F007, F008, F009, F010, F011})
		}), new Field[]{F001, F002, F003, F004, F005, F006, F007, F008, F009, F010, F011}, false);
		form.setOwner(parent);

		nodes.put(id, form);
	}

	static TaskList taskList = EntityBuilder.buildTaskList("Lista de tareas", ListBuilder.buildTaskListViewList(new TaskListView[]{
		ViewBuilder.buildTaskListView("001", "todas", false, TaskList.Situation.ALL),
		ViewBuilder.buildTaskListView("002", "en ejecución", false, TaskList.Situation.ALIVE),
		ViewBuilder.buildTaskListView("003", "activas", true, TaskList.Situation.ACTIVE),
		ViewBuilder.buildTaskListView("004", "pendientes", false, TaskList.Situation.PENDING),
		ViewBuilder.buildTaskListView("005", "finalizadas", false, TaskList.Situation.FINISHED)
	}));

	static List<Filter> filters = new MonetList<>(
		IndexBuilder.buildFilter("tipo", "Tipo"),
		IndexBuilder.buildFilter("entidadfinanciadora", "Entidad financiadora")
	);

	static List<Order> orders = new MonetList<>(
		IndexBuilder.buildOrder("fechacreacion", "Fecha de creación", Order.Mode.ASC),
		IndexBuilder.buildOrder("title", "Título", Order.Mode.ASC)
	);

	static Index indiceSituacionProfesional = IndexBuilder.buildIndex("indiceSituacionProfesional", IndexBuilder.EmptyAttributes);
	static final Collection situacionProfesional = NodeBuilder.buildCollection("situacionProfesional", "Situación profesional", "", ListBuilder.buildNodeViewList(new NodeView[] {
			ViewBuilder.buildCollectionView("c001", "Situación profesional", null, false, ViewDefinition.Design.DOCUMENT)
		}),
		indiceSituacionProfesional, ListBuilder.EmptyCommandList, new MonetList<Ref>() {{
			add(new client.core.system.definition.Ref("ic00"));
			add(new client.core.system.definition.Ref("ic01"));
	}}, filters, orders, true);
	
	static Index indiceFormacionAcademicaRecibida = IndexBuilder.buildIndex("indiceFormacionAcademicaRecibida", IndexBuilder.EmptyAttributes);
	static final Collection formacionAcademicaRecibida = NodeBuilder.buildCollection("formacionAcademicaRecibida", "Formación académica recibida", "", ListBuilder.buildNodeViewList(new NodeView[] {
			ViewBuilder.buildCollectionView("c002", "Formación académica recibida", null, false, ViewDefinition.Design.DOCUMENT),
		}),
		indiceFormacionAcademicaRecibida, ListBuilder.EmptyCommandList, new MonetList<Ref>() {{
			add(new client.core.system.definition.Ref("ic02"));
			add(new client.core.system.definition.Ref("ic03"));
			add(new client.core.system.definition.Ref("ic04"));
	}}, filters, orders, true);
	
	static Index indiceActividadDocente = IndexBuilder.buildIndex("indiceActividadDocente", IndexBuilder.EmptyAttributes);
	static final Collection actividadDocente = NodeBuilder.buildCollection("actividadDocente", "Actividad docente", "", ListBuilder.buildNodeViewList(new NodeView[] {
			ViewBuilder.buildCollectionView("c003", "Actividad docente", null, false, ViewDefinition.Design.DOCUMENT),
		}),
		indiceActividadDocente, ListBuilder.EmptyCommandList, new MonetList<Ref>() {{
			add(new client.core.system.definition.Ref("ic05"));
	}}, filters, orders, true);

	static Index indiceExperienciaCientificaTecnologica = IndexBuilder.buildIndex("indiceExperienciaCientificaTecnologica", IndexBuilder.EmptyAttributes);
	static final Collection experienciaCientificaTecnologica = NodeBuilder.buildCollection("experienciaCientificaTecnologica", "Experiencia científica y tecnológica", "", ListBuilder.buildNodeViewList(new NodeView[] {
			ViewBuilder.buildCollectionView("c004", "Experiencia científica y tecnológica", null, false, ViewDefinition.Design.DOCUMENT)
		}),
		indiceExperienciaCientificaTecnologica, ListBuilder.EmptyCommandList, new MonetList<Ref>() {{
			add(new client.core.system.definition.Ref("ic06"));
			add(new client.core.system.definition.Ref("ic07"));
			add(new client.core.system.definition.Ref("ic08"));
	}}, filters, orders, true);

	static Index indiceActividadCientificaTecnologica = IndexBuilder.buildIndex("indiceActividadCientificaTecnologica", IndexBuilder.EmptyAttributes);
	static final Collection actividadCientificaTecnologica = NodeBuilder.buildCollection("actividadCientificaTecnologica", "Actividad científica y tecnológica", "", ListBuilder.buildNodeViewList(new NodeView[] {
			ViewBuilder.buildCollectionView("c005", "Actividad científica y tecnológica", null, false, ViewDefinition.Design.DOCUMENT)
		}),
		indiceActividadCientificaTecnologica, ListBuilder.EmptyCommandList, new MonetList<Ref>() {{
			add(new client.core.system.definition.Ref("ic09"));
			add(new client.core.system.definition.Ref("ic10"));
			add(new client.core.system.definition.Ref("ic12"));
			add(new client.core.system.definition.Ref("ic13"));
			add(new client.core.system.definition.Ref("ic14"));
			add(new client.core.system.definition.Ref("ic15"));
	}}, filters, orders, true);

	static Index indiceActividadCampoSanidad = IndexBuilder.buildIndex("indiceActividadCampoSanidad", IndexBuilder.EmptyAttributes);
	static final Collection actividadCampoSanidad = NodeBuilder.buildCollection("actividadCampoSanidad", "Actividad en el campo de la sanidad", "", ListBuilder.buildNodeViewList(new NodeView[]{
			ViewBuilder.buildCollectionView("c006", "Actividad en el campo de la sanidad", null, false, ViewDefinition.Design.DOCUMENT)
		}),
		indiceActividadCampoSanidad, ListBuilder.EmptyCommandList, new MonetList<Ref>() {{
			add(new client.core.system.definition.Ref("ic16"));
			add(new client.core.system.definition.Ref("ic17"));
			add(new client.core.system.definition.Ref("ic18"));
			add(new client.core.system.definition.Ref("ic19"));
			add(new client.core.system.definition.Ref("ic20"));
			add(new client.core.system.definition.Ref("ic21"));
			add(new client.core.system.definition.Ref("ic22"));
			add(new client.core.system.definition.Ref("ic23"));
			add(new client.core.system.definition.Ref("ic24"));
			add(new client.core.system.definition.Ref("ic25"));
			add(new client.core.system.definition.Ref("ic26"));
			add(new client.core.system.definition.Ref("ic27"));
			add(new client.core.system.definition.Ref("ic28"));
			add(new client.core.system.definition.Ref("ic29"));
	}}, filters, orders, true);

	static TextField datosIdentificacionContacto_Nombre = FieldBuilder.buildText("F001", "Nombre");
	static TextField datosIdentificacionContacto_Apellidos = FieldBuilder.buildText("F002", "Apellidos");
	static Form datosIdentificacionContacto = NodeBuilder.buildForm("datosIdentificacionContacto", "Datos de identificación y contacto", "", ListBuilder.buildNodeViewList(new NodeView[] {
		ViewBuilder.buildFormView("c000", "Datos de identificación y contacto", null, true, new Field[]{
			datosIdentificacionContacto_Nombre, datosIdentificacionContacto_Apellidos
		})
	}), new Field[] {
		datosIdentificacionContacto_Nombre, datosIdentificacionContacto_Apellidos
	}, true);

	client.core.model.Space space = new SpaceBuilder().build((HttpInstance) JsonUtils.safeEval("{\"name\":\"monet\",\"title\":\"MiCV\",\"subTitle\":\"Gestor de items curriculares\",\"language\":\"es\",\"theme\":\"micv\",\"instanceId\":\"40fa4721-6b79-4531-a5aa-506b763fa1f0\",\"federation\":{\"label\":\"English Text\",\"logoUrl\":\"http://127.0.0.1:8080/monet/explorer/api/model$file/?file=images/organization.logo.png\",\"url\":\"http://127.0.0.1:8080/federation\"},\"account\":{\"id\":\"36\",\"user\":{\"id\":\"36\",\"fullName\":\"usuario\",\"email\":\"usuario@mock.com\",\"photo\":\"\"},\"rootNode\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true},\"owner\":{\"id\":\"17\",\"type\":\"collection\",\"label\":\"Curriculums\",\"definition\":{\"code\":\"currimulums\",\"name\":\"micv.Curriculums\",\"type\":\"collection\"}}}},\"configuration\":{\"domain\":\"127.0.0.1\",\"url\":\"http://127.0.0.1:8080/monet/explorer\",\"apiUrl\":\"http://127.0.0.1:8080/monet/explorer/api\",\"pushUrl\":\"http://127.0.0.1:8080/monet/explorer/push\",\"analyticsUrl\":\"http://127.0.0.1:8080/monet/analytics\",\"fmsUrl\":\"http://127.0.0.1:8080/monet/servlet/fms\",\"digitalSignatureUrl\":\"http://127.0.0.1:8080/monet/explorer/digitalsignature\",\"imagesPath\":\"/monet/explorer/images\"},\"tabs\":[{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"mcfQD9g\",\"name\":\"Perfil\",\"type\":\"NODE_VIEW\",\"label\":\"Datos de indentificación y contacto\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"m_2mxtq\",\"type\":\"NODE_VIEW\",\"label\":\"Situación profesional\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"m_eabug\",\"type\":\"NODE_VIEW\",\"label\":\"Formación académica recibida\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"mscsktq\",\"type\":\"NODE_VIEW\",\"label\":\"Actividad docente\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"mdopcxq\",\"type\":\"NODE_VIEW\",\"label\":\"Experiencia científica y tecnológica\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"mwp7mag\",\"type\":\"NODE_VIEW\",\"label\":\"Actividad científica y tecnología\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"environment\":true}},\"view\":{\"code\":\"miomlna\",\"type\":\"NODE_VIEW\",\"label\":\"Actividad en el campo de la sanidad\"},\"type\":\"ENVIRONMENT\",\"active\":false}],\"dictionary\":{\"definitions\":{\"totalCount\":102,\"items\":[{\"code\":\"mbk_yva\",\"name\":\"micv.entidades.Ficha\",\"label\":\"Entidad\"},{\"code\":\"Icpadre\",\"name\":\"micv.formularios.Icpadre\",\"label\":\"Item curricular\"},{\"code\":\"ic.030.010\",\"name\":\"micv.formularios.Ic030010\",\"label\":\"Docencia impartida\"},{\"code\":\"mnwlavw\",\"name\":\"micv.documentos.Aciisi.Exportador\",\"label\":\"Exportador Aciisi\"},{\"code\":\"mhtytaa\",\"name\":\"micv.CatalogoEditoriales\",\"label\":\"Editoriales\"},{\"code\":\"mamth_a\",\"name\":\"micv.RoleInvestigador\",\"label\":\"Investigador\"},{\"code\":\"m1rsupg\",\"name\":\"micv.TesauroLugares\",\"label\":\"Lugares\"},{\"code\":\"mjoy1rg\",\"name\":\"micv.CatalogoEntidades.Index\",\"label\":\"Entidades\"},{\"code\":\"ic.060.010.030\",\"name\":\"micv.formularios.Ic060010030\",\"label\":\"Trabajo de divulgación\"},{\"code\":\"ic.060.020.010\",\"name\":\"micv.formularios.Ic060020010\",\"label\":\"Comités científicos\"},{\"code\":\"mfnof4a\",\"name\":\"micv.documentos.Fecyt\",\"label\":\"Fecyt\"},{\"code\":\"ic.060.030.030\",\"name\":\"micv.formularios.Ic060030030\",\"label\":\"Pertenencia a sociedades científicas y/o profesionales\"},{\"code\":\"ic.010.020\",\"name\":\"micv.formularios.Ic010020\",\"label\":\"Cargo y actividad desempeñada\"},{\"code\":\"mnttymw\",\"name\":\"micv.documentos.Fecyt.Exportador\",\"label\":\"Exportador Fecyt\"},{\"code\":\"mnr8nqq\",\"name\":\"micv.formularios.ICColeccion.Index\",\"label\":\"Items curriculares\"},{\"code\":\"ic.040.080\",\"name\":\"micv.formularios.Ic040080\",\"label\":\"Tutoría de atención salud\"},{\"code\":\"m5_p1gg\",\"name\":\"micv.formularios.ICColeccion\",\"label\":\"Tipos de incidencia\"},{\"code\":\"mljv8aa\",\"name\":\"micv.editoriales.Coleccion.Index\",\"label\":\"Editoriales\"},{\"code\":\"mx63isq\",\"name\":\"micv.Curriculums.Index\",\"label\":\"Curriculums\"},{\"code\":\"ic.030.090\",\"name\":\"micv.formularios.Ic030090\",\"label\":\"Ponencia de innovación docente\"},{\"code\":\"ic.040.090\",\"name\":\"micv.formularios.Ic040090\",\"label\":\"Curso y seminario impartido en el campo de la salud\"},{\"code\":\"ic.060.030.040\",\"name\":\"micv.formularios.Ic060030040\",\"label\":\"Pertenencia a redes\"},{\"code\":\"mqhcwba\",\"name\":\"micv.TesauroDoctorados\",\"label\":\"Doctorados\"},{\"code\":\"ic.040.010\",\"name\":\"micv.formularios.Ic040010\",\"label\":\"Experiencias en instituciones de la UE\"},{\"code\":\"myu7xaw\",\"name\":\"micv.documentos.Coleccion.Index\",\"label\":\"Entidades\"},{\"code\":\"ic.060.010.020\",\"name\":\"micv.formularios.Ic060010020\",\"label\":\"Congresos, Jornadas, Seminarios...\"},{\"code\":\"ic.060.020.040\",\"name\":\"micv.formularios.Ic060020040\",\"label\":\"Gestión de I+D+i\"},{\"code\":\"mfgs_fw\",\"name\":\"micv.TesauroPalsClave\",\"label\":\"Palabras clave\"},{\"code\":\"mq6ecog\",\"name\":\"micv.procesos.TestAciisi\",\"label\":\"Testeo para la Aciisi\"},{\"code\":\"ic.050.020.020\",\"name\":\"micv.formularios.Ic050020020\",\"label\":\"Proyectos I+D+i\"},{\"code\":\"ic.060.020.050\",\"name\":\"micv.formularios.Ic060020050\",\"label\":\"Representación nacional en foros\"},{\"code\":\"mgjgfbq\",\"name\":\"micv.documentos.Micinn.Exportador\",\"label\":\"Exportador Micinn\"},{\"code\":\"ic.030.030\",\"name\":\"micv.formularios.Ic030030\",\"label\":\"Programa de Formación en I+D Sanitaria, y/o I+D Postformación Sanitaria Especializada Impartida\"},{\"code\":\"ic.060.030.080\",\"name\":\"micv.formularios.Ic060030080\",\"label\":\"Premio innovación docente\"},{\"code\":\"m4qf1eg\",\"name\":\"micv.documentos.CargaDocente\",\"label\":\"Carga docente\"},{\"code\":\"mdnaszg\",\"name\":\"micv.TesauroPostGrados\",\"label\":\"PostGrados\"},{\"code\":\"mw3uwrg\",\"name\":\"micv.CatalogoEditoriales.Index\",\"label\":\"Editoriales\"},{\"code\":\"m8s43nw\",\"name\":\"micv.TesauroTitulaciones\",\"label\":\"Tipos de entidad\"},{\"code\":\"mhp_atg\",\"name\":\"micv.editoriales.Ficha\",\"label\":\"Editorial\"},{\"code\":\"ic.020.050\",\"name\":\"micv.formularios.Ic020050\",\"label\":\"Curso y seminario recibido cuyo objetivo sea la mejora de la docencia\"},{\"code\":\"ic.040.120\",\"name\":\"micv.formularios.Ic040120\",\"label\":\"Proyecto de planificación/mejora sanitaria\"},{\"code\":\"m8mf2pw\",\"name\":\"micv.TesauroTiposEntidad\",\"label\":\"Tipos de entidad\"},{\"code\":\"ic.040.020\",\"name\":\"micv.formularios.Ic040020\",\"label\":\"Experiencias en instituciones de la UE\"},{\"code\":\"mxbh4ng\",\"name\":\"micv.documentos.Aciisi\",\"label\":\"Documento Aciisi\"},{\"code\":\"ic.060.020.030\",\"name\":\"micv.formularios.Ic060020030\",\"label\":\"Organización actividad I+D+i\"},{\"code\":\"mxap5iq\",\"name\":\"micv.editoriales.Coleccion\",\"label\":\"Editoriales\"},{\"code\":\"mk8fkfq\",\"name\":\"micv.documentos.Micinn\",\"label\":\"Micinn\"},{\"code\":\"ic.030.070\",\"name\":\"micv.formularios.Ic030070\",\"label\":\"Elaboración de material docente\"},{\"code\":\"maja39g\",\"name\":\"micv.documentos.Excel\",\"label\":\"Excel\"},{\"code\":\"mktvluw\",\"name\":\"micv.procesos.TestFecyt\",\"label\":\"Testeo para la Fecyt\"},{\"code\":\"ic.060.030.010\",\"name\":\"micv.formularios.Ic060030010\",\"label\":\"Ayuda y Beca Obtenida\"},{\"code\":\"ic.060.010.010\",\"name\":\"micv.formularios.Ic060010010\",\"label\":\"Publicación Científica o Técnica\"},{\"code\":\"ic.040.130\",\"name\":\"micv.formularios.Ic040130\",\"label\":\"Ponencia de atención a la salud\"},{\"code\":\"ic.030.040\",\"name\":\"micv.formularios.Ic030040\",\"label\":\"Dirección de Tesis y Proyectos\"},{\"code\":\"currimulums\",\"name\":\"micv.Curriculums\",\"label\":\"Curriculums\"},{\"code\":\"mu_sfca\",\"name\":\"micv.entidades.Coleccion.Index\",\"label\":\"Entidades\"},{\"code\":\"ic.060.030.100\",\"name\":\"micv.formularios.Ic060030100\",\"label\":\"Compendio de otros méritos\"},{\"code\":\"ic.020.010.020\",\"name\":\"micv.formularios.Ic020010020\",\"label\":\"Otra formación universitaria\"},{\"code\":\"ic.000.010\",\"name\":\"micv.formularios.Ic000010\",\"label\":\"Información personal\"},{\"code\":\"ic.060.030.070\",\"name\":\"micv.formularios.Ic060030070\",\"label\":\"Tramos de investigación\"},{\"code\":\"ic.060.030.050\",\"name\":\"micv.formularios.Ic060030050\",\"label\":\"Título y premio obtenido\"},{\"code\":\"SeleccionarTipo\",\"name\":\"micv.SeleccionarTipo\",\"label\":\"Seleccion del sitio al que se desea enviar el curriculum\"},{\"code\":\"ic.020.060\",\"name\":\"micv.formularios.Ic020060\",\"label\":\"Conocimiento de idiomas\"},{\"code\":\"mzxdakg\",\"name\":\"micv.documentos.Curriculum\",\"label\":\"Curriculum\"},{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"label\":\"Curriculum Vitae\"},{\"code\":\"ic.030.080\",\"name\":\"micv.formularios.Ic030080\",\"label\":\"Proyecto de innovación docente\"},{\"code\":\"mguslja\",\"name\":\"micv.SeleccionarItems\",\"label\":\"Seleccion de items que participarán en la generación de su curriculum\"},{\"code\":\"ic.030.020\",\"name\":\"micv.formularios.Ic030020\",\"label\":\"Programa de Formación Sanitaria Especializada Impartida\"},{\"code\":\"mutrlra\",\"name\":\"micv.RoleAdministrador\",\"label\":\"Administrador\"},{\"code\":\"mqwqr0a\",\"name\":\"micv.documentos.Xml\",\"label\":\"Xml\"},{\"code\":\"ic.040.140\",\"name\":\"micv.formularios.Ic040140\",\"label\":\"Otras actividades/méritos no incluidos en el campo de la sanidad\"},{\"code\":\"mjcpzxg\",\"name\":\"micv.CatalogoEntidades\",\"label\":\"Entidades\"},{\"code\":\"ms7ti8a\",\"name\":\"micv.procesos.TestMicinn\",\"label\":\"Testeo para la Micinn\"},{\"code\":\"ic.040.110\",\"name\":\"micv.formularios.Ic040110\",\"label\":\"Proyecto de Innovación Sanitaria\"},{\"code\":\"miikw2g\",\"name\":\"micv.EscritorioAdministrador\",\"label\":\"Admin\"},{\"code\":\"ic.030.060\",\"name\":\"micv.formularios.Ic030060\",\"label\":\"Cursos y seminarios impartidos\"},{\"code\":\"mbvpdkw\",\"name\":\"micv.entidades.Coleccion\",\"label\":\"Entidades\"},{\"code\":\"ic.050.020.010\",\"name\":\"micv.formularios.Ic050020010\",\"label\":\"Proyectos I+D+i\"},{\"code\":\"ic.060.020.020\",\"name\":\"micv.formularios.Ic060020020\",\"label\":\"Otros modos de colaboración\"},{\"code\":\"ic.050.020.030\",\"name\":\"micv.formularios.Ic050020030\",\"label\":\"Obras artísticas dirigidas\"},{\"code\":\"ic.010.010\",\"name\":\"micv.formularios.Ic010010\",\"label\":\"Cargo y actividad desempeñada\"},{\"code\":\"ic.050.010\",\"name\":\"micv.formularios.Ic050010\",\"label\":\"Participación grupos I+D+i\"},{\"code\":\"ic.060.030.090\",\"name\":\"micv.formularios.Ic060030090\",\"label\":\"Reconocimiento de actividad docente\"},{\"code\":\"mwgwh5a\",\"name\":\"micv.TesauroItemsCurriculares\",\"label\":\"Items curriculares\"},{\"code\":\"mj_j3iq\",\"name\":\"micv.documentos.Cvn\",\"label\":\"Cvn\"},{\"code\":\"ic.020.020\",\"name\":\"micv.formularios.Ic020020\",\"label\":\"Formación distinta a la académica reglada y a la sanitaria\"},{\"code\":\"ic.040.100\",\"name\":\"micv.formularios.Ic040100\",\"label\":\"Elaboración de protocolos de atención a la salud\"},{\"code\":\"m60by_a\",\"name\":\"micv.TesauroCategoriaA\",\"label\":\"Categorías A\"},{\"code\":\"ic.060.030.020\",\"name\":\"micv.formularios.Ic060030020\",\"label\":\"Pertenencia a sociedades científicas o profesionales\"},{\"code\":\"ic.060.020.060\",\"name\":\"micv.formularios.Ic060020060\",\"label\":\"Experiencia evaluación I+D\"},{\"code\":\"ic.030.050\",\"name\":\"micv.formularios.Ic030050\",\"label\":\"Tutoría académica\"},{\"code\":\"ic.060.010.050\",\"name\":\"micv.formularios.Ic060010050\",\"label\":\"Estancia en centros I+D+i\"},{\"code\":\"ic.050.030.010\",\"name\":\"micv.formularios.Ic050030010\",\"label\":\"Propiedad intelectual e industrial\"},{\"code\":\"ic.020.010.010\",\"name\":\"micv.formularios.Ic020010010\",\"label\":\"Diplomaturas, licenciaturas e ingenierías, grados y másteres\"},{\"code\":\"mzdpp8g\",\"name\":\"micv.documentos.Coleccion\",\"label\":\"Documentos\"},{\"code\":\"ic.060.030.060\",\"name\":\"micv.formularios.Ic060030060\",\"label\":\"Carrera profesional\"},{\"code\":\"ic.030.100\",\"name\":\"micv.formularios.Ic030100\",\"label\":\"Otras actividades/méritos no incluidos en la actividad docente\"},{\"code\":\"mfekrpa\",\"name\":\"micv.TesauroProvincia\",\"label\":\"Provincias\"},{\"code\":\"ic.040.030\",\"name\":\"micv.formularios.Ic040030\",\"label\":\"Experiencias en instituciones de la UE\"},{\"code\":\"ic.020.030\",\"name\":\"micv.formularios.Ic020030\",\"label\":\"Programa de Formación Sanitaria Especializada Recibida\"},{\"code\":\"mfcqxja\",\"name\":\"micv.TesauroIdiomas\",\"label\":\"Idiomas\"},{\"code\":\"_i1ppeg\",\"name\":\"org.monet.metamodel.internal.TaskOrderDefinition\",\"label\":\"Configuración de un encargo\"}]}}}"));
	client.core.model.Space.Tab tab = space.getTabs().get(0);

	static Container entornoCurriculumVitae = NodeBuilder.buildContainer("entornoCurriculumVitae", "Francisco Javier Ramirez Ojeda", "Francisco Javier Ramirez Ojeda", ListBuilder.buildNodeViewList(new NodeView[]{
		ViewBuilder.buildContainerView("c0", "Datos de identificación y contacto", datosIdentificacionContacto.getView("c000"), null, true),
		ViewBuilder.buildContainerView("c1", "Situación profesional", situacionProfesional.getView("c001"), null, false),
		ViewBuilder.buildContainerView("c2", "Formación académica recibida", formacionAcademicaRecibida.getView("c002"), null, false),
		ViewBuilder.buildContainerView("c3", "Actividad docente", actividadDocente.getView("c003"), null, false),
		ViewBuilder.buildContainerView("c4", "Experiencia científica y tecnológica", experienciaCientificaTecnologica.getView("c004"), null, false),
		ViewBuilder.buildContainerView("c5", "Actividad científica y tecnológica", actividadCientificaTecnologica.getView("c005"), null, false),
		ViewBuilder.buildContainerView("c6", "Actividad en el campo de la sanidad", actividadCampoSanidad.getView("c006"), null, false)
	}), ListBuilder.buildCommandList(new Command[]{
		EntityBuilder.buildCommand("GenerarMiCV", "Generar CV", true),
		EntityBuilder.buildCommand("Sugerencias", "Sugerencias", true),
		EntityBuilder.buildCommand("Comprobar", "Comprobar", true)
	}), true);

	static {
		nodes.put("datosIdentificacionContacto", datosIdentificacionContacto);
		nodes.put("situacionProfesional", situacionProfesional);
		nodes.put("formacionAcademicaRecibida", formacionAcademicaRecibida);
		nodes.put("actividadDocente", actividadDocente);
		nodes.put("experienciaCientificaTecnologica", experienciaCientificaTecnologica);
		nodes.put("actividadCientificaTecnologica", actividadCientificaTecnologica);
		nodes.put("actividadCampoSanidad", actividadCampoSanidad);

		createForm("1", situacionProfesional, "English");
		createForm("2", situacionProfesional, "Ciencias Empresariales");
		createForm("3", situacionProfesional, "Profesor asociado a tiempo completo");
		createForm("4", formacionAcademicaRecibida, "Contabilidad de las organizaciones no lucrativas españolas");
		createForm("5", formacionAcademicaRecibida, "Contabilidad mecanizada");
		createForm("6", formacionAcademicaRecibida, "Elementos técnicos y cuentas anuales");
		createForm("7", formacionAcademicaRecibida, "European Accounting Strategy: adopting the International Standards");
		createForm("8", formacionAcademicaRecibida, "Islas Canarias: más que sol y playa");
		createForm("9", experienciaCientificaTecnologica, "Reseñas en revistas científicas");
		createForm("10", experienciaCientificaTecnologica, "Comisión de investigación sobre lenguas criollas");
		createForm("11", experienciaCientificaTecnologica, "Centro de Estudios de Literaturas y Civilizaciones del Rio de la Plata");
		createForm("12", experienciaCientificaTecnologica, "I Premio de Investigación otorgado por los Círculos Científicos Estudiantiles de la Universidad de Bucarest por el estudio: ¿Existen ideas erasmistas en Lazarillo de Tormes?");
		createForm("13", actividadCientificaTecnologica, "Sector de la construcción");
		createForm("14", actividadCientificaTecnologica, "Accounting guides for social economy organisations");
		createForm("15", actividadCientificaTecnologica, "Análisis económico-financiero de las empresas de la provincia de Las Palmas (1992-1994)");
		createForm("16", actividadCientificaTecnologica, "Análisis económico-financiero de las empresas de la provincia de Las Palmas (1996-1997)");
		createForm("17", actividadCientificaTecnologica, "Aspectos económicos y contables del voluntariado en las ONGs");
		createForm("18", actividadCientificaTecnologica, "Contaduría y Administración");
		createForm("19", actividadCientificaTecnologica, "Contabilidad financiera. Volumen I");
		createForm("20", actividadCientificaTecnologica, "Contabilidad financiera. Volumen I (2ª edición)");
		createForm("21", actividadCientificaTecnologica, "Contabilidad financiera. Volumen II");
		createForm("22", actividadCientificaTecnologica, "Contabilidad financiera. Volumen III");
		createForm("23", actividadCientificaTecnologica, "Resultado contable y mantenimiento del capital en el ámbito no lucrativo");
		createForm("24", actividadCientificaTecnologica, "Una reseña de la incidencia contable de la introducción del euro");
		createForm("25", actividadCientificaTecnologica, "¿Responde el urbanismo a los requerimientos de un desarrollo sostenible?");
		createForm("26", actividadCientificaTecnologica, "4th International Conference of the Iberoamerican Academy of Management");
		createForm("27", actividadCientificaTecnologica, "5th International Conference of the Iberoamerican Academy of Management");
		createForm("28", actividadCientificaTecnologica, "6th International Conference of the Iberoamerican Academy of Management");
		createForm("29", actividadCampoSanidad, "Actividad sanitaria en instituciones de la UE");
		createForm("30", actividadCampoSanidad, "Actividad sanitaria en la OMS");
		createForm("31", actividadCampoSanidad, "Actividad sanitaria en otros organismos internacionales");
		createForm("32", actividadCampoSanidad, "Actividad en cooperación practicando la atención de salud en países en desarrollo");
		createForm("33", actividadCampoSanidad, "Actividad en cooperación sanitaria en países en desarrollo");
		createForm("34", actividadCampoSanidad, "Actividad de atención de salud en otros países extranjeros");
		createForm("35", actividadCampoSanidad, "Actividad sanitaria en países extranjeros");
		createForm("36", actividadCampoSanidad, "Tutorías/supervisión de actividades de atención de salud");
		createForm("37", actividadCampoSanidad, "Cursos y seminarios impartidos orientados a la mejora de la atención de salud para profesionales sanitarios");
		createForm("38", actividadCampoSanidad, "Elaboración de protocolos y otros materiales de atención de salud");
		createForm("39", actividadCampoSanidad, "Participación en proyectos de innovación sanitaria");
		createForm("40", actividadCampoSanidad, "Participación en proyectos para la planificación/mejora de la sanidad");
		createForm("41", actividadCampoSanidad, "Participación en congresos, cursos y seminarios orientados a la atención de salud");
		createForm("42", actividadCampoSanidad, "Otras actividades/méritos no incluidos en la relación anterior");
	}

}
