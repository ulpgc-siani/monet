package org.monet.grided.library;

import java.net.MalformedURLException;
import java.net.URL;

public class LibraryURL {

   public static URL toURL(String urlString) {
    URL url = null;
    try {
        url = new URL(urlString);
    } catch (MalformedURLException e) {        
        throw new RuntimeException(e);
    }
    return url;
   }
}

