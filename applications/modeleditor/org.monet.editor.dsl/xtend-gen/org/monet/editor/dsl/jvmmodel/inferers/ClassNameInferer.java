package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;

@SuppressWarnings("all")
public class ClassNameInferer {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  public String inferDefinition(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    QualifiedName _append = _fullyQualifiedName.append("$Definition$");
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
  
  public String inferSchemaBehaviourName(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    QualifiedName _append = _fullyQualifiedName.append("Schema");
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
  
  public String inferIndicatorFormulaName(final Definition definition, final String indicatorName) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    String definitionName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return ((definitionName + indicatorName) + "Formula");
  }
  
  public String inferTaxonomyClassifierName(final Definition definition, final String indicatorName) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    String definitionName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return ((definitionName + indicatorName) + "Classifier");
  }
  
  public String inferBehaviourName(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    return XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
  }
  
  public String inferExporterScopeName(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    QualifiedName _append = _fullyQualifiedName.append("ExporterScope");
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
  
  public String inferTaskLockClass(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    QualifiedName _append = _fullyQualifiedName.append("Lock");
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
  
  public String inferTaskPlaceClass(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    QualifiedName _append = _fullyQualifiedName.append("Place");
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
  
  public String inferDimensionName(final Property dimension) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(dimension);
    String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return (_convertQualifiedNameToGenName + "Dimension");
  }
  
  public String inferComponentName(final Property dimension) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(dimension);
    String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return (_convertQualifiedNameToGenName + "DimensionComponent");
  }
  
  public String inferCubeName(final Property cube) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(cube);
    String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return (_convertQualifiedNameToGenName + "Cube");
  }
  
  public String inferFactName(final Property cube) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(cube);
    String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return (_convertQualifiedNameToGenName + "CubeFact");
  }
  
  public String inferPropertyName(final Property property) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(property);
    return XtendHelper.convertQualifiedNameToGenNameWithSuffix(_fullyQualifiedName, "Property");
  }
  
  public String inferPropertyBehavior(final Property property) {
    QualifiedName fullyQN = this._iQualifiedNameProvider.getFullyQualifiedName(property);
    boolean _equals = Objects.equal(fullyQN, null);
    if (_equals) {
      EObject _eContainer = property.eContainer();
      QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_eContainer);
      String _id = property.getId();
      QualifiedName _append = _fullyQualifiedName.append(_id);
      fullyQN = _append;
    }
    return XtendHelper.convertQualifiedNameToGenNameWithSuffix(fullyQN, "Behavior");
  }
  
  public String inferMappingName(final Definition definition, final int index) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    String _valueOf = String.valueOf(index);
    String _plus = ("Mapping__" + _valueOf);
    QualifiedName _append = _fullyQualifiedName.append(_plus);
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
  
  public String inferPropertiesName(final Definition definition) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(definition);
    QualifiedName _append = _fullyQualifiedName.append("$Properties$");
    return XtendHelper.convertQualifiedNameToGenName(_append);
  }
}
