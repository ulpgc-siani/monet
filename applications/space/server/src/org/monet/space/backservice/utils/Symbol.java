package org.monet.space.backservice.utils;

public class Symbol {

	private String name;
	private String code;
	private String shortCode;
	private String codeDefinition;

	public Symbol(String code, String name, String shortCode, String codeDefinition) {
		this.name = name;
		this.code = code;
		this.shortCode = shortCode;
		this.codeDefinition = codeDefinition;

	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getShortCode() {
		return this.shortCode;
	}

	public String getCodeDefinition() {
		return this.codeDefinition;
	}


}
