package org.monet.editor.dsl.ui.descriptor;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;

public class MMLContentDescriber implements IContentDescriber {

  public int describe(InputStream contents, IContentDescription description) throws IOException {
    return VALID;
  }

  public QualifiedName[] getSupportedOptions() {
    // TODO Auto-generated method stub
    return null;
  }

}
