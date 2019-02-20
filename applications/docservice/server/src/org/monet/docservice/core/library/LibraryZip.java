package org.monet.docservice.core.library;

import java.util.ArrayList;

public interface LibraryZip {

  Boolean compress(String sSourceDir, ArrayList<String> aFiles,
      String sDestinationFilename);

  Boolean decompress(String sFilename, String sDestination);

}