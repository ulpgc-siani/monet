package org.monet.space.mobile.helpers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.monet.mobile.model.TaskDefinition.Step.Edit.PictureSize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


public class ImageHelper {
  
  private static final int COMPRESSION_QUALITY = 90;
  
  private static Bitmap readBitmap(Context context, Uri imageUri, PictureSize pictureSize) throws Exception {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    
    InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
    try {
      BitmapFactory.decodeStream(inputStream, null, options);
    } finally {
      StreamHelper.close(inputStream);
    }
    
    float heightRel = options.outHeight / pictureSize.height.intValue();
    float widthRel = options.outWidth / pictureSize.width.intValue(); 
    float rel = (heightRel > widthRel ? widthRel : heightRel);
    
    double pow = Math.floor(Math.log(rel) / Math.log(2));
    int scale = (int)Math.pow(2, pow);
    
    options = new BitmapFactory.Options();
    options.inSampleSize = scale;

    inputStream = context.getContentResolver().openInputStream(imageUri);
    try {
      return BitmapFactory.decodeStream(inputStream, null, options);
    } finally {
      StreamHelper.close(inputStream);
    }
  }

  public static void resizeImage(Context context, Uri imageUri, FileOutputStream outputStream, PictureSize pictureSize) throws Exception {
    if (pictureSize != null) {
      
      Bitmap bSource = null;
      Bitmap bDest = null;
      try {
        bSource = readBitmap(context, imageUri, pictureSize);

        bDest = Bitmap.createScaledBitmap(bSource, pictureSize.width.intValue(), pictureSize.height.intValue(), true);
        bDest.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream);
      } finally {
        if (bSource != null) bSource.recycle();
        if (bDest != null) bDest.recycle();
      }
    } else {
      InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
      try {
        StreamHelper.copyStream(inputStream, outputStream);
      } finally {
        StreamHelper.close(inputStream);
      }
    }
  }
  
  public static void resizeImage(Context context, Uri imageUri, PictureSize pictureSize) throws Exception {
    if (pictureSize == null) return;
    Bitmap bSource = null;
    Bitmap bDest = null;
    try {
      bSource = readBitmap(context, imageUri, pictureSize);
      
      OutputStream outputStream = null;
      try {
        outputStream = context.getContentResolver().openOutputStream(imageUri);
        
        bDest = Bitmap.createScaledBitmap(bSource, pictureSize.width.intValue(), pictureSize.height.intValue(), true);
        bDest.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream);
      } finally {
        StreamHelper.close(outputStream);
      }
    } finally {
      if (bSource != null) {
        bSource.recycle();
        bSource = null;
      }
      if (bDest != null) {
        bDest.recycle();
        bDest = null;
      }
    }
  }  

  
}
