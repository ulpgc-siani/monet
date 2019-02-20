package org.monet.editor.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;

public class CountingMultipartEntity extends MultipartEntity {

  private ProgressListener listener;
  private long contentLength = 0;

  public CountingMultipartEntity(org.apache.http.entity.mime.HttpMultipartMode mode, ProgressListener listener) {
    super(mode);
    this.listener = listener;
  }
  
  @Override
  public void addPart(FormBodyPart bodyPart) {
    super.addPart(bodyPart);
    this.contentLength += bodyPart.getBody().getContentLength();
  }
  
  @Override
  public void addPart(String name, ContentBody contentBody) {
    super.addPart(name, contentBody);
    this.contentLength += contentBody.getContentLength();
  }
  
  @Override
  public void writeTo(OutputStream outstream) throws IOException {
    super.writeTo(new CountingOutputStream(outstream, this.listener));
  }

  public static interface ProgressListener {
    void transferred(long num, long total);
  }

  public class CountingOutputStream extends FilterOutputStream {

    private final ProgressListener listener;

    private long                   transferred;

    public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
      super(out);
      this.listener = listener;
      this.transferred = 0;
    }

    public void write(byte[] b, int off, int len) throws IOException {
      out.write(b, off, len);
      this.transferred += len;
      this.listener.transferred(this.transferred, contentLength);
    }

    public void write(int b) throws IOException {
      out.write(b);
      this.transferred++;
      this.listener.transferred(this.transferred, contentLength);
    }
    
  }

}
