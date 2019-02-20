package client.core;

import client.core.model.Field;
import client.core.model.types.*;

public class TypeBuilder {

	public static Link buildLink(final String id, final String label) {
		return new client.core.system.types.Link(id, label);
	}

	public static Term buildTerm(final String value, final String label) {
		return new client.core.system.types.Term(value, label);
	}

	public static Check buildCheck(final String value, final String label, final boolean checked) {
		return new client.core.system.types.Check(value, label, checked);
	}

	public static Composite buildComposite(Field[] elements) {
		return new client.core.system.types.Composite(elements);
	}

	public static <T extends java.lang.Number> client.core.system.types.Number buildNumber(T value) {
		return new client.core.system.types.Number(value);
	}

	public static File buildFile(String filename, String url) {
		return new client.core.system.types.File(filename, url);
	}

	public static Picture buildPicture(String filename, String url) {
		return new client.core.system.types.Picture(filename, url);
	}

	public static Uri buildUri(String value) {
		return new client.core.system.types.Uri(value);
	}

	public static TermList buildTermList(final Term[] elements) {
		return new client.core.system.types.TermList(elements);
	}

	public static TermList buildTermList() {
		return buildTermList(new Term[0]);
	}

	public static CheckList buildCheckList(final Check[] elements) {
		return new client.core.system.types.CheckList(elements);
	}

}
