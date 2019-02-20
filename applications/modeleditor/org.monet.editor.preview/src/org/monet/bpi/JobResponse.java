package org.monet.bpi;

import org.monet.bpi.types.File;

public interface JobResponse {

  Schema getSchema();
  File getFile(String name);
  
}
