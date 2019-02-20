package org.monet.bpi;

import java.util.List;

public interface NodeContainer extends Node, Georeferenced {

	List<Node> getChildren();

}
