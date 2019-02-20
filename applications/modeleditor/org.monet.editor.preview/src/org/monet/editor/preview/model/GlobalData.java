package org.monet.editor.preview.model;

public interface GlobalData {

  public static final String MODEL_DIRECTORY  = "MODEL_DIRECTORY";
  public static final String OUTPUT_DIRECTORY = "OUTPUT_DIRECTORY";
  public static final String LANGUAGE         = "LANGUAGE";

  <T> T getData(Class<T> type, String key);

  void setData(String key, Object data);
}
