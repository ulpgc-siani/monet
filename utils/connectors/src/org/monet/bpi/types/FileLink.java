package org.monet.bpi.types;

import org.simpleframework.xml.Text;

public class FileLink {

	@Text
	private String key;

	public FileLink() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean equals(FileLink obj) {
		return this.key.equals(obj.key);
	}

}
