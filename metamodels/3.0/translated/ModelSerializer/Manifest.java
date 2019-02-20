package org.monet.metamodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Manifest {
	
	public static String getVersion() {
		return "3.0";
	}

	public static Date getDate() {
	  Date date = null;
	  
	  try {
	    date = new SimpleDateFormat("dd/MM/yyyy").parse("14/10/2013");
	  }
	  catch (ParseException exception) {
	    date = new Date();
	  }
	  
	  return date;
	}
}