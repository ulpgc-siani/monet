package org.monet.federation.accountoffice.core.components.certificatecomponent;

import org.monet.federation.accountoffice.core.model.User;

public interface CertificateComponent {

  public static final String ERROR_EXTRACT_CERTIFICATE_FROM_SIGNATURE = "error_extract_certificate";
  public static final String ERROR_EXTRACT_CERTIFICATE_CHAIN_FROM_SIGNATURE = "error_extract_certificate_chain";
  public static final String ERROR_UNKNOWN_ROOT_CERTIFICATE = "error_unknown_root_certificate";
  public static final String ERROR_UNKNOWN_FEDERATION = "error_unknown_federation";
  public static final String ERROR_NOT_TRUSTED_FEDERATION = "error_not_trusted_federation";
  public static final String ERROR_UNKNOWN_BUSINESS_UNIT = "error_unknown_business_unit";
  public static final String ERROR_NOT_PARTNER_BUSINESS_UNIT = "error_not_partner_business_unit";
  public static final String ERROR_INVALID_HASH = "error_no_equal_hash";
  public static final String ERROR_EXTRACT_ORIGINAL_CONTENT_FROM_SIGNATURE = "error_can't_extract_original_content";
  public static final String ERROR_VERIFYING_TIMESTAMP = "error_verifying_timestamp";

  public boolean checkRootCertificate(String hash, String signature, boolean verifyTimestamp);
  public boolean isValidCertificate(String hash, String signature, boolean verifyTimestamp);
  public User getUserFromSignature(String hash, String signature, boolean verifyTimestamp);
}
