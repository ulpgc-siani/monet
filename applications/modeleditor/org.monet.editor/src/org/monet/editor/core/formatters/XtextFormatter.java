package org.monet.editor.core.formatters;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextSourceViewerConfiguration;
import org.eclipse.xtext.ui.editor.model.XtextDocument;
import org.monet.editor.core.util.FileHelper;
import org.monet.editor.dsl.ui.internal.MonetLocalizationLanguageActivator;

import com.google.inject.Injector;
import com.google.inject.Provider;

public class XtextFormatter implements Formatter {

  @Override
  public void format(File file) {
    try {      
      Injector injector = MonetLocalizationLanguageActivator.getInstance().getInjector(MonetLocalizationLanguageActivator.ORG_MONET_EDITOR_DSL_MONETMODELINGLANGUAGE);
      Provider<XtextSourceViewerConfiguration> provider = injector.getProvider(XtextSourceViewerConfiguration.class);
      XtextDocument document = loadDocument(injector, file);
      
      IRegion region = new Region(0, document.getLength());      
      IContentFormatter formatter = provider.get().getContentFormatter(null);
      formatter.format(document, region);
      
      FileHelper.writeFile(file, document.get());
    } catch (MalformedTreeException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private XtextDocument loadDocument(Injector injector, File file) throws IOException {
    Provider<XtextDocument> providerDocument = injector.getProvider(XtextDocument.class);
    XtextDocument document = providerDocument.get();
    String source = FileHelper.readFile(file);
    
    document.setInput(loadResource(injector, file));
    document.set(source);
    
    return document;
  }
  
  private XtextResource loadResource(Injector injector, File file) throws IOException {
    Provider<XtextResource> providerResource = injector.getProvider(XtextResource.class);

    XtextResource resource = providerResource.get();
    resource.setURI(URI.createURI(file.toURI().toString()));
    resource.load(null);
    
    return resource;
  }
  
}
