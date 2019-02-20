package client.core.model.types;

import client.core.model.Instance;

public interface Term {
    Instance.ClassName CLASS_NAME = new Instance.ClassName("Term");

    String getValue();
    void setValue(String value);
	String getLabel();
    void setLabel(String value);
    String getFlattenLabel();
    void setFlattenLabel(String flattenLabel);
    boolean isLeaf();
    boolean equals(Object term);
}
