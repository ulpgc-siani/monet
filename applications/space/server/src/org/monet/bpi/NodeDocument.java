package org.monet.bpi;

import org.monet.bpi.exceptions.RoleException;
import org.monet.bpi.types.File;

public interface NodeDocument extends Node {

	public Schema genericGetSchema();

	public void setSignaturesCount(String signature, int count);

	public void setUsersForSignature(String signature, int signaturePos, String[] userIds) throws RoleException;

	public void disableUsersForSignature(String signature, int signaturePos);

	public void consolidate();

	public void overwriteContent(File newContent);

	public void overwriteContent(byte[] content, String contentType);

	public File getContent();

}
