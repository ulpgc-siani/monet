package client.presenters.displays;

import client.core.model.List;
import client.presenters.displays.entity.FieldDisplay;

public interface IsMultipleDisplay<Value> {
	FieldDisplay get(int index);
	Value getValue(int index);
    int getValueIndex(Value value);
	List<FieldDisplay> getAll();
	List<Value> getAllValues();
	void add(Value value);
	void delete(int index);
    void changeOrder(Value value, int newOrder);
    void clear();
	int size();

	boolean isEmpty();
}
