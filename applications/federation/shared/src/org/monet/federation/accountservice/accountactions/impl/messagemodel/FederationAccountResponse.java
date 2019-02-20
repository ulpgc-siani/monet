package org.monet.federation.accountservice.accountactions.impl.messagemodel;

public class FederationAccountResponse {
  private FederationAccount account;
  private String error;
  
  public FederationAccountResponse (FederationAccount account){
    this.account = account;
  }
  
  public FederationAccount getAccount() {
    return account;
  }
  
  public void setAccount(FederationAccount account) {
    this.account = account;
  }
  
  public String getError() {
    return error;
  }
  
  public void setError(String error) {
    this.error = error;
  }
}
