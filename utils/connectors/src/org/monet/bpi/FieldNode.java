package org.monet.bpi;

import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;

public interface FieldNode extends Field<Link> {

	public Term getAsTerm();

	public <N extends Node> Node getNode();

}