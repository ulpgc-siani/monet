package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDocument;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.User;
import org.monet.bpi.exceptions.RoleException;
import org.monet.bpi.types.File;

public abstract class NodeDocumentImpl  
  extends NodeImpl 
  implements NodeDocument, BehaviorNodeDocument {

  @Override
  public Schema genericGetSchema() {
    return null;
  }
  
  @Override
  public void setSignaturesCount(String signature, int count) {
  }
  
  @Override
  public void setUsersForSignature(String signature, int signaturePos, String[] userIds) throws RoleException {
  }
  
  @Override
  public void disableUsersForSignature(String signature, int signaturePos) {
  }

  public void consolidate() {
  }
  
  public void overwriteContent(File newContent) {
  }
  
  @Override
  public void overwriteContent(byte[] newContent, String contentType) {
  }  
  
  public File getContent() {
    return null;
  }
  
  @Override
  public void onSign(User user) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onSignsComplete() {
    // TODO Auto-generated method stub
    
  }
}

