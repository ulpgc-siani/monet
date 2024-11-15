package org.monet.api.space.backservice.impl;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.helpers.PersisterHelper;
import org.monet.api.space.backservice.impl.helpers.StreamHelper;
import org.monet.api.space.backservice.impl.library.*;
import org.monet.api.space.backservice.impl.model.*;
import org.monet.api.space.backservice.impl.model.workmap.Process;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class BackserviceApiImpl implements BackserviceApi {
	private String location;
	private byte[] certificate;
	private String certificatePassword;

	private final String PARAMETER_SEPARATOR = "#__#";

	public BackserviceApiImpl(String location, String certificateFilename, String certificatePassword) throws FileNotFoundException {
        this(location, new FileInputStream(certificateFilename), certificatePassword);
	}

    public BackserviceApiImpl(String location, InputStream certificate, String certificatePassword) {
	    ByteArrayOutputStream outputStream = null;
	    
	    try {
		    outputStream = new ByteArrayOutputStream();
		    StreamHelper.copyData(certificate, outputStream);

		    this.location = location;
		    this.certificate = outputStream.toByteArray();
		    this.certificatePassword = certificatePassword;
	    }
	    catch (Exception exception) {
		    exception.printStackTrace();
	    }
	    finally {
		    StreamHelper.close(certificate);
		    StreamHelper.close(outputStream);
	    }
	}

	@Override
	public boolean addDatastoreCubeFact(String datastore, String cube, CubeFact fact) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("adddatastorecubefact"));
			parameters.put("name", toStringBody(datastore));
			parameters.put("cube", toStringBody(cube));
			parameters.put("data", toStringBody(encode(fact.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addDatastoreCubeFacts(String datastore, String cube, CubeFactList factList) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("adddatastorecubefacts"));
			parameters.put("name", toStringBody(datastore));
			parameters.put("cube", toStringBody(cube));
			parameters.put("data", toStringBody(encode(factList.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addDatastoreDimensionComponent(String datastore, String dimension, DimensionComponent component) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("adddatastoredimensioncomponent"));
			parameters.put("name", toStringBody(datastore));
			parameters.put("dimension", toStringBody(dimension));
			parameters.put("data", toStringBody(encode(component.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addNodeFlag(String nodeId, String name, String value) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("addnodeflag"));
			parameters.put("id", toStringBody(nodeId));
			parameters.put("name", toStringBody(encode(name)));
			parameters.put("value", toStringBody(encode(value)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addNodeNote(String id, String name, String value) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("addnodenote"));
			parameters.put("id", toStringBody(id));
			parameters.put("name", toStringBody(encode(name)));
			parameters.put("value", toStringBody(encode(value)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addSourceTerm(String sourceId, String code, String label, int type, String parentCode, LinkedHashSet<String> tags) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("addsourceterm"));
			parameters.put("source", toStringBody(sourceId));
			parameters.put("code", toStringBody(code));
			parameters.put("label", toStringBody(encode(label)));
			parameters.put("type", toStringBody(String.valueOf(type)));

			if (parentCode != null)
				parameters.put("parent", toStringBody(parentCode));

			StringBuilder tagsBuilder = new StringBuilder();
			for (String tag : tags) {
				if (tagsBuilder.length() > 0)
					tagsBuilder.append(PARAMETER_SEPARATOR);
				tagsBuilder.append(tag);
			}
			parameters.put("tags", toStringBody(encode(tagsBuilder.toString())));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addTaskFact(String id, String title, String subTitle, String userId, MonetLink[] links) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("addtaskfact"));
			parameters.put("id", toStringBody(id));
			parameters.put("title", toStringBody(encode(title)));
			parameters.put("subtitle", toStringBody(encode(subTitle)));
			parameters.put("userId", toStringBody(userId));

			StringBuilder linksBuilder = new StringBuilder();
			for (MonetLink link : links) {
				if (linksBuilder.length() > 0)
					linksBuilder.append(PARAMETER_SEPARATOR);
				linksBuilder.append(link.toString());
			}
			parameters.put("links", toStringBody(encode(linksBuilder.toString())));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addTaskFlag(String taskId, String name, String value) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("addtaskflag"));
			parameters.put("id", toStringBody(taskId));
			parameters.put("name", toStringBody(encode(name)));
			parameters.put("value", toStringBody(encode(value)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addTaskShortCut(String taskId, String name, String nodeId) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("addtaskshortcut"));
			parameters.put("id", toStringBody(taskId));
			parameters.put("name", toStringBody(encode(name)));
			parameters.put("value", toStringBody(nodeId));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean consolidateNode(String nodeId) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("consolidatenode"));
			parameters.put("id", toStringBody(nodeId));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public Account createAccount(String code, String fullname, String email, String[] roles) {
		Account account = new Account();

		HashMap<String, ContentBody> parameters = new HashMap<>();
		try {
			parameters.put("op", toStringBody("createaccount"));
			parameters.put("code", toStringBody(code));
			parameters.put("fullname", toStringBody(encode(fullname)));
			parameters.put("email", toStringBody(encode(email)));
			parameters.put("roles", toStringBody(encode(LibraryArray.implode(roles, ","))));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			account.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return account;
	}

	@Override
	public Node createNode(String type, String parentId) {
		Node node = new Node();

		HashMap<String, ContentBody> parameters = new HashMap<>();
		try {
			parameters.put("op", toStringBody("createnode"));
			parameters.put("type", toStringBody(type));

			if (parentId != null)
				parameters.put("parent", toStringBody(parentId));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			node.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public Task createTask(String type) {
		Task task = new Task();

		HashMap<String, ContentBody> parameters = new HashMap<>();
		try {
			parameters.put("op", toStringBody("createtask"));
			parameters.put("type", toStringBody(type));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			task.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return task;
	}

	@Override
	public boolean deleteNodeFlag(String nodeId, String name) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("deletenodeflag"));
			parameters.put("id", toStringBody(nodeId));
			parameters.put("name", toStringBody(encode(name)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean deleteNodeNote(String nodeId, String name) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("deletenodenote"));
			parameters.put("id", toStringBody(nodeId));
			parameters.put("name", toStringBody(encode(name)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean deleteTaskFlag(String taskId, String name) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("deletetaskflag"));
			parameters.put("id", toStringBody(taskId));
			parameters.put("name", toStringBody(encode(name)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean deleteTaskShortCut(String taskId, String name) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("deletetaskshortcut"));
			parameters.put("id", toStringBody(taskId));
			parameters.put("name", toStringBody(encode(name)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public File downloadDistribution() {
		File zipFile = null;
		File directory = null;

		HashMap<String, ContentBody> parameters = new HashMap<>();
		try {
			parameters.put("op", toStringBody("downloaddistribution"));

			InputStream inputStream = LibraryRestfull.requestStream(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			zipFile = File.createTempFile("distribution", Long.toString(System.nanoTime()));
			OutputStream outputStream = new FileOutputStream(zipFile);
			StreamHelper.copyData(inputStream, outputStream);
			StreamHelper.close(inputStream);
			StreamHelper.close(outputStream);

			directory = new File(zipFile.getAbsolutePath() + "_dir");
			LibraryZip.decompress(zipFile, directory.getAbsolutePath());

		} catch (Exception e) {
			if (zipFile != null)
				zipFile.delete();

			e.printStackTrace();
		}

		return directory;
	}

	@Override
	public boolean emptyTrash() {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("emptytrash"));
			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public byte[] executeExporter(String exporter, String scope) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("executeexporter"));
			parameters.put("exporter", toStringBody(exporter));

			if (scope != null)
				parameters.put("scope", toStringBody(scope));

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			InputStream inputStream = LibraryRestfull.requestStream(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			StreamHelper.copyData(inputStream, outputStream);

			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean existsNode(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("existsnode"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			return result.toLowerCase().contains("true");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean executeNodeCommand(String idNode, String command) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("executenodecommand"));
			parameters.put("idnode", toStringBody(idNode));
			parameters.put("command", toStringBody(encode(command)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public ExportItem exportNode(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();
		ExportItem exportItem = new ExportItem();

		try {
			parameters.put("op", toStringBody("exportnode"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			exportItem.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exportItem;
	}

	@Override
	public ExportList exportNodes(String[] ids) {
		ExportList exportList = new ExportList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("exportnodes"));
			parameters.put("ids", toStringBody(LibraryArray.implode(ids, ",")));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			exportList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exportList;
	}

	@Override
	public NodeList getNodeAncestors(String id) {
		NodeList nodeList = new NodeList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodeancestors"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			nodeList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodeList;
	}

	@Override
	public InputStream getNodeDocument(String id) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodedocument"));
			parameters.put("id", toStringBody(id));

			return LibraryRestfull.requestStream(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getNodeDocumentContentType(String id) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodedocumentcontenttype"));
			parameters.put("id", toStringBody(id));

			return LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public InputStream getNodeFile(String id, String name) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodefile"));
			parameters.put("id", toStringBody(id));
			parameters.put("name", toStringBody(name));

			return LibraryRestfull.requestStream(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Map<String, String> getNodeFlags(String id) {
		HashMap<String, String> flags = new HashMap<>();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodeflags"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (result.isEmpty())
				return flags;

			String[] flagsArray = result.split(PARAMETER_SEPARATOR);
			for (String aFlagsArray : flagsArray) {
				String[] flagArray = aFlagsArray.split("=");
				String value = "";
				if (flagArray.length > 1) value = flagArray[1];
				flags.put(flagArray[0], URLDecoder.decode(value, "UTF-8"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flags;
	}

	@Override
	public Location getNodeLocation(String id) {
		Location location = new Location();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodelocation"));
			parameters.put("id", toStringBody(id));

			String data = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (data.isEmpty())
				return null;

			location.deserializeFromXML(data);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	@Override
	public Map<String, String> getNodeNotes(String id) {
		HashMap<String, String> notes = new HashMap<>();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodenotes"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (result.isEmpty())
				return notes;

			String[] notesArray = result.split(PARAMETER_SEPARATOR);
			for (int i = 0; i < notesArray.length; i++) {
				String[] noteArray = notesArray[i].split("=");
				String value = "";
				if (noteArray.length > 1) value = noteArray[1];
				notes.put(URLDecoder.decode(noteArray[0], "UTF-8"), URLDecoder.decode(value, "UTF-8"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return notes;
	}

	@Override
	public Reference getNodeReference(String nodeId, String name) {
		Reference reference = new Reference();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodereference"));
			parameters.put("id", toStringBody(nodeId));
			parameters.put("name", toStringBody(encode(name)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			reference.deserializeFromXML(result);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return reference;
	}

	@Override
	public ReferenceList getNodeReferences(String codeReference, String filter, String orderBy, Map<String, Object> params, int start, int limit) {
		ReferenceList referenceList = new ReferenceList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodereferences"));
			parameters.put("code", toStringBody(codeReference));
			parameters.put("filter", toStringBody(encode(filter)));
			parameters.put("orderby", toStringBody(encode(orderBy)));
			parameters.put("start", toStringBody(String.valueOf(start)));
			parameters.put("limit", toStringBody(String.valueOf(limit)));

			StringBuilder paramsBuilder = new StringBuilder();
			for (String name : params.keySet()) {
				if (paramsBuilder.length() != 0)
					paramsBuilder.append(PARAMETER_SEPARATOR);
				paramsBuilder.append(name + "=" + params.get(name));
			}
			parameters.put("parameters", toStringBody(encode(paramsBuilder.toString())));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			referenceList.deserializeFromXML(result);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return referenceList;
	}

	@Override
	public int getNodeReferencesCount(String codeReference, String filter, Map<String, Object> params) {
		int result = 0;

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodereferencescount"));
			parameters.put("code", toStringBody(codeReference));
			parameters.put("filter", toStringBody(encode(filter)));

			StringBuilder paramsBuilder = new StringBuilder();
			for (String name : params.keySet()) {
				if (paramsBuilder.length() != 0)
					paramsBuilder.append(PARAMETER_SEPARATOR);
				paramsBuilder.append(name + "=" + params.get(name));
			}
			parameters.put("parameters", toStringBody(encode(paramsBuilder.toString())));

			result = Integer.valueOf(LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String getNodeSchema(String id) {
		String result = "";

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodeschema"));
			parameters.put("id", toStringBody(id));

			result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public TaskList getNodeTasks(String id) {
		TaskList taskList = new TaskList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getnodetasks"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			taskList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskList;
	}

	@Override
	public TermList getSourceTerms(String id, String parent) {
		TermList termList = new TermList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getsourceterms"));
			parameters.put("id", toStringBody(id));

			if (parent != null)
				parameters.put("parent", toStringBody(parent));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			termList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return termList;
	}

	@Override
	public Term getSourceParentTerm(String id, String code) {
		Term term = new Term();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getsourceparentterm"));
			parameters.put("id", toStringBody(id));
			parameters.put("code", toStringBody(code));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (result == null || result.isEmpty())
				return null;

			term.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return term;
	}

	@Override
	public TaskList getTasks(String inbox, String folder, String condition, String owner, int start, int limit) {
		TaskList taskList = new TaskList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettasks"));
			parameters.put("inbox", toStringBody(encode(inbox)));
			parameters.put("folder", toStringBody(encode(folder)));
			parameters.put("condition", toStringBody(encode(condition)));
			parameters.put("owner", toStringBody(encode(owner)));
			parameters.put("start", toStringBody(String.valueOf(start)));
			parameters.put("limit", toStringBody(String.valueOf(limit)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			taskList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskList;
	}

	@Override
	public TaskList getTaskTray(String folder, String condition, String owner, int start, int limit) {
		TaskList taskList = new TaskList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettasks"));
			parameters.put("inbox", toStringBody("tasktray"));
			parameters.put("folder", toStringBody(encode(folder)));
			parameters.put("condition", toStringBody(encode(condition)));
			parameters.put("owner", toStringBody(encode(owner)));
			parameters.put("start", toStringBody(String.valueOf(start)));
			parameters.put("limit", toStringBody(String.valueOf(limit)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			taskList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskList;
	}

	@Override
	public TaskList getTaskBoard(String folder, String condition, int start, int limit) {
		TaskList taskList = new TaskList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettasks"));
			parameters.put("inbox", toStringBody("taskboard"));
			parameters.put("folder", toStringBody(encode(folder)));
			parameters.put("condition", toStringBody(encode(condition)));
			parameters.put("start", toStringBody(String.valueOf(start)));
			parameters.put("limit", toStringBody(String.valueOf(limit)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			taskList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskList;
	}

	@Override
	public TaskFactList getTaskFacts(String id) {
		TaskFactList taskFactList = new TaskFactList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettaskfacts"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			taskFactList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskFactList;
	}

	@Override
	public Map<String, String> getTaskFlags(String id) {
		HashMap<String, String> flags = new HashMap<>();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettaskflags"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (result.isEmpty())
				return flags;

			String[] flagsArray = result.split(PARAMETER_SEPARATOR);
			for (int i = 0; i < flagsArray.length; i++) {
				String[] flagArray = flagsArray[i].split("=");
				String value = "";
				if (flagArray.length > 1) value = flagArray[1];
				flags.put(flagArray[0], URLDecoder.decode(value, "UTF-8"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flags;
	}

	@Override
	public Process getTaskProcess(String id) {
		Process process = null;

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettaskprocess"));
			parameters.put("id", toStringBody(id));

			String data = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (data.isEmpty())
				return null;

			process = PersisterHelper.load(data, Process.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return process;
	}

	@Override
	public Map<String, String> getTaskShortCuts(String id) {
		HashMap<String, String> flags = new HashMap<>();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("gettaskshortcuts"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);

			if (result.isEmpty())
				return flags;

			String[] shortCutsArray = result.split(PARAMETER_SEPARATOR);
			for (int i = 0; i < shortCutsArray.length; i++) {
				String[] shortCutArray = shortCutsArray[i].split("=");
				String value = "";
				if (shortCutArray.length > 1) value = shortCutArray[1];
				flags.put(shortCutArray[0], value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flags;
	}

	@Override
	public User getUserLinkedToNode(String idNode) {
		User user = new User();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getuserlinkedtonode"));
			parameters.put("idnode", toStringBody(idNode));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			user.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public Node getUserNode(String codeUser, int depth) {
		Node node = new Node();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getusernode"));
			parameters.put("code", toStringBody(codeUser));
			parameters.put("depth", toStringBody(String.valueOf(depth)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			node.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public TaskList getUserTasks(String codeUser) {
		TaskList taskList = new TaskList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("getusertasks"));
			parameters.put("code", toStringBody(codeUser));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			taskList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskList;
	}

	@Override
	public boolean hasPermissions(String username) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("haspermissions"));
			parameters.put("username", toStringBody(username));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword, 60);
			return result != null && result.toLowerCase().contains("true");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean importNode(String importer, String scopeNodeId, String data) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("importnode"));
			parameters.put("importer", toStringBody(importer));
			parameters.put("data", toStringBody(encode(data)));

			if (scopeNodeId != null)
				parameters.put("idscope", toStringBody(scopeNodeId));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public User loadUser(String code) {
		User user = new User();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("loaduser"));
			parameters.put("code", toStringBody(code));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			user.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public Node locateNode(String code) {
		Node node = new Node();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("locatenode"));
			parameters.put("code", toStringBody(code));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			node.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public Node locateNode(String code, int depth) {
		Node node = new Node();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("locatenode"));
			parameters.put("code", toStringBody(code));
			parameters.put("depth", toStringBody(String.valueOf(depth)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			node.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public Source loadSource(String id) {
		Source source = new Source();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("loadsource"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			source.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return source;
	}

	@Override
	public Source locateSource(String code, String url) {
		Source source = new Source();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("locatesource"));
			parameters.put("code", toStringBody(code));

			if (url != null)
				parameters.put("url", toStringBody(encode(url)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			source.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return source;
	}

	@Override
	public boolean makeNodeDeleteable(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("makenodedeleteable"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean makeNodeEditable(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("makenodeeditable"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean makeNodePrivate(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("makenodeprivate"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean makeNodePublic(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("makenodepublic"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean makeNodeUnDeleteable(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("makenodeundeleteable"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean makeNodeUnEditable(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("makenodeuneditable"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public Datastore openDatastore(String name) {
		Datastore datastore = new Datastore();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("opendatastore"));
			parameters.put("name", toStringBody(name));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			datastore.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datastore;
	}

	@Override
	public Node openNode(String id) {
		Node node = new Node();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("opennode"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			node.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public NodeList openNodes(String[] ids) {
		NodeList nodeList = new NodeList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("opennodes"));
			parameters.put("ids", toStringBody(LibraryArray.implode(ids, ",")));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			nodeList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodeList;
	}

	@Override
	public Task openTask(String id) {
		Task task = new Task();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("opentask"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			task.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return task;
	}

	@Override
	public Node recoverNode(String id) {
		Node node = new Node();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("recovernode"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			node.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public boolean removeNode(String id) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("removenode"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean removeTask(String id) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("removetask"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean resetNodeForm(String id) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("resetnodeform"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public Task resumeTask(String id) {
		Task task = new Task();

		HashMap<String, ContentBody> parameters = new HashMap<>();
		try {
			parameters.put("op", toStringBody("resumetask"));
			parameters.put("id", toStringBody(id));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			task.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return task;
	}

	@Override
	public boolean runTask(String id) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("runtask"));
			parameters.put("id", toStringBody(id));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNode(Node node) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenode"));
			parameters.put("id", toStringBody(node.getId()));
			parameters.put("data", toStringBody(encode(node.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodeDocument(String id, InputStream data, String contentType) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodedocument"));
			parameters.put("id", toStringBody(id));
			parameters.put("data", new InputStreamBody(data, id));
			parameters.put("contenttype", toStringBody(contentType));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodeFile(String id, String name, InputStream data) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodefile"));
			parameters.put("id", toStringBody(id));
			parameters.put("name", toStringBody(name));
			parameters.put("data", new InputStreamBody(data, name));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodePicture(String id, String name, InputStream data) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodepicture"));
			parameters.put("id", toStringBody(id));
			parameters.put("name", toStringBody(name));
			parameters.put("data", new InputStreamBody(data, name));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodeParent(String nodeId, String parentId) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodeparent"));
			parameters.put("id", toStringBody(nodeId));
			parameters.put("parent", toStringBody(parentId));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodePartnerContext(String idNode, String context) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodepartnercontext"));
			parameters.put("id", toStringBody(idNode));
			parameters.put("context", toStringBody(encode(context)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodeReference(String idNode, Reference reference) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodereference"));
			parameters.put("id", toStringBody(idNode));
			parameters.put("code", toStringBody(reference.getCode()));

			StringBuilder attributesBuilder = new StringBuilder();
			for (ReferenceAttribute attribute : reference.getAttributes().values()) {
				if (attributesBuilder.length() != 0)
					attributesBuilder.append(PARAMETER_SEPARATOR);
				attributesBuilder.append(attribute.getName() + "=" + attribute.getValue());
			}
			parameters.put("attributes", toStringBody(encode(attributesBuilder.toString())));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveNodesAttribute(String[] idNodes, Attribute attribute) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savenodesattribute"));
			parameters.put("nodes", toStringBody(LibraryString.implodeAndWrap(idNodes, ",", null)));
			parameters.put("data", toStringBody(encode(attribute.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveTask(Task task) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("savetask"));
			parameters.put("id", toStringBody(task.getId()));
			parameters.put("data", toStringBody(encode(task.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveUser(User user) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("saveuser"));
			parameters.put("id", toStringBody(user.getId()));
			parameters.put("data", toStringBody(encode(user.serializeToXML()), ContentType.TEXT_XML));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public LogHistory searchEvent(String type, Date from, Date to) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
		LogHistory logHistory = new LogHistory();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("searchevent"));
			parameters.put("type", toStringBody(type));
			parameters.put("from", toStringBody(dateFormat.format(from)));
			parameters.put("to", toStringBody(dateFormat.format(to)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			logHistory.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logHistory;
	}

	@Override
	public NodeList searchNodes(String id, Date from, Date to) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
		NodeList nodeList = new NodeList();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("searchnodes"));
			parameters.put("id", toStringBody(id));
			parameters.put("from", toStringBody(dateFormat.format(from)));
			parameters.put("to", toStringBody(dateFormat.format(to)));

			String result = LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
			nodeList.deserializeFromXML(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodeList;
	}

	@Override
	public boolean subscribe(String type, String data) {

		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put("op", toStringBody("subscribe"));
			parameters.put("type", toStringBody(type));
			parameters.put("data", toStringBody(encode(data)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean unLockTask(String id, String place, String stop) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("unlocktask"));
			parameters.put("id", toStringBody(id));
			parameters.put("place", toStringBody(place));
			parameters.put("stop", toStringBody(stop));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean gotoPlaceInTask(String id, String place, String history) {
		HashMap<String, ContentBody> parameters = new HashMap<>();

		try {
			parameters.put("op", toStringBody("gotoplaceintask"));
			parameters.put("id", toStringBody(id));
			parameters.put("place", toStringBody(place));
			parameters.put("history", toStringBody(encode(history)));

			LibraryRestfull.request(this.location, parameters, this.loadCertificate(), this.certificatePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private String encode(String data) {
		try {
			if (data == null)
				return null;

			return URLEncoder.encode(data, "utf-8");
		} catch (Exception exception) {
			return data;
		}
	}

    private StringBody toStringBody(String content) {
        return toStringBody(content, ContentType.TEXT_PLAIN);
    }

    private StringBody toStringBody(String content, ContentType contentType) {
        return new StringBody(content, contentType);
    }

	private InputStream loadCertificate() {
		return new ByteArrayInputStream(this.certificate);
	}

}
