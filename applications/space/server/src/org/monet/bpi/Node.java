package org.monet.bpi;

import org.monet.bpi.types.Date;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Location;

import java.util.List;
import java.util.Map;

public interface Node {

	User getAuthor();

	String getOwnerId();

	void addPermission(String userId);

	void addPermission(User user);

	void addPermission(String userId, Date beginDate);

	void addPermission(User user, Date beginDate);

	void addPermission(String userId, Date beginDate, Date endDate);

	void addPermission(User user, Date beginDate, Date endDate);

	boolean hasPermission(String userId);

	boolean hasPermission(User user);

	void deletePermission(String userId);

	void deletePermission(User user);

	Node getOwner();

	void setOwner(Node parent);

	boolean isPrototype();

	Node getPrototypeNode();

	boolean isGeoReferenced();

	String getCode();

	String getName();

	String getLabel();

	void setLabel(String label);

	String getDescription();

	void setDescription(String description);

	Map<String, String> getFlags();

	String getColor();

	void setColor(String color);

	String getFlag(String name);

	void setFlag(String name, String value);

	void removeFlag(String name);

	String getNote(String name);

	void setNote(String name, String value);

	void removeNote(String name);

	void save();

	void lock();

	void unLock();

	boolean isLocked();

	Link toLink();

	MonetLink toMonetLink();

	MonetLink toMonetLink(boolean editMode);

	void transform();

	void evaluateRules();

	Node clone(Node parent);

	void merge(Node source);

	String getPartnerContext();

	void setPartnerContext(String context);

	void setEditable(boolean value);

	boolean isEditable();

	void setDeletable(boolean value);

	boolean isDeletable();

	Location getLocation();

	void setLocation(Location location);

	List<Node> getLinksIn();

	List<Node> getLinksOut();

	List<Task> getLinkedTasks();

}