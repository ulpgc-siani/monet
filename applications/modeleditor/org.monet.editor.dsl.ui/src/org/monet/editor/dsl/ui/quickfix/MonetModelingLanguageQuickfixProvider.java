package org.monet.editor.dsl.ui.quickfix;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguageFactory;
import org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.validation.MonetModelingLanguageJavaValidator;
import org.monet.editor.library.LibraryCodeGenerator;

public class MonetModelingLanguageQuickfixProvider extends DefaultQuickfixProvider {

  private MonetModelingLanguageFactory languageFactory = MonetModelingLanguageFactory.eINSTANCE;
  
  @Fix(MonetModelingLanguageJavaValidator.CODE_DUPLICATED)
  public void regenerateCode(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Generate a new code", "Generates a new code.", "upcase.png", new ISemanticModification() {
      public void apply(EObject element, IModificationContext context) {
        String generatedCode = LibraryCodeGenerator.generateCode("");
        Code code = (Code)element;
        code.setValue(generatedCode);
      }
    });
  }
  
  @Fix(MonetModelingLanguageJavaValidator.CODE_NOT_FOUND)
  public void generateCode(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Add a generated code", "Adds a generated code.", "upcase.png", new ISemanticModification() {
      public void apply(EObject element, IModificationContext context) {
        String generatedCode = LibraryCodeGenerator.generateCode("");
        Code newCode = languageFactory.createCode();
        newCode.setValue(generatedCode);
        if(element instanceof Definition) {
          Definition definition = (Definition)element;
          definition.setCode(newCode);
        } else {
          Property property = (Property)element;
          property.setCode(newCode);
        }
      }
    });
  }
  
  @Fix(MonetModelingLanguageJavaValidator.NAME_NOT_FOUND)
  public void generateName(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Add a genearted name", "Generates a new name.", null, new ISemanticModification() {
      public void apply(EObject element, IModificationContext context) {
        String generatedCode = "TODO";
        if(element instanceof Definition) {
          Definition definition = (Definition)element;
          definition.setName(generatedCode);
        } else {
          Property property = (Property)element;
          property.setId(generatedCode);
        }
      }
    });
  }
  
  @Fix(MonetModelingLanguageJavaValidator.PACKAGE_FOLDER_NOT_MACTH)
  public void moveOrRenamePackage(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Rename package to match current folder", "Renames package.", null, new ISemanticModification() {
      public void apply(EObject element, IModificationContext context) {
        List<String> fileParts = element.eResource().getURI().segmentsList();
        StringBuilder path = new StringBuilder();
        int filePartsSize = fileParts.size();
        for(int i=0;i<filePartsSize;i++) {
          if(i < 3 || i +1 == filePartsSize)
            continue;
          path.append(fileParts.get(i));
          path.append(".");
        }
        if(path.length() > 0)
          path.delete(path.length()-1, path.length());
        
        PackageDeclaration packageDeclaration = (PackageDeclaration)element;
        packageDeclaration.setName(path.toString());
      }
    });
  }

  // (MARIO - XText 2.4.0) Overriding this method fix a great bug in XText finding packages in a finite but expensive loop
  @Override
  protected Iterable<IEObjectDescription> queryScope(IScope scope) {
    return new ArrayList<IEObjectDescription>();
  }

  
//	@Fix(MyJavaValidator.INVALID_NAME)
//	public void capitalizeName(final Issue issue, IssueResolutionAcceptor acceptor) {
//		acceptor.accept(issue, "Capitalize name", "Capitalize the name.", "upcase.png", new IModification() {
//			public void apply(IModificationContext context) throws BadLocationException {
//				IXtextDocument xtextDocument = context.getXtextDocument();
//				String firstLetter = xtextDocument.get(issue.getOffset(), 1);
//				xtextDocument.replace(issue.getOffset(), 1, firstLetter.toUpperCase());
//			}
//		});
//	}

}
