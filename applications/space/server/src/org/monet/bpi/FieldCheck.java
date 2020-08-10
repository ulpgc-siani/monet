package org.monet.bpi;

import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.TermList;

public interface FieldCheck extends Field<CheckList> {

	public CheckList getChecked();

	public TermList getCheckedAsTermList();

	public void fill(CheckList checkList);

	public void fillFromTermList(TermList termList);

	public String getFrom();

	public String getSource();

	public void setFrom(String from);

}