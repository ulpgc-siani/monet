package org.monet.federation.setupservice.control.actions;

import org.apache.commons.mail.EmailException;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateFactory;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.BusinessUnitPartnerRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.FederationTrustRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.library.LibraryCodeGenerator;
import org.monet.federation.accountoffice.core.model.*;
import org.monet.federation.accountoffice.core.model.BusinessUnit.Type;
import org.monet.federation.accountoffice.utils.EmailService;
import org.monet.federation.accountoffice.utils.PersisterHelper;
import org.monet.federation.accountoffice.utils.Utils;
import org.monet.federation.setupservice.control.constants.Parameter;
import org.monet.federation.setupservice.core.constants.MessageCode;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class ActionRegisterPartner extends ActionSetupService {

    @Override
    public String execute() throws Exception {
        String name = (String) this.parameters.get(Parameter.NAME);
        String label = (String) this.parameters.get(Parameter.LABEL);
        String uri = (String) this.parameters.get(Parameter.URI);
        String businessUnitFederation = (String) this.parameters.get(Parameter.FEDERATION);
        String services = (String) this.parameters.get(Parameter.SERVICES);
        ServiceList serviceList = PersisterHelper.load(decode(services), ServiceList.class);
        String feeders = (String) this.parameters.get(Parameter.FEEDERS);
        FeederList feederList = PersisterHelper.load(decode(feeders), FeederList.class);
        Federation federation = PersisterHelper.load(decode(businessUnitFederation), Federation.class);
        BusinessUnit businessUnit = null;

        label = decode(label);
        uri = decode(uri);

        federation = this.registerFederation(federation);
        if (federation == null)
            return "Could not register partner";

        if (this.dataRepository.existsBusinessUnit(federation.getName(), name)) {
            businessUnit = this.dataRepository.loadBusinessUnit(federation.getName(), name);
            this.dataRepository.clearServices(businessUnit.getId());
            this.dataRepository.clearFeeders(businessUnit.getId());
        } else {
            businessUnit = new BusinessUnit();
            businessUnit.setName(name);
            businessUnit.setFederationId(federation.getId());
            businessUnit.setLabel(label);
            businessUnit.setType(Type.PARTNER);
            businessUnit.setUri(new URI(uri));
            businessUnit.setEnable(false);
            this.dataRepository.createBusinessUnit(businessUnit);

            try {
                this.sendBusinessUnitPartnerRequest(businessUnit);
            } catch (Exception exception) {
                this.dataRepository.removeBusinessUnit(businessUnit.getId());
                businessUnit = null;
                this.logger.error("Exception sending partner business unit request by email to " + configuration.getProperty(Configuration.SMTP_EMAIL_TO), exception);
            }
        }

        if (businessUnit == null)
            return "Could not register partner";

        for (Service service : serviceList.getAll()) {
            service.setBusinessUnitId(businessUnit.getId());

            if (businessUnit.isMember())
                service.setIsEnable(true);

            this.dataRepository.createService(service);
        }

        for (Feeder feeder : feederList.getAll()) {
            feeder.setBusinessUnitId(businessUnit.getId());

            if (businessUnit.isMember())
                feeder.setIsEnable(true);

            this.dataRepository.createFeeder(feeder);
        }

        return MessageCode.FEDERATION_OPERATION_OK;
    }

    private Federation registerFederation(Federation federation) throws NoSuchAlgorithmException, EmailException {

        if (this.dataRepository.existsFederation(federation.getName()))
            federation = this.dataRepository.loadFederation(federation.getName());
        else {
            this.dataRepository.createFederation(federation);
            try {
                this.sendFederationTrustedRequest(federation);
            } catch (Exception exception) {
                this.dataRepository.removeFederation(federation.getId());
                federation = null;
                this.logger.error("Exception sending trust federation request by email to " + this.configuration.getProperty(Configuration.SMTP_EMAIL_TO), exception);
            }
        }

        return federation;
    }

    private void sendFederationTrustedRequest(Federation federation) throws NoSuchAlgorithmException, EmailException {
        String validationCode = LibraryCodeGenerator.generateCode();
        Properties props = this.configuration.getLanguage(this.codeLanguage);

        FederationTrustRender render = (FederationTrustRender) this.templateFactory.getRender(TemplateFactory.Renders.FEDERATION_TRUST);
        render.init(this.codeLanguage);
        render.setParameter(FederationRender.Parameter.CODE, validationCode);
        render.setParameter(FederationRender.Parameter.FEDERATION, federation);
        render.setParameter(FederationRender.Parameter.BASE_URL, Utils.getBaseUrl(this.request));

        try {
            EmailService.sendMail(this.configuration, props.getProperty(TemplateComponent.FEDERATION_TRUST_REQUEST), render.getOutput());
        } catch (Exception exception) {
            this.logger.error("Could not send federation trusted request by email. Check mail configuration in federation");
        }

        this.dataRepository.createFederationTrustRequest(federation, validationCode);
    }

    private void sendBusinessUnitPartnerRequest(BusinessUnit businessUnit) throws NoSuchAlgorithmException, EmailException {
        String validationCode = LibraryCodeGenerator.generateCode();
        Properties props = this.configuration.getLanguage(this.codeLanguage);

        BusinessUnitPartnerRender render = (BusinessUnitPartnerRender) this.templateFactory.getRender(TemplateFactory.Renders.BUSINESS_UNIT_PARTNER);
        render.init(this.codeLanguage);
        render.setParameter(FederationRender.Parameter.CODE, validationCode);
        render.setParameter(FederationRender.Parameter.BUSINESS_UNIT, businessUnit);
        render.setParameter(FederationRender.Parameter.BASE_URL, Utils.getBaseUrl(this.request));

        try {
            EmailService.sendMail(this.configuration, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_REQUEST), render.getOutput());
        } catch (Exception exception) {
            this.logger.error("Could not send business unit partner request by email. Check mail configuration in federation");
        }

        this.dataRepository.createBusinessUnitPartnerRequest(businessUnit, validationCode);
    }
}
