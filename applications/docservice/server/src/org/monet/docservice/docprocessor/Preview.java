package org.monet.docservice.docprocessor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.sf.json.JSONSerializer;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.Resources;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentMetadata;
import org.monet.docservice.docprocessor.model.PreviewType;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.worker.WorkQueue;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.filesystem.StreamHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class Preview extends HttpServlet {

	private static final String JSON_MIMETYPE = "application/json";

	private static final long serialVersionUID = -5072357058374040174L;

	private Logger logger;
	private Provider<Repository> repositoryProvider;
	private WorkQueue workQueue;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void injectRepositoryProvider(Provider<Repository> repositoryProvider) {
		this.repositoryProvider = repositoryProvider;
	}

	@Inject
	public void injectWorkQueue(WorkQueue workQueue){
		this.workQueue = workQueue;
	}


  /*
   * Document Metadata => "docserver/preview/?id=d23df45adfdft324" --> &type= (XML | JSON)
   * 
   * Page URI => "docserver/preview/?id=d23df45adfdft324&page=1"
   * 
   * Page Thumbnail URI => "docserver/preview/?id=d23df45adfdft324&thumb=1"
   * 
   */

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("doPost(%s, %s)", req, resp);

		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("doGet(%s, %s)", req, resp);

		String documentId = req.getParameter("id");
		String page = req.getParameter("page");
		String thumb = req.getParameter("thumb");

		this.generateDocumentPreviewIfNotExists(documentId);

		int pageNumber = -1;
		boolean isThumb = false;

		if (page != null) {
			try {
				pageNumber = Integer.parseInt(page);
			} catch (NumberFormatException e) {
			}
		}

		if (thumb != null) {
			try {
				pageNumber = Integer.parseInt(thumb);
				isThumb = true;
			} catch (NumberFormatException e) {
			}
		}

		if (documentId != null && documentId.length() != 0) {
			if (pageNumber > 0) {
				getPagePreviewData(resp, documentId, pageNumber, isThumb);
			} else {
				getDocumentMetadata(resp, documentId);
			}
		} else {
			resp.getWriter().println("Invalid query string");
			return;
		}

	}

	private void generateDocumentPreviewIfNotExists(String documentId) {
		Repository repository = repositoryProvider.get();

		if (repository.existsDocumentPreview(documentId))
			return;

		if (this.workQueue.documentHasPendingOperationsOfType(documentId, Operation.OPERATION_GENERATE_DOCUMENT_PREVIEW))
			return;

		WorkQueueItem item = new WorkQueueItem(-1);
		item.setDocumentId(documentId);
		item.setOperation(Operation.OPERATION_GENERATE_DOCUMENT_PREVIEW);
		this.workQueue.queueNewWorkItem(item);
	}

	private void getDocumentMetadata(HttpServletResponse resp, String sDocumentId) {
		Repository repository = repositoryProvider.get();
		try {
			Document document = repository.getDocument(sDocumentId);
			DocumentMetadata metadata = repository.getDocumentMetadata(document);

			resp.setContentType(JSON_MIMETYPE);
			resp.getWriter().print(JSONSerializer.toJSON(metadata).toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error");
		}
	}

	private void getPagePreviewData(HttpServletResponse resp, String sDocumentId, int iPage, boolean isThumb) {
		Repository repository = repositoryProvider.get();
		try {
			int type = isThumb ? PreviewType.THUMBNAIL : PreviewType.PAGE;

			String sContentType = repository.getDocumentPreviewDataContentType(sDocumentId, iPage, type);

			resp.setContentType(sContentType);
			resp.setHeader("Content-Disposition", String.format("attachment; filename=%s_%s.png",
				sDocumentId,
				iPage));
			repository.readDocumentPreviewData(sDocumentId, iPage, resp.getOutputStream(), type);
		} catch (Exception e) {
			resp.setContentType("image/png");
			resp.setHeader("Content-Disposition", String.format("attachment; filename=%s_%s.png",
				sDocumentId,
				iPage));
			InputStream img;
			if (isThumb) img = Resources.getAsStream("/images/defaultThumb.png");
			else img = Resources.getAsStream("/images/default.png");
			try {
				StreamHelper.copy(img, resp.getOutputStream());
			} catch (IOException ioe) {
				logger.error(e.getMessage(), ioe);
			}

		}
	}

}
