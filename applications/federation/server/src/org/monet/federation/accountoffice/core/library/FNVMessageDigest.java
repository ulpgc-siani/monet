package org.monet.federation.accountoffice.core.library;

import java.security.MessageDigest;

public class FNVMessageDigest extends MessageDigest {

  public static final int FNV_32_PRIME = 0x01000193; 
  
  private int hval = 0;
  
  public FNVMessageDigest() {
    super("FNV-1a");
    engineReset();
  }

  @Override
  protected void engineUpdate(byte input) {
    /* xor the bottom with the current octet */
    hval ^= input;
    /* multiply by the 64 bit FNV magic prime mod 2^64 */
    hval *= FNV_32_PRIME;
  }

  @Override
  protected void engineUpdate(byte[] input, int offset, int length) {
    for (int i = 0; i < length; i++)
      engineUpdate(input[i + offset]);
  }

  @Override
  protected byte[] engineDigest() {
    byte b[] = new byte[4];
    b[0] = (byte) (hval >>> 24);
    b[1] = (byte) (hval >>> 16);
    b[2] = (byte) (hval >>>  8);
    b[3] = (byte) (hval >>>  0);
    engineReset();
    return b;
  }
  
  @Override
  protected void engineReset() {
    this.hval = 0;
  }

}
