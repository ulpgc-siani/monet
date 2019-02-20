package client.services;

import client.core.model.*;
import client.services.callback.*;

public interface NodeService extends Service {
	void open(String id, NodeCallback callback);
	void getLabel(Node node, Callback<String> callback);
	void add(Node parent, String code, NodeCallback callback);
	void delete(Node node, VoidCallback callback);
	void delete(Node[] nodes, VoidCallback callback);

	void saveNote(Node node, String name, String value, NoteCallback callback);
	void saveField(Node node, Field field, VoidCallback callback);
	void addField(Node node, MultipleField parent, Field field, int pos, VoidCallback callback);
	void deleteField(Node node, MultipleField parent, int pos, VoidCallback callback);
	void changeFieldOrder(Node node, MultipleField parent, int previousPos, int newPos, VoidCallback callback);
	void clearField(Node node, MultipleField parent, VoidCallback callback);
	void searchFieldHistory(Field field, String dataStore, String filter, int start, int limit, TermListCallback callback);
	void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback);
	void saveFile(String filename, String data, Node node, Callback<String> callback);
	void savePicture(String filename, String data, Node node, Callback<String> callback);

	void loadHelpPage(Node node, HtmlPageCallback callback);
	void executeCommand(Node node, Command command, NodeCommandCallback callback);

    void focusNodeField(Node node, Field field);
    void focusNodeView(Node node);
    void blurNodeField(Node node, Field field);
    void blurNodeView(Node node);

	String getNodePreviewBaseUrl(String documentId);
	String getDownloadNodeUrl(Node node);
	String getDownloadNodeImageUrl(Node node, String imageId);
	String getDownloadNodeThumbnailUrl(Node node, String imageId);
	String getDownloadNodeFileUrl(Node node, String fileId);
}
