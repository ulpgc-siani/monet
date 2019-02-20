package org.monet.bpi.java;

import org.monet.bpi.JobResponse;
import org.monet.bpi.Schema;
import org.monet.bpi.SensorResponse;
import org.monet.bpi.types.*;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;

import java.util.ArrayList;
import java.util.List;

public class JobResponseImpl extends ProviderResponseImpl implements JobResponse, SensorResponse {

	private Date startDate;
	private Date endDate;
	private Class<? extends Schema> schemaClass;

	public JobResponseImpl(Message message, Class<? extends Schema> schemaClass, Date startDate, Date endDate) {
		super(message);
		this.schemaClass = schemaClass;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public Schema getSchema() {
		return this.getSchema(".schema", this.schemaClass);
	}

	public File getFile(FileLink link) {
		MessageAttach attach = this.message.getAttachment(link.getKey());
		if (attach != null)
			return new File(attach);
		else
			return null;
	}

	public List<File> getFiles(List<FileLink> links) {
		ArrayList<File> files = new ArrayList<File>();
		for (FileLink link : links)
			files.add(getFile(link));
		return files;
	}

	public Picture getPicture(PictureLink link) {
		MessageAttach attach = this.message.getAttachment(link.getKey());
		if (attach != null)
			return new Picture(attach);
		else
			return null;
	}

	public List<Picture> getPictures(List<PictureLink> links) {
		ArrayList<Picture> pictures = new ArrayList<Picture>();
		for (PictureLink link : links)
			pictures.add(getPicture(link));
		return pictures;
	}

	public Video getVideo(VideoLink link) {
		MessageAttach attach = this.message.getAttachment(link.getKey());
		if (attach != null)
			return new Video(attach);
		else
			return null;
	}

	public List<Video> getVideos(List<VideoLink> links) {
		ArrayList<Video> videos = new ArrayList<Video>();
		for (VideoLink link : links)
			videos.add(getVideo(link));
		return videos;
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public Date getEndDate() {
		return this.endDate;
	}

}
