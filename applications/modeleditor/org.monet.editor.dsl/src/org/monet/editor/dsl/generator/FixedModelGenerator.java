package org.monet.editor.dsl.generator;

import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.xbase.compiler.GeneratorConfig;
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;

@SuppressWarnings("restriction")
public class FixedModelGenerator extends JvmModelGenerator {

  public ITreeAppendable generateMember(final JvmMember it, final ITreeAppendable appendable, GeneratorConfig config) {
    if (it instanceof JvmStaticBlock)
      return _generateMember((JvmStaticBlock)it, appendable, config);
    else 
      return super.generateMember(it, appendable, config);
  }

  private ITreeAppendable _generateMember(final JvmStaticBlock it, final ITreeAppendable appendable, GeneratorConfig config) {
    final ITreeAppendable tracedAppendable = appendable.trace(it);
    tracedAppendable.append("static {");
    tracedAppendable.newLine();
    this.generateExecutableBody(it, tracedAppendable, config);
    tracedAppendable.newLine();
    tracedAppendable.append("}");
    return tracedAppendable;
  }
}
