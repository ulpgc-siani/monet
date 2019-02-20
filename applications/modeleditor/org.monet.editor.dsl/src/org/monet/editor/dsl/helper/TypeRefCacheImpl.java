package org.monet.editor.dsl.helper;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.monet.bpi.ContestantRequest;
import org.monet.bpi.ContestantResponse;
import org.monet.bpi.CustomerRequest;
import org.monet.bpi.CustomerResponse;
import org.monet.bpi.DelegationSetup;
import org.monet.bpi.ExporterScope;
import org.monet.bpi.Expression;
import org.monet.bpi.FieldMultiple;
import org.monet.bpi.ImporterScope;
import org.monet.bpi.InsourcingRequest;
import org.monet.bpi.InsourcingResponse;
import org.monet.bpi.Job;
import org.monet.bpi.JobRequest;
import org.monet.bpi.JobResponse;
import org.monet.bpi.JobSetup;
import org.monet.bpi.Mapping;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.OrderExpression;
import org.monet.bpi.ProviderRequest;
import org.monet.bpi.ProviderResponse;
import org.monet.bpi.Role;
import org.monet.bpi.RoleChooser;
import org.monet.bpi.TimeoutSetup;
import org.monet.bpi.TransferRequest;
import org.monet.bpi.TransferResponse;
import org.monet.bpi.ValidationResult;
import org.monet.bpi.WaitSetup;
import org.monet.bpi.java.BehaviorTaskContestImpl;
import org.monet.bpi.java.BehaviorTaskContestantImpl;
import org.monet.bpi.java.BehaviorTaskCustomerImpl;
import org.monet.bpi.java.BehaviorTaskProviderExternalImpl;
import org.monet.bpi.java.BehaviorTaskProviderInternalImpl;
import org.monet.bpi.java.CubeFactImpl;
import org.monet.bpi.java.CubeImpl;
import org.monet.bpi.java.DimensionComponentImpl;
import org.monet.bpi.java.DimensionImpl;
import org.monet.bpi.java.ExporterImpl.ExporterScopeImpl;
import org.monet.bpi.java.MappingImpl;
import org.monet.editor.MonetLog;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.metamodel.interfaces.HasBehaviour;
import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.HasProperties;
import org.monet.metamodel.internal.Lock;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class TypeRefCacheImpl implements TypeRefCache {

  @Inject
  private JvmTypesBuilder typesBuilder;
  
  private HashMap<Object, JvmTypeReference> cache = new HashMap<Object, JvmTypeReference>();
  
  public void reset() {
    this.cache.clear();
  }
  
  public JvmTypeReference get(Object key) {
    return this.cache.get(key);
  }
  
  public JvmTypeReference resolve(EObject ctx, Object key, JvmTypeReference ... typeArgs) {
    if(key == null) {
      MonetLog.print("Key=null on " + ctx.toString());
      return null;
    }
//    int keyHash = key.hashCode();
//    if(typeArgs != null) {
//      for(JvmTypeReference type : typeArgs)
//        if(type != null)
//          keyHash += type.hashCode();
//    }
    
    JvmTypeReference result = null; //this.cache.get(keyHash);
    try {
      if(result == null) {
        if(key instanceof Class)
          result = this.typesBuilder.newTypeRef(ctx, (Class<?>)key, typeArgs);
        else
          result = this.typesBuilder.newTypeRef(ctx, (String)key, typeArgs);
        //this.cache.put(keyHash, result);
      }
    } catch (Exception e) {
      MonetLog.print(e);
    }
    return result;
  }

  @Override
  public JvmTypeReference resolveObjectType(EObject ctx) {
    return resolve(ctx, Object.class);
  }
  
  @Override
  public JvmTypeReference resolveStringType(EObject ctx) {
    return resolve(ctx, String.class);
  }
  
  @Override
  public JvmTypeReference resolveIntegerType(EObject ctx) {
    return resolve(ctx, Integer.class);
  }

  @Override
  public JvmTypeReference resolveDoubleType(EObject ctx) {
    return resolve(ctx, Double.class);
  }
  
  @Override
  public JvmTypeReference resolveLongType(EObject ctx) {
    return resolve(ctx, long.class);
  }
  
  @Override
  public JvmTypeReference resolveClassType(EObject ctx) {
    return resolve(ctx, Class.class);
  }
  
  @Override
  public JvmTypeReference resolveComparableType(EObject ctx, JvmTypeReference... typeArgs) {
    return resolve(ctx, Comparable.class, typeArgs);
  }

  @Override
  public JvmTypeReference resolveVoidType(EObject ctx) {
    return resolve(ctx, void.class);
  }
  
  @Override
  public JvmTypeReference resolveIterableType(EObject ctx, JvmTypeReference... typeArgs) {
    return resolve(ctx, Iterable.class, typeArgs);
  }
  
  @Override
  public JvmTypeReference resolveDimensionImplType(EObject ctx) {
    return resolve(ctx, DimensionImpl.class);
  }

  @Override
  public JvmTypeReference resolveDimensionComponentImplType(EObject ctx) {
    return resolve(ctx, DimensionComponentImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveCubeImplType(EObject ctx) {
    return resolve(ctx, CubeImpl.class);
  }

  @Override
  public JvmTypeReference resolveCubeFactImplType(EObject ctx) {
    return resolve(ctx, CubeFactImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveLockType(EObject ctx) {
    return resolve(ctx, Lock.class);
  }
  
  @Override
  public JvmTypeReference resolveProjectType(EObject ctx) {
    return resolve(ctx, Project.class);
  }
  
  @Override
  public JvmTypeReference resolveDistributionType(EObject ctx) {
    return resolve(ctx, Distribution.class);
  }

  @Override
  public JvmTypeReference resolveNodeType(EObject ctx) {
    return resolve(ctx, Node.class);
  }
  
  @Override
  public JvmTypeReference resolveValidationResultType(EObject ctx) {
    return resolve(ctx, ValidationResult.class);
  }
  
  @Override
  public JvmTypeReference resolveRoleChooserType(EObject ctx) {
    return resolve(ctx, RoleChooser.class);
  }
  
  @Override
  public JvmTypeReference resolveRoleType(EObject ctx) {
    return resolve(ctx, Role.class);
  }

  @Override
  public JvmTypeReference resolveDelegationSetupType(EObject ctx) {
    return resolve(ctx, DelegationSetup.class);
  }
  
  @Override
  public JvmTypeReference resolveWaitSetupType(EObject ctx) {
    return resolve(ctx, WaitSetup.class);
  }

  @Override
  public JvmTypeReference resolveTimeoutSetupType(EObject ctx) {
    return resolve(ctx, TimeoutSetup.class);
  }

  @Override
  public JvmTypeReference resolveJobSetupType(EObject ctx) {
    return resolve(ctx, JobSetup.class);
  }
  
  @Override
  public JvmTypeReference resolveMappingType(EObject ctx) {
    return resolve(ctx, Mapping.class);
  }
  
  @Override
  public JvmTypeReference resolveMappingImplType(EObject ctx) {
    return resolve(ctx, MappingImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveHasMappingsType(EObject ctx) {
    return resolve(ctx, HasMappings.class);
  }
  
  @Override
  public JvmTypeReference resolveHasPropertiesType(EObject ctx) {
    return resolve(ctx, HasProperties.class);
  }
  
  @Override
  public JvmTypeReference resolveExporterScopeType(EObject ctx) {
    return resolve(ctx, ExporterScope.class);
  }
  
  @Override
  public JvmTypeReference resolveExporterScopeImplType(EObject ctx) {
    return resolve(ctx, ExporterScopeImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveImporterScopeType(EObject ctx) {
    return resolve(ctx, ImporterScope.class);
  }
  
  @Override
  public JvmTypeReference resolveExpressionType(EObject ctx) {
    return resolve(ctx, Expression.class);
  }
  
  @Override
  public JvmTypeReference resolveOrderExpressionType(EObject ctx) {
    return resolve(ctx, OrderExpression.class);
  }

  @Override
  public JvmTypeReference resolveNodeDocumentType(EObject ctx) {
    return resolve(ctx, NodeDocument.class);
  }

  @Override
  public JvmTypeReference resolveSchemaType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.Schema.class);
  }

  @Override
  public JvmTypeReference resolveSchemaSectionType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.SchemaSection.class);
  }

  @Override
  public JvmTypeReference resolveBPIFileType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.types.File.class);
  }

  @Override
  public JvmTypeReference resolveURIType(EObject ctx) {
    return resolve(ctx, URI.class);
  }
  
  @Override
  public JvmTypeReference resolveParamType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.Param.class);
  }
  
  @Override
  public JvmTypeReference resolveTermType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.types.Term.class);
  }
  
  @Override
  public JvmTypeReference resolveTermListType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.types.TermList.class);
  }

  @Override
  public JvmTypeReference resolveDateType(EObject ctx) {
    return resolve(ctx, org.monet.bpi.types.Date.class);
  }
  
  @Override
  public JvmTypeReference resolveArrayListType(EObject ctx, JvmTypeReference... typeArgs) {
    return resolve(ctx, java.util.ArrayList.class, typeArgs);
  }
  
  @Override
  public JvmTypeReference resolveHasMapType(EObject ctx, JvmTypeReference... typeArgs) {
    return resolve(ctx, HashMap.class, typeArgs);
  }
  
  @Override
  public JvmTypeReference resolveDoubleArrayPrimitiveType(EObject ctx) {
    return resolve(ctx, double[].class);
  }
  
  @Override
  public JvmTypeReference resolveDoublePrimitiveType(EObject ctx) {
    return resolve(ctx, double.class);
  }
  
  @Override
  public JvmTypeReference resolveBooleanPrimitiveType(EObject ctx) {
    return resolve(ctx, boolean.class);
  }
  
  @Override
  public JvmTypeReference resolveMapType(EObject ctx, JvmTypeReference... typeArgs) {
    return resolve(ctx, HashMap.class, typeArgs);
  }

  @Override
  public JvmTypeReference resolveHasBehaviourType(EObject ctx) {
    return resolve(ctx, HasBehaviour.class);
  }
  
  @Override
  public JvmTypeReference resolveFieldMultipleType(EObject ctx, JvmTypeReference... typeArgs) {
    return resolve(ctx, FieldMultiple.class, typeArgs);
  }

  @Override
  public JvmTypeReference resolveTransferResponseType(EObject ctx) {
    return resolve(ctx, TransferResponse.class);
  }

  @Override
  public JvmTypeReference resolveTransferRequestType(EObject ctx) {
    return resolve(ctx, TransferRequest.class);
  }

  @Override
  public JvmTypeReference resolveBehaviorTaskContestImplType(EObject ctx) {
    return resolve(ctx, BehaviorTaskContestImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveCustomerResponseType(EObject ctx) {
    return resolve(ctx, CustomerResponse.class);
  }
  
  @Override
  public JvmTypeReference resolveContestantRequestType(EObject ctx) {
    return resolve(ctx, ContestantRequest.class);
  }
  
  @Override
  public JvmTypeReference resolveContestantResponseType(EObject ctx) {
    return resolve(ctx, ContestantResponse.class);
  }
  
  @Override
  public JvmTypeReference resolveCustomerRequestType(EObject ctx) {
    return resolve(ctx, CustomerRequest.class);
  }
  
  @Override
  public JvmTypeReference resolveBehaviorTaskCustomerImplType(EObject ctx) {
    return resolve(ctx, BehaviorTaskCustomerImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveBehaviorTaskContestantImplType(EObject ctx) {
    return resolve(ctx, BehaviorTaskContestantImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveProviderResponseType(EObject ctx) {
    return resolve(ctx, ProviderResponse.class);
  }
  
  @Override
  public JvmTypeReference resolveProviderRequestType(EObject ctx) {
    return resolve(ctx, ProviderRequest.class);
  }
  
  @Override
  public JvmTypeReference resolveJobType(EObject ctx) {
    return resolve(ctx, Job.class);
  }

  @Override
  public JvmTypeReference resolveJobResponseType(EObject ctx) {
    return resolve(ctx, JobResponse.class);
  }
  
  @Override
  public JvmTypeReference resolveJobRequestType(EObject ctx) {
    return resolve(ctx, JobRequest.class);
  }
  
  @Override
  public JvmTypeReference resolveBehaviorTaskProviderExternalImplType(EObject ctx) {
    return resolve(ctx, BehaviorTaskProviderExternalImpl.class);
  }
  
  @Override
  public JvmTypeReference resolveInsourcingResponseType(EObject ctx) {
    return resolve(ctx, InsourcingResponse.class);
  }
  
  @Override
  public JvmTypeReference resolveInsourcingRequestType(EObject ctx) {
    return resolve(ctx, InsourcingRequest.class);
  }
  
  @Override
  public JvmTypeReference resolveBehaviorTaskProviderInternalImplType(EObject ctx) {
    return resolve(ctx, BehaviorTaskProviderInternalImpl.class);
  }
  
}
