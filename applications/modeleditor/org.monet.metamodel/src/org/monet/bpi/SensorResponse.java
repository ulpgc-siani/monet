package org.monet.bpi;

import java.util.List;

import org.monet.bpi.types.Date;
import org.monet.bpi.types.File;
import org.monet.bpi.types.FileLink;
import org.monet.bpi.types.Picture;
import org.monet.bpi.types.PictureLink;
import org.monet.bpi.types.Video;
import org.monet.bpi.types.VideoLink;

public interface SensorResponse {

  Date getStartDate();
  Date getEndDate();
  
  Schema getSchema();
  File getFile(FileLink link);
  List<File> getFiles(List<FileLink> links);
  Picture getPicture(PictureLink link);
  List<Picture> getPictures(List<PictureLink> links);
  Video getVideo(VideoLink link);
  List<Video> getVideos(List<VideoLink> links);
  
}
