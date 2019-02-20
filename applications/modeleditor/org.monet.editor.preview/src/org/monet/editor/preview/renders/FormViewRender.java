package org.monet.editor.preview.renders;

import java.util.HashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.internal.Ref;

public class FormViewRender extends NodeViewRender {

  public FormViewRender(String language) {
    super(language);
  }

  protected void initContent(HashMap<String, Object> viewMap, FormViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String shows = "";

    for (Ref show : viewDefinition.getShow().getField()) {
      FieldProperty fieldDefinition = ((FormDefinition)this.definition).getField(show.getValue());

      try {
        PreviewRender render = this.rendersFactory.get(fieldDefinition, "view.html", this.codeLanguage);
        render.setParameters(this.getParameters());
        render.setParameter("readonly", this.definition.isReadonly() ? "true" : "false");
        render.setParameter("formDefinition", this.definition);
        map.put("render(field)", render.getOutput());
      }
      catch (Exception exception) {
        map.put("render(field)", "");
        this.problems.add(new Problem(String.format("Compiling definition %s", fieldDefinition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }

      shows += block("show", map);
      map.clear();
    }

    map.put("shows", shows);

    viewMap.put("content", block("content", map));
  }

  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    FormViewProperty view = (FormViewProperty) this.definition.getNodeViewProperty(codeView);

    boolean isLocationView = codeView.equals("location");

    if (isLocationView) {
      this.initMapWithoutView(map, "location");
      return this.initLocationView(map);
    } else if (view == null) {
      map.put("codeView", codeView);
      map.put("labelDefinition", this.definition.getLabelString(this.codeLanguage));
      return block("view.undefined", map);
    }

    this.initMap(map, view);

    if (this.isSystemView(view)) {
      return this.initSystemView(map, view);
    }

    this.initContent(map, view);

    return block("view", map);
  }

  @Override
  protected void init() {
    loadCanvas("node.form");
    super.init();
  }

}
