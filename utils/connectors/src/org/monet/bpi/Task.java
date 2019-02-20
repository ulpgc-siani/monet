package org.monet.bpi;

import org.monet.metamodel.internal.Lock;
import org.monet.api.space.backservice.impl.model.workmap.Process;

import java.util.Map;

public interface Task {

	String getId();

	String getCode();

	String getName();

	void setLabel(String label);

	String getLabel();

	void setDescription(String description);

	Process getProcess();

	Map<String, String> getShortCuts();

	Node getShortCut(String name);

	Map<String, String> getFlags();

	String getFlag(String name);

	void setFlag(String name, String value);

	void removeFlag(String name);

	void addLog(String title, String text);

	void addLog(String title, String text, Iterable<MonetLink> links);

	void save();

	void resume();

	void unLock(Lock lock);

	void doGoto(String place, String historyText);

	void free();

	void abort();

	MonetLink toMonetLink();

}