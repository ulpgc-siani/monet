package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class DownloadGlossaryRequest extends Request {

  public String Code;
  public String Context;
  public long   SyncMark;

  public DownloadGlossaryRequest() {
    super(ActionCode.DownloadGlossary);
  }

  public DownloadGlossaryRequest(String glossaryCode, String glossaryContext, long syncMark) {
    super(ActionCode.DownloadGlossary);
    this.Code = glossaryCode;
    this.Context = glossaryContext;
    this.SyncMark = syncMark;
  }

}
