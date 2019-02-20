package org.monet.grided.core.persistence.database.constants;

public abstract class Queries {

    public static final String LOAD_GRIDED_COMPONENTS  = "LOAD_GRIDED_COMPONENTS";
    public static final String INSERT_GRIDED_COMPONENT = "INSERT_GRIDED_COMPONENT";
    
    public static final String LOAD_SERVER    = "LOAD_SERVER";
    public static final String LOAD_SERVERS   =  "LOAD_SERVERS";
    public static final String INSERT_SERVER  = "INSERT_SERVER";
    public static final String UPDATE_SERVER  = "UPDATE_SERVER";
    public static final String DELETE_SERVER  = "DELETE_SERVER";
    
    public static final String LOAD_FEDERATION   = "LOAD_FEDERATION";
//    public static final String LOAD_FEDERATIONS  = "LOAD_FEDERATIONS";
    public static final String INSERT_FEDERATION = "INSERT_FEDERATION";
    public static final String UPDATE_FEDERATION = "UPDATE_FEDERATION";
    public static final String DELETE_FEDERATION = "DELETE_FEDERATION";

    public static final String LOAD_SPACE   = "LOAD_SPACE";
//    public static final String LOAD_FEDERATION_SPACES  = "LOAD_FEDERATION_SPACES";
    public static final String LOAD_SERVER_SPACES  = "LOAD_SERVER_SPACES";
    public static final String LOAD_SPACES_WITH_MODEL = "LOAD_SPACES_WITH_MODEL";
    public static final String INSERT_SPACE = "INSERT_SPACE";
    public static final String UPDATE_SPACE = "UPDATE_SPACE";
    public static final String DELETE_SPACE = "DELETE_SPACE";
    public static final String LOAD_FEDERATIONS = "LOAD_FEDERATIONS";
    public static final String LOAD_SERVER_FEDERATIONS = "LOAD_SERVER_FEDERATIONS";
    
    public static final String LOAD_FEDERATION_SPACES = "LOAD_FEDERATION_SPACES";
    public static final String LOAD_FEDERATION_OF_SPACE = "LOAD_FEDERATION_OF_SPACE";
    public static final String LOAD_SERVER_ID_OF_FEDERATION = "LOAD_SERVER_ID_OF_FEDERATION";
    
    public static final String LOAD_MODEL   = "LOAD_MODEL";
    public static final String LOAD_MODELS  = "LOAD_MODELS";    
    public static final String INSERT_MODEL = "INSERT_MODEL";    
    public static final String DELETE_MODEL = "DELETE_MODEL";
    public static final String UPDATE_MODEL = "UPDATE_MODEL";
        
    public static final String LOAD_MODEL_VERSION = "LOAD_MODEL_VERSION";    
    public static final String LOAD_LATEST_MODEL_VERSION = "LOAD_LATEST_MODEL_VERSION";
    public static final String INSERT_MODEL_VERSION = "INSERT_MODEL_VERSION";
    public static final String DELETE_MODEL_VERSION = "DELETE_MODEL_VERSION";
    public static final String LOAD_MODEL_VERSIONS = "LOAD_MODEL_VERSIONS";
    public static final String LOAD_MODEL_VERSIONS_BY_METAMODEL = "LOAD_MODEL_VERSIONS_BY_METAMODEL";
    public static final String LOAD_SPACES_BY_MODELVERSION = "LOAD_SPACES_BY_MODELVERSION";
}

