package org.monet.space.explorer.model;

import java.util.List;

public interface Filter {
	public String getName();
	public List<Option> getOptions();

	public interface Option {
		public String getValue();
		public String getLabel();
	}
}
