package org.monet.bpi;

import org.monet.bpi.types.Link;
import org.monet.bpi.types.Location;

import java.util.Map;

public interface Node {

	public User getAuthor();

	public String getOwnerId();

	public Node getOwner();

	public void setOwner(Node parent);

	public boolean isPrototype();

	public String getId();

	public String getCode();

	public String getName();

	public String getLabel();

	public void setLabel(String label);

	public Map<String, String> getFlags();

	public String getFlag(String name);

	public void setFlag(String name, String value);

	public void removeFlag(String name);

	public Map<String, String> getNotes();

	public String getNote(String name);

	public void setNote(String name, String value);

	public void removeNote(String name);

	public void save();

	public void lock();

	public void unLock();

	public boolean isLocked();

	public Link toLink();

	public MonetLink toMonetLink();

	public MonetLink toMonetLink(boolean editMode);

	public void transform();

	public void evaluateRules();

	public Node clone(Node parent);

	public void merge(Node source);

	public String getPartnerContext();

	public void setPartnerContext(String context);

	public void setEditable(boolean value);

	public boolean isEditable();

	public void setDeletable(boolean value);

	public boolean isDeletable();

	public void setLocation(Location location);

	public boolean isComponent();

}