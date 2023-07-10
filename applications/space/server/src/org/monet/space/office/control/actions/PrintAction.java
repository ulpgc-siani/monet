package org.monet.space.office.control.actions;

import org.monet.space.backservice.configuration.Configuration;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.model.Account;
import org.monet.space.office.presentation.user.renders.PrintRender;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

public abstract class PrintAction extends Action {

	private static final String PrintedNodePattern = "%s/printed_node.%s.%s";

	protected InputStream loadDocument(Account account, String idNode) {
		return AgentFilesystem.getInputStream(printedNodeFilename(account, idNode));
	}

	protected InputStream loadDocument(String idNode) {
		return loadDocument(this.getAccount(), idNode);
	}

	protected File documentFile(Account account, String idNode) {
		return new File(printedNodeFilename(account, idNode));
	}

	protected void saveDocument(Account account, String idNode, byte[] data) {
		String filename = printedNodeFilename(account, idNode);
		removeDocument(account, idNode);
		AgentFilesystem.forceDir(LibraryFile.getDirname(filename));
		AgentFilesystem.writeFile(new File(filename), new ByteArrayInputStream(data));
	}

	protected void saveDocument(String idNode, byte[] data) {
		saveDocument(this.getAccount(), idNode, data);
	}

	protected void removeDocument(Account account, String idNode) {
		String filename = printedNodeFilename(account, idNode);
		if (!AgentFilesystem.existFile(filename)) return;
		AgentFilesystem.removeFile(filename);
	}

	protected void removeDocument(String idNode) {
		removeDocument(this.getAccount(), idNode);
	}

	protected String userId(Account account) {
		return account.getUser().getId();
	}

	protected PrintRender.Range rangeOf(final String dateAttribute, final Date fromDate, final Date toDate) {
		if (dateAttribute == null)
			return null;

		return new PrintRender.Range() {
			@Override
			public String attribute() {
				return dateAttribute;
			}

			@Override
			public Date from() {
				return fromDate;
			}

			@Override
			public Date to() {
				return toDate;
			}
		};
	}

	private String printedNodeFilename(Account account, String idNode) {
		String tempDir = Configuration.getInstance().getTempDir();
		return String.format(PrintedNodePattern, tempDir, userId(account), idNode);
	}

}
