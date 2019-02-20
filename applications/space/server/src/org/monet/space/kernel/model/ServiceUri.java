package org.monet.space.kernel.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "string")
public class ServiceUri extends Uri {

    public static ServiceUri build(String partner, String id) {
      return new ServiceUri(partner, id);
    }

    public static ServiceUri build(String uri) {
        return build(uri, ServiceUri.class);
    }

    @Override
    public String toString() {
        return toString(SERVICE_URI_HEADER);
    }

    public ServiceUri(@Attribute String partner, @Attribute String id) {
        super(SERVICE_URI_HEADER, partner, id);
    }

    public ServiceUri() {
        super(SERVICE_URI_HEADER);
    }
}
