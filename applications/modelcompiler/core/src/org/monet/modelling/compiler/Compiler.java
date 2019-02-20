package org.monet.modelling.compiler;
import org.monet.modelling.compiler.stages.CodeGenerator;
import org.monet.modelling.compiler.stages.CompressModel;
import org.monet.modelling.compiler.stages.ProcessInheritance;

public class Compiler {
  public static void main(String[] args) {
    
    if (args.length < 2 ){
      System.out.println("You must specify the path to the model you want to compile and the output file name");
      
    } else {
     
      String path = args[0];
      String outputPath = args[1];
     
      CodeGenerator generator = new CodeGenerator();
      ProcessInheritance stage = new ProcessInheritance();
      //ProcessTheme processTheme = new ProcessTheme();
      //PreCompiler preCompiler = new PreCompiler();
      CompressModel compress = new CompressModel();
      
      generator.execute(path);
      stage.execute(path);
      //processTheme.execute(path);
      //preCompiler.execute(path);
      compress.execute(outputPath);
    }
    
  }

}
