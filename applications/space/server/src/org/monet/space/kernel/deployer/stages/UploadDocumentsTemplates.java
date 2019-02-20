package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.PositionEnumeration;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

public class UploadDocumentsTemplates extends Stage {

	@Override
	public void execute() {
		ComponentDocuments componentDocuments = this.globalData.getData(ComponentDocuments.class, GlobalData.COMPONENT_DOCUMENTS);
		BusinessUnit businessUnit = this.globalData.getData(BusinessUnit.class, GlobalData.BUSINESS_UNIT);
		List<DocumentDefinition> documentDefinitionList;
		MimeTypes mimeTypes = MimeTypes.getInstance();

		documentDefinitionList = businessUnit.getBusinessModel().getDictionary().getDocumentDefinitionList();
		for (DocumentDefinition definition : documentDefinitionList) {
			if (definition.isAbstract()) continue;
			InputStream template = null;
			String templateName = definition.getTemplate();
			try {
				String baseDir = Configuration.getInstance().getBusinessModelResourcesDir();
				File documentFile = new File(baseDir, templateName != null ? templateName : "");

				if (!this.isModified(documentFile, definition.getCode() + "//" + templateName)) {
					this.deployLogger.info("Document %s not modified, skipping.", templateName);
					continue;
				}

				String filename = definition.getTemplate();
				String mimeType = null;
				if (definition.getTemplate() == null) {
					template = new ByteArrayInputStream("".getBytes());
					mimeType = MimeTypes.XML;
				} else {
					template = AgentFilesystem.getInputStream(baseDir + "/" + filename);
					mimeType = mimeTypes.getFromFilename(filename);
				}
				ArrayList<String> signsFields = new ArrayList<String>();
				PositionEnumeration position = null;

				if (definition.getSignatures() != null) {
					SignaturesProperty signaturesDefinition = definition.getSignatures();

					for (SignatureProperty signature : signaturesDefinition.getSignatureList())
						signsFields.add(signature.getCode());

					position = signaturesDefinition.getPosition();
				}

				componentDocuments.uploadTemplate(definition.getCode(), mimeType, template, position, signsFields);
				this.deployLogger.info("Document %s uploaded to Document Engine.", templateName);
			} catch (Exception exception) {
				problems.add(new ServerError(definition.getName(), exception.getMessage()));
				this.deployLogger.error(exception);
				if (templateName != null)
					this.removeFromModified(new File(templateName));
			} finally {
				StreamHelper.close(template);
			}
		}

		updateHashFile();
	}

	@SuppressWarnings("unchecked")
	private void updateHashFile() {
		HashMap<String, String> hashTable = (HashMap<String, String>) this.globalData.getData(HashMap.class, GlobalData.HASH_TABLE);
		String hashFilePath = this.globalData.getData(String.class, GlobalData.MODEL_INSTALL_DIRECTORY).toString() + "/files.hash";

		AgentFilesystem.removeFile(hashFilePath);
		AgentFilesystem.createFile(hashFilePath);

		String sContent = "";
		Set<String> filenames = hashTable.keySet();

		Iterator<String> setIterator = filenames.iterator();
		while (setIterator.hasNext()) {
			String filename = setIterator.next();
			String fileHash = hashTable.get(filename);
			String hash = filename + "#" + fileHash;
			sContent += hash + "\n";
		}

		AgentFilesystem.appendToFile(hashFilePath, sContent);
	}

	@Override
	public String getStepInfo() {
		return "Uploading document templates";
	}
}
