package org.monet.grided.constants;

public abstract class ErrorCode {
    public static final String UNDEFINED            = "ERR_UNDEFINED";
    public static final String FEDERATION_CONNECTION = "FEDERATION_CONNECTION";
    public static final String INIT_TEMPLATE_ENGINE  = "INIT_TEMPLATE_ENGINE";
    public static final String TEMPLATE_MERGING      = "TEMPLATE_MERGING";
    
    public static final String LOAD_SERVERS = "LOAD_SERVERS";
    public static final String LOAD_SERVER = "LOAD_SERVER";
    public static final String SAVE_SERVER = "SAVE_SERVER";
    public static final String DELETE_SERVER = null;
    
    public static final String LOAD_FEDERATIONS = "LOAD_FEDERATIONS";
    public static final String LOAD_FEDERATION = "LOAD_FEDERATION";
    public static final String SAVE_FEDERATION = "SAVE_FEDERATION";
    public static final String DELETE_FEDERATION = null;

    public static final String LOAD_SPACES = "LOAD_SPACES";
    public static final String LOAD_SPACE = "LOAD_SPACE";
    public static final String SAVE_SPACE = "SAVE_SPACE";
    public static final String DELETE_SPACE = null;
    public static final String READ_DATA_FILE = "READ_DATA_FILE";
    public static final String WRITE_DATA_FILE = "WRITE_FEDERATION_FILE";
    public static final String MULTIPLE_DATA_FILE_IN_ZIP = "MULTIPLE_DATA_FILE_IN_ZIP";
    public static final String DATA_FILE_NOT_FOUND = "DATA_FILE_NOT_FOUND";
    public static final String UPDATE_EXCEPTION = "UPDATE_EXCEPTION";    
    public static final String BEGIN_DATABASE_TRANSACTION = "BEGIN_DATABASE_TRANSACTION";
    public static final String DATABASE_COMMIT = "DATABASE_COMMIT";
    public static final String DATABASE_ROLLBACK = "DATABASE_ROLLBACK";
    public static final String UPLOAD_IMAGE = "UPLOAD_IMAGE";
    public static final String DOWNLOAD_IMAGE = "DOWNLOAD_IMAGE";
    public static final String SAVE_IMAGE = "SAVE_IMAGE";
    public static final String LOAD_MODEL = "LOAD_MODEL";
    public static final String LOAD_MODELS = "LOAD_MODELS";
    public static final String DELETE_MODEL = "DELETE_MODEL";
    public static final String POPULATE_MODEL = "POPULATE_MODEL";
    public static final String SAVE_MODEL = "SAVE_MODEL";
    public static final String SAVE_MODEL_VERSION = "SAVE_MODEL_VERSION";
    public static final String DELETE_MODEL_VERSION = "DELETE_MODEL_VERSION";
    public static final String LOAD_MODEL_VERSION = "LOAD_MODEL_VERSION";
    public static final String LOAD_MODEL_VERSIONS = "LOAD_MODEL_VERSIONS";
    public static final String DUPLICATE_MODEL_VERSION = "DuplicateModelVersion";
    public static final String IMPORT_DATA = "IMPORT_DATA";
    public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
}
