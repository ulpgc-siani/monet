package org.monet.space.mobile.provider;

public class SuggestionsProvider extends android.content.SearchRecentSuggestionsProvider {

  public final static String AUTHORITY = "org.monet.mobile.suggestions";
  public final static int MODE = DATABASE_MODE_QUERIES;

  public SuggestionsProvider() {
      setupSuggestions(AUTHORITY, MODE);
  }
  
}
