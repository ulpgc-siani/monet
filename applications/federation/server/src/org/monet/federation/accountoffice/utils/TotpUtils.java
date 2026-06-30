package org.monet.federation.accountoffice.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public abstract class TotpUtils {
  private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

  public static String generateSecret() {
    byte[] randomBytes = new byte[20];
    new SecureRandom().nextBytes(randomBytes);
    return base32Encode(randomBytes);
  }

  public static Long validateCode(String secret, String code, int digits, int period, int validationWindow, Long lastUsedCounter) {
    if (secret == null || code == null)
      return null;

    String trimmedCode = code.trim();
    long currentCounter = System.currentTimeMillis() / 1000L / period;
    for (long offset = -validationWindow; offset <= validationWindow; offset++) {
      long counter = currentCounter + offset;
      if (counter < 0)
        continue;

      String expectedCode = generateCode(secret, counter, digits);
      if (!expectedCode.equals(trimmedCode))
        continue;

      if (lastUsedCounter != null && counter <= lastUsedCounter)
        return -1L;

      return counter;
    }

    return null;
  }

  public static String buildOtpAuthUri(String issuer, String username, String secret, int digits, int period) {
    String safeIssuer = issuer != null ? issuer : "Monet";
    String label = safeIssuer + ":" + username;
    return "otpauth://totp/" + urlEncode(label)
            + "?secret=" + urlEncode(secret)
            + "&issuer=" + urlEncode(safeIssuer)
            + "&algorithm=SHA1"
            + "&digits=" + digits
            + "&period=" + period;
  }

  private static String generateCode(String secret, long counter, int digits) {
    byte[] key = base32Decode(secret);
    byte[] data = new byte[8];
    long value = counter;
    for (int i = 7; i >= 0; i--) {
      data[i] = (byte) (value & 0xFF);
      value >>= 8;
    }

    try {
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(new SecretKeySpec(key, "HmacSHA1"));
      byte[] hash = mac.doFinal(data);
      int offset = hash[hash.length - 1] & 0x0F;
      int binary = ((hash[offset] & 0x7F) << 24)
              | ((hash[offset + 1] & 0xFF) << 16)
              | ((hash[offset + 2] & 0xFF) << 8)
              | (hash[offset + 3] & 0xFF);
      int otp = binary % (int) Math.pow(10, digits);
      return String.format("%0" + digits + "d", otp);
    } catch (Exception e) {
      throw new RuntimeException("Could not generate TOTP code", e);
    }
  }

  private static String base32Encode(byte[] data) {
    StringBuilder builder = new StringBuilder();
    int current = 0;
    int bits = 0;
    for (byte value : data) {
      current = (current << 8) | (value & 0xFF);
      bits += 8;
      while (bits >= 5) {
        builder.append(BASE32_ALPHABET.charAt((current >> (bits - 5)) & 0x1F));
        bits -= 5;
      }
    }

    if (bits > 0)
      builder.append(BASE32_ALPHABET.charAt((current << (5 - bits)) & 0x1F));

    return builder.toString();
  }

  private static byte[] base32Decode(String value) {
    String normalized = value.replace("=", "").replace(" ", "").toUpperCase();
    byte[] result = new byte[normalized.length() * 5 / 8];
    int buffer = 0;
    int bitsLeft = 0;
    int index = 0;

    for (char character : normalized.toCharArray()) {
      int digit = BASE32_ALPHABET.indexOf(character);
      if (digit < 0)
        throw new IllegalArgumentException("Illegal base32 character");

      buffer = (buffer << 5) | digit;
      bitsLeft += 5;
      if (bitsLeft >= 8) {
        result[index++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xFF);
        bitsLeft -= 8;
      }
    }

    return result;
  }

  private static String urlEncode(String value) {
    try {
      return URLEncoder.encode(value, "UTF_8");
    } catch (UnsupportedEncodingException e) {
      return value;
    }
  }
}
