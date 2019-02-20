package org.monet.grided.control.actions.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.monet.grided.control.actions.impl.BaseAction;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONModelSerializer;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.library.LibraryFileUploader;
import org.monet.grided.library.LibraryMultipartRequest;

import com.google.inject.Inject;

public class AddModelAction extends BaseAction {

	private Configuration configuration;
  private Filesystem filesystem;
	private GridedService service;
	private JSONModelSerializer serializer;

	@Inject
	public AddModelAction(Configuration configuration, Filesystem filesystem, GridedService service, JSONModelSerializer serializer) {
    this.configuration = configuration;
    this.filesystem = filesystem;
		this.service = service;
		this.serializer = serializer;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Model model = null;
		/*
		 * String name = request.getParameter(Params.NAME);
		 * 
		 * try { model = this.service.createModel(name); } catch (Exception ex) {
		 * sendErrorResponse(response, JSONErrorResponse.ERROR); return; }
		 * 
		 * sendResponse(response, this.serializer.serialize(model));
		 */

		LibraryMultipartRequest multipartRequest = new LibraryMultipartRequest(request);
		LibraryFileUploader uploader = new LibraryFileUploader();

		FileItem fileItem = multipartRequest.getFileParameter(Params.SOURCE);
		String name = multipartRequest.getParameter(Params.NAME);

		String uploadDirectory = configuration.getProperty(Configuration.TEMP_PATH);

		try {
			File versionFile = uploader.uploadFile(fileItem, uploadDirectory, fileItem.getName());
			model = this.service.createModel(name, versionFile);
//			model = this.gridedService.saveModelVersion(Long.valueOf(modelId), versionFile);

		} catch (Exception ex) {
			sendResponse(response, JSONErrorResponse.ERROR);
			return;
		} finally {
			String filename = uploadDirectory + Filesystem.FILE_SEPARATOR + fileItem.getName();
			this.filesystem.removeFile(filename);
		}

		sendResponse(response, this.serializer.serialize(model));

	}
}
