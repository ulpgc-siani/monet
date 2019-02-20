package org.monet.bpi;

import org.monet.bpi.types.Link;

import java.util.Map;

public interface BPIBaseNode<Schema extends BPISchema> {

	public String getLabel();

	public void setLabel(String label);

	public Map<String, String> getNotes();

	public String getNote(String name);

	public void addNote(String name, String value);

	public void deleteNote(String name);

	public void save();

	public void saveNotes();

	public void lock();

	public void unLock();

	public Schema getSchema();

	public Link toLink();

}