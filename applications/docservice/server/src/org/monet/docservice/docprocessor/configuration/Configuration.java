package org.monet.docservice.docprocessor.configuration;


public interface Configuration {

    public static final String JDBC_DATABASE = "Jdbc.Database";
    public static final String JDBC_DATASOURCE = "Jdbc.DataSource";
	public static final String JDBC_MAX_ACTIVE_CONNECTIONS = "Jdbc.MaxActiveConnections";
	public static final String JDBC_REMOVE_ABANDONED_TIMEOUT = "Jdbc.RemoveAbandonedTimeout";

	public static final String PATH_TRUETYPE_FONTS = "TrueTypeFonts";
    public static final String PATH_TEMP = "Temp";
    public static final String WORKQUEUE_THREADPOOL_SIZE = "WorkQueue.ThreadPoolSize";
    public static final String WORKQUEUE_THREADPOOL_PERIOD = "WorkQueue.CollectWorkPeriod";

	public static final String DOCUMENT_PREVIEWS_CACHE_SIZE = "Document.PreviewsCacheSizeInDays";
	public static final String DOCUMENT_DISKS = "Document.Disks";

    public static final String PDF_CONVERTER_CLASS = "PdfConverterClass";
    public static final String GENERATE_PDF_A = "GeneratePdfA";

    public static final String MODEL_PRODUCER_CLASS = "ModelProducerClass";

    public static final String SIGN_HEIGHT = "Height";
    public static final String OFFSET_X = "OffsetX";
    public static final String OFFSET_Y = "OffsetY";
    public static final String OFFSET_Y_FOR_BOTTOM = "OffsetYForBottom";
    public static final String COUNT_SIGN = "Count";

	String getPath(String key);

    String getJDBCDataSource();

    int getInt(String key);

    String getString(String key);

    boolean getBoolean(String key);

    int getSignConfig(String key);

	String getApplicationDir();

	String getUpgradesDir();

	int getDocumentPreviewsCacheSize();

	String getUserDataDir();

	String getLogsDir();

	String[] getDocumentDisks();

	void check();
}