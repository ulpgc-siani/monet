package org.monet.federation.accountoffice.core.components.accountcomponent.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.NoAuthenticatedUser;
import org.monet.federation.accountoffice.core.model.Session;
import org.monet.federation.accountoffice.utils.MD5;
import org.monet.federation.accountoffice.utils.Network;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Singleton
public class SessionComponentImpl implements SessionComponent {

	private HashMap<String, NoAuthenticatedUser> usersFailed;
	private Logger logger;
	private int maxSessions;
	private DataRepository dataRepository;

	@Inject
	public SessionComponentImpl(Logger logger, Configuration configuration, DataRepository dataRepository) {
		this.logger = logger;
		this.usersFailed = new HashMap<String, NoAuthenticatedUser>();
		this.maxSessions = configuration.getMaxSessions();
		this.dataRepository = dataRepository;
	}

	public synchronized String addUser(String username, String remoteAddr, String remoteUA, String lang, boolean rememberMe, boolean isMobile, String space) {
		logger.debug("addUser(%s)", username);

		String token = String.valueOf(UUID.randomUUID());
		if (remoteAddr == null)
			remoteAddr = "";
		if (remoteUA == null)
			remoteUA = "";
		String verifier = null;

		verifier = MD5.calculate(remoteAddr + remoteUA);

		Session session = new Session();
		session.setToken(token);
		session.setUsername(username);
		session.setVerifier(verifier);
		session.setLang(lang);
		session.setRememberMe(rememberMe);
		session.setMobile(isMobile);
		session.setSpace(space);

		Network network = new Network();
		session.setNode(network.getLocalIP());

		this.dataRepository.registerSession(session);

		return token;
	}

	public synchronized boolean deleteUser(String username) {
		logger.debug("deleteUser(%s)", username);

		Session[] sessions = this.dataRepository.loadSessionsFromUsername(username);
		ArrayList<String> tokens = new ArrayList<String>();
		for (Session session : sessions) {
		    this.dataRepository.unregisterSession(session.getToken());
		}

		return true;
	}

	public synchronized boolean deleteUserFromToken(String token) {
		logger.debug("deleteUserFromToken(%s)", token);

		Session session = this.dataRepository.loadSession(token);
		this.deleteUser(session.getUsername());

		return true;
	}

	public synchronized String getAccessToken(String token) {
		logger.debug("getAccessToken(%s)");

		Session session = this.dataRepository.loadSession(token);
		this.renewSession(session);

		return session != null ? session.getToken() : null;
	}

	public synchronized String[] getAccessTokensFromUsername(String username) {
		logger.debug("getAccessToken(%s)");

		Session[] sessions = this.dataRepository.loadSessionsFromUsername(username);
		ArrayList<String> tokens = new ArrayList<String>();
		for (Session session : sessions) {
		  this.renewSession(session);
		  tokens.add(session.getToken());
		}

		return tokens.toArray(new String[0]);
	}

	public synchronized boolean isUserOnline(String token) {
		logger.debug("isUserOnline(%s)", token);

		Session session = this.dataRepository.loadSession(token);
		this.renewSession(session);

		return session != null;
	}

	@Override
	public String getUsername(String token) {
		logger.debug("getUsername(%s)", token);

		Session session = this.dataRepository.loadSession(token);
		this.renewSession(session);

		return session != null ? session.getUsername() : null;
	}

	@Override
	public synchronized boolean hasAvailableSessions() {
		logger.debug("hasAvailableSessions()");

		int count = this.dataRepository.loadSessionsCount();
		return (count < maxSessions);
	}

	@Override
	public synchronized boolean hasSessions(String username) {
		logger.debug("hasSessions(%s)", username);

		int count = this.dataRepository.loadSessionsFromUsername(username).length;
		return (count > 0);
	}

	@Override
	public String getVerifier(String token) {
		logger.debug("getVerifier(%s)", token);

		Session session = this.dataRepository.loadSession(token);
		this.renewSession(session);

		return session != null ? session.getVerifier() : null;
	}

	@Override
	public int addAuthTryToUser(String username) {
		logger.debug("addAuthTryToUser(%s)", username);

		int tryCount = 1;
		if (this.usersFailed.containsKey(username)) {
			NoAuthenticatedUser noUser = this.usersFailed.get(username);
			noUser.setTryCount(noUser.getTryCount() + 1);
			tryCount = noUser.getTryCount();
			if (tryCount > NoAuthenticatedUser.MAX_RETRIES)
				noUser.setSuspendTime(System.currentTimeMillis());
		} else {
			NoAuthenticatedUser noUser = new NoAuthenticatedUser();
            noUser.setTryCount(1);
			this.usersFailed.put(username, noUser);
		}

		return tryCount;
	}

	@Override
	public boolean isUserSuspended(String username) {
		logger.debug("isUserSuspended(%s)", username);

		NoAuthenticatedUser user = this.usersFailed.get(username);
		return user != null && user.isSuspend();
	}

	@Override
	public int getAuthTriesOfUser(String username) {
		logger.debug("getAuthTriesOfUser(%s)", username);

		NoAuthenticatedUser user = this.usersFailed.get(username);
		return user != null ? user.getTryCount() : 0;
	}

	@Override
	public void resetUserTries(String username) {
		logger.debug("resetUserTries(%s)", username);

		this.usersFailed.remove(username);
	}

	@Override
	public void setCaptchaAnswerToUser(String username, String captchaAnswer) {
		logger.debug("setCaptchaAnswerToUser(%s, %s)", username, captchaAnswer);

		NoAuthenticatedUser user = this.usersFailed.get(username);
		if (user != null)
			user.setCaptchaAnswer(captchaAnswer);
	}

	@Override
	public boolean isUserCaptchaAnswerCorrect(String username, String answer) {
		logger.debug("isUserCaptchaAnswerCorrect(%s, %s)", username, answer);

		NoAuthenticatedUser user = this.usersFailed.get(username);
		return user == null || (user != null && user.isCaptchaAnswerCorrect(answer));
	}

	private void renewSession(Session session) {

		if (session == null)
			return;

		session.setLastUse(new Date());
		this.dataRepository.saveSession(session);
	}

}
