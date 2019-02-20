package org.monet.bpi.java;

import org.monet.bpi.BehaviorImporter;
import org.monet.bpi.ContestantRequest;
import org.monet.bpi.CustomerRequest;
import org.monet.bpi.Importer;
import org.monet.bpi.ImporterScope;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.ProviderResponse;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;

public abstract class ImporterImpl implements Importer, BehaviorImporter {
  
  @Override
  public void onInitialize() {}

  public void onImportItem(Schema schema) {
  }
  
  protected abstract Class<?> getTargetSchemaClass();
  
  protected Node getScope() {
    return null;
  }
  
  public ImporterScope prepareImportOf(String key, CustomerRequest msg) {
    return null;
  }
  
  public ImporterScope prepareImportOf(String key, ContestantRequest msg) {
    return null;
  }
  
  public ImporterScope prepareImportOf(String key, ProviderResponse msg) {
    return null;
  }
  
  public ImporterScope prepareImportOf(NodeDocument node) {
    return new ImporterScopeImpl();
  }
  
  public ImporterScope prepareImportOf(Schema schema) {
    return new ImporterScopeImpl();
  }
  
  public ImporterScope prepareImportOf(File file) {
    return new ImporterScopeImpl();
  }
  
  public ImporterScope prepareImportOf(String url) {
    return new ImporterScopeImpl();
  }
  
  private class ImporterScopeImpl implements ImporterScope {

    @Override
    public void atScope(Node scope) {
    }

    @Override
    public void atGlobalScope() {
      this.atScope(null);
    }
    
  }
  
}
