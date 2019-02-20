package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import java.util.ArrayList
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmVisibility
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.metamodel.Pair
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Method
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral
import org.monet.editor.dsl.jvmmodel.inferers.TaskContestantsInferer
import org.monet.editor.dsl.helper.JavaHelper

class TaskInferer extends BaseTaskInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache
	
	@Inject private TaskContestInferer contestInferer
	@Inject private TaskCustomerInferer customerInferer
	@Inject private TaskContestantsInferer contestantsInferer
	@Inject private TaskProviderExternalInferer providerExternalInferer
	@Inject private TaskProviderInternalInferer providerInternalInferer
	@Inject private ClassNameInferer classNameInferer
	
	def void inferClasses(Definition taskDefinition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		taskDefinition.properties
					  .forEach([p | 
					  	switch(p.id) {
					  		case "contest": {
					  			contestInferer.infer(taskDefinition, p, acceptor, prelinkingPhase);
				  			}
					  		case "customer": {
					  			customerInferer.infer(taskDefinition, p, acceptor, prelinkingPhase);
				  			}
					  		case "contestants": {
					  			contestantsInferer.infer(taskDefinition, p, acceptor, prelinkingPhase);
				  			}
					  		case "provider": {
					  			p.properties
			  					 .forEach([px | 
								  	switch(px.id) {
									  		case "internal": {
			  									providerInternalInferer.infer(taskDefinition, px, acceptor, prelinkingPhase);
			  								}
									  		case "external": {
			  									providerExternalInferer.infer(taskDefinition, px, acceptor, prelinkingPhase);
			  								}
  								}]);
				  			}
				  			case "place": {
				  				
				  			}
					  	}
					  ]);
					  
	  var className = this.classNameInferer.inferTaskLockClass(taskDefinition);
	  acceptor.accept(taskDefinition.toClass(className))
	  		  .initializeLater[
	  		  	  superTypes += taskDefinition.resolveLockType;
	  		  	
	  		  	  members += taskDefinition.toConstructor() [
	  		  	  	parameters += taskDefinition.toParameter("place", taskDefinition.resolveStringType)
	  		  	  	parameters += taskDefinition.toParameter("id", taskDefinition.resolveStringType)
	  		  	  	body = [ap | ap.append("super(place, id);");]
	  		  	  ];
	  		  	  
	  		  	  taskDefinition.getProperties("place")
  		  	  					.map([p | p.getProperties("door", "line")
  		  	  							   .map([px | px.getProperties("exit", "stop"); ]).flatten
  		  	  					])
  		  	  					.flatten
  		  	  					.forEach([exit | 
  		  	  						var place = (exit.eContainer.eContainer as Property);
  		  	  						val placeName = place.name;
  		  	  						if(place.code != null) {
	  		  	  						val placeCode = place.code.value;
	  		  	  						members += exit.toField(placeName + "_" + exit.name, it.newTypeRef()) [
						  					setStatic(true);
						  					setVisibility(JvmVisibility::PUBLIC);
						  					setInitializer([ap | 
						  						ap.append("new Lock(\"");
						  						ap.append(placeCode);
						  						ap.append("\",\"");
						  						if(exit.code != null)
						  							ap.append(exit.code.value);
						  						ap.append("\")");
						  					]);
						  				];
  		  	  						}
  		  	  					]);
  		  	  					
	  		  	  taskDefinition.getProperties("place")
  		  	  					.filter([p | !p.hasProperties("door", "line")])
  		  	  					.forEach([place | 
  		  	  						val placeName = place.name;
  		  	  						val placeCode = place.code;
  		  	  						members += place.toField(placeName, it.newTypeRef()) [
					  					setStatic(true);
					  					setVisibility(JvmVisibility::PUBLIC);
					  					setInitializer([ap |
					  						var String codeValue = null;
					  						if(placeCode != null)
					  							codeValue = placeCode.value; 
					  						ap.append("new Lock(\"");
					  						ap.append(codeValue);
					  						ap.append("\",\"");
					  						ap.append(codeValue);
					  						ap.append("\")");
					  					]);
					  				];
  		  	  					]);
	  		  ];
	  		  
	  acceptor.accept(taskDefinition.toClass(this.classNameInferer.inferTaskPlaceClass(taskDefinition)))
	  		  .initializeLater[
	  		  	  members += taskDefinition.toConstructor() [
	  		  	  	parameters += taskDefinition.toParameter("id", taskDefinition.resolveStringType)
	  		  	  	body = [ap | ap.append("this.id = id;");]
	  		  	  ];
	  		  	  
	  		  	  var idFieldType = taskDefinition.resolveStringType;
	  		  	  members += taskDefinition.toField("id", idFieldType);
	  		  	  members += taskDefinition.toGetter("id", idFieldType);
	  		  	  
				  taskDefinition.properties
						        .forEach([p | 
								  	switch(p.id) {
								  		case "place": {
							  				members += p.toField(p.name, it.newTypeRef()) [
							  					setStatic(true);
							  					setVisibility(JvmVisibility::PUBLIC);
							  					setInitializer([ap | 
							  						ap.append("new Place(\"");
							  						ap.append(p.name);
							  						ap.append("\")");
							  					]);
							  				];
							  			}
								  	}
							  	]);
	  		  ];
	}
	
	def void addAllMethods(Property p, ArrayList<Method> methodList) {
		methodList.addAll(p.methods);
		p.properties.forEach([px | addAllMethods(px, methodList);]);
	}
	
	def void inferMethods(Definition taskDefinition, JvmGenericType behaviourClazz) {
		behaviourClazz.members += taskDefinition.toMethod("doUnlock", taskDefinition.resolveVoidType)[
			var lockClassName = this.classNameInferer.inferTaskLockClass(taskDefinition);
			parameters += taskDefinition.toParameter("lock", taskDefinition.resolve(lockClassName));
			body = [ap | ap.append("this.unlock(lock);"); ];
		];
		behaviourClazz.members += taskDefinition.toMethod("doLock", taskDefinition.resolveVoidType)[
			var lockClassName = this.classNameInferer.inferTaskLockClass(taskDefinition);
			parameters += taskDefinition.toParameter("lock", taskDefinition.resolve(lockClassName));
			body = [ap | ap.append("this.lock(lock);"); ];
		];
		behaviourClazz.members += taskDefinition.toMethod("doGoto", taskDefinition.resolveVoidType)[
			var placeClassName = this.classNameInferer.inferTaskPlaceClass(taskDefinition);
			parameters += taskDefinition.toParameter("place", taskDefinition.resolve(placeClassName));
			parameters += taskDefinition.toParameter("historyText", taskDefinition.resolveStringType);
			body = [ap | ap.append("this._goto(place.getId(), historyText);"); ];
		];
		
		inferCreateInstance(taskDefinition, behaviourClazz);
				
		val propertyMethods = new ArrayList<Method>();
		taskDefinition.getProperties("place").forEach([p | addAllMethods(p, propertyMethods);]);
										
		val methodMap = XtendHelper::createMultimap();
		propertyMethods.forEach([pm, i | 
			var Property parent = pm.eContainer as Property;
			var name = pm.id + i;
			behaviourClazz.members += pm.toMethod(name, pm.resolveVoidType)[
				body = pm.body;
				for(p : pm.params) {
					parameters += p.toParameter(p.name, p.parameterType);
				}
				setVisibility(JvmVisibility::PRIVATE);
			];
			var parentName = new StringBuilder();
			while(parent.id != "place") {
				if(parent.code != null)
					parentName.append(parent.code.value);
				parent = parent.eContainer as Property;
			}
			if(parent.code != null)
					parentName.append(parent.code.value);
			methodMap.put(pm.id, new Pair<String, Integer>(name, parentName.toString.hashCode, pm.params));
		]);
		
		behaviourClazz.members += taskDefinition.toMethod("onArrivePlace", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			
			body = [x | 
				var declMethods = methodMap.get("onArrive");
				x.append("int hash = (placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods)
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onTimeoutPlace", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			
			body = [x | 
				var declMethods = methodMap.get("onTimeout");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods)
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onTakePlace", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("routeCode", taskDefinition.resolveStringType);
			
			body = [x | 
				var declMethods = methodMap.get("onTake");
				x.append("int hash = (routeCode + actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods)
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSolveAction", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveNodeType);
			
			body = [x | 
				var declMethods = methodMap.get("onSolve");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onCreateJobAction", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveJobRequestType);
			
			body = [x | 
				var declMethods = methodMap.get("onCreate");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onCreatedJobAction", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveJobType);
			
			body = [x | 
				var declMethods = methodMap.get("onCreated");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];

		behaviourClazz.members += taskDefinition.toMethod("onFinishedJobAction", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveJobResponseType);
			
			body = [x | 
				var declMethods = methodMap.get("onFinished");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSelectJobRole", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveRoleChooserType);
			
			body = [x | 
				var declMethods = methodMap.get("onSelectJobRole");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSelectJobRoleComplete", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveRoleType);
			
			body = [x | 
				var declMethods = methodMap.get("onSelectJobRoleComplete");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];

		behaviourClazz.members += taskDefinition.toMethod("onSetupJob", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveJobSetupType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetupJob");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSetupJobComplete", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p1", taskDefinition.resolveDateType);
			parameters += taskDefinition.toParameter("p2", taskDefinition.resolveDateType);
			parameters += taskDefinition.toParameter("p3", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p4", taskDefinition.resolveBooleanPrimitiveType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetupJobComplete");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSelectDelegationRole", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveRoleChooserType);
			
			body = [x | 
				var declMethods = methodMap.get("onSelectRole");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSetupDelegation", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveDelegationSetupType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetup");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSetupDelegationComplete", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p1", taskDefinition.resolveDateType);
			parameters += taskDefinition.toParameter("p2", taskDefinition.resolveDateType);
			parameters += taskDefinition.toParameter("p3", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p4", taskDefinition.resolveBooleanPrimitiveType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetupComplete");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onSetupWait", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveWaitSetupType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetup");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];

		behaviourClazz.members += taskDefinition.toMethod("onSetupTimeout", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveTimeoutSetupType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetup");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];

		behaviourClazz.members += taskDefinition.toMethod("onSetupEdition", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveNodeType);
			
			body = [x | 
				var declMethods = methodMap.get("onSetup");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];

		behaviourClazz.members += taskDefinition.toMethod("onValidateForm", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("p0", taskDefinition.resolveNodeType);
			parameters += taskDefinition.toParameter("p1", taskDefinition.resolveValidationResultType);
			
			body = [x | 
				var declMethods = methodMap.get("onValidate");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onIsBackEnable", taskDefinition.resolveBooleanPrimitiveType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			
			body = [x | 
				x.append("int hash = (actionCode + placeCode).hashCode();\nswitch(hash) {\n");
				taskDefinition.getProperties("place")
						  	  .filter([p | p.hasProperty("back-enable")])
						      .map([p | p.getProperty("back-enable")])
						      .forEach([p | 
						      	  var place = p.eContainer as Property;
						      	  var placeCode = place.code.value;
						      	  var placeName = place.name;
						      	  var actionCode = p.getAttribute("code").valueAsString;
						      	  val whenAttribute = p.getAttribute("when");
								  if(whenAttribute != null && whenAttribute.value instanceof ExpressionLiteral) {
						      	  	x.append("case ");
						      	  	x.append(String::valueOf((actionCode + placeCode).hashCode));
						      	  	x.append(": return isBackEnableWhen");
						      	  	x.append(placeName);
						      	  	x.append("();\n");
						      	  }
						      ]);
				x.append("}\n return false;");
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onBack", taskDefinition.resolveVoidType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			
			body = [x | 
				var declMethods = methodMap.get("onBack");
				x.append("int hash = (actionCode + placeCode).hashCode();\n");
				inferSwitchMethodBody(x, "hash", declMethods);
			];
		];
		
		behaviourClazz.members += taskDefinition.toMethod("onCalculateClassificatorPlace", taskDefinition.resolveStringType)[
			parameters += taskDefinition.toParameter("placeCode", taskDefinition.resolveStringType);
			parameters += taskDefinition.toParameter("actionCode", taskDefinition.resolveStringType);
			
			body = [x | 
				x.append("int hash = (actionCode + placeCode).hashCode();\nswitch(hash) {\n");
				taskDefinition.getProperties("place")
						  	  .filter([p | p.hasProperty("enroll")])
						      .map([p | p.getProperty("enroll")])
						      .forEach([p | 
						      	  var place = p.eContainer as Property;
						      	  var placeCode = place.code.value;
						      	  var placeName = place.name;
						      	  var actionCode = p.getAttribute("code").valueAsString;
						      	  val classificateAttribute = p.getAttribute("classificate");
								  if(classificateAttribute != null && classificateAttribute.value instanceof ExpressionLiteral) {
						      	  	x.append("case ");
						      	  	x.append(String::valueOf((actionCode + placeCode).hashCode));
						      	  	x.append(": return classificate");
						      	  	x.append(placeName);
						      	  	x.append("();\n");
						      	  }
						      ]);
				x.append("}\n return null;");
			];
		];
		
		taskDefinition.getProperties("place")
				  	  .filter([p | p.hasProperty("enroll")])
				      .map([p | p.getProperty("enroll")])
				      .forEach([p | 
				      	  var place = p.eContainer as Property;
				      	  var placeName = place.name;
				      	  inferClassificateExpression(placeName, p, behaviourClazz);
				      ]);
				      
		taskDefinition.getProperties("place")
				  	  .filter([p | p.hasProperty("back-enable")])
				      .map([p | p.getProperty("back-enable")])
				      .forEach([p | 
				      	  var place = p.eContainer as Property;
				      	  var placeName = place.name;
				      	  inferIsBackEnableExpression(placeName, p, behaviourClazz);
				      ]);
				      
		taskDefinition.getProperties("shortcut")
	  	  				.forEach([p | 
			  				behaviourClazz.members += p.toMethod("get" + JavaHelper::toJavaIdentifier(p.name), p.resolveNodeType) [
			  					body = [appender | 
			  						appender.append("return this.getShortCut(\"");
			  						appender.append(p.name);
			  						appender.append("\");");
			  					]
			  				];
			  				
			  				behaviourClazz.members += p.toMethod("set" + JavaHelper::toJavaIdentifier(p.name), p.resolveVoidType) [
			  					parameters += p.toParameter("node", p.resolveNodeType);
			  					body = [appender | 
			  						appender.append("this.setShortCut(\"");
			  						appender.append(p.name);
			  						appender.append("\", node);");
			  					]
			  				];
			  				
			  				behaviourClazz.members += p.toMethod("remove" + JavaHelper::toJavaIdentifier(p.name), p.resolveVoidType) [
			  					body = [appender | 
			  						appender.append("this.removeShortCut(\"");
			  						appender.append(p.name);
			  						appender.append("\");");
			  					]
			  				];
	  	  				]);
	}
	
	def private inferCreateInstance(Definition e, JvmGenericType behaviourClazz) {
		val returnType = e.resolve(this.classNameInferer.inferBehaviourName(e));
		
		var method = e.toMethod("createNew", returnType)[
			body = [ap | 
				ap.append("return (");
				ap.append(returnType.qualifiedName);
				ap.append(")org.monet.bpi.TaskService.create(");
				ap.append(this.classNameInferer.inferBehaviourName(e));
				ap.append(".class);");
			];
		];
		method.setStatic(true);
		method.setVisibility(JvmVisibility::PUBLIC);
		behaviourClazz.members += method;
	}
	
	def private boolean inferClassificateExpression(String placeName, Property transference, JvmGenericType behaviourClazz) {
		val classificateAttribute = transference.getAttribute("classificate");
		if(classificateAttribute != null && classificateAttribute.value instanceof ExpressionLiteral) {
			val classificateExpression = classificateAttribute.value as ExpressionLiteral; 
			var classificateMethod = classificateAttribute.toMethod("classificate" + placeName, classificateAttribute.resolveStringType) [
				visibility = JvmVisibility::PROTECTED;
				if(classificateExpression.value != null)
					body = classificateExpression.value;
			];
			behaviourClazz.members += classificateMethod;
		}
		return classificateAttribute != null;
	}
	
	def private boolean inferIsBackEnableExpression(String placeName, Property backEnable, JvmGenericType behaviourClazz
	) {
		val backEnableAttribute = backEnable.getAttribute("when");
		if(backEnableAttribute != null && backEnableAttribute.value instanceof ExpressionLiteral) {
			val backEnableExpression = backEnableAttribute.value as ExpressionLiteral; 
			var backEnableMethod = backEnableAttribute.toMethod("isBackEnableWhen" + placeName, backEnableAttribute.resolveBooleanPrimitiveType) [
				visibility = JvmVisibility::PROTECTED;
				if(backEnableExpression.value != null)
					body = backEnableExpression.value;
			];
			behaviourClazz.members += backEnableMethod;
		}
		return backEnableAttribute != null;
	}
	
}