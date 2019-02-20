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
    
    translator.translate(TranslateMode.editor_gramatic, filepath + "/translated/MML/MonetModelingLanguage.xtext");
    System.out.println("MML gramatic created");
    
    translator.translate(TranslateMode.editor_structure, filepath + "/translated/MML/MetaModelStructureImpl.java");
    System.out.println("MML structure created");
    
    translator.translate(TranslateMode.core_java, filepath + "/translated/ModelSerializer");
    System.out.println("JAVA Model Serializers created");
    
    translator.translate(TranslateMode.html, filepath + "/translated/HTML/monet.html");
    System.out.println("HTML Metamodel created");
    
  }

}