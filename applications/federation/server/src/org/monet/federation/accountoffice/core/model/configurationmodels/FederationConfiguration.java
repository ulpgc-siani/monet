package org.monet.federation.accountoffice.core.model.configurationmodels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "federation")
public class FederationConfiguration {
  @Element
  Organization organization;
  @Element(name = "allow-user-registration", required = false)
  AllowUserRegistration allowUserRegistration;
  @Element(name = "authentication")
  Authentication authentication;

  public Organization getOrganization() {
    return organization;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public boolean allowUserRegistration() {
    return allowUserRegistration != null;
  }


  public static class Organization {
    @ElementList(entry = "label", name = "label", inline = true)
    List<Label> labels;
    @Element
    Image image;

    public List<Label> getListLabel() {
      return labels;
    }

    public String getImage() {
      return image.getImage();
    }

    public static class Label {
      @Attribute
      String lang;
      @Text
      String text;

      public String getLang() {
        return lang;
      }

      public String getText() {
        return StringEscapeUtils.escapeHtml(text);
      }

      public void setText(String label) {
        this.text = label;
      }
    }

    public static class Image {
      @Attribute
      String src;

      public Image() {
      }

      public String getImage() {
        return src;
      }
    }
  }

  public static class AllowUserRegistration {
  }

  public static class Authentication {


    @Element(name = "use-open-id", required = false)
    OpenID useOpenId;
    @Element(name = "use-certificate", required = false)
    Certificate useCertificate;
    @Element(name = "use-password", required = false)
    UserPassword usePassword;
    @Element(name = "use-micv", required = false)
    Micv useMicv;

    public OpenID useOpenId() {
      return useOpenId;
    }

    public Certificate useCertificate() {
      return useCertificate;
    }

    public UserPassword usePassword() {
      return usePassword;
    }

    public Micv useMicv() {
      return useMicv;
    }


    public static class OpenID {
    }

    public static class Certificate {
    }

    public static class Micv {
      @Element(name = "micv-auth", required = false)
      MicvAuth micvAuth;

      public MicvAuth getMicvAuth() {
        return micvAuth;
      }
    }

    public static class UserPassword {
      @Element(name = "mock-auth", required = false)
      MockAuth mockAuth;
      @Element(name = "ldap-auth", required = false)
      LdapAuth ldapAuth;
      @Element(name = "database-auth", required = false)
      DatabaseAuth databaseAuth;
      @Element(name = "custom-auth", required = false)
      CustomAuth customAuth;

      public LdapAuth getLdapAuth() {
        return ldapAuth;
      }

      public DatabaseAuth getDatabaseAuth() {
        return databaseAuth;
      }

      public CustomAuth getCustomAuth() {
        return customAuth;
      }

      public MockAuth getMockAuth() {
        return mockAuth;
      }
    }

    public abstract static class Auth {
      @Attribute(name = "read-only", required = false)
      private boolean readOnly = false;

      public boolean isReadOnly() {
        return readOnly;
      }
    }

    public static class MockAuth extends Auth {
    }

    public static class DatabaseAuth extends Auth {
    }

    public static class CustomAuth extends Auth {
      @Attribute(name = "class")
      private String clazz;

      public String getClassname() {
        return clazz;
      }
    }

    public static class LdapAuth extends Auth {
      @Attribute
      private String url;
      @Attribute
      private String user;
      @Attribute
      private String password;
      @Attribute
      private String schema;
      @ElementList(entry = "parameter", inline = true)
      public List<Parameter> parameters;
      private Map<String, String> aliasParameters;

      public String getUrl() {
        return url;
      }

      public String getUser() {
        return user;
      }

      public String getPassword() {
        return password;
      }

      public String getSchema() {
        return schema;
      }

      public Map<String, String> getAliasParamMap() {
        if (this.aliasParameters == null) {
          this.aliasParameters = new HashMap<String, String>();
          for (int i = 0; i < this.parameters.size(); i++)
            this.aliasParameters.put(this.parameters.get(i).getName(), this.parameters.get(i).getAs());
        }
        return this.aliasParameters;
      }

      public static class Parameter {
        @Attribute
        private String name;
        @Attribute
        private String as;

        public String getName() {
          return name;
        }

        public String getAs() {
          return as;
        }
      }

    }

    public static class MicvAuth extends Auth {
      @Attribute
      private String url;
      @Attribute
      private String ldap_url;
      @Attribute
      private String error_url;

      public String getUrl() {
        return url;
      }

      public String getLdapUrl() {
        return ldap_url;
      }

      public String getErrorUrl() {
        return error_url;
      }
    }

  }

}
