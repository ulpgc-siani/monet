package org.monet.federation.accountoffice.core.components.templatecomponent;

import org.monet.federation.accountoffice.core.model.BusinessUnit;
import org.monet.federation.accountoffice.core.model.Federation;

import javax.servlet.http.HttpServletResponse;

public interface TemplateComponent {

    // TEMPLATES
    public static final String TEMPLATE_LOGIN = "login.html";
    public static final String TEMPLATE_SERVICES = "services.html";
    public static final String TEMPLATE_REGISTER = "register.html";
    public static final String TEMPLATE_REGISTER_OK = "userregister.html";

    public static final String ORGANIZATION = "organization";
    public static final String RETRIES = "retries";
    public static final String RETRIES_MULTIPLE = "retriesMultiple";
    public static final String RETRIES_SUSPENDED = "retriesSuspended";
    public static final String SECONDS = "seconds";
    public static final String USER_NOT_FOUND = "userNotFound";
    public static final String USER = "user";
    public static final String HOME = "home";
    public static final String SURNAME = "surname";
    public static final String FULLNAME = "fullname";
    public static final String PASSWORD = "password";
    public static final String USERNAME_PASSWORD = "usernamePassword";
    public static final String CERTIFICATE = "certificate";
    public static final String OPEN_ID = "openId";
    public static final String RPASSWORD = "rPassword";
    public static final String SIGNIN = "signin";
    public static final String SEND = "send";
    public static final String CHANGE = "change";
    public static final String REMEMBERME = "rememberme";
    public static final String REGISTER = "register";
    public static final String REGISTER_MESSAGE = "registerMessage";
    public static final String CANCEL = "cancel";
    public static final String ABOUT = "about";
    public static final String COPYRIGHT = "copyright";
    public static final String PRIVACY = "privacy";
    public static final String OATH_TOKEN = "token";
    public static final String LANGUAGES = "languages";
    public static final String LANGUAGE = "lang";
    public static final String LOGO = "logo";
    public static final String USER_CREATE_MSG = "userCreateMsg";
    public static final String USER_LOGGER_MSG = "userLogged";
    public static final String ENABLE_REGISTER = "allowRegister";
    public static final String APPLICATIONS = "apps";
    public static final String NO_SERVICE_ENABLE = "noServiceEnable";
    public static final String CLOSE = "closeSession";
    public static final String WELLCOME = "wellcome";
    public static final String POWERED_BY = "poweredBy";
    public static final String CHANGE_LANGUAGE = "changeLanguage";
    public static final String USECERTIFICATE = "useCertificate";
    public static final String USEUSERPASS = "useUserPass";
    public static final String UNITS = "units";
    public static final String LOGIN_TITLE = "loginTitle";
    public static final String REGISTER_TITLE = "registerTitle";
    public static final String FEDERATION_TRUST_REQUEST = "federationTrustRequest";
    public static final String FEDERATION_TRUST_REQUEST_SUBTITLE = "federationTrustRequestSubtitle";
    public static final String FEDERATION_TRUST_REQUEST_MESSAGE = "federationTrustRequestMessage";
    public static final String FEDERATION_TRUST_INFO = "federationTrustInfo";
    public static final String FEDERATION_TRUST_NAME = "federationTrustName";
    public static final String FEDERATION_TRUST_LABEL = "federationTrustLabel";
    public static final String FEDERATION_TRUST_URL = "federationTrustUrl";
    public static final String FEDERATION_TRUST_VALIDATION_SUCCESS = "federationTrustValidationSuccess";
    public static final String FEDERATION_TRUST_VALIDATION_FAILURE = "federationTrustValidationFailure";
    public static final String BUSINESS_UNIT_PARTNER_REQUEST = "businessUnitPartnerRequest";
    public static final String BUSINESS_UNIT_PARTNER_REQUEST_SUBTITLE = "businessUnitPartnerRequestSubtitle";
    public static final String BUSINESS_UNIT_PARTNER_REQUEST_MESSAGE = "businessUnitPartnerRequestMessage";
    public static final String BUSINESS_UNIT_PARTNER_INFO = "businessUnitPartnerInfo";
    public static final String BUSINESS_UNIT_PARTNER_NAME = "businessUnitPartnerName";
    public static final String BUSINESS_UNIT_PARTNER_LABEL = "businessUnitPartnerLabel";
    public static final String BUSINESS_UNIT_PARTNER_URL = "businessUnitPartnerUrl";
    public static final String BUSINESS_UNIT_PARTNER_VALIDATION_SUCCESS = "businessUnitPartnerValidationSuccess";
    public static final String BUSINESS_UNIT_PARTNER_VALIDATION_FAILURE = "businessUnitPartnerValidationFailure";
    public static final String VALIDATION_CODE_TITLE = "validationCodeTitle";

    // ERRORS
    public static final String ERROR_CREATE_USER = "errorCreateUser";
    public static final String ERROR_USER_EXISTS = "errorExistsUser";

    public static final String ERROR_DISABLE_APPLICATION = "errorDisableApplication";
    public static final String ERROR_ALLOW_FOLLOW = "errorAllowFollow";
    public static final String ERROR = "error";
    public static final String ERROR_RESET_PASSWORD = "errorResetPassword";
    public static final String ERROR_CHANGE_PASSWORD = "errorChangePassword";

    public static final String OTHER_PROVIDER = "otherProvider";
    public static final String ORGANIZATION_NAME = "organizationName";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String CITY = "city";
    public static final String PROVINCE = "province";
    public static final String COUNTRY = "country";
    public static final String REGISTER_SAVE = "registerSave";
    public static final String RESET_PASSWORD = "resetPassword";
    public static final String RESET_PASSWORD_TITLE = "resetPasswordTitle";
    public static final String RESET_PASSWORD_SEND = "resetPasswordSend";
    public static final String RESET_PASSWORD_MESSAGE = "resetPasswordMessage";
    public static final String SUCCESSFULLY_RESET_PASSWORD = "successfullyResetPassword";
    public static final String CHANGE_PASSWORD_TITLE = "changePasswordTitle";
    public static final String CHANGE_PASSWORD_SEND = "changePasswordSend";
    public static final String OLD_PASSWORD = "oldPassword";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String SUCCESSFULLY_CHANGE_PASSWORD = "successfullyChangePassword";
    public static final String CHANGE_PASSWORD = "changePassword";

    public void createTemplate(HttpServletResponse response, String path, boolean showLoginError, String redirectURL, String lang, String page, String urlrequest);

    public void createLoginTemplate(HttpServletResponse response, boolean showLoginError, String requestToken, String lang, String urlrequest, int retries);

    public void createRegisterTemplate(HttpServletResponse response, String path, boolean showRegisterError, String token, String lang, String requestURL, String additionalMessage);

    public void createLoggedTemplate(HttpServletResponse response, String user, String error, String lang, String requestURL);

    public void createResetPasswordTemplate(HttpServletResponse response, String path, boolean b, String token, String lang, String requestURL, String string);

    public void createChangePasswordTemplate(HttpServletResponse response, String path, boolean b, String token, String lang, String requestURL, String additionalMessage);

    public void createValidateFederationTrustTemplate(HttpServletResponse response, Federation federation, String requestURL, Boolean result, String lang);

    public void createValidateBusinessUnitPartnerTemplate(HttpServletResponse response, BusinessUnit businessUnit, String requestURL, Boolean result, String lang);
}
