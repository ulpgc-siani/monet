package org.monet.docservice.docprocessor.model;

import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "presigned")
public class PresignedDocument {

  @Attribute(name = "sign-id", required = false)
  private String signId;
  @Attribute(name = "hash", required = false)
  private String hash;
  @Attribute(name = "date", required = false)
  private Date date;

  public void setSignId(String signId) {
    this.signId = signId;
  }

  public String getSignId() {
    return signId;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getHash() {
    return hash;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}
