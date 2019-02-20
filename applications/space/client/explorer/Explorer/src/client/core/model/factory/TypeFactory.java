package client.core.model.factory;

import client.core.model.types.*;
import client.core.model.types.Number;

public interface TypeFactory {
	TermList createTermList();
	TermList createTermList(Term[] terms);

	<T extends java.lang.Number> Number<T> createNumber(T value);
	Term createTerm(final String value, final String label);
	Link createLink(final String id, final String label);
	Composite createComposite();
	File createFile(String label);
	File createFile(String id, String label);
	Picture createPicture(String label);
	Picture createPicture(String id, String label);
	Uri createUri(String value);
}
