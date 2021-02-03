package org.monet.space.kernel.utils;

import org.monet.mobile.model.TaskMetadata;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.monet.space.kernel.machines.ttm.persistence.PersistenceService.MonetReferenceFileExtension;

public class MessageHelper {

	public static void parseMessageContent(File messageFile, File messageDir, Message message) throws Exception {
		if (isZipFile(messageFile)) {
			ZipInputStream zipStream = new ZipInputStream(new FileInputStream(messageFile));
			messageDir.mkdirs();

			/**
			 *
			 * ZipStream Structure
			 *
			 * .content -> Text content of the message
			 * attach_XX -> Attachment file keyName in comments of the ZipEntry
			 *
			 */
			ZipEntry entry;
			while ((entry = zipStream.getNextEntry()) != null) {
				String name = entry.getName();
				if (name.equals(".content")) {
					message.setContent(StreamHelper.toString(zipStream));
				} else if (name.equals(".metadata")) {
					message.setMetadata(PersisterHelper.load(zipStream, TaskMetadata.class));
				} else {
					if (entry.getExtra() == null) continue;
					if (entry.getName().contains(MonetReferenceFileExtension) && !Kernel.getInstance().isDocumentServiceShared()) continue;
					if (!entry.getName().contains(MonetReferenceFileExtension) && Kernel.getInstance().isDocumentServiceShared()) continue;
					String key = new String(entry.getExtra(), "UTF-8");
					File entryFile = new File(messageDir, name);
					FileOutputStream entryOutputStream = null;
					try {
						entryOutputStream = new FileOutputStream(entryFile);
						StreamHelper.copyData(zipStream, entryOutputStream);
					} finally {
						StreamHelper.close(entryOutputStream);
					}

					message.addAttachment(new MessageAttach(key, entryFile));
				}
			}
		} else {
			FileInputStream messageFileStream = null;
			try {
				messageFileStream = new FileInputStream(messageFile);
				message.setContent(StreamHelper.toString(messageFileStream));
			} finally {
				StreamHelper.close(messageFileStream);
			}
		}
	}

	private static boolean isZipFile(File file) throws IOException {
		if (file.isDirectory()) {
			return false;
		}
		if (!file.canRead()) {
			throw new IOException("Cannot read file " + file.getAbsolutePath());
		}
		if (file.length() < 4) {
			return false;
		}
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		int test = in.readInt();
		in.close();
		return test == 0x504b0304;
	}

}
