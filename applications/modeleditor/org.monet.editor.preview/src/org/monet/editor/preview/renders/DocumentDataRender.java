package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.monet.editor.preview.utils.StringHelper;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.metamodel.RoleDefinition;
import org.monet.metamodel.internal.Ref;

public class DocumentDataRender extends DataRender {
  private DocumentDefinition definition;
  
  public DocumentDataRender(String language) {
    super(language);
  }
  
  @Override public void setTarget(Object target) {
    this.definition = (DocumentDefinition)target;    
  }

  @Override protected void init() {
    loadCanvas("data");

    String view = (String)this.getParameter("view");
    StringBuilder data = new StringBuilder();
    
    if (view.equals("signs"))
      data.append(this.initSignatures());
    else {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("nrows", "0");
      map.put("rows", "");
      data.append(block("data", map));
    }
    
    addMark("data", data.toString());
  }
  
  protected String initSignatures() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    StringBuilder signatures = new StringBuilder();
    int pos = 0;
    
    if (this.definition.getSignatures() == null)
      return "";
    
    Collection<SignatureProperty> signatureList = this.definition.getSignatures().getSignatureList();
    
    for (SignatureProperty signatureDefinition : signatureList) {
      HashMap<String, Object> signMap = new HashMap<String, Object>();
      SignatureProperty precedenceDefinition = null;
      
      if (signatureDefinition.getAfter() != null) {
        precedenceDefinition = this.definition.getSignature(signatureDefinition.getAfter().getValue());
      }

      ArrayList<String> roles = new ArrayList<String>();
      for (Ref roleRef : signatureDefinition.getFor()) {
        RoleDefinition roleDefinition = dictionary.getRoleDefinition(roleRef.getValue());
        roles.add(language.getModelResource(roleDefinition.getLabel(), this.codeLanguage));
      }
      
      String precedence = "";
      if (precedenceDefinition != null) {
        HashMap<String, Object> labelMap = new HashMap<String, Object>();
        labelMap.put("signature", language.getModelResource(precedenceDefinition.getLabel(), this.codeLanguage));
        precedence = block("signatureMessage", labelMap);
      }
      
      signMap.put("id", signatureDefinition.getCode());
      signMap.put("name", signatureDefinition.getName());
      signMap.put("code", signatureDefinition.getCode());
      signMap.put("label", language.getModelResource(signatureDefinition.getLabel(), this.codeLanguage));
      signMap.put("precedence", precedence);
      signMap.put("roles", StringHelper.implode(roles, ", "));
      signMap.put("comma", (pos == signatureList.size()-1)?"":"comma");
      
      signatures.append(block("row.document", signMap));
      pos++;
    }    
    
    map.put("nrows", String.valueOf(signatureList.size()));
    map.put("rows", signatures.toString());
    
    return block("data", map);
  }
  
}
