package org.monet.editor.preview.stages;

import java.util.ArrayList;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Dictionary;
import org.monet.editor.preview.model.GlobalData;
import org.monet.editor.preview.model.Problem;
import org.monet.editor.preview.model.RenderException;
import org.monet.editor.preview.renders.PreviewRender;
import org.monet.editor.preview.renders.RendersFactory;
import org.monet.editor.preview.utils.FilesystemHelper;
import org.monet.metamodel.Definition;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeViewProperty;

public class CompileDefinitions extends Stage {

  private <T extends Definition> void compileDefinitions(ArrayList<T> definitionList, String outputDir, String language) {
    RendersFactory rendersFactory = RendersFactory.getInstance();

    for (T definition : definitionList) {
      
      if (definition.isAbstract())
        continue;
      
      try {
        PreviewRender render = rendersFactory.get(definition, "page.html", language);
        render.setParameter("path", "../");
        render.setParameter("imagesPath", "../images/");
        FilesystemHelper.writeFile(outputDir + "/pages/" + definition.getName() + ".html", render.getOutput());
        this.problems.addAll(render.getProblems());

        if (definition instanceof NodeDefinition) {
          NodeDefinition nodeDefinition = (NodeDefinition)definition;
          
          for (NodeViewProperty viewDefinition : nodeDefinition.getTabViewList()) {
            PreviewRender renderData = rendersFactory.get(definition, "data.html", language);
            renderData.setParameter("path", "../");
            renderData.setParameter("imagesPath", "../images/");
            renderData.setParameter("view", viewDefinition);
            FilesystemHelper.writeFile(outputDir + "/data/" + definition.getName() + "." + viewDefinition.getCode() + ".json", renderData.getOutput());
            this.problems.addAll(renderData.getProblems());
          }
  
          for (NodeViewProperty viewDefinition : nodeDefinition.getWidgetViewList()) {
            PreviewRender renderData = rendersFactory.get(definition, "data.html", language);
            renderData.setParameter("path", "../");
            renderData.setParameter("imagesPath", "../images/");
            renderData.setParameter("view", viewDefinition);
            FilesystemHelper.writeFile(outputDir + "/data/" + definition.getName() + "." + viewDefinition.getCode() + ".json", renderData.getOutput());
            this.problems.addAll(renderData.getProblems());
          }
          
          if (definition instanceof DocumentDefinition) {
            PreviewRender renderData = rendersFactory.get(definition, "data.html", language);
            renderData.setParameter("path", "../");
            renderData.setParameter("imagesPath", "../images/");
            renderData.setParameter("view", "signs");
            FilesystemHelper.writeFile(outputDir + "/data/" + definition.getName() + "." + "signs.json", renderData.getOutput());
            this.problems.addAll(renderData.getProblems());
          }
        }
      }
      catch (RenderException exception) {
        this.problems.add(new Problem(String.format("Error compiling definition %s", exception.getDefinitionName()), exception.getCause().getClass().toString(), ExceptionUtils.getStackTrace(exception.getCause()), Problem.SEVERITY_ERROR));
      }
      catch (Exception exception) {
        this.problems.add(new Problem(String.format("Error compiling definition %s", definition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }
    }
  }

  private void compileDictionary(String modelDir, String outputDir, String language) {
    Dictionary dictionary = Dictionary.getInstance();
    RendersFactory rendersFactory = RendersFactory.getInstance();

    try {
      PreviewRender render = rendersFactory.get(dictionary, "page.html", language);
      render.setParameter("path", "");
      render.setParameter("imagesPath", "images/");
      FilesystemHelper.writeFile(outputDir + "/index.html", render.getOutput());
      FilesystemHelper.forceDir(outputDir + "/pages");
      FilesystemHelper.forceDir(outputDir + "/data");
      this.problems.addAll(render.getProblems());
    }
    catch (Exception exception) {
      this.problems.add(new Problem(String.format("Error compiling dictionary"), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
    }
  }

  @Override
  public void execute() {
    Dictionary dictionary = Dictionary.getInstance();
    String modelDir = this.globalData.getData(String.class, GlobalData.MODEL_DIRECTORY);
    String outputDir = this.globalData.getData(String.class, GlobalData.OUTPUT_DIRECTORY);
    String language = this.globalData.getData(String.class, GlobalData.LANGUAGE);

    compileDictionary(modelDir, outputDir, language);
    compileDefinitions(dictionary.getDesktopDefinitionList(), outputDir, language);
    compileDefinitions(dictionary.getContainerDefinitionList(), outputDir, language);
    compileDefinitions(dictionary.getCollectionDefinitionList(), outputDir, language);
    compileDefinitions(dictionary.getCatalogDefinitionList(), outputDir, language);
    compileDefinitions(dictionary.getFormDefinitionList(), outputDir, language);
    compileDefinitions(dictionary.getDocumentDefinitionList(), outputDir, language);
    compileDefinitions(dictionary.getCubeDefinitionList(), outputDir, language);
    
  }

  @Override
  public String getStepInfo() {
    return "Prepare output";
  }

}
