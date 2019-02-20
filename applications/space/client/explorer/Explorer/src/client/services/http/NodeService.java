package client.services.http;

import client.core.adapters.NodeDefinitionAdapter;
import client.core.model.*;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.NodeDefinition;
import client.core.model.types.TermList;
import client.services.Services;
import client.services.callback.*;
import client.services.http.dialogs.node.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class NodeService extends HttpService implements client.services.NodeService {

	public NodeService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public void open(String id, final NodeCallback callback) {
		stub.request(new LoadNodeDialog(id), Node.CLASS_NAME, new NodeCallback() {
            @Override
            public void success(final Node node) {
                loadDefinition(node, nodeCallback(node, callback));
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        });
	}
	
	@Override
	public void getLabel(Node node, Callback<String> callback) {
		stub.requestString(new LoadNodeLabelDialog(node), callback);
	}

	@Override
	public void add(Node parent, String code, final NodeCallback callback) {
		stub.request(new AddNodeDialog(code, parent), Node.CLASS_NAME, new NodeCallback() {
            @Override
            public void success(Node node) {
                loadDefinition(node, callback);
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        });
	}

	@Override
	public void delete(Node node, VoidCallback callback) {
		stub.request(new DeleteNodeDialog(node), null, callback);
	}

	@Override
	public void delete(Node[] nodes, VoidCallback callback) {
		stub.request(new DeleteNodesDialog(nodes), null, callback);
	}

	@Override
	public void saveNote(Node node, final String name, final String value, final NoteCallback callback) {
		stub.request(new NoteDialog(node, name, value), null, new VoidCallback() {
			@Override
			public void success(Void result) {
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
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void saveField(Node node, Field field, VoidCallback callback) {
		stub.request(new SaveFieldDialog(node, field, services.getSpaceService().load()), null, callback);
	}

	@Override
	public void addField(Node node, MultipleField parent, Field field, int pos, VoidCallback callback) {
		stub.request(new AddFieldDialog(node, parent, field, pos, services.getSpaceService().load()), null, callback);
	}

	@Override
	public void deleteField(Node node, MultipleField parent, int pos, VoidCallback callback) {
		stub.request(new DeleteFieldDialog(node, parent, pos, services.getSpaceService().load()), null, callback);
	}

	@Override
	public void changeFieldOrder(Node node, MultipleField parent, int previousPos, int newPos, VoidCallback callback) {
		stub.request(new ChangeFieldOrderDialog(node, parent, previousPos, newPos), null, callback);
	}

	@Override
	public void clearField(Node node, MultipleField parent, VoidCallback callback) {
		stub.request(new ClearFieldDialog(node, parent), null, callback);
	}

	@Override
	public void searchFieldHistory(Field field, String dataStore, String condition, int start, int limit, final TermListCallback callback) {
		stub.request(new HistoryDialog(dataStore, condition, start, limit), TermList.CLASS_NAME, callback);
	}

	@Override
	public void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback) {
		stub.request(new HistoryDialog(dataStore, null, start, limit), TermList.CLASS_NAME, callback);
	}

	@Override
	public void saveFile(String filename, String data, Node node, Callback<String> callback) {
		saveFile(new UploadNodeFileDialog(node, filename, data), callback);
	}

	@Override
	public void savePicture(String filename, String data, Node node, Callback<String> callback) {
		saveFile(new UploadNodeImageDialog(node, filename, data), callback);
	}

	private void saveFile(UploadNodeFileDialog dialog, final Callback<String> callback) {
		stub.requestFile(dialog, new Callback<String>() {
            @Override
            public void success(String result) {
                JSONObject value = JSONParser.parseStrict(result).isObject();
                callback.success(value.get("id").isString().stringValue());
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        });
	}

	@Override
	public void loadHelpPage(Node node, HtmlPageCallback callback) {
		stub.request(new HelpPageDialog(node), HtmlPage.CLASS_NAME, callback);
	}

	@Override
	public void executeCommand(Node node, Command command, final NodeCommandCallback callback) {
		stub.request(new CommandDialog(node, command), Space.Action.CLASS_NAME, callback);
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
	public String getNodePreviewBaseUrl(String nodeId) {
		return stub.getRequestUrl(new PreviewNodeDialog(nodeId));
	}

	@Override
	public String getDownloadNodeUrl(Node node) {
		return stub.getRequestUrl(new DownloadNodeDialog(node));
	}

	@Override
	public String getDownloadNodeImageUrl(client.core.model.Node node, String imageId) {
		return stub.getRequestUrl(new DownloadNodeImageDialog(node, imageId));
	}

	@Override
	public String getDownloadNodeThumbnailUrl(client.core.model.Node node, String imageId) {
		return stub.getRequestUrl(new DownloadNodeThumbnailDialog(node, imageId));
	}

	@Override
	public String getDownloadNodeFileUrl(client.core.model.Node node, String fileId) {
		return stub.getRequestUrl(new DownloadNodeFileDialog(node, fileId));
	}

	private void loadDefinition(final Node node, final Callback callback) {
		services.getSpaceService().loadDefinition(node, new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition definition) {
				new NodeDefinitionAdapter(services.getTranslatorService()).adapt(node, (NodeDefinition) definition);
				callback.success(node);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	private Callback nodeCallback(final Node node, final NodeCallback callback) {
		return new Callback() {
			@Override
			public void success(Object object) {

				if (!node.isSet())
					callback.success(node);
				else
					loadIndexDefinition((Set) node, callback);
			}


			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		};
	}

	private void loadIndexDefinition(final Set node, final NodeCallback callback) {
		services.getIndexService().open(node.getIndex().getId(), new IndexCallback() {
			@Override
			public void success(Index object) {
				node.setIndex(object);
				callback.success((Node) node);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

    private Callback<Object> ignoredCallback() {
        return new Callback<Object>() {
            @Override
            public void success(Object object) {
            }

            @Override
            public void failure(String error) {
            }
        };
    }
}
