package org.monet.editor.dsl.jvmmodel.inferers

import java.util.Set
import org.eclipse.emf.common.util.EList
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.monet.editor.dsl.metamodel.Pair
import org.eclipse.xtext.common.types.JvmGenericType
import com.google.common.collect.HashMultimap
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.helper.JavaHelper
import org.eclipse.xtext.common.types.JvmVisibility
import org.monet.editor.dsl.helper.TypeRefCache
import com.google.inject.Inject
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.jvmmodel.MMLExtensions

class BaseTaskInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache
	
	def protected void inferSwitchMethodBody(ITreeAppendable x, String switchParam, Set<Pair<String, Integer>> methods) {
		if(methods != null) {
			x.append("switch(");
			x.append(switchParam);
			x.append(") {");
			for(m : methods) {
				x.append("case ");
				x.append(String::valueOf(m.second));
				x.append(" :\n  ");
				x.append(m.first);
				x.append("(");
				var isFirst = true;
				var extraParams = m.extra.get(0) as EList<JvmFormalParameter>;
				var i=0;
				for(extra : extraParams) {
					var p = extra as JvmFormalParameter;
					if(isFirst) {
						isFirst = false;						
					} else {
						x.append(",");
					}
					x.append("(");
					x.append(p.parameterType.qualifiedName);
					x.append(") p");
					x.append(String::valueOf(i));
					i=i+1;
				}
				x.append(");\n");
				x.append("break;\n");
			}
			x.append("}");
		}
	}
	
	def protected void inferMethod(Property p, JvmGenericType type, String methodName, int index, HashMultimap<String, Pair<String, Integer>> methodMap) {
		val pm = p.getMethod(methodName);
		if(pm != null) {
			var code = p.getAttribute("code").valueAsString;
			var name = pm.id + index;
			methodMap.put(pm.id, new Pair<String, Integer>(name, code.hashCode, pm.params));
			
			type.members += pm.toMethod(name, pm.resolveVoidType)[
				body = pm.body;
				for(param : pm.params) {
					parameters += p.toParameter(param.name, param.parameterType);
				}
				setVisibility(JvmVisibility::PRIVATE);
			];
		}
	}
	
	def protected void inferUniqueMethod(Property p, JvmGenericType type, String methodName) {
		val pm = p.getMethod(methodName);
		if(pm != null) {
			type.members += pm.toMethod(JavaHelper::toAttributeJavaIdentifier("on-" + p.id), pm.resolveVoidType)[
				body = pm.body;
				for(param : pm.params) {
					parameters += p.toParameter(param.name, param.parameterType);
				}
				setVisibility(JvmVisibility::PUBLIC);
			];
		}
	}
	
}