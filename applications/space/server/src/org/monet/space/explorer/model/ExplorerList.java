package org.monet.space.explorer.model;

import java.util.ArrayList;

public class ExplorerList<T> extends ArrayList<T> implements List<T> {
	private int totalCount = -1;

	@Override
	public int getTotalCount() {
		return totalCount!=-1?totalCount:size();
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
