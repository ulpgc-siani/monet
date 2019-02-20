package org.monet.space.mobile.jtm.editors;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.fragment.ChooseImageSourceDialogFragment;
import org.monet.space.mobile.helpers.ImageHelper;
import org.monet.space.mobile.helpers.ImageProvider;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.helpers.StreamHelper;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.mvp.BusProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.monet.space.mobile.R;

public class EditPictureHolder extends EditHolder<String> implements OnClickListener {

    private static final int RESULT_FOR_TAKE_NEW_FROM_CAMERA = 0;
    private static final int RESULT_FOR_PICK_FROM_GALLERY = 1;

    private Long tempFileId = null;
    private String tempFileName = null;
    private ImageView currentImageView = null;

    private void updateCurrentImageId() {
        if (this.currentImageView.getTag() != null)
            ImageProvider.delete(this.currentImageView.getContext(), String.valueOf(this.taskId), (String) this.currentImageView.getTag());
        this.currentImageView.setTag(this.tempFileName);
        this.tempFileName = null;
        this.tempFileId = null;
    }

    @Override
    protected View createEditor(String value, ViewGroup container) {
        View editorView = inflater.inflate(R.layout.edit_picture, container, false);

        ImageView pictureView = (ImageView) editorView.findViewById(R.id.field_picture);
        pictureView.setTag(value);

        Button pictureBtn = (Button) editorView.findViewById(R.id.pick_picture);
        pictureBtn.setOnClickListener(this);

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
    public void onLoad(Schema schema) {
        BusProvider.get().register(this);

        super.onLoad(schema);
    }


    @Override
    public void onDestroy() {
        BusProvider.get().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_picture:
                this.currentImageView = (ImageView) ((View) v.getParent()).findViewById(R.id.field_picture);

                if (this.isReadOnly()) {
                    try {
                        this.doViewCurrentPicture(new EditPictureViewCurrentEvent((String) currentImageView.getTag()));
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                } else {
                    ChooseImageSourceDialogFragment.create((String) currentImageView.getTag()).show(this.fragmentManager, "choose_source");
                    BusProvider.get().post(new StartLoadingEvent());
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Subscribe
    public void doSelectFromGallery(EditPictureFromGalleryEvent event) throws IOException {
        this.tempFileId = this.repository.newFileId();
        this.tempFileName = this.taskId + "_" + String.valueOf(this.tempFileId);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        this.activityManager.startActivityForResult(intent, RESULT_FOR_PICK_FROM_GALLERY, this);
    }

    @Subscribe
    public void doSelectFromCamera(EditPictureFromCameraEvent event) throws IOException {
        this.tempFileId = this.repository.newFileId();
        this.tempFileName = this.taskId + "_" + String.valueOf(this.tempFileId) + ".jpg";
        Uri tempFileUri = ImageProvider.getUri(this.getContext(), String.valueOf(this.taskId), this.tempFileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        this.activityManager.startActivityForResult(intent, RESULT_FOR_TAKE_NEW_FROM_CAMERA, this);
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
        ImageProvider.delete(this.getContext(), String.valueOf(this.taskId), this.tempFileName);
        this.tempFileName = null;
        this.tempFileId = null;
        this.currentImageView = null;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkLastEditorIsEmpty();
                BusProvider.get().post(new FinishLoadingEvent());
            }
        });
    }

    @Override
    public void onActivityForResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            doCleanUp(null);
            return;
        }

        switch (requestCode) {
            case RESULT_FOR_TAKE_NEW_FROM_CAMERA:
                try {
                    CopyFromSourceTask.execute(this, ImageProvider.getUri(this.getContext(), String.valueOf(this.taskId), this.tempFileName), true);
                } catch (Exception e) {
                }
                break;
            case RESULT_FOR_PICK_FROM_GALLERY:
                CopyFromSourceTask.execute(this, data.getData(), false);
                break;
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {
        if (holderBundle.containsKey("TEMP_FILE_ID"))
            this.tempFileId = holderBundle.getLong("TEMP_FILE_ID");
        if (holderBundle.containsKey("TEMP_FILE_NAME"))
            this.tempFileName = holderBundle.getString("TEMP_FILE_NAME");
        if (holderBundle.containsKey("EDITOR_ID"))
            this.currentImageView = (ImageView) this.getEditor(holderBundle.getInt("EDITOR_ID")).findViewById(R.id.field_picture);
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {
        if (this.tempFileId != null)
            holderBundle.putLong("TEMP_FILE_ID", this.tempFileId);
        if (this.tempFileName != null)
            holderBundle.putString("TEMP_FILE_NAME", this.tempFileName);
        if (this.currentImageView != null) {
            int id = ((LinearLayout) this.currentImageView.getParent().getParent()).indexOfChild((View) this.currentImageView.getParent());
            holderBundle.putInt("EDITOR_ID", id);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = this.getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private String getFileExtension(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0)
            extension = filename.substring(i);
        return extension;
    }

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
            return ImageProvider.loadImageThumbnail(String.valueOf(this.jobId), this.imageId, this.view.getContext(), false);
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


    private static class CopyFromSourceParams {
        private Uri source;
        private boolean overwrite;

        public CopyFromSourceParams(Uri source, boolean overwrite) {
            this.source = source;
            this.overwrite = overwrite;
        }
    }

    private static class CopyFromSourceTask extends AsyncTask<CopyFromSourceParams, Void, Boolean> {

        private EditPictureHolder instance;

        public CopyFromSourceTask(EditPictureHolder instance) {
            this.instance = instance;
        }

        public static void execute(EditPictureHolder instance, Uri source, boolean overwrite) {
            new CopyFromSourceTask(instance).execute(new CopyFromSourceParams(source, overwrite));
        }

        @Override
        protected Boolean doInBackground(CopyFromSourceParams... params) {
            Uri source = params[0].source;
            boolean overwrite = params[0].overwrite;

            if (overwrite) {
                try {
                    ImageHelper.resizeImage(instance.getContext(), source, instance.edit.pictureSize);
                } catch (Exception e) {
                    Log.error(e.getMessage(), e);
                    return false;
                }
            } else {
                String realFilePathSource = instance.getRealPathFromURI(source);
                String extension = instance.getFileExtension(realFilePathSource);
                instance.tempFileName += extension;

                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {

                    ImageProvider.getUri(instance.getContext(), String.valueOf(instance.taskId), instance.tempFileName);
                    outputStream = new FileOutputStream(ImageProvider.getFile(instance.getContext(), String.valueOf(instance.taskId), instance.tempFileName));

                    ImageHelper.resizeImage(instance.getContext(), source, outputStream, instance.edit.pictureSize);
                } catch (Exception e) {
                    Log.error(e.getMessage(), e);
                    return false;
                } finally {
                    StreamHelper.close(outputStream);
                    StreamHelper.close(inputStream);
                }
            }

            instance.repository.updateFileName(instance.tempFileId, instance.tempFileName);
            instance.updateCurrentImageId();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (!result) {
                Toast.makeText(instance.getContext(), R.string.error_processing_image, Toast.LENGTH_SHORT).show();
                return;
            }

            LoadImageTask.execute(instance.currentImageView, instance.taskId);
            instance.checkLastEditorIsEmpty();
        }
    }

}
