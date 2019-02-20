package org.monet.grided.core.services.grided.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.monet.grided.core.constants.ModelTypes;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.persistence.FederationsRepository;
import org.monet.grided.core.persistence.ModelsRepository;
import org.monet.grided.core.persistence.ServersRepository;
import org.monet.grided.core.persistence.SpacesRepository;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class GridedServiceImpl implements GridedService {

    private ServersRepository serversRepository;
    private FederationsRepository federationsRepository;
    private SpacesRepository spacesRepository;
    private ModelsRepository modelsRepository;
        
    @Inject
    public GridedServiceImpl() {
        this.serversRepository = null;        
    }
    
    @Inject
    public void injectServersRepository(ServersRepository serversRepository) {
        this.serversRepository = serversRepository;        
    }
    
    @Inject
    public void injectFederationsRepository(FederationsRepository federationsRepository) {
        this.federationsRepository = federationsRepository;        
    }
        
    @Inject
    public void injectSpacesRepository(SpacesRepository spacesRepository) {
        this.spacesRepository = spacesRepository;        
    }
    
    @Inject
    public void injectModelsRepository(ModelsRepository modelsRepository) {
        this.modelsRepository = modelsRepository;
    }
    
    @Override
    public Server createServer(String name, String ip, boolean enabled) {
        return this.serversRepository.createServer(name, ip, enabled);        
    }

    @Override
    public List<Server> loadServers() {
        return this.serversRepository.loadServers();
    }

    @Override
    public Server loadServer(long id) {
        return this.serversRepository.loadServer(id);
    }

    @Override
    public void save(Server server) {
        this.serversRepository.save(server);                
    }

    @Override
    public void deleteServer(long serverId) {
        this.serversRepository.deleteServer(serverId);
    }        
    
    public void deleteServers(long[] serverIds) {
        this.serversRepository.deleteServers(serverIds);
    }

        
    @Override
    public Federation createFederation(long serverId, String federationName, String url) {
        return this.federationsRepository.createFederation(serverId, federationName, url);        
    }
    
    @Override
    public Federation createFederation(Federation federation, Image image) {        
        return this.federationsRepository.createFederation(federation, image);
    }

    
    @Override
    public Federation loadFederation(long id) {
        Federation federation = this.federationsRepository.loadFederation(id);        
        return federation;
    }

    @Override
    public List<Federation> loadFederations(long serverId) {
        return this.federationsRepository.loadFederations(serverId);
    }

    @Override
    public List<Federation> loadAllFederations() {
        return this.federationsRepository.loadAllFederations();
    }

//    @Override
//    public void saveFederation(Federation federation, File folder) {
//        this.federationsRepository.saveFederation(federation, folder);        
//    }

    @Override
    public void saveFederation(Federation federation) {
       this.federationsRepository.saveFederation(federation);        
    }
    
    @Override
    public void saveFederation(Federation federation, Image image) {
        this.federationsRepository.saveFederation(federation, image);        
    }

    @Override
    public void deleteFederation(long id) {
        this.federationsRepository.deleteFederation(id);        
    }

    @Override
    public void deleteFederations(long[] ids) {
        this.federationsRepository.deleteFederations(ids);
    }

    @Override
    public byte[] loadFederationImage(long serverId, long id, String filename) {
        return this.federationsRepository.loadFederationImage(serverId, id, filename);        
    }

//    @Override
//    public Space createSpace(long serverId, long federationId, File folder) {        
//        return this.spacesRepository.createSpace(serverId, federationId, folder);
//    }

    @Override
    public Space createSpace(long serverId, long federationId, String federationName, String url, ModelVersion modelVersion) {        
        return this.spacesRepository.createSpace(serverId, federationId, federationName, url, modelVersion);
    }

    @Override
    public Space createSpace(Space space, Image image) {
        return this.spacesRepository.createSpace(space, image);
    }

    @Override
    public Space loadSpace(long id) {        
        return this.spacesRepository.loadSpace(id);
    }

    @Override
    public List<Space> loadFederationSpaces(long federationId) {        
        return this.spacesRepository.loadFederationSpaces(federationId);
    }

    @Override
    public List<Space> loadServerSpaces(long serverId) {        
        return this.spacesRepository.loadServerSpaces(serverId);
    }
        
    @Override
    public List<Space> loadSpacesWithModel(long modelId) {        
        return this.spacesRepository.loadSpacesWithModel(modelId);
    }

    @Override
    public void saveSpace(Space space, File file) {
        this.spacesRepository.saveSpace(space, file);        
    }

    @Override
    public void saveSpace(Space space) {
        this.spacesRepository.saveSpace(space);        
    }

    @Override
    public void saveSpace(Space space, Image image) {
        this.spacesRepository.saveSpace(space, image);        
    }

    @Override
    public void deleteSpace(long id) {
        this.spacesRepository.deleteSpace(id);        
    }
    
    @Override
    public void deleteSpaces(long[] ids) {
        this.spacesRepository.deleteSpaces(ids);
    }
    
    @Override
    public byte[] loadSpaceImage(long serverId, long federationId, long id, String filename) {        
        return this.spacesRepository.loadSpaceImage(serverId, federationId, id, filename);
    }
        
    @Override
    public Model createModel(String name, File versionFile) {
        return this.modelsRepository.createModel(name, versionFile);        
    }

    @Override
    public List<Model> loadModels() {
        return this.modelsRepository.loadModels();       
    }

    @Override
    public Model loadModel(long id) {
        return this.modelsRepository.loadModel(id);
    }

    @Override
    public void saveModel(Model model) {
        this.modelsRepository.saveModel(model);
    }
    
    @Override
    public void saveModel(Model model, Image image) {
        this.modelsRepository.saveModel(model, image);
    }

    @Override
    public void deleteModel(long modelId) {
        this.modelsRepository.deleteModel(modelId);
    }

    @Override
    public void deleteModels(long[] ids) {
        for (long id : ids) {
            this.deleteModel(id);
        }
    }
    
    @Override
    public ModelVersion loadModelVersion(long modelId, long id) {
        return this.modelsRepository.loadModelVersion(modelId, id);
    }
    
    @Override
    public List<ModelVersion> loadModelVersions(long modelId) {
        return this.modelsRepository.loadModelVersions(modelId);
    }

    @Override
    public List<ModelVersion> loadModelsVersions(long modelId, String metaModelVersion) {
        return this.modelsRepository.loadModelsVersions(modelId, metaModelVersion);
    }
    
    @Override
    public ModelVersion loadLatestModelVersion(long modelId) {
        return this.modelsRepository.loadLatestModelVersion(modelId);        
    }
    
    @Override
    public byte[] loadVersionFile(long modelId, long versionId) {    
        return this.modelsRepository.loadVersionFile(modelId, versionId);
    }
    
    @Override
    public ModelVersion saveModelVersion(long modelId, File versionFile) {
        return this.modelsRepository.saveModelVersion(modelId, versionFile);        
    }
            
    @Override
    public byte[] loadModelImage(long modelId, String filename) {
        return this.modelsRepository.loadModelImage(modelId, filename);        
    }
    
    public byte[] loadImage(Map<String, String> parameters) {
        byte[] image = new byte[0];
        String modelType = parameters.get(Params.MODEL_TYPE);
        
        switch (Integer.valueOf(modelType)) {
          case ModelTypes.FEDERATION: 
            image = this.federationsRepository.loadFederationImage(Long.valueOf(parameters.get(Params.SERVER_ID)), Long.valueOf(parameters.get(Params.FEDERATION_ID)), parameters.get(Params.FILENAME));                    
            break;
          case ModelTypes.SPACE:      
            image = this.loadSpaceImage(Long.valueOf(parameters.get(Params.SERVER_ID)), Long.valueOf(parameters.get(Params.FEDERATION_ID)), Long.valueOf(parameters.get(Params.SPACE_ID)), parameters.get(Params.FILENAME));
            break;
          case ModelTypes.MODEL:      
            image = this.loadModelImage(Long.valueOf(parameters.get(Params.MODEL_ID)), parameters.get(Params.FILENAME));
            break;                
        }      
        return image;
    }
}
 