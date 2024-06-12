package org.monet.bpi.java;

import org.monet.bpi.JobResponse;
import org.monet.bpi.Schema;
import org.monet.bpi.SensorResponse;
import org.monet.bpi.types.*;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;

public class JobResponseImpl extends ProviderResponseImpl implements JobResponse, SensorResponse {

	private Date startDate;
	private Date endDate;
	private Class<? extends Schema> schemaClass;

	@Override
	public Schema getSchema() {
		throw new NotImplementedException();
	}

	public File getFile(FileLink link) {
		throw new NotImplementedException();
	}

	public List<File> getFiles(List<FileLink> links) {
		throw new NotImplementedException();
	}

	public Picture getPicture(PictureLink link) {
		throw new NotImplementedException();
	}

	public List<Picture> getPictures(List<PictureLink> links) {
		throw new NotImplementedException();
	}

	public Video getVideo(VideoLink link) {
		throw new NotImplementedException();
	}

	public List<Video> getVideos(List<VideoLink> links) {
		throw new NotImplementedException();
	}

	@Override
	public Date getStartDate() {
		throw new NotImplementedException();
	}

	@Override
	public Date getEndDate() {
		throw new NotImplementedException();
	}

}
