package org.monet.metamodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Manifest {
	
	public static String getVersion() {
		return "::version::";
	}

	public static Date getDate() {
	  Date date = null;
	  
	  try {
	    date = new SimpleDateFormat("dd/MM/yyyy").parse("::date::");
	  }
	  catch (ParseException exception) {
	    date = new Date();
	  }
	  
	  return date;
	}
}