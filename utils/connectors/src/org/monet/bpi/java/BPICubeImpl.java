package org.monet.bpi.java;

import org.monet.bpi.BPICube;
import org.monet.v2.metamodel.CubeDefinition;
import org.monet.v2.metamodel.Definition;

import java.util.ArrayList;
import java.util.List;

public class BPICubeImpl<Fact> implements BPICube<Fact> {

	private CubeDefinition definition;
	private ArrayList<Fact> facts = new ArrayList<Fact>();

	public void addFact(Fact fact) {
		this.facts.add(fact);
	}

	List<Fact> getFacts() {
		return this.facts;
	}

	void setDefinition(Definition definition) {
		this.definition = (CubeDefinition) definition;
	}

	CubeDefinition getDefinition() {
		return this.definition;
	}

}
