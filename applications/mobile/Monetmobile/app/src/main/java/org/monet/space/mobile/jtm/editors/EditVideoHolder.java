package org.monet.space.mobile.jtm.editors;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import org.monet.space.mobile.fragment.ChooseVideoSourceDialogFragment;
import org.monet.space.mobile.helpers.ImageProvider;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.helpers.StreamHelper;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.mvp.BusProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.monet.space.mobile.R;

public class EditVideoHolder extends EditHolder<String> implements OnClickListener {

    private static final int MAX_FILE_SIZE_MB = 15;

    private static final int RESULT_FOR_TAKE_NEW_FROM_CAMERA = 0;
    private static final int RESULT_FOR_PICK_FROM_GALLERY = 1;

    private Long tempFileId = null;
    private String tempFileName = null;
    private ImageView currentImageView = null;


    private static class LoadVideoPreviewTask extends AsyncTask<String, Void, Drawable> {

        private ImageView view;
        private long jobId;
        private String videoId;

        private LoadVideoPreviewTask(ImageView view, long jobId) {
            this.view = view;
            this.jobId = jobId;
            this.videoId = (String) view.getTag();
        }

        public static void execute(ImageView view, long jobId) {
            BusProvider.get().post(new StartLoadingEvent());
            new LoadVideoPreviewTask(view, jobId).execute();
        }

        @TargetApi(10)
        @Override
        protected Drawable doInBackground(String... params) {

            Drawable result = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                try {
                    mRetriever.setDataSource(ImageProvider.getFile(this.view.getContext(), String.valueOf(this.jobId), this.videoId).getAbsolutePath());
                } catch (Exception e) {
                    Log.error(e.getMessage(), e);
                }
                result = new BitmapDrawable(this.view.getResources(), mRetriever.getFrameAtTime());
            } else {
                result = this.view.getResources().getDrawable(R.drawable.no_video_preview);
            }

            return result;
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

    private static class CopyFromSourceResult {
        private boolean success;
        private String message;

        public CopyFromSourceResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    private static class CopyFromSourceTask extends AsyncTask<CopyFromSourceParams, Void, CopyFromSourceResult> {

        private EditVideoHolder instance;

        public CopyFromSourceTask(EditVideoHolder instance) {
            this.instance = instance;
        }

        public static void execute(EditVideoHolder instance, Uri source, boolean overwrite) {
            new CopyFromSourceTask(instance).execute(new CopyFromSourceParams(source, overwrite));
        }

        @Override
        protected CopyFromSourceResult doInBackground(CopyFromSourceParams... params) {
            Uri source = params[0].source;
            boolean overwrite = params[0].overwrite;

            String realFilePath = this.getRealPathFromURI(source);
            if (!this.checkFileSize(realFilePath)) {
                String line = String.format(instance.getContext().getString(R.string.error_video_file_size), EditVideoHolder.MAX_FILE_SIZE_MB);
                return new CopyFromSourceResult(false, line);
            }

            if (!overwrite) {
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    String extension = this.getFileExtension(realFilePath);

                    instance.tempFileName += extension;

                    ImageProvider.getUri(instance.getContext(), String.valueOf(instance.taskId), instance.tempFileName);

                    inputStream = instance.getContext().getContentResolver().openInputStream(source);
                    outputStream = new FileOutputStream(ImageProvider.getFile(this.instance.getContext(), String.valueOf(instance.taskId), instance.tempFileName));
                    StreamHelper.copyStream(inputStream, outputStream);
                } catch (IOException e) {
                    Log.error(e.getMessage(), e);
                    return new CopyFromSourceResult(false, instance.getContext().getString(R.string.error_processing_video));
                } finally {
                    StreamHelper.close(outputStream);
                    StreamHelper.close(inputStream);
                }
            }

            instance.repository.updateFileName(instance.tempFileId, instance.tempFileName);
            instance.updateCurrentVideoId();

            return new CopyFromSourceResult(true, null);
        }

        @Override
        protected void onPostExecute(CopyFromSourceResult result) {
            super.onPostExecute(result);
            if ((result != null) && (!result.success)) {
                Toast.makeText(instance.getContext(), result.message, Toast.LENGTH_SHORT).show();
                return;
            }

            LoadVideoPreviewTask.execute(instance.currentImageView, instance.taskId);
            instance.checkLastEditorIsEmpty();
        }

        private String getRealPathFromURI(Uri contentURI) {
            Cursor cursor = instance.getContext().getContentResolver().query(contentURI, null, null, null, null);
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

        private boolean checkFileSize(String filename) {
            File file = new File(filename);
            return (file.length() / (1024 * 1024)) <= MAX_FILE_SIZE_MB;
        }
    }

    private void updateCurrentVideoId() {
        if (this.currentImageView.getTag() != null)
            ImageProvider.delete(this.currentImageView.getContext(), String.valueOf(this.taskId), (String) this.currentImageView.getTag());
        this.currentImageView.setTag(this.tempFileName);
        this.tempFileName = null;
        this.tempFileId = null;
    }

    @Override
    protected View createEditor(String value, ViewGroup container) {
        View editorView = inflater.inflate(R.layout.edit_video, container, false);

        ImageView pictureView = (ImageView) editorView.findViewById(R.id.field_picture);
        pictureView.setTag(value);

        Button pictureBtn = (Button) editorView.findViewById(R.id.pick_picture);
        pictureBtn.setOnClickListener(this);

        if (value != null && value.length() > 0)
            LoadVideoPreviewTask.execute(pictureView, this.taskId);

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
        return schema.getVideo(this.edit.name);
    }

    @Override
    protected List<String> onLoadValueArray(Schema schema) {
        return schema.getVideoList(this.edit.name);
    }

    @Override
    protected void onSaveValue(Schema schema, String value) {
        if (this.isReadOnly()) return;

        schema.putVideo(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<String> value) {
        if (this.isReadOnly()) return;

        schema.putVideoList(this.edit.name, value);
    }

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
                if (this.isReadOnly()) {
                    try {
                        this.doViewCurrentVideo(new EditVideoViewCurrentEvent());
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                } else {
                    this.currentImageView = (ImageView) ((View) v.getParent()).findViewById(R.id.field_picture);
                    String videoId = (String) currentImageView.getTag();
                    ChooseVideoSourceDialogFragment.create(videoId).show(this.fragmentManager, "choose_source");
                    BusProvider.get().post(new StartLoadingEvent());
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Subscribe
    public void doSelectFromGallery(EditVideoFromGalleryEvent event) throws IOException {
        this.tempFileId = this.repository.newFileId();
        this.tempFileName = this.taskId + "_" + String.valueOf(this.tempFileId);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        this.activityManager.startActivityForResult(intent, RESULT_FOR_PICK_FROM_GALLERY, this);
    }

    @Subscribe
    public void doSelectFromCamera(EditVideoFromCameraEvent event) throws IOException {
        this.tempFileId = this.repository.newFileId();
        this.tempFileName = this.taskId + "_" + String.valueOf(this.tempFileId);
        Uri tempFileUri = ImageProvider.getUri(this.getContext(), String.valueOf(this.taskId), this.tempFileName);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * MAX_FILE_SIZE_MB);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
        this.activityManager.startActivityForResult(intent, RESULT_FOR_TAKE_NEW_FROM_CAMERA, this);
    }

    @Subscribe
    public void doViewCurrentVideo(EditVideoViewCurrentEvent event) throws IOException {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(ImageProvider.getUri(this.getContext(), String.valueOf(this.taskId), (String) this.currentImageView.getTag()), "video/*");
        this.activityManager.startActivity(intent);
        BusProvider.get().post(new FinishLoadingEvent());
    }

    @Subscribe
    public void doCleanUp(EditVideoCancelEvent event) {
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
            tempFileId = holderBundle.getLong("TEMP_FILE_ID");
        if (holderBundle.containsKey("TEMP_FILE_NAME"))
            tempFileName = holderBundle.getString("TEMP_FILE_NAME");
        if (holderBundle.containsKey("EDITOR_ID"))
            currentImageView = (ImageView) getEditor(holderBundle.getInt("EDITOR_ID")).findViewById(R.id.field_picture);
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {
        if (tempFileId != null)
            holderBundle.putLong("TEMP_FILE_ID", tempFileId);
        if (tempFileName != null)
            holderBundle.putString("TEMP_FILE_NAME", tempFileName);
        if (currentImageView != null)
            holderBundle.putInt("EDITOR_ID", ((LinearLayout) currentImageView.getParent().getParent()).indexOfChild((View) currentImageView.getParent()));
    }

}
