package org.monet.docservice.docprocessor.worker;

import org.monet.docservice.core.Key;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class WorkQueueItem {

  public static final int STATE_PENDING = 1;
  public static final int STATE_IN_PROGRESS = 2;
  public static final int STATE_FINISH = 3;
  public static final int STATE_ERROR = 4;
  
  private long id;
  private Key documentKey;
  private int operation;
  private Date queueDate;
  private Date startDate;
  private Date finishDate;
  private int state;
  private String errorMessage;
  private InputStream inputExtraData;
  private OutputStream outputExtraData;
  private String xmlData;
  private File dataFile;

  public WorkQueueItem(long id) {
    this.id = id;
  }
  
  public long getId() {
    return id;
  }

  public void setDocumentKey(Key documentKey) {
    this.documentKey = documentKey;
  }

  public Key getDocumentKey() {
    return documentKey;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  public int getOperation() {
    return operation;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }

  public Date getFinishDate() {
    return finishDate;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getState() {
    return state;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setQueueDate(Date queueDate) {
    this.queueDate = queueDate;
  }

  public Date getQueueDate() {
    return queueDate;
  }
  
  public InputStream getExtraDataInputStream() {
    return this.inputExtraData;
  }
  
  public void setExtraDataInputStream(InputStream stream) {
    this.inputExtraData = stream;
  }
  
  public OutputStream getExtraDataOutputStream() {
    return this.outputExtraData;
  }
  
  public void setExtraDataOutputStream(OutputStream stream) {
    this.outputExtraData = stream;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  public String getXmlData() {
    return xmlData;
  }

  public void setDataFile(File dataFile) {
    this.dataFile = dataFile;
  }

  public File getDataFile() {
    return dataFile;
  }
}
