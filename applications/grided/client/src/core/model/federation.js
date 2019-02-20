
var Federation = Model.extend({
  init : function() {
    this._super();
    this.spaces = new Spaces();
    
    this.validations = {
      'name' : Validators.Required,
      'url': Validators.Required
    };    
  }
});

Federation.NAME   = 'name';
Federation.LABEL  = 'label';
Federation.URL    = 'url';
Federation.SPACES = 'spaces';
Federation.SERVER = 'server';
Federation.LOGO  = 'logo';
Federation.STATE  = 'state';
Federation.RUNNING_TIME = 'running_time',

Federation.USER_AUTH = 'user_auth';
Federation.CERTIFICATE_AUTH = 'certificate_auth';
Federation.OPENID_AUTH = 'openid_auth';
Federation.CONNECTION = 'connection';
Federation.CONNECTION_TYPE = 'type';


var DatabaseConnection = Model.extend({
  init : function() {
    this._super();
    this.type = ConnectionTypes.DATABASE;
  }
});

DatabaseConnection.URL = 'url';
DatabaseConnection.USER = 'user';
DatabaseConnection.PASSWORD = 'password';
DatabaseConnection.CONFIG = 'config';

var DatabaseConnectionConfig = Model.extend({
  init : function() {
    this._super();    
  }
});

DatabaseConnectionConfig.DATABASE_TYPE  = 'databasetype';

var LDAPConnection = Model.extend({
  init : function() {
    this._super();
    this.type = ConnectionTypes.LDAP;
  }
});


LDAPConnection.URL = 'url';
LDAPConnection.USER = 'user';
LDAPConnection.PASSWORD = 'password';
LDAPConnection.CONFIG = 'config';

var LDAPConnectionConfig = Model.extend({
  init : function() {
    this._super();    
  }
});

LDAPConnectionConfig.SCHEMA = 'schema';
LDAPConnectionConfig.CN_FIELD = 'cn_field';
LDAPConnectionConfig.UID_FIELD = 'uid_field';
LDAPConnectionConfig.EMAIL_FIELD = 'email_field';
LDAPConnectionConfig.LANG_FIELD = 'lang_field';

