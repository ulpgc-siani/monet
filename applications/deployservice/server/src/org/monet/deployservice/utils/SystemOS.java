package org.monet.deployservice.utils;

public class SystemOS {

	public static final boolean isWindows()  {
    return System.getProperty("os.name").startsWith("Windows");
 }	

}
