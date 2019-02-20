package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.UUID;

public class ActionSaveNodePicture extends ActionSaveNodeFile {

	public ActionSaveNodePicture() {
	}

	@Override
	public String execute() {
		String name = (String) this.parameters.get(Parameter.NAME);
		InputStream fileStream = (InputStream) this.parameters.get(Parameter.DATA);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		File tempFile = null;
		FileOutputStream tempFileStream = null;
		FileInputStream sourceStream = null, imageStream = null;
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

		try {
			tempFile = File.createTempFile(UUID.randomUUID().toString(), "");
			tempFileStream = new FileOutputStream(tempFile);
			StreamHelper.copyData(fileStream, tempFileStream);
			sourceStream = new FileInputStream(tempFile);
			String contentType = getContentType(name, tempFile);

			if (!mimeTypes.isImage(contentType))
				throw new RuntimeException(String.format("File is not a picture %s", name));

			imageStream = new FileInputStream(tempFile);
			Size size = getSize(name, tempFile);
			componentDocuments.uploadImage(name, sourceStream, contentType, size.getWidth(), size.getHeight());

		} catch (IOException e) {
			AgentLogger.getInstance().error(e);
			throw new RuntimeException(e);
		}
		finally {
			StreamHelper.close(tempFileStream);
			StreamHelper.close(sourceStream);
			StreamHelper.close(imageStream);

			if (tempFile != null && tempFile.exists())
				tempFile.delete();
		}

		return MessageCode.NODE_FILE_SAVED;
	}

	private Size getSize(String name, File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			return createSize(image.getWidth(), image.getHeight());
		} catch (Throwable e) {
			try {
				RenderedImage image = JAI.create("fileload", file.getAbsolutePath());
				return createSize(image.getWidth(), image.getHeight());
			}
			catch (Throwable exception) {
				throw new RuntimeException(String.format("Could not load image size for file %s: %s", name, exception.getMessage()));
			}
		}
	}

	private Size createSize(final int width, final int height) {
		return new Size() {
			@Override
			public int getWidth() {
				return width;
			}

			@Override
			public int getHeight() {
				return height;
			}
		};
	}

	private interface Size {
		int getWidth();
		int getHeight();
	}
}
