package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.List;

import org.monet.editor.preview.model.Dictionary;
import org.monet.editor.preview.model.Language;
import org.monet.editor.preview.model.Problem;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.templation.Canvas;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

public abstract class PreviewRender extends Render {
  protected RendersFactory rendersFactory;
  protected String codeLanguage;
  protected ArrayList<Problem> problems;
  
  protected static final Integer ELEMENTS_PER_PAGE = 10;
  protected static final Dictionary dictionary = Dictionary.getInstance();
  protected static final Language language = Language.getInstance();

  public static final class Mode {
  	public static final String PAGE = "page";
  	public static final String VIEW = "view";
  }
  
  private static class Logger implements CanvasLogger {
    @Override public void debug(String message, Object... args) {
      System.out.println(String.format(message, args));
    }
  }

  public PreviewRender(String language)  {
  	super(new Logger(), Canvas.FROM_RESOURCES_PREFIX + "/templates/" + language);
  	this.codeLanguage = language;
    this.rendersFactory = RendersFactory.getInstance();
    this.problems = new ArrayList<Problem>();
  }
  
  public void setTemplate(String template) {
    Integer pos = template.lastIndexOf("?");
    if (pos == -1) pos = template.length();
    this.template = template.substring(0, pos);
    this.template = this.template.replaceAll(".html", "");
    this.template = this.template.replaceAll(".js", "");
    if (pos < template.length()) this.setParameters(template.substring(pos+1));
  }
  
  protected boolean isSystemView(NodeViewProperty viewDefinition) {

    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getLinksIn() != null || showDefinition.getLinksOut() != null || 
             showDefinition.getNotes() != null || showDefinition.getRevisions() != null || showDefinition.getTasks() != null;
    }  
    
    if (viewDefinition instanceof SetViewProperty) {
      SetViewProperty.ShowProperty showDefinition = ((SetViewProperty) viewDefinition).getShow();
      return showDefinition.getPrototypes() != null; 
    }  

    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return showDefinition.getLinksIn() != null || showDefinition.getLinksOut() != null || 
             showDefinition.getNotes() != null || showDefinition.getRevisions() != null || showDefinition.getTasks() != null ||
             showDefinition.getAttachments() != null;
    }  
    
    return false;
  }
  
  protected boolean isNotesSystemView(NodeViewProperty viewDefinition) {
    
    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getNotes() != null;
    }
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return showDefinition.getNotes() != null;
    }
    
    return false;
  }

  protected boolean isLinksInSystemView(NodeViewProperty viewDefinition) {
    
    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getLinksIn() != null;
    }
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return showDefinition.getLinksIn() != null;
    }
    
    return false;
  }

  protected boolean isLinksOutSystemView(NodeViewProperty viewDefinition) {
    
    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getLinksOut() != null;
    }
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return showDefinition.getLinksOut() != null;
    }
    
    return false;
  }
  
  protected boolean isAttachmentsSystemView(NodeViewProperty viewDefinition) {
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return (showDefinition.getAttachments() != null);
    }
    
    return false;
  }

  protected boolean isRevisionsSystemView(NodeViewProperty viewDefinition) {
    
    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getRevisions() != null;
    }
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return showDefinition.getRevisions() != null;
    }
    
    return false;
  }
  
  protected boolean isPrototypesSystemView(NodeViewProperty viewDefinition) {
    if (! (viewDefinition instanceof SetViewProperty)) return false;
    return ((SetViewProperty)viewDefinition).getShow().getPrototypes() != null;
  }
  
  protected boolean isTasksSystemView(NodeViewProperty viewDefinition) {
    
    if (viewDefinition instanceof DesktopDefinition.ViewProperty) {
      DesktopDefinition.ViewProperty.ShowProperty showDefinition = ((DesktopDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getTasks() != null;
    }  

    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      return showDefinition.getTasks() != null;
    }  
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      return showDefinition.getTasks() != null;
    }  
    
    return false;
  }
  
  public String getDefinitionType(NodeDefinition definition) {
    if (definition.isForm()) return "form";
    else if (definition.isDesktop()) return "desktop";
    else if (definition.isContainer()) return "container";
    else if (definition.isCollection()) return "collection";
    return "notype";
  }
  
  public List<Problem> getProblems() {
    return this.problems;
  }
  
}
