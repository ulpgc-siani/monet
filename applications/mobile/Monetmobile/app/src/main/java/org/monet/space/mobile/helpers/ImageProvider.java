/*
 * Copyright (C) 2008 Romain Guy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Modified by Rayco Ara�a
 * 15/09/2010
 * 
 */

package org.monet.space.mobile.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.monet.space.mobile.R;

public class ImageProvider {

  private static final HashMap<String, WeakReference<Drawable>> imageCache = new HashMap<String, WeakReference<Drawable>>();
  private static final HashMap<String, Object>                  syncMap    = new HashMap<String, Object>();

  public static Drawable loadImageThumbnail(String jobId, String imageId, Context context, boolean isAbsolutePath) {
    Drawable image = null;
    Object syncObject;

    String uniqueId = jobId + "/" + imageId;

    synchronized (syncMap) {
      if (syncMap.containsKey(uniqueId))
        syncObject = syncMap.get(uniqueId);
      else {
        syncObject = new Object();
        syncMap.put(uniqueId, syncObject);
      }
    }

    synchronized (syncObject) {
      image = load(context, jobId, imageId, isAbsolutePath);
      if (image == null) {
        final Bitmap bitmap = loadFromSdCard(context, jobId, imageId, isAbsolutePath);
        if (bitmap != null) {
          File thumbnailFile = null;
          if(isAbsolutePath)
            thumbnailFile = new File(imageId + ".thumb");
          else
            thumbnailFile = new File(LocalStorage.getTaskStoreImageCache(context, jobId), imageId);
          Bitmap thumbnail = saveOnSDCache(context, bitmap, thumbnailFile, true); // Save
                                                                                  // thumbnail
          bitmap.recycle();
          imageCache.put(imageId, new WeakReference<Drawable>(new BitmapDrawable(context.getResources(), thumbnail)));
          image = new BitmapDrawable(context.getResources(), thumbnail);
        }
      }
    }
    return image;
  }
  
  public static void removeReference(String jobId, String imageId) {
    Object syncObject;

    String uniqueId = jobId + "/" + imageId;

    synchronized (syncMap) {
      if (syncMap.containsKey(uniqueId))
        syncObject = syncMap.get(uniqueId);
      else {
        syncObject = new Object();
        syncMap.put(uniqueId, syncObject);
      }
    }

    synchronized (syncObject) {
      WeakReference<Drawable> ref = imageCache.get(uniqueId);
      if (ref != null) {
        Drawable image = ref.get();
        if (image != null)
          image.setCallback(null);
      }

      ref = imageCache.get(uniqueId);
      if (ref != null) {
        Drawable image = ref.get();
        if (image != null)
          image.setCallback(null);
      }
    }
  }

  public static Bitmap saveOnSDCache(Context context, Bitmap bitmap, File imageFile, boolean thumbnail) {
    FileOutputStream stream = null;
    Bitmap imageToProcess = null;
    CompressFormat compressFormat = null;
    try {
      imageFile.getParentFile().mkdirs();
      stream = new FileOutputStream(imageFile);
      if (thumbnail) {
        float aspectRatioW = bitmap.getWidth() / (float) bitmap.getHeight();
        float aspectRatioH = bitmap.getHeight() / (float) bitmap.getWidth();
        float size = context.getResources().getDimension(R.dimen.edit_picture_width);
        int width = (int) (bitmap.getWidth() > bitmap.getHeight() ? size : (size * aspectRatioW));
        int height = (int) (bitmap.getHeight() > bitmap.getWidth() ? size : (size * aspectRatioH));
        imageToProcess = Bitmap.createScaledBitmap(bitmap, width, height, false);
      } else {
        imageToProcess = bitmap;
      }
      compressFormat = CompressFormat.JPEG;
      imageToProcess.compress(compressFormat, 90, stream);
    } catch (Exception e) {
      Log.error(e);
    } finally {
      if (stream != null)
        try {
          stream.close();
        } catch (Exception e2) {
        }
    }
    return imageToProcess;
  }

  private static Drawable load(Context context, String jobId, String imageId, boolean isAbsolutePath) {
    Drawable drawable = null;
    String uniqueId = jobId + "/" + imageId;

    WeakReference<Drawable> reference = imageCache.get(uniqueId);
    if (reference != null)
      drawable = reference.get();

    if (drawable == null) {
      final Bitmap bitmap = loadFromSdCardCache(context, jobId, imageId, isAbsolutePath);
      if (bitmap != null)
        drawable = new BitmapDrawable(context.getResources(), bitmap);
      imageCache.put(uniqueId, new WeakReference<Drawable>(drawable));
    }

    return drawable;
  }

  private static Bitmap loadFromSdCardCache(Context context, String jobId, String uid, boolean isAbsolutePath) {
    if(isAbsolutePath)
      return loadFromSdCard(new File(uid + ".thumb"));
    else
      return loadFromSdCard(new File(LocalStorage.getTaskStoreImageCache(context, jobId), uid));
  }

  private static Bitmap loadFromSdCard(Context context, String jobId, String uid, boolean isAbsolutePath) {
    if(isAbsolutePath)
      return loadFromSdCard(new File(uid));
    else
      return loadFromSdCard(new File(LocalStorage.getTaskResultStore(context, jobId), uid));
  }

  private static Bitmap loadFromSdCard(File file) {
    if (file != null && file.exists()) {
      InputStream stream = null;
      try {
        stream = new FileInputStream(file);
        return BitmapFactory.decodeStream(stream, null, null);
      } catch (FileNotFoundException e) {
      } finally {
        try {
          stream.close();
        } catch (IOException e) {
        }
      }
    }
    return null;
  }

  public static Uri getUri(Context context, String jobId, String uid) throws IOException {
    return Uri.fromFile(getFile(context, jobId, uid));
  }

  public static File getFile(Context context, String jobId, String uid) throws IOException {
    File file = new File(LocalStorage.getTaskResultStore(context, jobId), uid);
    if (!file.exists())
      file.createNewFile();
    return file;
  }

  public static void delete(Context context, String jobId, String uid) {
    if (uid == null)
      return;

    File file = new File(LocalStorage.getTaskResultStore(context, jobId), uid);
    if (file.exists())
      file.delete();
    file = new File(LocalStorage.getTaskStoreImageCache(context, jobId), uid);
    if (file.exists())
      file.delete();
  }
}
