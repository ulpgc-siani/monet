package org.monet.bpi.java;

import org.monet.bpi.DelegationSetup;
import org.monet.bpi.types.Date;

public class DelegationSetupImpl implements DelegationSetup {

  private boolean executed = false;
  
  private boolean canceled = false;
  private boolean withDefaultValues = false;
  private boolean withValues = false;

  private Date suggestedStartDate;
  private Date suggestedEndDate;
  private String comments;
  private boolean urgent;
  
  @Override
  public void cancel() {
    checkExecuted();
    
    this.canceled = true;
  }

  @Override
  public void withDefaultValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
    checkExecuted();
    
    this.suggestedStartDate = suggestedStartDate;
    this.suggestedEndDate = suggestedEndDate;
    this.comments = comments;
    this.urgent = urgent;
    this.withDefaultValues = true;
  }

  @Override
  public void withValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
    checkExecuted();
    
    this.suggestedStartDate = suggestedStartDate;
    this.suggestedEndDate = suggestedEndDate;
    this.comments = comments;
    this.urgent = urgent;
    this.withValues = true;
  }
  
  private void checkExecuted() {
    if(this.executed)
      throw new RuntimeException("Delegation already setup");
    
    this.executed = true;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public boolean isWithDefaultValues() {
    return withDefaultValues;
  }

  public boolean isWithValues() {
    return withValues;
  }

  public Date getSuggestedStartDate() {
    return suggestedStartDate;
  }

  public Date getSuggestedEndDate() {
    return suggestedEndDate;
  }
  
  public String getComments() {
    return comments;
  }

  public boolean isUrgent() {
    return urgent;
  }

}
