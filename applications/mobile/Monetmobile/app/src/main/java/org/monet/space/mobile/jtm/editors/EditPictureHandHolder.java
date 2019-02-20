package org.monet.space.mobile.jtm.editors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.otto.Subscribe;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.fragment.ImageHandwritingDialogFragment;
import org.monet.space.mobile.helpers.ImageProvider;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.mvp.BusProvider;

import java.io.*;
import java.util.List;
import java.util.UUID;

import org.monet.space.mobile.R;

public class EditPictureHandHolder extends EditHolder<String> {


    private static class LoadImageTask extends AsyncTask<String, Void, Drawable> {

        private ImageView view;
        private long jobId;
        private String imageId;

        private LoadImageTask(ImageView view, long jobId) {
            this.view = view;
            this.jobId = jobId;
            this.imageId = (String) view.getTag();
        }

        public static void execute(ImageView view, long jobId) {
            BusProvider.get().post(new StartLoadingEvent());
            new LoadImageTask(view, jobId).execute();
        }

        @Override
        protected Drawable doInBackground(String... params) {

            String filename = LocalStorage.getTaskResultStore(this.view.getContext(), String.valueOf(this.jobId)) + "/" + this.imageId;

            File file = new File(filename);
            if (!file.exists()) return null;

            InputStream stream = null;
            try {
                stream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);

                return new BitmapDrawable(this.view.getContext().getResources(), bitmap);
            } catch (FileNotFoundException e) {
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            if (result != null) {
                this.view.setImageDrawable(result);
            }
            BusProvider.get().post(new FinishLoadingEvent());
        }
    }


    private static final int COMPRESSION_QUALITY = 90;

    private String tempImageId = null;
    private ImageView currentImageView = null;

    @Override
    protected View createEditor(String value, ViewGroup container) {
        View editorView = inflater.inflate(R.layout.edit_picture, container, false);

        ImageView pictureView = (ImageView) editorView.findViewById(R.id.field_picture);
        pictureView.setTag(value);

        if (!this.isReadOnly()) {
            Button pictureBtn = (Button) editorView.findViewById(R.id.pick_picture);
            pictureBtn.setOnClickListener(this);
        }

        if (value != null && value.length() > 0)
            LoadImageTask.execute(pictureView, this.taskId);

        return editorView;
    }

    @Override
    protected void clearEditor(View editor) {
        if (this.isReadOnly()) return;

        ImageView pictureView = (ImageView) editor.findViewById(R.id.field_picture);
        pictureView.setTag(null);
        pictureView.setImageResource(R.drawable.ic_empty_picture);
    }

    @Override
    protected String onExtractItem(View editor) {
        ImageView pictureView = (ImageView) editor.findViewById(R.id.field_picture);
        return (String) pictureView.getTag();
    }

    @Override
    protected String onLoadValue(Schema schema) {
        return schema.getPicture(this.edit.name);
    }

    @Override
    protected List<String> onLoadValueArray(Schema schema) {
        return schema.getPictureList(this.edit.name);
    }

    @Override
    public void onLoad(Schema schema) {
        BusProvider.get().register(this);

        super.onLoad(schema);
    }

    @Override
    protected void onSaveValue(Schema schema, String value) {
        if (this.isReadOnly()) return;

        schema.putPicture(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<String> value) {
        if (this.isReadOnly()) return;

        schema.putPictureList(this.edit.name, value);
    }

    @Override
    public void onDestroy() {
        BusProvider.get().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_picture:
                if (edit.isReadOnly) {
                    try {
                        this.doViewCurrentPicture(new EditPictureViewCurrentEvent((String) currentImageView.getTag()));
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                } else {
                    try {
                        this.currentImageView = (ImageView) ((View) v.getParent()).findViewById(R.id.field_picture);

                        this.doSelectFromHandwriting(new EditPictureFromHandwritingEvent((String) currentImageView.getTag()));
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Subscribe
    public void doSelectFromHandwriting(EditPictureFromHandwritingEvent event) throws IOException {
        if (this.currentImageView.getTag() == null) {
            this.tempImageId = UUID.randomUUID().toString() + ".jpg";
        } else {
            this.tempImageId = (String) this.currentImageView.getTag();
        }
        Uri tempFileUri = Uri.fromFile(new File(LocalStorage.getTaskResultStore(this.getContext(), String.valueOf(this.taskId)), tempImageId));

        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        DialogFragment dialogFragment = ImageHandwritingDialogFragment.create(tempFileUri, Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY);
        dialogFragment.show(ft, "image_handwriting_dialog");
    }

    @Subscribe
    public void doViewCurrentPicture(EditPictureViewCurrentEvent event) throws IOException {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(ImageProvider.getUri(this.getContext(), String.valueOf(this.taskId), event.ImageId), "image/*");
        this.activityManager.startActivity(intent);
        BusProvider.get().post(new FinishLoadingEvent());
    }

    @Subscribe
    public void doCleanUp(EditImageCancelEvent event) {
        ImageProvider.delete(this.getContext(), String.valueOf(this.taskId), this.tempImageId);
        this.tempImageId = null;
        this.currentImageView = null;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkLastEditorIsEmpty();
                BusProvider.get().post(new FinishLoadingEvent());
            }
        });
    }

    @Subscribe
    public void onHandwriteImageSaved(HandwritingImageSavedEvent event) {
        File file = new File(LocalStorage.getTaskResultStore(this.getContext(), String.valueOf(this.taskId)), tempImageId);
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);

            Drawable d = new BitmapDrawable(this.getContext().getResources(), bitmap);
            this.currentImageView.setImageDrawable(d);

            this.currentImageView.setTag(this.tempImageId);
            this.tempImageId = null;
        } catch (FileNotFoundException e) {
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }

        this.checkLastEditorIsEmpty();
    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {
        if (holderBundle.containsKey("TEMP_IMAGE_ID"))
            this.tempImageId = holderBundle.getString("TEMP_IMAGE_ID");
        if (holderBundle.containsKey("EDITOR_ID"))
            this.currentImageView = (ImageView) this.getEditor(holderBundle.getInt("EDITOR_ID")).findViewById(R.id.field_picture);
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {
        if (this.tempImageId != null)
            holderBundle.putString("TEMP_IMAGE_ID", this.tempImageId);
        if (this.currentImageView != null) {
            int id = ((LinearLayout) this.currentImageView.getParent().getParent()).indexOfChild((View) this.currentImageView.getParent());
            holderBundle.putInt("EDITOR_ID", id);
        }
    }

}
