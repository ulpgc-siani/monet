package utils;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

public class ThreadLocalCookieStore implements CookieStore {
	CookieStore[] store = new CookieStore[1000];
	int index;

	private final static ThreadLocal<CookieStore> ms_cookieJars = new ThreadLocal<CookieStore>() {
		@Override
		protected synchronized CookieStore initialValue() {
			return (new CookieManager()).getCookieStore(); /* InMemoryCookieStore */
		}
	};

	public void add(URI uri, HttpCookie cookie) {
		ms_cookieJars.get().add(uri, cookie);
	}

	public List<HttpCookie> get(URI uri) {
		return ms_cookieJars.get().get(uri);
	}

	public List<HttpCookie> getCookies() {
		return store[index].getCookies();
	}

	public List<URI> getURIs() {
		return store[index].getURIs();
	}

	public boolean remove(URI uri, HttpCookie cookie) {
		return store[index].remove(uri, cookie);
	}

	public boolean removeAll() {
		return store[index].removeAll();
	}
}