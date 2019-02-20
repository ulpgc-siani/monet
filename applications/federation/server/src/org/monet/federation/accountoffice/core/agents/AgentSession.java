package org.monet.federation.accountoffice.core.agents;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class AgentSession extends TimerTask {

  private long inactivityTime;
  private DataRepository dataRepository;
  private Logger logger;
  private Timer timer;

  @Inject 
  private AgentSession(Configuration configuration, DataRepository dataRepository, Logger logger) {
    this.inactivityTime = configuration.getInactivityTime();
    this.dataRepository = dataRepository;
    this.logger = logger;
  }

  public synchronized void init() {
    this.timer = new Timer("Federation-Session", true);
    this.timer.schedule(this, 1000, this.inactivityTime * 2);
  }

  @Override
  public synchronized void run() {
    long miliseconds = System.currentTimeMillis();
    
    Date to = new Date();
    to.setTime(miliseconds-this.inactivityTime);
    
    this.logger.info("Removing federation sessions until %s", to.toString());
    this.dataRepository.unregisterSessions(to);
    this.logger.info("Federacion sessions until %s removed", to.toString());
  }

  public synchronized void stop() {
    if (timer != null) {
      this.timer.cancel();
      this.timer.purge();
    }
  }

}
