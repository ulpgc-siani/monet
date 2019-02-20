package org.monet.metamodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Manifest {
	
	public static String getVersion() {
		return "3.3.1";
	}

	public static Date getDate() {
	  Date date = null;
	  
	  try {
	    date = new SimpleDateFormat("dd/MM/yyyy").parse("07/10/2015");
	  }
	  catch (ParseException exception) {
	    date = new Date();
	  }
	  
	  return date;
	}
}