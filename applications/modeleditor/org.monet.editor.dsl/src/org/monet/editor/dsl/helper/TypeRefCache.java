package org.monet.editor.dsl.helper;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;

public interface TypeRefCache {

  public void reset();
  public JvmTypeReference get(Object key);
  public JvmTypeReference resolve(EObject ctx, Object key, JvmTypeReference ... typeArgs);
  
  public JvmTypeReference resolveObjectType(EObject ctx);
  public JvmTypeReference resolveStringType(EObject ctx); 
  public JvmTypeReference resolveIntegerType(EObject ctx); 
  public JvmTypeReference resolveDoubleType(EObject ctx); 
  public JvmTypeReference resolveLongType(EObject ctx); 
  public JvmTypeReference resolveClassType(EObject ctx);
  public JvmTypeReference resolveComparableType(EObject ctx, JvmTypeReference... typeArgs);
  public JvmTypeReference resolveVoidType(EObject ctx);
  public JvmTypeReference resolveIterableType(EObject ctx, JvmTypeReference... typeArgs);
  public JvmTypeReference resolveArrayListType(EObject ctx, JvmTypeReference... typeArgs);
  public JvmTypeReference resolveDoubleArrayPrimitiveType(EObject ctx);
  public JvmTypeReference resolveDoublePrimitiveType(EObject ctx);
  public JvmTypeReference resolveBooleanPrimitiveType(EObject ctx);
  public JvmTypeReference resolveMapType(EObject ctx, JvmTypeReference... typeArgs);
  
  public JvmTypeReference resolveHasMapType(EObject ctx, JvmTypeReference... typeArgs);
  
  public JvmTypeReference resolveDimensionImplType(EObject ctx);
  public JvmTypeReference resolveDimensionComponentImplType(EObject ctx);
  public JvmTypeReference resolveCubeImplType(EObject ctx);
  public JvmTypeReference resolveCubeFactImplType(EObject ctx);
  public JvmTypeReference resolveLockType(EObject ctx);
  public JvmTypeReference resolveProjectType(EObject ctx);
  public JvmTypeReference resolveDistributionType(EObject ctx);
  public JvmTypeReference resolveNodeType(EObject ctx);
  public JvmTypeReference resolveValidationResultType(EObject ctx);
  public JvmTypeReference resolveRoleChooserType(EObject ctx);
  public JvmTypeReference resolveRoleType(EObject ctx);
  public JvmTypeReference resolveDelegationSetupType(EObject ctx);
  public JvmTypeReference resolveWaitSetupType(EObject ctx);
  public JvmTypeReference resolveTimeoutSetupType(EObject ctx);
  public JvmTypeReference resolveJobSetupType(EObject ctx);
  public JvmTypeReference resolveMappingType(EObject ctx);
  public JvmTypeReference resolveMappingImplType(EObject ctx);
  public JvmTypeReference resolveHasMappingsType(EObject ctx);
  public JvmTypeReference resolveHasPropertiesType(EObject ctx);
  public JvmTypeReference resolveExporterScopeType(EObject ctx);
  public JvmTypeReference resolveExporterScopeImplType(EObject ctx);
  public JvmTypeReference resolveImporterScopeType(EObject ctx);
  public JvmTypeReference resolveExpressionType(EObject ctx);
  public JvmTypeReference resolveOrderExpressionType(EObject ctx);
  public JvmTypeReference resolveNodeDocumentType(EObject ctx);
  public JvmTypeReference resolveSchemaType(EObject ctx);
  public JvmTypeReference resolveSchemaSectionType(EObject ctx);
  public JvmTypeReference resolveBPIFileType(EObject ctx);
  public JvmTypeReference resolveURIType(EObject ctx);
  public JvmTypeReference resolveParamType(EObject ctx);
  public JvmTypeReference resolveTermType(EObject ctx);
  public JvmTypeReference resolveTermListType(EObject ctx);
  public JvmTypeReference resolveDateType(EObject ctx);
  public JvmTypeReference resolveHasBehaviourType(EObject ctx);
  public JvmTypeReference resolveFieldMultipleType(EObject ctx, JvmTypeReference... typeArgs);
  public JvmTypeReference resolveTransferResponseType(EObject ctx);
  public JvmTypeReference resolveTransferRequestType(EObject ctx);
  public JvmTypeReference resolveBehaviorTaskContestImplType(EObject ctx);
  public JvmTypeReference resolveCustomerResponseType(EObject ctx);
  public JvmTypeReference resolveCustomerRequestType(EObject ctx);
  public JvmTypeReference resolveContestantResponseType(EObject ctx);
  public JvmTypeReference resolveContestantRequestType(EObject ctx);
  public JvmTypeReference resolveBehaviorTaskCustomerImplType(EObject ctx);
  public JvmTypeReference resolveBehaviorTaskContestantImplType(EObject ctx);
  public JvmTypeReference resolveProviderResponseType(EObject ctx);
  public JvmTypeReference resolveProviderRequestType(EObject ctx);
  public JvmTypeReference resolveJobType(EObject ctx);
  public JvmTypeReference resolveJobResponseType(EObject ctx);
  public JvmTypeReference resolveJobRequestType(EObject ctx);
  public JvmTypeReference resolveBehaviorTaskProviderExternalImplType(EObject ctx);
  public JvmTypeReference resolveInsourcingResponseType(EObject ctx);
  public JvmTypeReference resolveInsourcingRequestType(EObject ctx);
  public JvmTypeReference resolveBehaviorTaskProviderInternalImplType(EObject ctx);
  
}
