package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.io.Serializable;

@Root(name="SocketMessage", strict=false)
public class SocketMessageModel implements Serializable{
  private static final long serialVersionUID = -7153328988847336846L;

  @Attribute String id;
  @Attribute String action;
  @Attribute String idUnit;

  @Element SimpleElement accesToken;
  @Element SimpleElement verifier;
  @Element VerifierSign verifierSign;
  @Element SignCertificate signcertificate;
  @Element UnitInfo presentation;
  @Element(required=false) String username;
  @Element(required=false) String fullname;
  @Element(required=false) String email;
  @Element String role;
  @Element String condition;
  @Element(required=false) int startPos;
  @Element(required=false) int limit;
  @Element(required=false) String partnerId;
  @Element(required=false) String partnerName;
  @Element(required=false) String deviceId;

  public String getId(){ return id; }
  public String getAction(){ return action; }
  public String getIdUnit(){ return idUnit; }
  public UnitInfo getPresentionLabel(){ return presentation; }
  public String getAccesToken() { return accesToken.getText(); }
  public String getVerifier() { return verifier.getText(); }
  public VerifierSign getVerifierSign(){return this.verifierSign;}
  public SignCertificate getSignCertificate(){ return signcertificate; }
  public String getUsername() { return this.username; }
  public String getFullname() { return this.fullname; }
  public String getEmail() { return this.email; }
  public String getRole() { return this.role; }
  public String getCondition() { return this.condition; }
  public int getStartPos() { return this.startPos; }
  public int getLimit() { return this.limit; }
  public String getPartnerId() { return this.partnerId; }
  public String getPartnerName() { return this.partnerName; }
  public String getDeviceId() { return this.deviceId; }

  public void setId(String id) { this.id = id; }
  public void setAction(String action) { this.action = action; }
  public void setIdUnit(String idUnit) { this.idUnit = idUnit; }
  public void setVerifierSign(String signature, String hash) { this.verifierSign = new VerifierSign(signature, hash); }
  public void setSignCertificate(SignCertificate signcertificate) { this.signcertificate = signcertificate; }
  public void setPresentionLabel(UnitInfo presentation) { this.presentation = presentation; }
  public void setAccesToken(String accesToken) { this.accesToken = new SimpleElement(accesToken); }
  public void setVerifier(String verifier) { this.verifier = new SimpleElement(verifier); }
  public void setUsername(String username) { this.username = username; }
  public void setFullname(String fullname) { this.fullname = fullname; }
  public void setEmail(String email) { this.email = email; }
  public void setRole(String role) { this.role = role; }
  public void setCondition(String condition) { this.condition = condition; }
  public void setStartPos(int startPos) { this.startPos = startPos; }
  public void setLimit(int limit) { this.limit = limit; }
  public void setPartnerId(String partnerId) { this.partnerId = partnerId; }
  public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
  public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

  public static class VerifierSign implements Serializable{
    private @Element SimpleElement signature;
    private @Element SimpleElement hash;
    public VerifierSign(){}
    public VerifierSign(String signature, String hash){
      this.signature = new SimpleElement(signature);
      this.hash = new SimpleElement(hash);}
    public SimpleElement getSignature(){ return signature;}
    public SimpleElement getHash(){ return hash;}
    public void setSignature(SimpleElement signature) { this.signature = signature; }
    public void setHash(SimpleElement hash) { this.hash = hash; }
  }

  public static class UnitInfo implements Serializable{
    @Element (name="label") EntryLabel label;
    @Element (name="logo-url", required=false) String logoUrl;

    public EntryLabel getLabel() { return label;}
    public String getLogoUrl() { return logoUrl; }

    public static class EntryLabel implements Serializable{
      @Text String text;
      public String getText(){ return text; }
    }
  }

  public static class SignCertificate implements Serializable{
    private @Element SimpleElement publickey;
    private @Element SimpleElement email;
    public SimpleElement getPublicKey(){ return publickey;}
    public SimpleElement getEmail(){ return email;}
    public void setPublickey(SimpleElement publickey) { this.publickey = publickey; }
    public void setEmail(SimpleElement email) { this.email = email; }
  }

  public static class SimpleElement implements Serializable{
    private @Text String text;
    public  SimpleElement (){}
    public  SimpleElement (String text) {this.text = text;}
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
  }

}
