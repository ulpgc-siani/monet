package org.monet.editor.core.security;

import java.security.Provider;

public class FNVProvider extends Provider {

  private static final long serialVersionUID = 5396817131971896385L;

  public FNVProvider() {
    super("FNV-1a", 1.0, "Fowler–Noll–Vo 1a Hash Function Provider v1.0");
    put("MessageDigest.FNV-1a", "org.monet.editor.core.security.FNVMessageDigest");
  }
}
