package org.monet.metacompiler.engine.renders;

public class ManifestSerializerRender extends MetaModelRender {

  public ManifestSerializerRender() {
    super();
  }
  
  @Override
  protected void init() {
    loadCanvas("manifest.serializer", true);
    
    addMark("version", this.metamodel.getVersion());
    addMark("date", this.metamodel.getDate());
  }
  
}
