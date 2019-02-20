package org.monet.space.mobile.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MonetDataProvider extends android.content.ContentProvider {

  public static final String AUTHORITIES = "org.monet.mobile.sync";
  

  @Override
  public boolean onCreate() {
    return false;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    return null;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return 0;
  }

}
