package org.monet.editor.dsl.scoping;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.monet.editor.MonetLog;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration;

import com.google.inject.Inject;

public class MonetModelingLanguageImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

  @Inject
  private IQualifiedNameProvider qualifiedNameProvider;

  private IProject               currentIProject;
  private String                 currentPackageBase;

  @Override
  protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(EObject context, boolean ignoreCase) {
    List<ImportNormalizer> result = super.internalGetImportedNamespaceResolvers(context, ignoreCase);
    result.add(createImportedNamespaceResolver("java.lang.Boolean", false));
    result.add(createImportedNamespaceResolver("java.lang.String", false));
    result.add(createImportedNamespaceResolver("java.lang.Integer", false));
    result.add(createImportedNamespaceResolver("java.lang.Double", false));
    result.add(createImportedNamespaceResolver("java.lang.SupressWarnings", false));
    result.add(createImportedNamespaceResolver("java.util.ArrayList", false));
    result.add(createImportedNamespaceResolver("java.lang.Comparable", false));
    result.add(createImportedNamespaceResolver("java.lang.Exception", false));
    result.add(createImportedNamespaceResolver("java.text.ParseException", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorImporter", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorSource", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNode", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNodeCatalog", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNodeCollection", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNodeContainer", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNodeDesktop", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNodeDocument", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorNodeForm", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorServiceProvider", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BehaviorTask", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.BusinessUnit", false));
    
    // services
    result.add(createImportedNamespaceResolver("org.monet.bpi.ClientService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.DatastoreService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.DelivererService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FileService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.EventService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ConsoleService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NewsService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.SourceService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ExporterService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ImporterService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.MailService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.RoleService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.TranslationService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.JobService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.TaskService", false));

    result.add(createImportedNamespaceResolver("org.monet.bpi.Expression", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Field", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldBoolean", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldCheck", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldDate", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldFile", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldLink", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldMemo", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldMultiple", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldNode", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldNumber", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldPicture", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldComposite", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldSelect", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldSerial", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldSummation", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.FieldText", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Georeferenced", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.IndexEntry", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Lock", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.LockForm", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.LockService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.MonetLink", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Cube", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Dimension", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Source", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Node", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeCatalog", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeCollection", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeContainer", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeDesktop", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeDocument", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.NodeForm", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.OperationExpression", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Outsourcing", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Param", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Post", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Schema", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.SchemaSection", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Task", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Thesaurus", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ThesaurusService", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ContestantRequest", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ContestantResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.CustomerRequest", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.CustomerResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.InsourcingRequest", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.InsourcingResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ProviderRequest", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ProviderResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.TransferRequest", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.TransferResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Job", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.JobRequest", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.JobResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.JobSetup", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.SensorResponse", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Check", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.CheckList", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Date", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.File", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Link", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Location", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Number", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Picture", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.SummationItem", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Term", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.TermList", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.types.Event", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Setup", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.ValidationResult", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.DelegationSetup", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.WaitSetup", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.TimeoutSetup", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.RoleChooser", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.Role", false));
    result.add(createImportedNamespaceResolver("org.monet.bpi.User", false));
    
    if(context instanceof PackageDeclaration) {
      String currentPackage = ((PackageDeclaration)context).getName();
      result.add(createImportedNamespaceResolver(currentPackage + ".*", false));
    }
    
    IProject project = XtendHelper.getIProject(context);
    if(project != this.currentIProject) {
      this.currentIProject = project;
      this.currentPackageBase = ProjectHelper.getPackageBase(project);
    }
    result.add(createImportedNamespaceResolver(this.currentPackageBase + ".Assets", false));
    result.add(createImportedNamespaceResolver(this.currentPackageBase + ".Resources", false));
    
    try {
      if(context instanceof Definition) {
        QualifiedName contextQualifiedName = qualifiedNameProvider.apply(context);
        if(context != null && contextQualifiedName != null) {
          Definition definition = (Definition)context;
          if(definition.getDefinitionType().equals("task")) {
            result.add(createImportedNamespaceResolver(XtendHelper.convertQualifiedNameToGenName(contextQualifiedName.append("Place")), false));
            result.add(createImportedNamespaceResolver(XtendHelper.convertQualifiedNameToGenName(contextQualifiedName.append("Lock")), false));
          }
          if(definition.getDefinitionType().equals("form") ||
             definition.getDefinitionType().equals("document")) {
            result.add(createImportedNamespaceResolver(XtendHelper.convertQualifiedNameToGenName(contextQualifiedName.append("Schema")), false));
          }
          
          result.add(createImportedNamespaceResolver(contextQualifiedName.toString().toLowerCase() + ".*", false));
        }
      }
    } catch(Throwable ex) {
      MonetLog.print(ex);
    }
    
    return result;
  }
}
