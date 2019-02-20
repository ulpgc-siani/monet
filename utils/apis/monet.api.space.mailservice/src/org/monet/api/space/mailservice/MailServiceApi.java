package org.monet.api.space.mailservice;

import org.monet.api.space.mailservice.impl.model.Email;

public interface MailServiceApi {

  public boolean sendMail(Email email);
}
