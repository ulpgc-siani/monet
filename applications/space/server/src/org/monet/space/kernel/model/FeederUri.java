package org.monet.space.kernel.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "string")
public class FeederUri extends Uri {

    public static FeederUri build(String partner, String id) {
      return new FeederUri(partner, id);
    }

    public static FeederUri build(String uri) {
        return build(uri, FeederUri.class);
    }

    @Override
    public String toString() {
        return toString(FEEDER_URI_HEADER);
    }

    public FeederUri(@Attribute String partner, @Attribute String id) {
        super(FEEDER_URI_HEADER, partner, id);
    }

    public FeederUri() {
        super(FEEDER_URI_HEADER);
    }
}
