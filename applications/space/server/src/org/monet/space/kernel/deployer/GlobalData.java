package org.monet.space.kernel.deployer;

public interface GlobalData {

	public static final String MODEL_ZIP_FILE = "MODEL_STREAM";
	public static final String WORKING_DIRECTORY = "WORKING_DIRECTORY";
	public static final String MODEL_INSTALL_DIRECTORY = "MODEL_INSTALL_DIRECTORY";
	public static final String TEMP_DIRECTORY = "TEMP_DIRECTORY";
	public static final String COMPONENT_PERSISTENCE = "COMPONENT_PERSISTENCE";
	public static final String COMPONENT_FEDERATION = "COMPONENT_FEDERATION";
	public static final String COMPONENT_DOCUMENTS = "COMPONENT_DOCUMENTS";
	public static final String BUSINESS_UNIT = "BUSINESS_UNIT";
	public static final String HASH_TABLE = "HASH_TABLE";
	public static final String REFERENCE_DEFINITIONS = "REFERENCE_DEFINITIONS";
	public static final String SOURCE_DEFINITIONS = "SOURCE_DEFINITIONS";
	public static final String DATASTORE_DEFINITIONS = "DATASTORE_DEFINITIONS";
	public static final String DASHBOARD_DEFINITIONS = "DASHBOARD_DEFINITIONS";
	public static final String BUSINESS_MODEL_ZIP_LOCATION = "BUSINESS_MODEL_ZIP_LOCATION";

	<T> T getData(Class<T> type, String key);

	void setData(String key, Object data);
}
