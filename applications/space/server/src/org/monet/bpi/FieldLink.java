package org.monet.bpi;

import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;

public interface FieldLink extends Field<Link> {

	public Node getNode();

	public Term getAsTerm();
}