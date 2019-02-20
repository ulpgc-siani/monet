package org.monet.space.mobile.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.HandwritingImageSavedEvent;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.widget.DrawingSurface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class ImageHandwritingDialogFragment extends DialogFragment implements android.content.DialogInterface.OnClickListener {

  private static final String KEY_FILE_URI = "FILE_URI";
  private static final String KEY_COMPRESS_FORMAT = "COMPRESS_FORMAT";
  private static final String KEY_QUALITY = "QUALITY";
  private static final String KEY_IMAGE = "IMAGE";
  
  
  private Uri fileUri;
  private Bitmap.CompressFormat compressFormat;
  private int quality;

  private View customView;
  private DrawingSurface drawingSurface;
  
  public ImageHandwritingDialogFragment() {
    
  }
  
  public static ImageHandwritingDialogFragment create(Uri fileUri, Bitmap.CompressFormat compressFormat, int quality) {
    ImageHandwritingDialogFragment f = new ImageHandwritingDialogFragment();
    f.compressFormat = compressFormat;
    f.fileUri = fileUri;
    f.quality = quality;
    return f;
  }
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    
      this.createView();
      
      AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
      builder.setTitle(R.string.draw_image_dialog_title);
      builder.setPositiveButton(android.R.string.ok, this);
      builder.setNeutralButton(R.string.remove, this);
      builder.setCancelable(true);
      
      builder.setView(this.customView);
      
      Dialog dialog = builder.create();
      
      if (savedInstanceState != null)
        this.loadInstanceState(savedInstanceState);
      else 
        this.loadPreviousImage();
      
      return dialog;
      
  }
  
  private void createView() {
    this.customView = this.getLayoutInflater().inflate(R.layout.fragment_image_handwriting, null);
    
    this.drawingSurface = (DrawingSurface)this.customView.findViewById(R.id.drawingSurface);
    this.drawingSurface.setZOrderOnTop(true);
  }

  private void loadPreviousImage() {
    File file = new File(this.fileUri.getPath().toString());
    if (!file.exists()) return;
      
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);
      
      Drawable d = new BitmapDrawable(getResources(), bitmap);
      this.drawingSurface.setImageDrawable(d);
    } catch (FileNotFoundException e) {
    } finally {
      try { stream.close(); } catch (IOException e) {
      }
    }
  }

  private LayoutInflater getLayoutInflater() {
    return (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void saveImageToFile() {
    try {
      final FileOutputStream out = new FileOutputStream(new File(this.fileUri.getPath().toString()));
      this.drawingSurface.getImageBitmap().compress(this.compressFormat, this.quality, out);
      out.flush();
      out.close();
    } catch (IOException e) {
    }
  }
  
  @Override
  public void onClick(DialogInterface dialog, int which) {
    switch (which) {
    case AlertDialog.BUTTON_POSITIVE:
      this.saveImageToFile();
      BusProvider.get().post(new HandwritingImageSavedEvent(this.fileUri));
      break;
      
    case AlertDialog.BUTTON_NEUTRAL:
      this.drawingSurface.clear();
      this.saveImageToFile();
      BusProvider.get().post(new HandwritingImageSavedEvent(this.fileUri));
      break;

    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    
    outState.putString(KEY_FILE_URI, this.fileUri.toString());
    outState.putString(KEY_COMPRESS_FORMAT, this.compressFormat.name());
    outState.putInt(KEY_QUALITY, this.quality);
    
    Bitmap b = this.drawingSurface.getImageBitmap();
    outState.putParcelable(KEY_IMAGE, b);
  }
  
  private void loadInstanceState(Bundle savedInstanceState) {
    this.fileUri = Uri.parse(savedInstanceState.getString(KEY_FILE_URI));
    this.compressFormat = Bitmap.CompressFormat.valueOf(savedInstanceState.getString(KEY_COMPRESS_FORMAT));
    this.quality = savedInstanceState.getInt(KEY_QUALITY);

    Bitmap b = (Bitmap) savedInstanceState.getParcelable(KEY_IMAGE);
    Drawable d = new BitmapDrawable(getResources(), b);
    this.drawingSurface.setImageDrawable(d);
  }
  
}
