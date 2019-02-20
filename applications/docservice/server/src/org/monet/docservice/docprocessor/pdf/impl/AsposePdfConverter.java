package org.monet.docservice.docprocessor.pdf.impl;

import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.PdfCompliance;
import com.aspose.words.PdfSaveOptions;
import com.google.inject.Inject;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.pdf.PdfConverter;

import java.io.FileInputStream;

public class AsposePdfConverter implements PdfConverter {

	private Configuration configuration;
	private Logger logger;

	@Inject
	public AsposePdfConverter(Logger logger, Configuration configuration) {
		this.configuration = configuration;
		this.logger = logger;

		try {
			String licencePath = this.configuration.getString("PdfLicencePath");
			License license = new License();
			license.setLicense(new FileInputStream(licencePath));
		} catch (Exception e) {
			this.logger.warn("Error configuring Aspose.Words licence file. Using trial licence. Error:" + e.getMessage());
		}
	}

	public void generatePdf(String sourceDocument, String destination) {
		logger.debug("generatePdf(%s, %s)", sourceDocument, destination);

		com.aspose.words.Document doc;
		try {
			doc = new com.aspose.words.Document(sourceDocument);

			PdfSaveOptions options = new PdfSaveOptions();
			options.setHeadingsOutlineLevels(3);
			options.setExpandedOutlineLevels(1);
			options.setPreserveFormFields(true);
			if (this.configuration.getBoolean(Configuration.GENERATE_PDF_A))
				options.setCompliance(PdfCompliance.PDF_A_1_B);

			// Aspose.Words for Java requires that you specify the true type fonts directory explicitly before attempting to convert any documents to PDF.
			// It does not have to be an OS fonts folder. You can create your own folder and copy all the fonts there you think might ever be required.
			FontSettings.setFontsFolder(this.configuration.getPath(Configuration.PATH_TRUETYPE_FONTS), false);

			doc.save(destination, options);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(String.format("Error converting '%s' to pdf", sourceDocument));
		}
	}

	public boolean check() {
		try {
			Class.forName("com.aspose.words.Document");
			return true;
		} catch (ClassNotFoundException e) {
		}

		return false;
	}

}
