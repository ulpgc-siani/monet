package client.setup.campos;

import client.core.*;
import client.core.model.*;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.IndexDefinition;
import client.core.model.definition.views.IndexViewDefinition;
import client.core.model.factory.EntityFactory;
import client.core.model.fields.*;
import client.core.model.types.Link;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.core.system.Document;
import client.core.system.MonetList;
import client.core.system.Report;
import client.core.system.types.TermCategory;
import client.core.system.types.SuperTerm;
import client.services.*;
import client.services.callback.*;
import client.setup.SetupApplication;
import cosmos.types.Date;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class CamposApplication extends SetupApplication {

	@Override
	protected String getTheme() {
		return "default";
	}

	@Override
	protected Node getRootNode() {
		return entornoCampos;
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
				space.setDictionary(new client.core.model.definition.Dictionary() {
					@Override
					public String getDefinitionLabel(String key) {
						return "añadir elemento";
					}

					@Override
					public String getDefinitionCode(String key) {
						return null;
					}

					@Override
					public List<String> getDefinitionCodes(List<Ref> refList) {
						return new MonetList<>();
					}

					@Override
					public int getDefinitionCount() {
						return 0;
					}
				});

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
						if (id.equals("entornoCampos")) callback.success(entornoCampos);
						if (id.equals("form")) callback.success(form);
						if (id.equals("419")) callback.success(document);
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
						callback.success(null);
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
						callback.success(null);
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
						TypeBuilder.buildTerm("001", "Limpieza"),
						TypeBuilder.buildTerm("002", "Vaciado"),
						TypeBuilder.buildTerm("003", "Reparación 1"),
						TypeBuilder.buildTerm("007", "Reparación 5"),
						TypeBuilder.buildTerm("006", "Reparación 6")
					});

					@Override
					public void searchFieldHistory(Field field, String dataStore, String filter, int start, int limit, TermListCallback callback) {
						if (field instanceof SelectField || field instanceof MultipleSelectField)
							callbackTerm(filter, callback);
						else
							callbackString(filter, callback);
					}

					private void callbackTerm(String filter, TermListCallback callback) {
						TermList callbackHistory = TypeBuilder.buildTermList();
						for (Term term : history) {
							if (term.getLabel().toLowerCase().startsWith(filter.toLowerCase()))
								callbackHistory.add(term);
						}
						callback.success(callbackHistory);
					}

					private void callbackString(String filter, TermListCallback callback) {
						TermList callbackHistory = TypeBuilder.buildTermList();
						if (filter == null) {
							callback.success(history);
							return;
						}
						for (Term term : history) {
							if (term.getLabel().toLowerCase().startsWith(filter.toLowerCase()))
								callbackHistory.add(term);
						}
						callback.success(callbackHistory);
					}

					@Override
					public void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback) {
						TermList list = new client.core.system.types.TermList();
						for (int i = start; i < limit; i++) {
							if (history.size() > i) list.add(history.get(i));
						}
						callback.success(list);
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
					}

					@Override
					public void saveUrgency(Task task, boolean value, VoidCallback callback) {

					}

					@Override
					public void saveOwner(Task task, User owner, String reason, VoidCallback callback) {

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
						callback.success(ListBuilder.buildBusinessUnitList(new BusinessUnit[] {
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
						new TermCategory("000", "Categoría") {{
							setTerms(TypeBuilder.buildTermList(new Term[]{
								TypeBuilder.buildTerm("001", "Limpieza"),
								TypeBuilder.buildTerm("002", "Vaciado"),
								new SuperTerm("000a", "SuperTerm") {{
									setTerms(TypeBuilder.buildTermList(new Term[]{
										TypeBuilder.buildTerm("003", "Reparación 1"),
										TypeBuilder.buildTerm("004", "Reparación 2"),
										TypeBuilder.buildTerm("005", "Reparación 3"),
										TypeBuilder.buildTerm("006", "Reparación 4")
									}));
								}}
							}));
						}},
						TypeBuilder.buildTerm("007", "Reparación 5"),
						TypeBuilder.buildTerm("008", "Reparación 6"),
						TypeBuilder.buildTerm("009", "Reparación 7"),
						TypeBuilder.buildTerm("010", "Reparación 8"),
						TypeBuilder.buildTerm("011", "Reparación 9"),
						TypeBuilder.buildTerm("012", "Reparación 10"),
						TypeBuilder.buildTerm("013", "Limpieza"),
						TypeBuilder.buildTerm("014", "Vaciado"),
						TypeBuilder.buildTerm("015", "Reparación 1"),
						TypeBuilder.buildTerm("016", "Reparación 2"),
						TypeBuilder.buildTerm("017", "Reparación 3"),
						TypeBuilder.buildTerm("018", "Reparación 4"),
						TypeBuilder.buildTerm("019", "Reparación 5"),
						TypeBuilder.buildTerm("020", "Reparación 6"),
						TypeBuilder.buildTerm("021", "Reparación 7"),
						TypeBuilder.buildTerm("022", "Reparación 8"),
						TypeBuilder.buildTerm("023", "Reparación 9"),
						TypeBuilder.buildTerm("024", "Reparación 10"),
						TypeBuilder.buildTerm("025", "Reparación 5"),
						TypeBuilder.buildTerm("026", "Reparación 6"),
						TypeBuilder.buildTerm("027", "Reparación 7"),
						TypeBuilder.buildTerm("028", "Reparación 8"),
						TypeBuilder.buildTerm("029", "Reparación 9"),
						TypeBuilder.buildTerm("030", "Reparación 10")
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
						if (limit == -1)
							limit = termList.size() - start;
						TermList terms = new client.core.system.types.TermList();
						terms.setTotalCount(30);
						for (int i = start; i < Math.min(start + limit, termList.size()); i++) {
							terms.add(termList.get(i));
						}
						callback.success(terms);
					}

					@Override
					public void searchTerms(Source source, Mode mode, final String condition, int start, int limit, String flatten, String depth, TermListCallback callback) {
						List<Term> terms = new MonetList<>();

						for (int i = start; i < start + limit; i++) {
							if (termList.get(i).getLabel().toLowerCase().startsWith(condition))
								terms.add(termList.get(i));
						}

						callback.success(TypeBuilder.buildTermList(terms.toArray(new Term[terms.size()])));
					}
				};
			}

			private IndexService createIndexService() {
				return new IndexService() {

					@Override
					public void open(String code, IndexCallback callback) {
					}

					@Override
					public void getEntries(Index index, Scope scope, List<Filter> filters, List<Order> orders, final int start, final int limit, NodeIndexEntriesCallback callback) {
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

					@Override
					public void getEntry(Index index, Scope scope, Node entryNode, NodeIndexEntryCallback callback) {
					}

					@Override
					public void searchEntries(Index index, Scope scope, String condition, List<Filter> filters, List<Order> orders, int start, int count, NodeIndexEntriesCallback callback) {

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
					public void getFilterOptions(Index index, IndexService.Scope scope, Filter filter, FilterOptionsCallback callback) {
						List<Filter.Option> options = new MonetList<>();

						if (filter.getName().equals("Categoría")) {
							options.add(IndexBuilder.buildFilterOption("limpieza", "Limpieza"));
							options.add(IndexBuilder.buildFilterOption("rotura", "Rotura"));
							options.add(IndexBuilder.buildFilterOption("extraccion", "Extracción"));
						} else {
							options.add(IndexBuilder.buildFilterOption("es", "España"));
							options.add(IndexBuilder.buildFilterOption("hl", "Holanda"));
							options.add(IndexBuilder.buildFilterOption("ch", "Chile"));
							options.add(IndexBuilder.buildFilterOption("au", "Australia"));
							options.add(IndexBuilder.buildFilterOption("po", "Portugal"));
							options.add(IndexBuilder.buildFilterOption("ar", "Argentina"));
							options.add(IndexBuilder.buildFilterOption("br", "Brazil"));
							options.add(IndexBuilder.buildFilterOption("fr", "Francia"));
							options.add(IndexBuilder.buildFilterOption("be", "Belgica"));
							options.add(IndexBuilder.buildFilterOption("en", "Inglaterra"));
							options.add(IndexBuilder.buildFilterOption("it", "Italia"));
							options.add(IndexBuilder.buildFilterOption("ek", "Eslovaquia"));
							options.add(IndexBuilder.buildFilterOption("rc", "República Checa"));
							options.add(IndexBuilder.buildFilterOption("po", "Polonia"));
							options.add(IndexBuilder.buildFilterOption("ru", "Rusia"));
						}
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

	static TextField textField = FieldBuilder.buildText("TextField", "Campo texto");
	static BooleanField booleanField = FieldBuilder.buildBoolean("BooleanField", "Campo booleano");
	static NumberField numberField = FieldBuilder.buildNumber("NumberField", "Campo numérico");
	static MultipleNumberField multipleNumberField = FieldBuilder.buildNumberMultiple("MultipleNumberField", "Campo numérico multiple");
	static SerialField serialField = FieldBuilder.buildSerial("SerialField", "Campo serial");
	static MultipleSerialField multipleSerialField = FieldBuilder.buildSerialMultiple("MultipleSerialField", "Campo serial multiple");
	static UriField uriField = FieldBuilder.buildUri("UriField", "Campo uri");
	static MemoField memoField = FieldBuilder.buildMemo("MemoField", "Campo memo");
	static MultipleMemoField multipleMemoField = FieldBuilder.buildMemoMultiple("MultipleMemoField", "Campo memo multiple");
	static MultipleTextField multipleTextField = FieldBuilder.buildTextMultiple("MultipleTextField", "Campo texto multiple", new MonetList<TextField>() {{
		add(FieldBuilder.buildText("0", "Campo texto", "Mario"));
		add(FieldBuilder.buildText("1", "Campo texto", "Caballero"));
		add(FieldBuilder.buildText("2", "Campo texto", "Ramírez"));
	}});
	static FileField fileField = FieldBuilder.buildFile("FileField", "Campo file");
	static MultipleFileField multipleFileField = FieldBuilder.buildFileMultiple("MultipleFileField", "Campo file");
	static PictureField pictureField = FieldBuilder.buildPicture("PictureField", "Campo picture");
	static MultiplePictureField multiplePictureField = FieldBuilder.buildPictureMultiple("MultiplePictureField", "Campo picture");
	//static MemoField richMemoField = FieldBuilder.buildMemo("RichMemoField", "Campo rich memo"),
	static CheckField checkField = FieldBuilder.buildCheck("CheckField", "Campo check");
	static DateField dateField = FieldBuilder.buildDate("DateField", "Campo fecha");
	static MultipleDateField multipleDateField = FieldBuilder.buildDateMultiple("MultipleDateField", "Campo fecha multiple");
	static SelectField selectField = FieldBuilder.buildSelect("SelectField", "Campo select");
	static MultipleSelectField multipleSelectField = FieldBuilder.buildSelectMultiple("MultipleSelectField", "Campo select multiple");
	static SelectField selectFieldEmbedded = FieldBuilder.buildSelectEmbedded("SelectFieldEmbedded", "Campo select embebido");
	static LinkField linkField = FieldBuilder.buildLink("LinkField", "Campo link");
	static MultipleLinkField multipleLinkField = FieldBuilder.buildLinkMultiple("MultipleLinkField", "Campo link multiple");
	static CompositeField compositeField = FieldBuilder.buildComposite("CompositeField", "Campo composite", TypeBuilder.buildComposite(new Field[] {
			FieldBuilder.buildText("TextField", "Campo texto"),
			FieldBuilder.buildBoolean("BooleanField", "Campo booleano"),
			FieldBuilder.buildNumber("NumberField", "Campo numerico")
	}));
	static MultipleCompositeField summaryCompositeField = FieldBuilder.buildCompositeMultipleSummary("SummaryCompositeField", "Campo composite multiple sumario");
	static MultipleCompositeField tableCompositeField = FieldBuilder.buildCompositeMultipleTable("TableCompositeField", "Campo composite multiple tabla");

	static BooleanField booleanPoll = FieldBuilder.buildBooleanPoll("BooleanFieldPoll", "Campo booleano poll");
	static SelectField selectPoll = FieldBuilder.buildSelectPoll("SelectFieldPoll", "Campo select poll");
	static MultipleSelectField multipleSelectPoll = FieldBuilder.buildSelectMultiplePoll("MultipleSelectFieldPoll", "Campo select multiple poll");

	static Form form = NodeBuilder.buildForm("form", "Formulario con todos los campos posibles", "", ListBuilder.buildNodeViewList(new NodeView[]{
		ViewBuilder.buildFormView("c001", "Text", null, true, new Field[]{
			textField, booleanField, numberField, multipleNumberField, multipleTextField, serialField, multipleSerialField, uriField, compositeField, summaryCompositeField, tableCompositeField
		}),
		ViewBuilder.buildFormView("c002", "File", null, false, new Field[]{
			fileField, multipleFileField, multiplePictureField, pictureField
		}),
		ViewBuilder.buildFormView("c003", "Memo", null, false, new Field[]{
			memoField, multipleMemoField
		}),
		ViewBuilder.buildFormView("c004", "Date", null, false, new Field[]{
			dateField, multipleDateField
		}),
		ViewBuilder.buildFormView("c005", "Select", null, false, new Field[]{
			selectField, multipleSelectField, selectFieldEmbedded, checkField
		}),
		ViewBuilder.buildFormView("c006", "Link", null, false, new Field[]{
			linkField, multipleLinkField
		}),
		ViewBuilder.buildFormView("c007", "Poll", null, false, new Field[]{
			booleanPoll, selectPoll, multipleSelectPoll
		}),
		ViewBuilder.buildFormViewLayout("c008", "Layout", null, false, new Field[]{
			pictureField, textField, serialField, textField, compositeField, multipleSelectField, dateField, linkField
		}),

	}), new Field[]{
		textField, booleanField, numberField, multipleNumberField, serialField, multipleSerialField, uriField, memoField, multipleMemoField, multipleTextField,
		fileField, multipleFileField, pictureField, multiplePictureField, checkField, dateField, multipleDateField, selectField, multipleSelectField,
		selectFieldEmbedded, linkField, multipleLinkField, compositeField, summaryCompositeField, tableCompositeField, booleanPoll, selectPoll, multipleSelectPoll
	}, true);

	static Document document = NodeBuilder.buildDocument("419", "label", "description", ListBuilder.buildNodeViewList(new NodeView[]{
		ViewBuilder.buildDocumentView("c010", "Documento", null, false)
	}));

	static Container entornoCampos = NodeBuilder.buildContainer("entornoCampos", "Campos", "", ListBuilder.buildNodeViewList(new NodeView[]{
		ViewBuilder.buildContainerView("c1", "Text", form.getView("c001"), null, true),
		ViewBuilder.buildContainerView("c2", "File", form.getView("c002"), null, false),
		ViewBuilder.buildContainerView("c3", "Memo", form.getView("c003"), null, false),
		ViewBuilder.buildContainerView("c4", "Date", form.getView("c004"), null, false),
		ViewBuilder.buildContainerView("c5", "Select", form.getView("c005"), null, false),
		ViewBuilder.buildContainerView("c6", "Link", form.getView("c006"), null, false),
		ViewBuilder.buildContainerView("c7", "Poll", form.getView("c007"), null, false),
		ViewBuilder.buildContainerView("c8", "Layout", form.getView("c008"), null, false),
		ViewBuilder.buildContainerView("c9", "MiCV", form.getView("c009"), null, false),
		ViewBuilder.buildContainerView("c10", "Documento", document.getView("c010"), null, false)
	}), true);
}
