package app;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tests.Micv3DevTest;
import utils.Files;
import utils.Strings;
import utils.ThreadLocalCookieStore;

public class Main {

	public static String agent;
	
	public static void main(String[] args) throws Exception {
		System.out.println("Simultaneous connections simulator for Monet. Version 1.0.\n");

		CookieHandler.setDefault(new CookieManager(new ThreadLocalCookieStore(), null));   
		List<String> users = null;
		Integer maxUsers = 0;
		agent = "";

		try {
		  users = loadUsers(args[0]);
	    if (! args[1].equals("")) agent = args[1];
		  
	    maxUsers = users.size();
	    if (! args[2].equals(""))  maxUsers = Integer.parseInt(args[2]);
	    
		} catch (Exception e) {
			System.out.println("Use: java -jar sicosimo.jar <user list> <browser> <connections number>\n");
			System.exit(1);
		}
		
		if (! agent.equals("")) System.out.println("User agent: " + agent);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dateStart = new Date();
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		if (agent.equals("firefox")) { 
			int index = users.size();
	 		for (int x = 0; x < maxUsers; x++) {
	 			index--;
				System.out.println("Connecting with user: " + users.get(index) + " (" + (index + 1) + "/" + users.size() + ")");
				es.execute(new Micv3DevTest(users.get(index)));
			}		
	  } else {		
  		for (int x = 0; x < maxUsers; x++) {
	  		System.out.println("Connecting with user: " + users.get(x) + " (" + (x + 1) + "/" + users.size() + ")");
		  	es.execute(new Micv3DevTest(users.get(x)));
		 }
	  }
		
		
		es.shutdown();
		boolean finished = es.awaitTermination(30, TimeUnit.MINUTES);

		if (finished) {
			Date dateEnd = new Date();
			System.out.println("Date start: " + dateFormat.format(dateStart));
			System.out.println("Date end: " + dateFormat.format(dateEnd));

			long diff = dateEnd.getTime() - dateStart.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			System.out.println("Time in seconds: " + diffSeconds + " seconds.");
			System.out.println("Time in minutes: " + diffMinutes + " minutes.");
			System.out.println("Time in hours: " + diffHours + " hours.");
		} else {
			System.out.println("Error: timeout 30 minutes");
		}
	}

	public static List<String> loadUsers(String filename) throws Exception {
		Files files = new Files();
		Strings strings = new Strings();
		List<String> users = new ArrayList<String>();
		String expression = ".*\t(.*)\t.*\t.*\t.*\t.*";
		String username = "";

		List<String> list_users = files.loadTextFile(filename);
		for (int x = 1; x < list_users.size(); x++) {
			username = strings.getRegularExpression(expression, list_users.get(x), 0)[0];
			users.add(username);
		}

		return users;
	}
}
