package org.monet.metacompiler.engine.renders;

import org.monet.core.metamodel.MetaModel;
import org.monet.metacompiler.engine.MetaModelTranslator.JavaSyntax;
import org.monet.templation.Canvas;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

public abstract class MetaModelRender extends Render {

  protected MetaModel  metamodel;
  protected JavaSyntax syntax;

  private static class Logger implements CanvasLogger {
    @Override
    public void debug(String message, Object... args) {
      System.out.printf(message, args);
    }
  }

  public MetaModelRender() {
    super(new Logger(), Canvas.FROM_RESOURCES_PREFIX + "/templates");
  }

  public void setMetaModel(MetaModel metamodel) {
    this.metamodel = metamodel;
  }

  public JavaSyntax getSyntax() {
    return this.syntax;
  }

  public void setSyntax(JavaSyntax syntax) {
    this.syntax = syntax;
  }

}
