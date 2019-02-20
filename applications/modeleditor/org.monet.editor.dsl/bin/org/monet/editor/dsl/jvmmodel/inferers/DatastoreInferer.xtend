package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.JavaHelper
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Property

class DatastoreInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache

	@Inject private TypeReferences references
	@Inject private ClassNameInferer classNameInferer

	def void infer(Definition definition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		inferDimensions(definition, acceptor, prelinkingPhase);
		inferComponents(definition, acceptor, prelinkingPhase);

		inferCubes(definition, acceptor, prelinkingPhase);
		inferFacts(definition, acceptor, prelinkingPhase);
	}

	def void inferDimensions(Definition definition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		definition.getProperties("dimension").forEach(
			[ dimension |
				inferDimensionClass(definition, dimension, acceptor, prelinkingPhase);
			]
		);
	}

	def void inferDimensionClass(Definition definition, Property dimension, IJvmDeclaredTypeAcceptor acceptor,
		boolean prelinkingPhase) {
		acceptor.accept(dimension.toClass(classNameInferer.inferDimensionName(dimension))).initializeLater [
			superTypes += dimension.resolveDimensionImplType;
			val componentTypeName = classNameInferer.inferComponentName(dimension);
			val componentType = references.getTypeForName(componentTypeName, definition);
			members += dimension.toMethod("insertComponent", componentType) [
				parameters += dimension.toParameter("id", dimension.resolveStringType);
				body = [ ap |
					ap.append("return (" + componentTypeName + ")");
					ap.append("this.insertComponentImpl(" + componentTypeName + ".class,id);");
				];
			];
		]
	}

	def void inferCubes(Definition definition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		definition.getProperties("cube").forEach(
			[ cube |
				inferCubeClass(definition, cube, acceptor, prelinkingPhase);
			]
		);
	}

	def void inferCubeClass(Definition definition, Property cube, IJvmDeclaredTypeAcceptor acceptor,
		boolean prelinkingPhase) {
		acceptor.accept(cube.toClass(classNameInferer.inferCubeName(cube))).initializeLater [
			superTypes += cube.resolveCubeImplType;
			val factTypeName = classNameInferer.inferFactName(cube);
			val factType = references.getTypeForName(factTypeName, definition);
			members += cube.toMethod("insertFact", factType) [
				parameters += cube.toParameter("date", cube.resolveDateType);
				body = [ ap |
					ap.append("return (");
					ap.append(factTypeName);
					ap.append(")this.insertFactImpl(" + factTypeName + ".class, date);");
				];
			];
		];
	}

	def void inferComponents(Definition definition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		definition.getProperties("dimension").forEach(
			[ dimension |
				inferComponent(definition, dimension, acceptor, prelinkingPhase);
			]);
	}

	def void inferComponent(Definition d, Property dimension, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		acceptor.accept(d.toClass(classNameInferer.inferComponentName(dimension))).initializeLater [
			if (d.superType != null) {
				superTypes += references.getTypeForName(classNameInferer.inferComponentName(dimension), d);
			} else {
				superTypes += d.resolveDimensionComponentImplType;
			}
			dimension.getProperties("feature").forEach(
				[ p |
					val featureCode = p.code;
					val type = p.getAttribute("type").valueAsString;
					val featureTypeName = inferFeatureType(type);
					if (featureCode != null && featureTypeName != null) {
						var methodSuffix = JavaHelper::toJavaIdentifier(p.name);
						val featureType = references.getTypeForName(featureTypeName, p);
						members += d.toMethod("get" + methodSuffix + "Value", featureType) [
							body = [ ap |
								ap.append("return (");
								ap.append(featureTypeName);
								ap.append(")getFeatureValue(\"");
								ap.append(featureCode.value);
								ap.append("\");");
							];
						];

						val featureTypeArray = references.createArrayType(featureType);
						members += d.toMethod("get" + methodSuffix + "Values", featureTypeArray) [
							body = [ ap |
								ap.append("return getFeatureValues(\"");
								ap.append(featureCode.value);
								ap.append("\").toArray(new " + featureTypeName + "[0]);");
							];
						];

						if (type == "TERM") {
							members += d.toMethod("add" + methodSuffix, d.resolveVoidType) [
								parameters += d.toParameter("value", featureType);
								parameters += d.toParameter("ancestors", d.resolveArrayListType(featureType));
								body = [ ap |
									ap.append("this.addFeature(\"");
									ap.append(featureCode.value);
									ap.append("\", value, ancestors);");
								];
							];
						} else {
							members += d.toMethod("add" + methodSuffix, d.resolveVoidType) [
								parameters += d.toParameter("value", featureType);
								body = [ ap |
									ap.append("this.addFeature(\"");
									ap.append(featureCode.value);
									ap.append("\", value);");
								];
							];

						}
					}
				]);
		];
	}

	def String inferFeatureType(String type) {
		switch (type) {
			case "STRING": {
				return "java.lang.String"
			}
			case "TERM": {
				return "org.monet.bpi.types.Term"
			}
			case "BOOLEAN": {
				return "java.lang.Boolean"
			}
			case "INTEGER": {
				return "org.monet.bpi.types.Number"
			}
			case "REAL": {
				return "org.monet.bpi.types.Number"
			}
		}
		return null;
	}

	def void inferFacts(Definition definition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		definition.getProperties("cube").forEach(
			[ cube |
				inferFact(definition, cube, acceptor, prelinkingPhase);
			]);
	}

	def void inferFact(Definition e, Property cube, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {

		acceptor.accept(e.toClass(classNameInferer.inferFactName(cube))).initializeLater [
			superTypes += e.resolveCubeFactImplType;
			cube.getAttributes("use").forEach(
				[ use |
					val dimension = use.valueAsProperty;
					if (dimension != null) {
						val dimensionName = dimension.name;
						var methodSuffix = JavaHelper::toJavaIdentifier(dimensionName);

						members += use.toMethod("get" + methodSuffix, use.resolveStringType) [
							body = [ ap |
								ap.append("return ");
								ap.append("getComponentId(");
								ap.append("\"" + dimensionName + "\"");
								ap.append(");");
							];
						];
						members += use.toMethod("set" + methodSuffix, use.resolveVoidType) [
							parameters += use.toParameter("componentId", use.resolveStringType);
							body = [ ap |
								ap.append("this.setComponent(\"");
								ap.append(dimensionName);
								ap.append("\", componentId);");
							];
						];

					}
				]);
			cube.getProperties("metric").forEach(
				[ m |
					if (m.code != null) {
						var methodSuffix = JavaHelper::toJavaIdentifier(m.name);
						members += m.toMethod("get" + methodSuffix, m.resolveDoubleType) [
							body = [ ap |
								ap.append("return getMeasure(\"");
								ap.append(m.code.value);
								ap.append("\");");
							];
						];

						members += m.toMethod("set" + methodSuffix, m.resolveVoidType) [
							parameters += m.toParameter("value", m.resolveDoubleType);
							body = [ ap |
								ap.append("this.setMeasure(\"");
								ap.append(m.code.value);
								ap.append("\", value);");
							];
						];

					}
				]);
		];

	}

}
