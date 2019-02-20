package org.monet.editor.dsl.jvmmodel

import com.google.inject.Inject
import java.io.File
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.core.ProjectHelper
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.JavaHelper
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.monetLocalizationLanguage.DomainModel
import org.monet.metamodel.interfaces.Language

class MonetLocalizationLanguageJvmModelInferrer extends AbstractModelInferrer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension TypeRefCache
	
	def dispatch infer(DomainModel m, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		var project = XtendHelper::getIProject(m);
        var packageBase = ProjectHelper::getPackageBase(project);
				
		val qualifiedName = packageBase + ".Language" + JavaHelper::toJavaIdentifier(m.code);
		val defaultLngClassname = packageBase + ".Language";
		var JvmGenericType defaultLngClass = null;
		
		if(m.code == "default") {
			defaultLngClass = m.toClass(defaultLngClassname);
			acceptor.accept(defaultLngClass).initializeLater[
				var counter = 0;
				for(p : m.features) {
					counter = counter + 1;
					val id = counter;
					members += m.toField(p.name, p.newTypeRef(typeof(int)))[
						setInitializer(it, [appender | appender.append(String::valueOf(id)); ]);
						setStatic(true);
						setFinal(true);
						setVisibility(JvmVisibility::PUBLIC);
					];
				}
			];
		}

		acceptor.accept(m.toClass(qualifiedName)).initializeLater[
			superTypes += m.newTypeRef(typeof(Language));
			val x = new StringBuilderBasedAppendable();
			
			for(p : m.features) {
				x.append("this.labelMap.put(");
				x.append(defaultLngClassname);
				x.append(".");
				x.append(p.name);
				x.append(", \"");
				if(p.value != null)
					x.append(JavaHelper::toStringLiteral(p.value));
				x.append("\");\n");
			}
			members += m.toMethod("init", m.resolveVoidType)[
				body = [ap | ap.append(x.toString)];
			];
			members += m.toStaticBlock()[
				body = [ap |
					ap.append("org.monet.metamodel.Dictionary.getCurrentInstance().registerLanguage("); 
					ap.append(qualifiedName); 
					ap.append(".class, \""); 
					ap.append(m.code); 
					ap.append("\");"); 
				];
			];
		];
		
		return;
	}
}