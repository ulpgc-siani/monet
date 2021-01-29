package org.monet.docservice.core.util;

public class Normalize {

    public static String normalize(String idDocument, String space){
        return space + "_" + idDocument;
    }
}
