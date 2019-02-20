package org.monet.bpi.java;

import org.monet.bpi.BehaviorExporter;
import org.monet.bpi.Exporter;
import org.monet.bpi.ExporterScope;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;

public abstract class ExporterImpl implements Exporter, BehaviorExporter {
  
  protected Node getScope() {
    return null;
  }
  
  @Override
  public void onInitialize() {}

  public void onExportItem(Schema schema) throws Exception {
  }
  
  public abstract ExporterScope prepareExportOf(Node node);
  
  public class ExporterScopeImpl implements ExporterScope {

    public ExporterScopeImpl(Node scope) {
    }
    
    protected void internalToDocument(NodeDocument document) {
    }

    protected NodeDocument internalToNewDocument(String code) {
      return null;
    }
    
  }
  
}
