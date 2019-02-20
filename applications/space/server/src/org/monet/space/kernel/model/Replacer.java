package org.monet.space.kernel.model;

import org.monet.metamodel.Definition;

public class Replacer {

	public Class<? extends Definition> Clazz;
	public String Name;
	public String ReplacedName;

	public Replacer(Class<? extends Definition> clazz, String name, String replacedName) {
		this.Clazz = clazz;
		this.Name = name;
		this.ReplacedName = replacedName;
	}

}
