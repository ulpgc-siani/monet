package org.monet.space.kernel.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "string") // compatibility mode for task processes data
public class MailBoxUri extends Uri {

    public static MailBoxUri build(String partner, String id) {
        return new MailBoxUri(partner, id);
    }

    public static MailBoxUri build(String uri) {
        return build(uri, MailBoxUri.class);
    }

    @Override
    public String toString() {
        return toString(MAILBOX_URI_HEADER);
    }

    public MailBoxUri(@Attribute String partner, @Attribute String id) {
        super(MAILBOX_URI_HEADER, partner, id);
    }

    public MailBoxUri() {
        super(MAILBOX_URI_HEADER);
    }

}
