package org.monet.metamodel.internal;

public class Lock {

	private String place;
	private String id;

	public Lock(String place, String id) {
		this.place = place;
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
