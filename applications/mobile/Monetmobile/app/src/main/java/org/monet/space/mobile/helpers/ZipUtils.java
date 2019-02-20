package org.monet.space.mobile.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskDetails.Attachment;

public class ZipUtils {


  public static ArrayList<TaskDetails.Attachment> decompress(File zipFile, File destinationFolder) throws IOException {
    ArrayList<TaskDetails.Attachment> attachments = new ArrayList<TaskDetails.Attachment>();
    FileOutputStream currentOutputFile = null;
    ZipInputStream zipStream = null;

    if ((zipFile == null) || (!zipFile.exists()) || (zipFile.length() <= 0)) return null;
    
    try {
      zipStream = new ZipInputStream(new FileInputStream(zipFile));

      ZipEntry entry;
      while ((entry = zipStream.getNextEntry()) != null) {
        File entryFile = new File(destinationFolder, entry.getName());

        if (entry.getExtra() != null) {
          String key = new String(entry.getExtra(), "UTF-8");
          String[] keyParts = key.split(";");
          if (keyParts.length == 2)
            attachments.add(new Attachment(keyParts[0], keyParts[1], entryFile.getAbsolutePath()));
        }

        if (entry.isDirectory())
          entryFile.mkdirs();
        else {
          try {
            entryFile.getParentFile().mkdirs();
            currentOutputFile = new FileOutputStream(entryFile);
            StreamHelper.copyStream(zipStream, currentOutputFile);
          } finally {
            StreamHelper.close(currentOutputFile);
          }
        }
      }

    } finally {
      StreamHelper.close(zipStream);
    }

    return attachments;
  }

}