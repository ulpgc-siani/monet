function HomePlace() {
  this.toString = function() {
    return HomePlace.token;     
  };
};
HomePlace.token = 'home/';

function ServersPlace() {
  this.toString = function() {
    return ServersPlace.token;     
  };
};
ServersPlace.token = 'servers/';


function ServerPlace(id) {
  this.toString = function() {
    return ServersPlace.token + id;     
  };
};

function ModelsPlace() {
  this.toString = function() {
    return ModelsPlace.token;     
  };
};
ModelsPlace.token = 'models/';

function ModelPlace(id) {
  this.toString = function() {
    return ModelsPlace.token + id;     
  };
};

function DeploymentPlace() {
  this.toString = function() {
    return DeploymentPlace.token;     
  };
};
DeploymentPlace.token = "deployment/";

function FederationPlace(id) {
  this.toString = function() {
	  return FederationPlace.token + id;
  };	
};
FederationPlace.token = 'deployment/federations/';


function SpacePlace(id) {
  this.toString = function() {
    return SpacePlace.token + id;
  };
};
SpacePlace.token = 'deployment/spaces/';
