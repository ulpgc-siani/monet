package org.monet.editor.dsl.ui.template;

public class Template extends org.eclipse.jface.text.templates.Template {

  protected String help;
  
  public Template() {
    super();
  }
  
  public Template(org.eclipse.jface.text.templates.Template template) {
    super(template);
  }
  
  @SuppressWarnings("deprecation")
  public Template(String name, String description, String contextTypeId, String pattern) {
    super(name, description, contextTypeId, pattern);
  }
  
  public Template(String name, String description, String contextTypeId, String pattern, boolean isAutoInsertable) {
    super(name, description, contextTypeId, pattern, isAutoInsertable);
  }
  
  public String getHelp() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getName());
    builder.append("\n\n");
    builder.append(this.help);
    builder.append("\n\n");
    builder.append(this.getPattern());
    return builder.toString();
  }
  
  public void setHelp(String help) {
    this.help = help;
  }
  
}
