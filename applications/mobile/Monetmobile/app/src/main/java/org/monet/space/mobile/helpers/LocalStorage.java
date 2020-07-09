package org.monet.space.mobile.helpers;

import android.content.Context;
import android.media.MediaScannerConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LocalStorage {

    private static final String DATABASE_NAME = "database.db";

    private static final String MONET_TEMP_STORE = "temp/";
    private static final String MONET_TASK_STORE = "tasks/%s";
    private static final String MONET_TASK_SOURCE_STORE = "tasks/%s/source";
    private static final String MONET_TASK_RESULT_STORE = "tasks/%s/result";
    private static final String MONET_TASK_INITIALIZE_SCHEMA_FILE = ".default.txt";
    private static final String MONET_TASK_SCHEMA_FILE = ".schema";
    private static final String MONET_TASK_METADATA_FILE = ".metadata";

    private static File getDirectory(Context context, String directory) {
        File dirFile = new File(context.getExternalFilesDir(null).getAbsolutePath(), directory);
        if (!dirFile.exists())
            dirFile.mkdirs();
        return dirFile;
    }

    public static String getDBFile(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME;
    }

    public static File getTaskSourceStore(Context context, String taskId) {
        return getDirectory(context, String.format(MONET_TASK_SOURCE_STORE, taskId));
    }

    public static File getTaskResultStore(Context context, String taskId) {
        return getDirectory(context, String.format(MONET_TASK_RESULT_STORE, taskId));
    }

    public static File getTaskResultSchemaFile(Context context, String taskId) {
        return new File(getTaskResultStore(context, taskId), MONET_TASK_SCHEMA_FILE);
    }

    public static File getTaskInitializeStore(Context context, String taskId) {
        return getDirectory(context, String.format(MONET_TASK_STORE, taskId));
    }

    public static File getTaskInitializeSchemaFile(Context context, String taskId) {
        return new File(getTaskInitializeStore(context, taskId), "");
    }

    public static File getTaskDefaultSchemaStore(Context context, String taskId) {
        return getDirectory(context, String.format(MONET_TASK_STORE, taskId));
    }

    public static File getTaskDefaultSchemaFile(Context context, String taskId) {
        return new File(getTaskInitializeStore(context, taskId), MONET_TASK_INITIALIZE_SCHEMA_FILE);
    }

    public static File getTaskResultMetadataFile(Context context, String taskId) {
        return new File(getTaskResultStore(context, taskId), MONET_TASK_METADATA_FILE);
    }

    public static File[] getTaskResultDataFiles(Context context, String taskId) {
        File dir = getTaskResultStore(context, taskId);

        ArrayList<File> files = new ArrayList<File>();

        for (final File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) continue;
            if (fileEntry.getName().equals(MONET_TASK_METADATA_FILE)) continue;
            if (fileEntry.getName().equals(MONET_TASK_SCHEMA_FILE)) continue;

            files.add(fileEntry);
        }

        File[] result = new File[files.size()];
        return files.toArray(result);
    }

    public static File getTaskStoreImageCache(Context context, String taskId) {
        return new File(context.getCacheDir(), taskId);
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists())
            deleteDirectoryFiles(directory.listFiles());
        return directory.delete();
    }

    private static void deleteDirectoryFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory())
                deleteDirectory(file);
            else
                file.delete();
        }
    }

    public static void deleteTask(Context context, long taskId) {
        File taskDir = getDirectory(context, String.format(MONET_TASK_STORE, taskId));
        deleteDirectory(taskDir);
        MediaScannerConnection.scanFile(context, new String[]{taskDir.getParent()}, null, null);

        File cacheDir = getTaskStoreImageCache(context, String.valueOf(taskId));
        deleteDirectory(cacheDir);
        MediaScannerConnection.scanFile(context, new String[]{cacheDir.getParent()}, null, null);

    }

    public static File createTempFile(Context context) throws IOException {
        return File.createTempFile("result", null, getDirectory(context, MONET_TEMP_STORE));
    }

    public static File getAppIconFile(Context context) {
        return new File(context.getExternalFilesDir(null).getAbsolutePath(), "appicon.png");
    }

    public static void copyMonetSchemaFile(File defaultValues, Context context, String taskId) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(defaultValues);
            File defaultValues2 = new File(LocalStorage.getTaskInitializeSchemaFile(context,taskId), ".default.txt");
            outputStream = new FileOutputStream(defaultValues2);
            if (inputStream.available() > 0) {
                int data;
                while  ((data=inputStream.read()) != -1){
                    char caracter = (char) data;
                    if (caracter == '\t') continue;
                    if (caracter == '\r') continue;
                    if (caracter == '\n') continue;
                    outputStream.write(data);
                }

            }
        } catch (Exception e) {
            Log.error("Error reading default schema: " + e.getMessage(), e);
            throw new RuntimeException("Can't read default schema", e);
        } finally {
            StreamHelper.close(inputStream);
            StreamHelper.close(outputStream);
        }

    }
}
