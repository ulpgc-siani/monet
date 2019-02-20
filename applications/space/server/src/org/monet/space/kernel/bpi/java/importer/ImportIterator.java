package org.monet.space.kernel.bpi.java.importer;

import org.monet.space.kernel.model.Progress;
import org.monet.space.kernel.model.ProgressCallback;
import org.monet.space.kernel.utils.PersisterHelper;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Iterator;

public class ImportIterator<T> implements Iterator<T> {

	private Class<?> itemClass;
	private Reader sourceReader;
	private StringBuilder workingBuffer = new StringBuilder();

	private String startTagToken, endTagToken;
	private boolean readyToRead = false;
	private ProgressCallback progressCallback;
	private Date startDate = new Date();
	private Progress progress = new Progress();
	private final long sourceSize;
	private long sourceReaded;
	private long itemsImported = 0;

	public ImportIterator(Class<?> itemClass, Reader sourceReader, long sourceSize, String tag) {
		this(itemClass, sourceReader, sourceSize, tag, null);
	}

	public ImportIterator(Class<?> itemClass, Reader sourceReader, long sourceSize, String tag, ProgressCallback progressCallback) {
		this.itemClass = itemClass;
		this.sourceReader = sourceReader;
		this.progressCallback = progressCallback;
		this.sourceSize = sourceSize;

		this.startTagToken = String.format("<%s>", tag);
		this.endTagToken = String.format("</%s>", tag);
	}

	@SuppressWarnings("unchecked")
	private T buildItem(String data) throws Exception {
		return (T) PersisterHelper.load(data, this.itemClass);
	}

	private boolean fetchNext() {
		try {
			int n = 0;
			while ((n = this.workingBuffer.indexOf(this.startTagToken)) < 0) {
				String nextSlice = this.readNextSlice();
				if (nextSlice == null)
					return false;
				if (this.itemsImported == 0 && this.workingBuffer.length() == 0 && nextSlice.trim().indexOf("<") != 0) {
					//Check if it is an xml
					throw new RuntimeException("Input is not an Xml");
				}
				this.workingBuffer.append(nextSlice);
			}
			this.workingBuffer.delete(0, n);
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	private int fetchEnd() {
		try {
			int n = 0;
			while ((n = this.workingBuffer.indexOf(this.endTagToken)) < 0) {
				String nextSlice = this.readNextSlice();
				if (nextSlice == null)
					return -1;
				this.workingBuffer.append(nextSlice);
			}
			return n + this.endTagToken.length();
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public boolean hasNext() {
		if (!this.readyToRead) {
			this.readyToRead = fetchNext();
			return this.readyToRead;
		}
		return true;
	}

	@Override
	public T next() {
		try {
			this.itemsImported++;
			String newItem = this.readNextItem();
			return buildItem(newItem);
		} catch (Exception e) {
			throw new RuntimeException("Can't read next element", e);
		}
	}

	private String readNextItem() {
		if (!this.readyToRead) {
			this.readyToRead = fetchNext();
		}
		if (!this.readyToRead) throw new RuntimeException("No more items to read");

		int endIndex = fetchEnd();
		String item = this.workingBuffer.substring(0, endIndex);
		this.workingBuffer.delete(0, endIndex);
		this.readyToRead = false;
		return item;
	}

	private String readNextSlice() throws IOException {
		char[] buffer = new char[2048];
		int readed = this.sourceReader.read(buffer);
		this.sourceReaded += readed;
		if (readed <= 0)
			return null;

		return new String(buffer, 0, readed);
	}

	@Override
	public void remove() {
	}

	public void onComplete() {
		long currentTime = (new Date()).getTime();
		long timeElapsed = currentTime - this.startDate.getTime();

		this.progress.update(100, this.itemsImported, timeElapsed, currentTime, 0);
		if (this.progressCallback != null)
			this.progressCallback.onComplete(this.progress);
	}

	public void onItemImported() {
		long currentTime = (new Date()).getTime();
		long timeElapsed = currentTime - this.startDate.getTime();
		int relativeProgress = (int) (((this.sourceReaded - this.workingBuffer.length()) * 100.0f) / this.sourceSize);

		if (relativeProgress == 0) relativeProgress = 1;
		long estimatedDateTime = currentTime + (timeElapsed * 100 / relativeProgress);

		this.progress.update(relativeProgress, this.itemsImported, timeElapsed, estimatedDateTime, estimatedDateTime - currentTime);
		if (this.progressCallback != null)
			this.progressCallback.onProgressUpdate(this.progress);
	}

}