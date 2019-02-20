package org.monet.editor.dsl.ui.descriptor;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.monet.editor.Constants;
import org.monet.editor.library.StreamHelper;

public class LanguageContentDescriber implements IContentDescriber {

  public int describe(InputStream contents, IContentDescription description) throws IOException {
    if(description == null || description.getContentType().getId().equals(Constants.LANGUAGE_CONTENT_TYPE)) {
      return StreamHelper.toString(contents).trim().startsWith("language") ? VALID : INVALID;
    }
    return INVALID;
  }

  public QualifiedName[] getSupportedOptions() {
    // TODO Auto-generated method stub
    return null;
  }

}
