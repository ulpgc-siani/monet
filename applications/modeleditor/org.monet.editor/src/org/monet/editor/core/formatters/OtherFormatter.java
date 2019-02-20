package org.monet.editor.core.formatters;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.monet.editor.core.util.FileHelper;

public class OtherFormatter implements Formatter {

  @Override
  public void format(File file) {
    final CodeFormatter codeFormatter = getEclipseFormatter();
    
    try {
      String source = FileHelper.readFile(file);
      
      final TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT,
          source,
          0,
          source.length(),
          0,
          System.getProperty("line.separator")
          );
        
      IDocument document = new Document(source);
      edit.apply(document);
      
      FileHelper.writeFile(file, document.get());
    } catch (MalformedTreeException e) {
      e.printStackTrace();
    } catch (BadLocationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private CodeFormatter getEclipseFormatter() {
    Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
    
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
    
    return ToolFactory.createCodeFormatter(JavaCore.getOptions(),ToolFactory.M_FORMAT_EXISTING);
  }

}
