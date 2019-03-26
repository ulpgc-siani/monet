package org.monet.federation.accountoffice.core.configuration.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.encrypt.Certificater;
import org.monet.encrypt.Hasher;
import org.monet.exceptions.ConfigurationException;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.configuration.ServerConfigurator;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration.Authentication.UserPassword;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration.Authentication.Micv;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration.Organization;
import org.monet.filesystem.StreamHelper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.*;

@Singleton
public class ConfigurationImpl implements Configuration {
    private Logger logger;
    private ServerConfigurator serverConfigurator;
    private Properties properties;
    private Properties queries;

    private FederationConfiguration federationConfiguration;
    private Map<String, String> languages;
    private List<Organization.Label> languagesSupport;

    @Inject
    public ConfigurationImpl(Logger logger, ServerConfigurator serverConfigurator) {
        this.logger = logger;
        this.serverConfigurator = serverConfigurator;
        this.properties = new Properties();
        this.queries = new Properties();

        this.initialize();
    }

    public void initialize() {
        InputStream configurationFileStream = null;
        InputStream queriesFileStream = null;
        InputStreamReader languagesFileStream = null;

        try {
            String configurationPath = this.serverConfigurator.getUserPath() + File.separator + CONFIGURATION_FILENAME;
            configurationFileStream = new FileInputStream(configurationPath);
            this.properties.loadFromXML(configurationFileStream);

            String queriesPath = "/accountoffice/database/%s.queries.sql";
            String databaseType = ((String) this.properties.get(Configuration.JDBC_DATABASE)).toLowerCase();
            queriesFileStream = (InputStream) ConfigurationImpl.class.getResourceAsStream(String.format(queriesPath, databaseType));
            this.queries.load(queriesFileStream);

        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new ConfigurationException("Error loading configuration file", e);
        } finally {
            StreamHelper.close(configurationFileStream);
            StreamHelper.close(queriesFileStream);
        }

        readServiceConfig();

        try {
            String languagePath = "/accountoffice/languages/language.types";
            languagesFileStream = new InputStreamReader((InputStream) ConfigurationImpl.class.getResourceAsStream(languagePath), "UTF-8");
            Properties langs = new Properties();
            langs.load(languagesFileStream);

            this.languages = new HashMap<String, String>();
            Iterator<Object> itr = langs.keySet().iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                String lang = isLanguageSupport(key);
                if (langs.getProperty(lang) != null)
                    languages.put(lang, langs.getProperty(lang));
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new ConfigurationException("Error loading languages types file", e);
        } finally {
            if (languagesFileStream != null) {
                try {
                    languagesFileStream.close();
                } catch (Exception e) {
                }
            }
        }

        //isCertificateAuthorityCreated();
    }

    @Override
    public String getCertificateAuthorityIdentifier() {
        return CERTIFICATE_AUTHORITY_IDENTIFIER;
    }

    @Override
    public String getCertificateAuthorityPassword() {
        String password = (String) this.properties.get(Configuration.CERTIFICATE_AUTHORITY_PASSWORD);
        if (password == null) {
            this.logger.error("CertificateAuthority.Password configuration variable not found");
            throw new ConfigurationException("CA password not found");
        }

        try {
            password = Hasher.getMD5asHexadecimal(password);
        } catch (Exception e) {
            this.logger.error("Error get password MD5 as Hex.");
            throw new ConfigurationException("Error get password MD5 as Hex.");
        }
        return password;
    }

    @Override
    public String getCertificateAuthorityPath() {
        return this.serverConfigurator.getUserPath() + File.separator + "ca.keyStore";
    }

    @Override
    public String getCertificatePath() {
        String filePath = (String) this.properties.get(Configuration.CERTIFICATE_FILE);
        if (filePath == null) return null;
        return this.serverConfigurator.getUserPath() + File.separator + filePath;
    }

    @Override
    public String getCertificatePassword() {
        return (String) this.properties.get(Configuration.CERTIFICATE_PASSWORD);
    }

    @Override
    public String getSocketHost() {
        String host = this.properties.getProperty(Configuration.SOCKET_HOST);

        if (host == null) {
            host = "localhost";
            this.logger.error("Socket.Host configuration variable not found. Assuming host: localhost");
        }

        return host;
    }

    @Override
    public int getSocketPort() {
        String port = this.properties.getProperty(Configuration.SOCKET_PORT);

        if (port == null) {
            port = "5348";
            this.logger.error("Socket.Port configuration variable not found. Assuming port: 5348");
        }

        return Integer.parseInt(port);
    }

    @Override
    public int getSocketMaxSessions() {
        String maxSessions = this.properties.getProperty(Configuration.SOCKET_MAX_SESSIONS);

        if (maxSessions == null) {
            maxSessions = "10";
            this.logger.error("Socket.MaxSessions configuration variable not found. Assuming max sessions value: 10");
        }

        return Integer.parseInt(maxSessions);
    }

	@Override
	public Set<String> getSocketAllowedAddresses() {
		String allowedAddresses = this.properties.getProperty(Configuration.SOCKET_ALLOWED_ADDRESSES);

		if (allowedAddresses == null) {
			this.logger.error("Socket.AllowedAddresses configuration variable not found. Assuming [" + this.getSocketHost() + "] as allowed addresses list");
			return new HashSet<String>() {{ add(ConfigurationImpl.this.getSocketHost()); }};
		}

		String[] allowedAddressesArray = allowedAddresses.split(",");

		return new HashSet<String>(Arrays.asList(allowedAddressesArray));
	}

	@Override
    public String getDatabaseSource() {
        String dataSource = this.properties.getProperty(Configuration.JDBC_DATASOURCE);
        if (dataSource == null) {
            this.logger.error("Jdbc.DataSource configuration variable not found");
            throw new ConfigurationException("Jdbc.DataSource configuration variable not found");
        }

        return dataSource;
    }

    @Override
    public Properties getQueries() {
        return this.queries;
    }

    @Override
    public String getName() {
        return this.getProperty(Configuration.NAME);
    }

    @Override
    public void setLabel(String label) {
        for (Organization.Label orgLabel : this.languagesSupport) {
            orgLabel.setText(label);
        }
    }

    @Override
    public String getLabel() {
        for (Organization.Label orgLabel : this.languagesSupport) {
            return orgLabel.getText();
        }
        return "";
    }

    @Override
    public long getInactivityTime() {
        String sSeconds = this.properties.getProperty(Configuration.INACTIVITY_TIME);
        if (sSeconds == null) {
            sSeconds = "600";
            this.logger.error("InactivityTime configuration variable not found");
            throw new ConfigurationException("InactivityTime configuration variable not found");
        }

        long mlSeconds = Integer.parseInt(sSeconds) * 1000;

        return mlSeconds;
    }

    @Override
    public String getLogoPath() {
        return "/accounts/authorization/resources/?id=logo";
    }

	@Override
    public String getLogoFilename() {
        String filename = this.serverConfigurator.getUserPath() + Configuration.IMAGES_FOLDER + File.separator + Configuration.LOGO;

        if (new File(filename).exists())
            return filename;

        return this.serverConfigurator.getImagesPath() + File.separator + Configuration.DEFAULT_LOGO;
    }

	@Override
	public String getDefaultSpaceLogoPath() {
		return "/accounts/authorization/resources/?id=space";
	}

	@Override
	public String getDefaultSpaceLogoFilename() {
		return this.serverConfigurator.getImagesPath() + File.separator + Configuration.DEFAULT_SPACE_LOGO;
	}

    @Override
    public long getMaxRememberTime() {
        String sDays = this.properties.getProperty(Configuration.MAX_REMEMBER_TIME);
        if (sDays == null) {
            sDays = "7";
            this.logger.error("MaxRememberTime configuration variable not found");
            throw new ConfigurationException("MaxRememberTime configuration variable not found");
        }

        long mlSeconds = Integer.parseInt(sDays) * 24 * 60 * 60 * 1000;

        return mlSeconds;
    }

    @Override
    public int getMaxSessions() {
        String sMax = this.properties.getProperty(Configuration.MAX_SESSIONS);
        if (sMax == null) {
            sMax = "100";
            this.logger.error("MaxSessions configuration variable not found");
            throw new ConfigurationException("MaxSessions configuration variable not found");
        }

        int max = Integer.parseInt(sMax);
        return max;
    }

    @Override
    public boolean isRegisterEnable() {
        return federationConfiguration.allowUserRegistration();
    }

    @Override
    public int getSuspendTime() {
        String suspendTime = (String) this.properties.get(Configuration.SUSPEND_TIME);
        if (suspendTime == null) {
            this.logger.error("SuspendTime configuration variable not found");
            throw new ConfigurationException("SuspendTime configuration variable not found");
        }
        return Integer.parseInt(suspendTime);
    }

    @Override
    public int getRemoveSuspendTime() {
        String removeSuspendTime = (String) this.properties.get(Configuration.REMOVE_SUSPEND_TIME);
        if (removeSuspendTime == null) {
            this.logger.error("RemoveSuspendTime configuration variable not found");
            throw new ConfigurationException("RemoveSuspendTime configuration variable not found");
        }
        return Integer.parseInt(removeSuspendTime);
    }

    @Override
    public UserPassword getUserPasswordAuth() {
        return this.federationConfiguration.getAuthentication().usePassword();
    }

    @Override
    public Micv getMicvAuth() {
        return this.federationConfiguration.getAuthentication().useMicv();
    }

    @Override
    public long getSignTimestampInterval() {
        String sSeconds = this.properties.getProperty(Configuration.SIGN_TIMESTAMP_INTERVAL);
        if (sSeconds == null) {
            sSeconds = "600";
            this.logger.error("SignTimestampInterval configuration variable not found");
            throw new ConfigurationException("SignTimestampInterval configuration variable not found");
        }

        long mlSeconds = Integer.parseInt(sSeconds) * 1000;

        return mlSeconds;
    }

    @Override
    public String getConfigurationFile() {
        return this.serverConfigurator.getUserPath() + File.separator + CONFIGURATION_FILENAME;
    }

	@Override
	public String getBackservicePath() {
		return "/servlet/backservice";
	}

	@Override
    public Properties getLanguage(String lang) {
        if (lang == null) lang = "en";

        Properties langProperties = new Properties();
        String languagePath = "/accountoffice/languages/%s.lang";
        InputStreamReader languageFileStream = null;

        try {
            languageFileStream = new InputStreamReader(ConfigurationImpl.class.getResourceAsStream(String.format(languagePath, lang.toLowerCase())), "UTF-8");
            langProperties.load(languageFileStream);

            String orgName = "";
            for (Organization.Label orgLabel : this.languagesSupport) {
                if (orgLabel.getLang().equalsIgnoreCase(lang))
                    orgName = orgLabel.getText();
            }
            langProperties.put(TemplateComponent.ORGANIZATION, orgName);
        } catch (Exception e) {
            try {
                languageFileStream.close();
            } catch (IOException e1) {
            }
            logger.error(e.getMessage(), e);
        }
        return langProperties;
    }

    @Override
    public String isLanguageSupport(String lang) {
        for (Organization.Label org : this.languagesSupport) {
            if (org.getLang().equalsIgnoreCase(lang))
                return lang;
        }
        return this.languagesSupport.get(0).getLang();
    }

    @Override
    public Map<String, String> getSelectableLanguages() {
        return this.languages;
    }

    @Override
    public String getTemplatePath() {
        return ConfigurationImpl.class.getResource("/accountoffice").getPath();
    }

    @Override
    public String getResourcePath() {
        return this.serverConfigurator.getResourcePath();
    }

    @Override
    public String getP7B() {
        String filePath = (String) this.properties.get(Configuration.CERTIFICATE_AUTHORITY_FILE);
        if (filePath == null) {
            this.logger.error("p7b file not found");
            throw new ConfigurationException("p7b file not found");
        }
        return this.serverConfigurator.getUserPath() + File.separator + filePath;
    }

    @Override
    public boolean isUserPasswordActive() {
        return this.federationConfiguration.getAuthentication().usePassword() != null;
    }

    @Override
    public boolean isCertificateActive() {
        return this.federationConfiguration.getAuthentication().useCertificate() != null;
    }

    @Override
    public boolean isOpenIDActive() {
        return this.federationConfiguration.getAuthentication().useOpenId() != null;
    }

    @Override
    public boolean isMicvActive() {
        return this.federationConfiguration.getAuthentication().useMicv() != null;
    }

    @Override
    public String getProperty(String propertyName) {
        String propertyValue = (String) this.properties.get(propertyName);
        if (propertyValue == null) {
            this.logger.error("Property (" + propertyName + ") configuration variable not found");
            throw new ConfigurationException("Property (" + propertyName + ") configuration variable not found");
        }
        return propertyValue;
    }

    @Override
    public String getProperty(String propertyName, String defaultValue) {
        String propertyValue = (String) this.properties.get(propertyName);
        if (propertyValue == null) return defaultValue;
        return propertyValue;
    }

    @Override
    public void reload() {
        this.initialize();
    }

    @Override
    public String getMobileFCMSettingsFile() {
        String fileName = this.properties.getProperty(Configuration.MOBILE_FCM_SETTINGS_FILE);
        if (fileName == null) {
            this.logger.warn("Mobile.FCM.SettingsFile configuration variable not defined. Push notifications disabled!");
            return "";
        }

        return fileName;
    }

    @Override
    public String getMobileFCMProjectId() {
        String fileName = this.properties.getProperty(Configuration.MOBILE_FCM_PROJECT_ID);
        if (fileName == null) {
            this.logger.warn("Mobile.FCM.ProjectId configuration variable not defined. Push notifications disabled!");
            return "";
        }

        return fileName;
    }

    @Override
    public String getSpaceBackserviceServletPath(URI spaceUri) {
        return spaceUri.toString() + "/servlet/backservice";
    }

    private void isCertificateAuthorityCreated() {
        String pathStorePKCS12 = this.serverConfigurator.getUserPath() + File.separator + "ca.keyStore";
        String pathStorePKCS7 = this.getP7B();
        File caStore = new File(pathStorePKCS12);
        if (!caStore.exists()) {
            Date startDate = new Date(System.currentTimeMillis());
            Date expiryDate = new Date(System.currentTimeMillis() + 31536000000L * 10); // Ten
            // years
            BigInteger serial = new BigInteger(String.valueOf(System.currentTimeMillis()));
            try {
                KeyPair keyPair = Certificater.generateKeyPair(Certificater.ALGORITHM_RSA, 1024);
                X509Certificate ca = Certificater.generateCertificateV1(startDate, expiryDate, serial, keyPair, Certificater.SIGNATURE_ALGORITHM_SHA1_RSA, "CN=RootCertificate");
                RSAPrivateCrtKey privKey = (RSAPrivateCrtKey) keyPair.getPrivate();
                Certificater.savePKCS12(pathStorePKCS12, CERTIFICATE_AUTHORITY_IDENTIFIER, ca, privKey, this.getCertificateAuthorityPassword());
                File p7bStore = new File(pathStorePKCS7);
                List<Certificate> certificates = new ArrayList<Certificate>();
                if (p7bStore.exists())
                    certificates = Certificater.loadCertificatesFromP7bFile(pathStorePKCS7);

                certificates.add(ca);
                Certificater.saveCertificatesInP7bFile(pathStorePKCS7, privKey, ca, certificates);
            } catch (Exception e) {
                this.logger.error(e.getMessage(), e);
                throw new ConfigurationException("Error creating root certificate", e);
            }
        }

    }

    private void readServiceConfig() {
        try {
            File input = new File(this.serverConfigurator.getUserPath() + File.separator + SERVICE_CONFIGURATION_FILENAME);
            Serializer serializer = new Persister();

            this.federationConfiguration = serializer.read(FederationConfiguration.class, input, false);

            // Languages
            this.languagesSupport = federationConfiguration.getOrganization().getListLabel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConfigurationException("Error loading authentication service configuration file", e);
        }
    }

}
