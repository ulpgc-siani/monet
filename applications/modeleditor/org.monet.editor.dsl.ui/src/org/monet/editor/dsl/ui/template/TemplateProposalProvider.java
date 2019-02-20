package org.monet.editor.dsl.ui.template;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;
import org.eclipse.xtext.ui.editor.templates.DefaultTemplateProposalProvider;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.metamodel.MetaModelStructure;
import org.monet.editor.dsl.metamodel.Pair;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.Referenciable;
import org.monet.editor.dsl.monetModelingLanguage.Schema;
import org.monet.editor.dsl.monetModelingLanguage.SchemaProperty;
import org.monet.editor.dsl.monetModelingLanguage.XTReference;
import org.monet.editor.dsl.services.MonetModelingLanguageGrammarAccess;
import org.monet.editor.library.LibraryCodeGenerator;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class TemplateProposalProvider extends DefaultTemplateProposalProvider {

  private ContextTypeIdHelper        helper;

  @Inject
  MonetModelingLanguageGrammarAccess ga;

  @Inject
  private MetaModelStructure         metaModelStructure;

  @Inject
  private IQualifiedNameProvider     qualifiedNameProvider;

  @Inject
  private TypeReferences             references;

  @Inject
  public TemplateProposalProvider(TemplateStore templateStore, ContextTypeRegistry registry, ContextTypeIdHelper helper) {
    super(templateStore, registry, helper);
    this.helper = helper;
  }

  private static final String   PROPERTY_PROPOSAL             = "%s%s%s%s";
  private static final String   ATTRIBUTE_PROPOSAL            = "%s = %s;";
  private static final String   ATTRIBUTE_EXPRESSION_PROPOSAL = "%s = { %s }";
  private static final String   METHOD_PROPOSAL               = "%s(%s) {\n\n}";
  private static final String   PROPERTY_CODE                 = "[\"%s\"]\n";
  private static final String   PROPERTY_NAME                 = " ${%sName}";

  private static final String[] SchemaTypes                   = { "Term", "Date", "CheckList", "String", "Number", "File", "Picture", "Boolean" };

  @Override
  protected void createTemplates(TemplateContext templateContext, ContentAssistContext context, ITemplateAcceptor acceptor) {

    Pair<EObject, Item> pair = checkElement(context.getCurrentModel());
    EObject model = pair.getFirst();
    Definition definition = (pair != null && pair.getFirst() instanceof Definition) ? (Definition)pair.getFirst() : null;
    Item currentItem = pair != null ? pair.getSecond() : null;
    if (context.getCurrentModel() instanceof SchemaProperty) {
      for (String type : SchemaTypes) {
        Template template = new Template(type, "SchemaType", type + "SchemaType", type, false);
        TemplateProposal tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
        acceptor.accept(tp);
      }
    } else if (context.getCurrentModel() instanceof Schema) {
      Template template = new Template("Attribute", "Schema", "SimpleSchemaAttribute", "${attributeName} ${attributeType};", false);
      TemplateProposal tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
      acceptor.accept(tp);
      template = new Template("Attribute (with value)", "Schema", "SimpleSchemaAttribute", "${attributeName} ${attributeType} { ${attributeValue} }", false);
      tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
      acceptor.accept(tp);
      template = new Template("Attribute (multiple)", "Schema", "ManySchemaAttribute", "many ${attributeName} ${attributeType} { ${attributeValue} }", false);
      tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
      acceptor.accept(tp);
      template = new Template("Section", "Schema", "SchemaSection", "${sectionName} { ${attributes} }", false);
      tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
      acceptor.accept(tp);
      template = new Template("Section (multiple)", "Schema", "ManySchemaSection", "many ${sectionName} { ${attributes} }", false);
      tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
      acceptor.accept(tp);
    }

    if(currentItem == null)
      return;
    
    // Is an enumeration attribute?
    if (currentItem.getType() == Item.ATTRIBUTE && currentItem.getValueTypeQualifiedName().contains("Enumeration")) {
      Class<?> clazz;
      try {
        clazz = XtendHelper.getEnumType(currentItem.getValueTypeQualifiedName());
        if (clazz != null && clazz.isEnum()) {
          for (Object obj : clazz.getEnumConstants()) {
            Template template = new Template(obj.toString(), clazz.getSimpleName(), clazz.getName() + obj.toString(), obj.toString(), false);
            TemplateProposal tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
            acceptor.accept(tp);
          }
        }
      } catch (ClassNotFoundException e) {
      }
      return;
    }

    if (currentItem.getType() == Item.ATTRIBUTE && currentItem.getValueTypeQualifiedName().startsWith("resource:")) {
      String resourceType = currentItem.getValueTypeQualifiedName();
      resourceType = resourceType.substring(resourceType.indexOf(":") + 1);
      

      String packageBase = ProjectHelper.getPackageBase(XtendHelper.getIProject(model));
      String resourcesClassName = String.format("%s.Resources$%s", packageBase, JavaHelper.toJavaIdentifier(resourceType));
      JvmTypeReference type = this.references.getTypeForName(resourcesClassName, model);
      if(type != null) {
        JvmGenericType jvmType = ((JvmGenericType) type.getType());
        for (JvmMember member : jvmType.getMembers()) {
          if (member instanceof JvmField) {
            Template template = new Template(member.getSimpleName(), resourceType + " Resource", member.getSimpleName(), resourceType + " " + member.getSimpleName(), false);
            TemplateProposal tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
            acceptor.accept(tp);
          }
        }
      }
      return;
    }
    
    if (context.getCurrentModel() instanceof Attribute && currentItem.getValueTypeQualifiedName().equals("language")) {
      IProject project = XtendHelper.getIProject(model);
      String packageBase = ProjectHelper.getPackageBase(project);
      
      JvmTypeReference type = references.getTypeForName(packageBase + ".Language", model);
      if (type != null) {
        JvmGenericType jvmType = ((JvmGenericType) type.getType());
        for (JvmMember member : jvmType.getMembers()) {
          if (member instanceof JvmField) {
            Template template = new Template(member.getSimpleName(), "Text Resource", member.getSimpleName(), "text " + member.getSimpleName(), false);
            TemplateProposal tp = createProposal(template, templateContext, context, getImage(template), getRelevance(template));
            acceptor.accept(tp);
          }
        }
      }
      return;
    }

    for (Item childProposal : currentItem.getItems()) {
      String proposalTemplate = null;
      String description = null;
      if (notInValidContext(ga.getAttributeRule(), templateContext) && notInValidContext(ga.getMethodRule(), templateContext) && notInValidContext(ga.getPropertyRule(), templateContext))
        continue;
      switch (childProposal.getType()) {
        case Item.ATTRIBUTE:
          description = "Attribute";
          if (childProposal.getValueTypeQualifiedName().startsWith("Expression:"))
            proposalTemplate = String.format(ATTRIBUTE_EXPRESSION_PROPOSAL, childProposal.getToken(), this.getTemplateForValue(childProposal));
          else if (childProposal.getValueTypeQualifiedName().equals("variable")) {
            description = JavaHelper.toJavaIdentifier(childProposal.getToken());
            proposalTemplate = childProposal.getToken() + " ${varName} = \"${varValue}\";";
          } else
            proposalTemplate = String.format(ATTRIBUTE_PROPOSAL, childProposal.getToken(), this.getTemplateForValue(childProposal));
          break;
        case Item.PROPERTY:
          StringBuilder builder = new StringBuilder();
          this.createPropertyTemplate(definition, childProposal, builder, context.getCurrentModel());
          String code = childProposal.hasCode() && childProposal.isCodeRequired() ? String.format(PROPERTY_CODE, LibraryCodeGenerator.generateCode(childProposal.getToken())) : "";
          String name = childProposal.hasName() && childProposal.isNameRequired() ? String.format(PROPERTY_NAME, JavaHelper.toJavaIdentifier(childProposal.getToken())) : "";
          proposalTemplate = String.format(PROPERTY_PROPOSAL, code, childProposal.getToken(), name, builder.toString());
          description = "Property";
          break;
        case Item.METHOD:
          proposalTemplate = methodProposal(definition, childProposal, context.getCurrentModel());
          description = "Method";
          break;
        default:
          continue;
      }

      Template template = new Template(childProposal.getToken(), description, childProposal.getToken(), proposalTemplate, false);
      template.setHelp(childProposal.getDescription() + (childProposal.getHint().isEmpty() ? "" : "\n\nHint: " + childProposal.getHint()));
      TemplateProposal tp = doCreateProposal(template, templateContext, context, getImage(template), getRelevance(template));
      acceptor.accept(tp);
    }
  }

  protected TemplateProposal doCreateProposal(Template template, TemplateContext templateContext, ContentAssistContext context, Image image, int relevance) {
    if (!validate(template, context))
      return null;
    TemplateProposal tp = new MonetTemplateProposal(template, templateContext, context.getReplaceRegion(), image, relevance);
    return tp;
  }

  protected String methodProposal(Definition definition, Item childProposal, EObject current) {
    String proposalTemplate;
    StringBuilder parameters = new StringBuilder();
    for (Item parameter : childProposal.getItems()) {
      String type = parameter.getValueTypeQualifiedName();
      if (type == null)
        continue;
      if (type.startsWith("/")) {
        String relativeTypeName = type.substring(1).replaceAll("/", ".");
        QualifiedName typeQualifiedName = qualifiedNameProvider.getFullyQualifiedName(definition);
        if(!relativeTypeName.trim().isEmpty())
          typeQualifiedName = typeQualifiedName.append(relativeTypeName);
        type = XtendHelper.convertQualifiedNameToGenName(typeQualifiedName);
      } else if (type.startsWith("../")) {
        EObject eType = current;
        type = type.substring(3);
        while (type.startsWith("../")) {
          eType = eType.eContainer();
          type = type.substring(3);
        }
        String[] childPath = type.split("/");
        for (String child : childPath) {
          if (eType instanceof Attribute) {
            Attribute attribute = (Attribute) eType;
            if (attribute.getValue() instanceof XTReference) {
              Referenciable referenciable = ((XTReference) attribute.getValue()).getValue();
              if ((referenciable instanceof Definition && ((Definition) referenciable).getDefinitionType().equals(child)) ||
                  (referenciable instanceof Property && ((Property) referenciable).getId().equals(child))) {
                eType = referenciable;
                continue;
              }
            }
          } else {
            for (EObject eChild : eType.eContents()) {
              if ((eChild instanceof Definition && ((Definition) eChild).getDefinitionType().equals(child)) ||
                  (eChild instanceof Property && ((Property) eChild).getId().equals(child)) || 
                  (eChild instanceof Schema && ((Schema) eChild).getId().equals(child)) || 
                  (eChild instanceof Attribute && ((Attribute) eChild).getId().equals(child))) {
                eType = eChild;
                break;
              }
            }
          }
        }

        QualifiedName typeQualifiedName = null; 
        if (eType instanceof Attribute) {
          Attribute attribute = (Attribute) eType;
          if (attribute.getValue() instanceof XTReference) {
            typeQualifiedName = qualifiedNameProvider.getFullyQualifiedName(((XTReference) attribute.getValue()).getValue());
          }
        } else if (eType instanceof Schema) {
          typeQualifiedName = qualifiedNameProvider.getFullyQualifiedName(eType.eContainer()).append("Schema");
        } else {
          typeQualifiedName = qualifiedNameProvider.getFullyQualifiedName(eType);
        }
        type = XtendHelper.convertQualifiedNameToGenName(typeQualifiedName);
      }
      parameters.append(type);
      parameters.append(" ");
      parameters.append(parameter.getName());
      parameters.append(",");
    }
    if (parameters.length() > 0)
      parameters.delete(parameters.length() - 1, parameters.length());
    proposalTemplate = String.format(METHOD_PROPOSAL, childProposal.getToken(), parameters.toString());
    return proposalTemplate;
  }

  protected void createPropertyTemplate(Definition definition, Item childProposal, StringBuilder builder, EObject current) {
    ArrayList<Item> requiredChilds = childProposal.getRequiredItems();
    if (requiredChilds.size() > 0) {
      builder.append(" {\n");
      for (Item child : requiredChilds) {
        builder.append("\t");
        switch (child.getType()) {
          case Item.ATTRIBUTE:
            if (child.getValueTypeQualifiedName().startsWith("Expression:"))
              builder.append(String.format(ATTRIBUTE_EXPRESSION_PROPOSAL, child.getToken(), this.getTemplateForValue(child)));
            else
              builder.append(String.format(ATTRIBUTE_PROPOSAL, child.getToken(), this.getTemplateForValue(child)));
            break;
          case Item.PROPERTY:
            StringBuilder childBuilder = new StringBuilder();
            createPropertyTemplate(definition, child, childBuilder, current);
            String name = child.hasName() && child.isNameRequired() ? String.format(PROPERTY_NAME, JavaHelper.toJavaIdentifier(child.getToken())) : "";
            String code = child.hasCode() && child.isCodeRequired() ? String.format(PROPERTY_CODE, LibraryCodeGenerator.generateCode(name)) : "";
            builder.append(String.format(PROPERTY_PROPOSAL, code, child.getToken(), name, childBuilder.toString()));
            break;
          case Item.METHOD:
            builder.append(methodProposal(definition, child, current));
            break;
        }
        builder.append("\n");
      }
      builder.append("}");
    } else if (childProposal.getItems().size() > 0) {
      builder.append(" {  }");
    } else {
      builder.append(";");
    }
  }

  protected String getTemplateForValue(Item child) {
    if (child.getMetaModelType().equals("uuid"))
      return "\"" + UUID.randomUUID().toString() + "\"";
    else if (child.getValueTypeQualifiedName().equals("java.lang.String"))
      return "\"${T" + JavaHelper.toJavaIdentifier(child.getToken()) + "}\"";
    else if (child.getValueTypeQualifiedName().equals("java.lang.Integer") || child.getValueTypeQualifiedName().equals("java.lang.Double"))
      return "${TValue}";
    else
      return "${T" + JavaHelper.toJavaIdentifier(child.getValueTypeQualifiedName().substring(child.getValueTypeQualifiedName().lastIndexOf(".") + 1).replaceAll("\\$", "")) + "}";
  }

  private boolean notInValidContext(AbstractRule rule, TemplateContext templateContext) {
    return !templateContext.getContextType().getId().equals(helper.getId(rule));
  }

  private Pair<EObject, Item> checkElement(EObject current) {
    LinkedList<EObject> queue = new LinkedList<EObject>();
    Item definitionItem = XtendHelper.getRootElement(current, queue, this.metaModelStructure, false);
    EObject model = queue.peek();
    Item currentItem = XtendHelper.checkElement(definitionItem, queue);

    return new Pair<EObject, Item>(model, currentItem);
  }
}