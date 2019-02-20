package org.monet.editor.preview.renders;

import java.util.HashMap;

import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FileFieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty.AttachmentsProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeFieldProperty;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.internal.Ref;

public class NodeViewRender extends ViewRender {
  protected NodeDefinition definition;

  public NodeViewRender(String language) {
    super(language);
  }

  @Override
  public void setTarget(Object target) {
    this.definition = (NodeDefinition) target;
  }

  protected String initToolbar(NodeViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String operations = this.initToolbarOperations(viewDefinition);

    map.put("codeNode", this.definition.getCode());
    map.put("operations", operations);

    if (viewDefinition != null) {
      String id = this.definition.getCode() + viewDefinition.getCode();
      map.put("id", id);
      
      if (this.isSystemView(viewDefinition) && !this.isPrototypesSystemView(viewDefinition)) {
        if (this.isAttachmentsSystemView(viewDefinition)) {
          FormDefinition formDefinition = (FormDefinition) this.definition;
          FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
          AttachmentsProperty attachmentsDefinition = showDefinition.getAttachments();

          for (Ref field : attachmentsDefinition.getFieldNode()) {
            NodeFieldProperty fieldDefinition = (NodeFieldProperty) formDefinition.getField(field.getValue());
            
            if (fieldDefinition.getAdd() != null) {
              for (Ref add : fieldDefinition.getAdd().getNode()) {
                HashMap<String, Object> localMap = new HashMap<String, Object>();
  
                localMap.put("label", language.getModelResource(fieldDefinition.getLabel(), this.codeLanguage));
                localMap.put("code", fieldDefinition.getCode());
                localMap.put("path", formDefinition.getFieldPath(fieldDefinition.getCode()));
                localMap.put("codeNode", dictionary.getDefinition(add.getValue()).getCode());
                localMap.put("id", id);
  
                operations += block("toolbar.systemView.attachments$addNode", localMap);
              }
            }
          }

          for (Ref field : attachmentsDefinition.getFieldFile()) {
            FileFieldProperty fieldDefinition = (FileFieldProperty) formDefinition.getField(field.getValue());
            HashMap<String, Object> localMap = new HashMap<String, Object>();

            localMap.put("label", language.getModelResource(fieldDefinition.getLabel(), this.codeLanguage));
            localMap.put("code", fieldDefinition.getCode());
            localMap.put("path", formDefinition.getFieldPath(fieldDefinition.getCode()));
            localMap.put("id", id);

            operations += block("toolbar.systemView.attachments$addFile", localMap);
          }

          map.put("operations", operations);

          return block("toolbar.systemView.attachments", map);
        } else
          return block("toolbar.systemView", map);
      }
    }

    return block("toolbar", map);
  }

  protected String initToolbarOperations(NodeViewProperty viewDefinition) {
    return "";
  }

  protected String initToolbarWithoutView() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    return block("toolbar", map);
  }

  protected String initNotesSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String notesResult = "";

    contentMap.put("type", "notes");
    notesResult = block("content.notes$empty", map);
    contentMap.put("notes", notesResult);

    return block("content.notes", contentMap);
  }

  protected String initLinksInSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
    contentMap.put("type", "links_in");
    return block("content.linksIn", contentMap);
  }

  protected String initLinksOutSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
    contentMap.put("type", "links_out");
    return block("content.linksIn", contentMap);
  }

  protected String initAttachmentsSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
    FormDefinition definition = (FormDefinition) this.definition;
    FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) view).getShow();
    AttachmentsProperty attachmentsDefinition = showDefinition.getAttachments();
    StringBuilder options = new StringBuilder();

    for (Ref fieldRef : attachmentsDefinition.getFieldNode()) {
      FieldProperty fieldDefinition = definition.getField(fieldRef.getValue());
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("code", fieldDefinition.getCode());
      localMap.put("label", language.getModelResource(fieldDefinition.getLabel(), this.codeLanguage));
      options.append(block("content.attachments$option", localMap));
    }

    for (Ref fieldRef : attachmentsDefinition.getFieldFile()) {
      FieldProperty fieldDefinition = definition.getField(fieldRef.getValue());
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("code", fieldDefinition.getCode());
      localMap.put("label", language.getModelResource(fieldDefinition.getLabel(), this.codeLanguage));
      options.append(block("content.attachments$option", localMap));
    }

    contentMap.put("type", "attachments");
    contentMap.put("imagesPath", this.getParameterAsString("imagesPath"));
    contentMap.put("options", options.toString());

    return block("content.attachments", contentMap);
  }

  protected String initTasksSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String tasksResult = "";

    contentMap.put("type", "tasks");
    tasksResult = block("content.tasks$empty", map);
    contentMap.put("tasks", tasksResult);

    return block("content.tasks", contentMap);
  }

  protected String initPrototypesSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
    contentMap.put("type", "prototypes");
    contentMap.put("addList", "");
    contentMap.put("sortByList", "");
    contentMap.put("groupByList", "");
    return block("content.prototypes", contentMap);
  }

  protected String initSystemView(HashMap<String, Object> viewMap, NodeViewProperty viewDefinition) {

    viewMap.put("clec", "clec");
    viewMap.put("codeDefinition", this.definition.getCode());
    viewMap.put("codeView", viewDefinition.getCode());

    if (this.isNotesSystemView(viewDefinition))
      viewMap.put("content", this.initNotesSystemView(viewDefinition, viewMap));
    else if (this.isLinksInSystemView(viewDefinition))
      viewMap.put("content", this.initLinksInSystemView(viewDefinition, viewMap));
    else if (this.isLinksOutSystemView(viewDefinition))
      viewMap.put("content", this.initLinksOutSystemView(viewDefinition, viewMap));
    else if (this.isAttachmentsSystemView(viewDefinition))
      viewMap.put("content", this.initAttachmentsSystemView(viewDefinition, viewMap));
    else if (this.isTasksSystemView(viewDefinition))
      viewMap.put("content", this.initTasksSystemView(viewDefinition, viewMap));
    else if (this.isRevisionsSystemView(viewDefinition)) {
      viewMap.put("type", "revisions");
      viewMap.put("content", block("content.revisions", viewMap));
    } else if (this.isPrototypesSystemView(viewDefinition))
      viewMap.put("content", this.initPrototypesSystemView(viewDefinition, viewMap));

    return block("view", viewMap);
  }

  protected String initLocationView(HashMap<String, Object> viewMap) {
    viewMap.put("codeDefinition", this.definition.getCode());
    viewMap.put("codeView", "location");
    viewMap.put("content", block("content.location", viewMap));
    return block("view", viewMap);
  }

  protected void initMap(HashMap<String, Object> map, NodeViewProperty view) {
    map.put("type", this.getDefinitionType(this.definition));
    map.put("from", this.getParameterAsString("from"));
    map.put("codeView", view != null ? view.getCode() : "");
    map.put("page", this.getParameterAsString("page"));
    map.put("clec", "");
    map.put("toolbar", this.initToolbar(view));
  }

  protected void initMapWithoutView(HashMap<String, Object> map, String codeView) {
    map.put("type", this.getDefinitionType(this.definition));
    map.put("from", this.getParameterAsString("from"));
    map.put("codeView", codeView);
    map.put("page", this.getParameterAsString("page"));
    map.put("clec", "");
    map.put("toolbar", this.initToolbarWithoutView());
  }

  @Override
  protected void init() {
    loadCanvas("node");
    super.init();
  }

}
