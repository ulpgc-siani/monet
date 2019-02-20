package org.monet.metacompiler.control;

import org.monet.core.metamodel.MetaModel;
import org.monet.metacompiler.engine.MetaModelLoader;
import org.monet.metacompiler.engine.MetaModelTranslator;
import org.monet.metacompiler.engine.MetaModelTranslator.TranslateMode;

public class Main {

  static private Main instance = new Main();

  public static void main(String args[])  {
    if (args.length < 1) {
      System.out.println("Usage: java -jar metacompiler.jar 'path to metamodel'");
      return;
    }
    try {
      instance.execute(args[0]);
    }
    catch (ClassNotFoundException e) {
      System.out.printf("ERROR. SimpleXML tiene reservado el uso del atributo class");
    }
    catch (Exception e) {
      System.out.printf("ERROR. %s", e.getMessage());
      e.printStackTrace();
    }
  }

  private void execute(String filepath) throws Exception {
    System.out.println("Loading metamodel...");
    MetaModelLoader loader = new MetaModelLoader();
    MetaModel metamodel = loader.load(filepath + "/source");
    System.out.println("Metamodel loaded");
    
    MetaModelTranslator translator = new MetaModelTranslator(metamodel);
    
    translator.translate(TranslateMode.html, filepath + "/generated/HTML/monet.html");
    System.out.println("HTML Metamodel created");
    
    translator.translate(TranslateMode.xsd, filepath + "/generated/XSD/monet.xsd");
    System.out.println("XSD Metamodel created");
    
    translator.translate(TranslateMode.core_java, filepath + "/generated/CoreModelSerializer");
    System.out.println("Core Model JAVA Serializers created");
    
    translator.translate(TranslateMode.editor_java, filepath + "/generated/EditorModelSerializer");
    System.out.println("Editor Model JAVA Serializers created");
    
    translator.translate(TranslateMode.editor_semantic, filepath + "/generated/EditorSemanticRules");
    System.out.println("Semantic rules for model editor created");
    
    translator.translate(TranslateMode.editor_sync_ddbb, filepath + "/generated/EditorSyncDDBB");
    System.out.println("Sync DDBB for model editor created");
  }

}