package org.monet.bpi;

import org.monet.bpi.types.*;

import java.util.List;

public interface JobResponse {

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
