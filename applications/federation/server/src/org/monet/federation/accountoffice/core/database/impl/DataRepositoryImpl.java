package org.monet.federation.accountoffice.core.database.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.exceptions.DatabaseException;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.database.DataRepositorySource;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.*;
import org.monet.federation.accountoffice.utils.DBHelper;
import org.monet.federation.accountoffice.utils.NamedParameterStatement;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.Device;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

@Singleton
public class DataRepositoryImpl implements DataRepository {
	private Logger logger;
	private DataSource dataSource;
	private Properties queries;
	private Configuration configuration;

	@Inject
	public DataRepositoryImpl(Logger logger, Configuration configuration, DataRepositorySource dataRepositorySource) {
		this.logger = logger;
		this.configuration = configuration;
		try {
			this.dataSource = dataRepositorySource.getDataSource();
			this.queries = dataRepositorySource.getQueries();

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	@Override
	public FederationList loadFederations() {
		this.logger.debug("loadFederations()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FederationList federationList = new FederationList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_FEDERATIONS));

			result = statement.executeQuery();
			while (result.next()) {
				Federation federation = new Federation();
				this.fillFederation(federation, result);
				federationList.add(federation);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return federationList;
	}

	@Override
	public FederationList loadTrustedFederations() {
		this.logger.debug("loadTrustedFederations()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FederationList federationList = new FederationList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_TRUSTED_FEDERATIONS));

			result = statement.executeQuery();
			while (result.next()) {
				Federation federation = new Federation();
				this.fillFederation(federation, result);
				federationList.add(federation);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return federationList;
	}

	@Override
	public Federation loadFederation(String name) {
		this.logger.debug("loadFederation(%s)", name);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		if (this.isSystemFederation(name))
			return this.loadSystemFederation();

		Federation federation = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_FEDERATION));
			statement.setString(DataRepository.PARAM_NAME, name);

			result = statement.executeQuery();
			if (!result.next())
				throw new DatabaseException(String.format("Federation '%s' not found.", name));

			federation = new Federation();
			this.fillFederation(federation, result);

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return federation;
	}

	@Override
	public Federation loadFederationById(String id) {
		this.logger.debug("loadFederationById(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		Federation federation = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_FEDERATION_BY_ID));
			statement.setString(DataRepository.PARAM_ID, id);

			result = statement.executeQuery();
			if (!result.next())
				throw new DatabaseException(String.format("Federation '%s' not found.", id));

			federation = new Federation();
			this.fillFederation(federation, result);

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return federation;
	}

	@Override
	public boolean existsFederation(String name) {
		this.logger.debug("existsFederation(%s)", name);

		if (this.isSystemFederation(name))
			return true;

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		boolean exists;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_FEDERATION));
			statement.setString(DataRepository.PARAM_NAME, name);

			result = statement.executeQuery();
			exists = result.next();
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return exists;
	}

	@Override
	public Federation createFederation(Federation federation) {
		logger.debug("createFederation(%s)", federation);

		if (this.isSystemFederation(federation.getName()))
			throw new DatabaseException("You cant create federation with that name " + federation.getName());

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_FEDERATION), Statement.RETURN_GENERATED_KEYS);
			statement.setString(DataRepository.PARAM_NAME, federation.getName());
			statement.setString(DataRepository.PARAM_LABEL, federation.getLabel());
			statement.setString(DataRepository.PARAM_URI, federation.getUri().toString());
			statement.setBoolean(DataRepository.PARAM_TRUSTED, federation.isTrusted());

			result = statement.executeUpdateAndGetGeneratedKeys();
			result.next();
			federation.setId(result.getString(1));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return federation;
	}

	@Override
	public void saveFederation(Federation federation) {
		logger.debug("saveFederation(%s)", federation.getName());

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SAVE_FEDERATION));
			statement.setString(DataRepository.PARAM_ID, federation.getId());
			statement.setString(DataRepository.PARAM_NAME, federation.getName());
			statement.setString(DataRepository.PARAM_URI, federation.getUri().toString());
			statement.setString(DataRepository.PARAM_LABEL, federation.getLabel());
			statement.setBoolean(DataRepository.PARAM_TRUSTED, federation.isTrusted());
			statement.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			try {
				connection.commit();
			} catch (Throwable ex) {
				logger.error(ex.getMessage(), ex);
			}
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void removeFederation(String id) {
		logger.debug("removeFederation(%s)", id);

		if (id == null)
			return;

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_FEDERATION_TRUST_REQUEST));
			statement.setString(DataRepository.PARAM_ID_FEDERATION, id);
			statement.executeUpdate();

			DBHelper.close(statement);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_FEDERATION));
			statement.setString(DataRepository.PARAM_ID, id);
			statement.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			try {
				connection.commit();
			} catch (Throwable ex) {
				logger.error(ex.getMessage(), ex);
			}
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void createFederationTrustRequest(Federation federation, String validationCode) {
		logger.debug("createFederationTrustRequest(%s)", federation.getId());

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_FEDERATION_TRUST_REQUEST));
			statement.setString(DataRepository.PARAM_ID_FEDERATION, federation.getId());
			statement.setString(DataRepository.PARAM_VALIDATION_CODE, validationCode);
			statement.setTimestamp(DataRepository.PARAM_CREATE_DATE, this.getDateAsTimestamp(new Date()));
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public boolean existsFederationTrustRequest(String federationId, String validationCode) {
		this.logger.debug("existsFederationTrustRequest(%s)", federationId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		boolean exists;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_FEDERATION_TRUST_REQUEST));
			statement.setString(DataRepository.PARAM_ID_FEDERATION, federationId);
			statement.setString(DataRepository.PARAM_VALIDATION_CODE, validationCode);

			result = statement.executeQuery();
			exists = result.next();
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return exists;
	}

	@Override
	public void removeFederationTrustRequest(String id) {
		logger.debug("removeFederationTrustRequest(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_FEDERATION_TRUST_REQUEST));
			statement.setString(DataRepository.PARAM_ID_FEDERATION, id);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public FederationUnitList loadMemberBusinessUnits() {
		this.logger.debug("loadBusinessUnits()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FederationUnitList federationUnitList = new FederationUnitList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_MEMBER_BUSINESS_UNITS));

			result = statement.executeQuery();
			while (result.next()) {
				FederationUnit federationUnit = new FederationUnit();
				this.fillBusinessUnit(federationUnit, result);
				federationUnitList.add(federationUnit);
			}

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return federationUnitList;
	}

	@Override
	public FederationUnitList loadMemberBusinessUnitsWithServicesOrFeeders() {
		this.logger.debug("loadMemberBusinessUnitsWithServicesOrFeeders()");

		FederationUnitList memberUnitList = this.loadMemberBusinessUnits();
		FederationUnitList result = new FederationUnitList();

		for (FederationUnit memberUnit : memberUnitList.getAll()) {

			memberUnit.setServiceList(this.loadServices(memberUnit.getId()));
			memberUnit.setFeederList(this.loadFeeders(memberUnit.getId()));

			if (memberUnit.isEnable())
				result.add(memberUnit);
		}

		return result;
	}

	@Override
	public FederationUnitList loadPartnerBusinessUnits() {
		this.logger.debug("loadPartnerBusinessUnits()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FederationUnitList partnerList = new FederationUnitList();
		ArrayList<FederationUnit> partners = new ArrayList<FederationUnit>();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_PARTNER_BUSINESS_UNITS));

			result = statement.executeQuery();
			while (result.next()) {
				FederationUnit partner = new FederationUnit();
				this.fillBusinessUnit(partner, result);
				partners.add(partner);
			}

			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);

			for (FederationUnit partner : partners) {
				partner.setServiceList(this.loadServices(partner.getId()));
				partner.setFeederList(this.loadFeeders(partner.getId()));

				if (partner.getServiceList().getAll().size() > 0 || partner.getFeederList().getAll().size() > 0)
					partnerList.add(partner);
			}

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return partnerList;
	}

	@Override
	public boolean existsPartnerBusinessUnit(String id) {
		this.logger.debug("existsPartnerBusinessUnit(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FederationUnit partner = new FederationUnit();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_BUSINESS_UNIT_BY_ID));
			statement.setString(DataRepository.PARAM_ID, id);

			result = statement.executeQuery();
			return result.next();
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

    @Override
    public FederationUnit loadPartnerBusinessUnit(String id) {
        this.logger.debug("loadPartnerBusinessUnit(%s)", id);

        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet result = null;
        FederationUnit partner = new FederationUnit();

        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_BUSINESS_UNIT_BY_ID));
            statement.setString(DataRepository.PARAM_ID, id);

            result = statement.executeQuery();
            if (!result.next())
                throw new DatabaseException(String.format("FederationUnit '%s' not found.", id));

            this.fillBusinessUnit(partner, result);

            DBHelper.close(result);
            DBHelper.close(statement);

            partner.setServiceList(this.loadServices(partner.getId()));
            partner.setFeederList(this.loadFeeders(partner.getId()));

        } catch (Throwable e) {
            this.logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage());
        } finally {
            DBHelper.close(result);
            DBHelper.close(statement);
            DBHelper.close(connection);
        }

        return partner;
    }

    @Override
	public FederationUnit locatePartnerBusinessUnit(String name) {
		this.logger.debug("locatePartnerBusinessUnit(%s)", name);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FederationUnit partner = new FederationUnit();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_PARTNER_BUSINESS_UNIT));
			statement.setString(DataRepository.PARAM_NAME, name);

			result = statement.executeQuery();
			if (!result.next())
				throw new DatabaseException(String.format("FederationUnit '%s' not found.", name));

			this.fillBusinessUnit(partner, result);

			DBHelper.close(result);
			DBHelper.close(statement);

			partner.setServiceList(this.loadServices(partner.getId()));
			partner.setFeederList(this.loadFeeders(partner.getId()));

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return partner;
	}

	@Override
	public BusinessUnitList loadBusinessUnits(String federationName) {
		this.logger.debug("loadBusinessUnits()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		BusinessUnitList businessUnitList = new BusinessUnitList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_BUSINESS_UNITS));
			statement.setString(DataRepository.PARAM_FEDERATION, federationName);

			result = statement.executeQuery();
			while (result.next()) {
				BusinessUnit businessUnit = new BusinessUnit();
				this.fillBusinessUnit(businessUnit, result);
				businessUnitList.add(businessUnit);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return businessUnitList;
	}

	@Override
	public BusinessUnitList loadBusinessUnitsOfType(String type) {
		this.logger.debug(String.format("loadBusinessUnitsOfType(%s)", type));

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		BusinessUnitList businessUnitList = new BusinessUnitList();
		String query = null;

		if (type.equals(FederationUnit.Type.MEMBER))
			query = DataRepository.LOAD_MEMBER_BUSINESS_UNITS;
		else if (type.equals(FederationUnit.Type.PARTNER))
			query = DataRepository.LOAD_PARTNER_BUSINESS_UNITS;

		if (query == null)
			return null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(query));

			result = statement.executeQuery();
			while (result.next()) {
				BusinessUnit businessUnit = new BusinessUnit();
				this.fillBusinessUnit(businessUnit, result);
				businessUnitList.add(businessUnit);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage() + ". Query: " + (String) this.queries.get(query), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return businessUnitList;
	}

	@Override
	public BusinessUnit loadBusinessUnit(String name) {
		this.logger.debug("loadBusinessUnit(%s)", name);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		BusinessUnit businessUnit = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_MEMBER_BUSINESS_UNIT));
			statement.setString(DataRepository.PARAM_NAME, name);

			result = statement.executeQuery();
			if (!result.next())
				throw new DatabaseException(String.format("Business unit '%s' not found.", name));

			businessUnit = new BusinessUnit();
			this.fillBusinessUnit(businessUnit, result);

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);

		}

		return businessUnit;
	}

	@Override
	public BusinessUnit loadBusinessUnit(String federationName, String name) {
		this.logger.debug("loadBusinessUnit(%s)", name);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		BusinessUnit businessUnit = null;
		String query = this.isSystemFederation(federationName) ? DataRepository.LOAD_MEMBER_BUSINESS_UNIT : DataRepository.LOAD_BUSINESS_UNIT;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(query));
			if (query.equals(DataRepository.LOAD_BUSINESS_UNIT))
				statement.setString(DataRepository.PARAM_FEDERATION, federationName);
			statement.setString(DataRepository.PARAM_NAME, name);

			result = statement.executeQuery();
			if (!result.next())
				throw new DatabaseException(String.format("Business unit '%s' not found.", name));

			businessUnit = new BusinessUnit();
			this.fillBusinessUnit(businessUnit, result);

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return businessUnit;
	}

	@Override
	public BusinessUnit loadBusinessUnitById(String id) {
		this.logger.debug("loadBusinessUnitById(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		BusinessUnit businessUnit = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_BUSINESS_UNIT_BY_ID));
			statement.setString(DataRepository.PARAM_ID, id);

			result = statement.executeQuery();
			if (!result.next())
				throw new DatabaseException(String.format("Business unit '%s' not found.", id));

			businessUnit = new BusinessUnit();
			this.fillBusinessUnit(businessUnit, result);

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return businessUnit;
	}

	@Override
	public boolean existsBusinessUnit(String federationName, String name) {
		this.logger.debug("existsBusinessUnit(%s)", name);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		boolean exists;
		String query = this.isSystemFederation(federationName) ? DataRepository.EXISTS_MEMBER_BUSINESS_UNIT : DataRepository.EXISTS_BUSINESS_UNIT;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(query));
			if (query.equals(DataRepository.EXISTS_BUSINESS_UNIT))
				statement.setString(DataRepository.PARAM_FEDERATION, federationName);
			statement.setString(DataRepository.PARAM_NAME, name);

			result = statement.executeQuery();
			exists = result.next();
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return exists;
	}

	@Override
	public BusinessUnit createBusinessUnit(BusinessUnit businessUnit) {
		logger.debug("createBusinessUnit(%s)", businessUnit);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_BUSINESS_UNIT), Statement.RETURN_GENERATED_KEYS);
			statement.setString(DataRepository.PARAM_ID_FEDERATION, businessUnit.getFederationId());
			statement.setString(DataRepository.PARAM_NAME, businessUnit.getName());
			statement.setString(DataRepository.PARAM_LABEL, businessUnit.getLabel());
			statement.setString(DataRepository.PARAM_TYPE, businessUnit.getType().toString());
			statement.setString(DataRepository.PARAM_URI, businessUnit.getUri().toString());
			statement.setString(DataRepository.PARAM_SECRET, businessUnit.getSecret());
			statement.setBoolean(DataRepository.PARAM_ENABLE, businessUnit.isEnable());
			statement.setBoolean(DataRepository.PARAM_VISIBLE, businessUnit.isVisible());

			result = statement.executeUpdateAndGetGeneratedKeys();
			result.next();
			businessUnit.setId(result.getString(1));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return businessUnit;
	}

	@Override
	public void saveBusinessUnit(BusinessUnit businessUnit) {
		logger.debug("saveBusinessUnit(%s)", businessUnit.getName());

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SAVE_BUSINESS_UNIT));
			statement.setString(DataRepository.PARAM_ID, businessUnit.getId());
			statement.setString(DataRepository.PARAM_LABEL, businessUnit.getLabel());
			statement.setString(DataRepository.PARAM_TYPE, businessUnit.getType().toString());
			statement.setString(DataRepository.PARAM_URI, businessUnit.getUri().toString());
			statement.setString(DataRepository.PARAM_SECRET, businessUnit.getSecret());
			statement.setBoolean(DataRepository.PARAM_ENABLE, businessUnit.isEnable());
			statement.setBoolean(DataRepository.PARAM_VISIBLE, businessUnit.isVisible());
			statement.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			try {
				connection.commit();
			} catch (Throwable ex) {
				logger.error(ex.getMessage(), ex);
			}
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void removeBusinessUnit(String id) {
		logger.debug("removeBusinessUnit(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_BUSINESS_UNIT_PARTNER_REQUEST));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, id);
			statement.executeUpdate();

			DBHelper.close(statement);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CLEAR_BUSINESS_UNIT_SERVICES));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, id);
			statement.executeUpdate();

			DBHelper.close(statement);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CLEAR_BUSINESS_UNIT_FEEDERS));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, id);
			statement.executeUpdate();

			DBHelper.close(statement);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_BUSINESS_UNIT));
			statement.setString(DataRepository.PARAM_ID, id);
			statement.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			try {
				connection.commit();
			} catch (Throwable ex) {
				logger.error(ex.getMessage(), ex);
			}
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void createBusinessUnitPartnerRequest(BusinessUnit businessUnit, String validationCode) {
		logger.debug("createBusinessUnitPartnerRequest(%s)", businessUnit.getId());

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_BUSINESS_UNIT_PARTNER_REQUEST));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, businessUnit.getId());
			statement.setString(DataRepository.PARAM_VALIDATION_CODE, validationCode);
			statement.setTimestamp(DataRepository.PARAM_CREATE_DATE, this.getDateAsTimestamp(new Date()));
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public boolean existsBusinessUnitPartnerRequest(String businessUnitId, String validationCode) {
		this.logger.debug("existsBusinessUnitPartnerRequest(%s)", businessUnitId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		boolean exists;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_BUSINESS_UNIT_PARTNER_REQUEST));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, businessUnitId);
			statement.setString(DataRepository.PARAM_VALIDATION_CODE, validationCode);

			result = statement.executeQuery();
			exists = result.next();
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return exists;
	}

	@Override
	public void removeBusinessUnitPartnerRequest(String id) {
		logger.debug("removeBusinessUnitPartnerRequest(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_BUSINESS_UNIT_PARTNER_REQUEST));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, id);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public ServiceList loadServices(String businessUnitId) {
		this.logger.debug("loadServices(%s)", businessUnitId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		ServiceList serviceList = new ServiceList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_BUSINESS_UNIT_SERVICES));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, businessUnitId);

			result = statement.executeQuery();
			while (result.next()) {
				Service service = new Service();
				this.fillService(service, result);
				serviceList.add(service);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return serviceList;
	}

	@Override
	public Service createService(Service service) {
		logger.debug("addService(%s)", service);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_BUSINESS_UNIT_SERVICE));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, service.getBusinessUnitId());
			statement.setString(DataRepository.PARAM_NAME, service.getName());
			statement.setString(DataRepository.PARAM_LABEL, service.getLabel());
			statement.setString(DataRepository.PARAM_ONTOLOGY, service.getOntology());
			statement.setBoolean(DataRepository.PARAM_ENABLE, service.isEnable());
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return service;
	}

	@Override
	public void clearServices(String businessUnitId) {
		logger.debug("clearServices(%s)", businessUnitId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CLEAR_BUSINESS_UNIT_SERVICES));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, businessUnitId);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public FeederList loadFeeders(String businessUnitId) {
		this.logger.debug("loadFeeders(%s)", businessUnitId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		FeederList feederList = new FeederList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_BUSINESS_UNIT_FEEDERS));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, businessUnitId);

			result = statement.executeQuery();
			while (result.next()) {
				Feeder feeder = new Feeder();
				this.fillFeeder(feeder, result);
				feederList.add(feeder);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return feederList;
	}

	@Override
	public Feeder createFeeder(Feeder feeder) {
		logger.debug("addFeeder(%s)", feeder);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_BUSINESS_UNIT_FEEDER));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, feeder.getBusinessUnitId());
			statement.setString(DataRepository.PARAM_NAME, feeder.getName());
			statement.setString(DataRepository.PARAM_LABEL, feeder.getLabel());
			statement.setString(DataRepository.PARAM_ONTOLOGY, feeder.getOntology());
			statement.setBoolean(DataRepository.PARAM_ENABLE, feeder.isEnable());
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return feeder;
	}

	@Override
	public void clearFeeders(String businessUnitId) {
		logger.debug("clearFeeders(%s)", businessUnitId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CLEAR_BUSINESS_UNIT_FEEDERS));
			statement.setString(DataRepository.PARAM_ID_BUSINESS_UNIT, businessUnitId);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public String generateIdForUser(User user) {
		this.logger.debug("generateIdForUser(%s)", user.getUsername());

		if (!this.existsUser(user.getUsername()))
			this.createUser(user);

		return this.loadUserFromUsername(user.getUsername()).getId();
	}

	@Override
	public UserList searchUsers(String condition, int start, int limit) {
		this.logger.debug("searchUsers(%s,%d,%d)", condition, start, limit);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		UserList userList = new UserList();

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SEARCH_USERS_COUNT));
			statement.setString(DataRepository.PARAM_CONDITION, condition);
			statement.setString(DataRepository.PARAM_CONDITION_LIKE, "%" + condition + "%");
			result = statement.executeQuery();

			if (!result.next()) {
				this.logger.error("DataRepository. Could not load users counter.");
				throw new DatabaseException("DataRepository. Could not load users counter.");
			}

			int totalCount = result.getInt("counter");
			userList.setTotalCount(totalCount != -1 ? totalCount : 0);
			DBHelper.close(result);
			DBHelper.close(statement);

			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SEARCH_USERS));
			statement.setString(DataRepository.PARAM_CONDITION, condition);
			statement.setString(DataRepository.PARAM_CONDITION_LIKE, "%" + condition + "%");
			statement.setInt(DataRepository.PARAM_START, start != -1 ? start : 0);
			statement.setInt(DataRepository.PARAM_LIMIT, limit != -1 ? limit : userList.getTotalCount());

			result = statement.executeQuery();
			while (result.next()) {
				User user = new User();
				this.fillUser(user, result);
				userList.add(user);
			}

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return userList;
	}

	@Override
	public boolean existsUser(String idOrUsername) {
		this.logger.debug("existsUser(%s)", idOrUsername);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_USER));
			statement.setString(DataRepository.PARAM_ID, idOrUsername);

			result = statement.executeQuery();
			return result.next();

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public User loadUser(String id) {
		this.logger.debug("loadUser(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		User user = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_USER));
			statement.setString(DataRepository.PARAM_ID, id);

			result = statement.executeQuery();

			if (!result.next()) {
				this.logger.error("DataRepository. Could not load user " + id + ".");
				throw new DatabaseException("DataRepository. Could not load user " + id + ".");
			}

			user = new User();
			this.fillUser(user, result);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return user;
	}

	@Override
	public User loadUserFromUsername(String username) {
		this.logger.debug("loadUserFromUsername(%s)", username);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		User user = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_USER_FROM_USERNAME));
			statement.setString(DataRepository.PARAM_USERNAME, username);

			result = statement.executeQuery();

			if (!result.next()) {
				this.logger.error("DataRepository. Could not load user " + username + ".");
				throw new DatabaseException("DataRepository. Could not load user " + username + ".");
			}

			user = new User();
			this.fillUser(user, result);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return user;
	}

	@Override
	public User loadUserFromEmail(String email) {
		logger.debug("loadUserFromEmail(%s)", email);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		User user = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_USER_FROM_EMAIL));
			statement.setString(DataRepository.PARAM_EMAIL, email);

			result = statement.executeQuery();

			if (!result.next()) {
				this.logger.error("DataRepository. Could not load user by email " + email + ".");
				throw new DatabaseException("DataRepository. Could not load user by email " + email + ".");
			}

			user = new User();
			this.fillUser(user, result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return user;
	}

	@Override
	public User createUser(User user) {
		logger.debug("createUser(%s)", user);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			String email = user.getEmail();

			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_USER), Statement.RETURN_GENERATED_KEYS);
			statement.setString(DataRepository.PARAM_USERNAME, user.getUsername());
			statement.setString(DataRepository.PARAM_FULLNAME, user.getFullname());
			statement.setString(DataRepository.PARAM_EMAIL, email != null && !email.isEmpty() ? email : null);
			statement.setString(DataRepository.PARAM_LANG, user.getLang());
			statement.setBoolean(DataRepository.PARAM_IS_HUMAN, user.isHuman());
			statement.setString(DataRepository.PARAM_MODE, (user.getMode() != null) ? user.getMode().toString() : null);

			result = statement.executeUpdateAndGetGeneratedKeys();
			result.next();
			user.setId(result.getString(1));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return user;
	}

	@Override
	public void saveUser(User user) {
		this.logger.debug("saveUser(%s)", user);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			String email = user.getEmail();

			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SAVE_USER));
			statement.setString(DataRepository.PARAM_ID, user.getId());
			statement.setString(DataRepository.PARAM_USERNAME, user.getUsername());
			statement.setString(DataRepository.PARAM_FULLNAME, user.getFullname());
			statement.setString(DataRepository.PARAM_EMAIL, email != null && !email.isEmpty() ? email : null);
			statement.setString(DataRepository.PARAM_LANG, user.getLang());
			statement.setBoolean(DataRepository.PARAM_IS_HUMAN, user.isHuman());
			statement.setString(DataRepository.PARAM_MODE, (user.getMode() != null) ? user.getMode().toString() : null);

			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void ensureUserTotpSchema() {
		this.logger.debug("ensureUserTotpSchema()");

		Connection connection = null;
		Statement statement = null;

		try {
			connection = this.dataSource.getConnection();
			if (this.existsTable(connection, "fs$user_totp") || this.existsTable(connection, "FS$USER_TOTP")) {
				this.logger.debug("TOTP table already exists");
				return;
			}

			String databaseType = this.configuration.getProperty(Configuration.JDBC_DATABASE, "").toLowerCase();
			statement = connection.createStatement();

			if ("mysql".equals(databaseType)) {
				statement.executeUpdate("CREATE TABLE `fs$user_totp` (" +
						"`id` BIGINT(20) NOT NULL AUTO_INCREMENT," +
						"`id_user` VARCHAR(255) NOT NULL," +
						"`secret` VARCHAR(255) NOT NULL," +
						"`enable` INT(1) NOT NULL DEFAULT 0," +
						"`last_used_counter` BIGINT(20) DEFAULT NULL," +
						"PRIMARY KEY (`id`)," +
						"UNIQUE KEY `FS_USER_TOTP_ID_USER` (`id_user`)" +
						") DEFAULT CHARSET=utf8");
			} else if ("oracle".equals(databaseType)) {
				statement.executeUpdate("CREATE TABLE FS$USER_TOTP (" +
						"id NUMBER(10,0) NOT NULL ENABLE," +
						"id_user VARCHAR2(255 CHAR) NOT NULL," +
						"secret VARCHAR2(255 CHAR) NOT NULL," +
						"enable NUMBER(10,0) DEFAULT 0 NOT NULL," +
						"last_used_counter NUMBER(20,0)," +
						"CONSTRAINT FS$USER_TOTP_PK PRIMARY KEY (id) ENABLE," +
						"CONSTRAINT FS$USER_TOTP_ID_USER UNIQUE (id_user) ENABLE" +
						")");
				statement.executeUpdate("CREATE SEQUENCE FS$USER_TOTP_ID_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE");
				statement.executeUpdate("CREATE OR REPLACE TRIGGER FS$USER_TOTP_ID_TRG BEFORE INSERT OR UPDATE ON FS$USER_TOTP " +
						"FOR EACH ROW " +
						"DECLARE " +
						"v_newVal NUMBER(12) := 0; " +
						"v_incval NUMBER(12) := 0; " +
						"BEGIN " +
						"IF INSERTING AND :new.ID IS NULL THEN " +
						"SELECT FS$USER_TOTP_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL; " +
						"IF v_newVal = 1 THEN " +
						"SELECT NVL(max(ID),0) INTO v_newVal FROM FS$USER_TOTP; " +
						"v_newVal := v_newVal + 1; " +
						"LOOP " +
						"EXIT WHEN v_incval>=v_newVal; " +
						"SELECT FS$USER_TOTP_ID_SEQ.nextval INTO v_incval FROM dual; " +
						"END LOOP; " +
						"END IF; " +
						":new.ID := v_newVal; " +
						"END IF; " +
						"END");
				statement.executeUpdate("ALTER TRIGGER FS$USER_TOTP_ID_TRG ENABLE");
			} else {
				this.logger.info("Skipping TOTP schema auto-creation for unsupported database type: %s", databaseType);
				return;
			}

			this.logger.info("Created missing TOTP table fs$user_totp");
		} catch (Throwable e) {
			this.logger.error("Could not ensure TOTP schema", e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					this.logger.error(e.getMessage(), e);
				}
			}
			DBHelper.close(connection);
		}
	}

	@Override
	public UserTotpInfo loadUserTotpInfo(String userId) {
		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_USER_TOTP));
			statement.setString(DataRepository.PARAM_ID_USER, userId);
			result = statement.executeQuery();

			if (!result.next())
				return null;

			UserTotpInfo userTotpInfo = new UserTotpInfo();
			userTotpInfo.setUserId(result.getString(DataRepository.PARAM_ID_USER));
			userTotpInfo.setSecret(result.getString(DataRepository.PARAM_SECRET));
			userTotpInfo.setEnabled(result.getBoolean(DataRepository.PARAM_ENABLE));
			long lastUsedCounter = result.getLong(DataRepository.PARAM_LAST_USED_COUNTER);
			userTotpInfo.setLastUsedCounter(result.wasNull() ? null : lastUsedCounter);
			return userTotpInfo;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public boolean existsUserTotpInfo(String userId) {
		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_USER_TOTP));
			statement.setString(DataRepository.PARAM_ID_USER, userId);
			result = statement.executeQuery();
			return result.next();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void createUserTotpInfo(UserTotpInfo userTotpInfo) {
		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.CREATE_USER_TOTP));
			statement.setString(DataRepository.PARAM_ID_USER, userTotpInfo.getUserId());
			statement.setString(DataRepository.PARAM_SECRET, userTotpInfo.getSecret());
			statement.setBoolean(DataRepository.PARAM_ENABLE, userTotpInfo.isEnabled());
			if (userTotpInfo.getLastUsedCounter() != null)
				statement.setLong(DataRepository.PARAM_LAST_USED_COUNTER, userTotpInfo.getLastUsedCounter());
			else
				statement.setString(DataRepository.PARAM_LAST_USED_COUNTER, null);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void saveUserTotpInfo(UserTotpInfo userTotpInfo) {
		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SAVE_USER_TOTP));
			statement.setString(DataRepository.PARAM_ID_USER, userTotpInfo.getUserId());
			statement.setString(DataRepository.PARAM_SECRET, userTotpInfo.getSecret());
			statement.setBoolean(DataRepository.PARAM_ENABLE, userTotpInfo.isEnabled());
			if (userTotpInfo.getLastUsedCounter() != null)
				statement.setLong(DataRepository.PARAM_LAST_USED_COUNTER, userTotpInfo.getLastUsedCounter());
			else
				statement.setString(DataRepository.PARAM_LAST_USED_COUNTER, null);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void removeUserTotpInfo(String userId) {
		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REMOVE_USER_TOTP));
			statement.setString(DataRepository.PARAM_ID_USER, userId);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public boolean isIpBanned(String remoteAddr) {
		this.logger.debug("isIpSuspend(%s)?", remoteAddr);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SELECT_BANNED_IP));
			statement.setString(DataRepository.PARAM_IP, remoteAddr);
			result = statement.executeQuery();
			if (result.next()) {
				int attempts = result.getInt(DataRepository.PARAM_ATTEMPTS);
				long lastAttemp = result.getLong(DataRepository.PARAM_LAST_ATTEMPT);

				long diffTime = System.currentTimeMillis() - lastAttemp;

				int suspendTime = this.configuration.getSuspendTime();
				int removeSuspendTime = this.configuration.getRemoveSuspendTime();

				if (diffTime > removeSuspendTime * 1000) {
					removeIpBan(remoteAddr);
					return false;
				} else if (attempts > 6 && diffTime < suspendTime * 1000)
					return true;
			} else
				return false;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
		return false;
	}

	@Override
	public void addIpWrongAccess(String remoteAddr) {
		logger.debug("addIpWrongAccess(%s)", remoteAddr);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SELECT_BANNED_IP));
			statement.setString(DataRepository.PARAM_IP, remoteAddr);
			result = statement.executeQuery();

			if (result.next()) {
				int attempts = result.getInt(DataRepository.PARAM_ATTEMPTS);
				attempts++;
				statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.UPDATE_BANNED_IP));
				statement.setString(DataRepository.PARAM_IP, remoteAddr);
				statement.setInt(DataRepository.PARAM_ATTEMPTS, attempts);
				statement.setLong(DataRepository.PARAM_LAST_ATTEMPT, System.currentTimeMillis());
				statement.executeUpdate();
			} else {
				statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.INSERT_BANNED_IP));
				statement.setString(DataRepository.PARAM_IP, remoteAddr);
				statement.setInt(DataRepository.PARAM_ATTEMPTS, 0);
				statement.setLong(DataRepository.PARAM_LAST_ATTEMPT, System.currentTimeMillis());
				statement.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void removeIpBan(String remoteAddr) {
		logger.debug("removeIpBan(%s)", remoteAddr);

		Connection connection = null;
		NamedParameterStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.DELETE_BANNED_IP));
			statement.setString(DataRepository.PARAM_IP, remoteAddr);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public HashMap<String, Session> loadSessions() {
		logger.debug("loadSessions()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		HashMap<String, Session> sessionsMap = new HashMap<String, Session>();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_SESSIONS));

			result = statement.executeQuery();
			while (result.next()) {
				Session session = new Session();
				this.fillSession(session, result);
				sessionsMap.put(session.getToken(), session);
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return sessionsMap;
	}

	@Override
	public int loadSessionsCount() {
		logger.debug("loadSessionsCount()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		int count = 0;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_SESSIONS_COUNT));

			result = statement.executeQuery();
			while (result.next())
				count++;
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return count;
	}

	@Override
	public Session loadSession(String token) {
		logger.debug("loadSession(%s)", token);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		Session session = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_SESSION));
			statement.setString(DataRepository.PARAM_TOKEN, token);

			result = statement.executeQuery();
			if (!result.next())
				return session;

			session = new Session();
			this.fillSession(session, result);

		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return session;
	}

	@Override
	public Session[] loadSessionsFromUsername(String username) {
		logger.debug("loadSessionsFromUsername(%s)", username);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		ArrayList<Session> sessions = new ArrayList<Session>();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_SESSION_FROM_USERNAME));
			statement.setString(DataRepository.PARAM_USERNAME, username);

			result = statement.executeQuery();
			while (result.next()) {
				Session session = new Session();
				this.fillSession(session, result);
				sessions.add(session);
			}


		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage());
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return sessions.toArray(new Session[0]);
	}

	@Override
	public void registerSession(Session session) {
		String token = session.getToken();
		String username = session.getUsername();
		String verifier = session.getVerifier();
		String lang = session.getLang();
		boolean isMobile = session.isMobile();
		boolean rememberMe = session.isRememberMe();
		String space = session.getSpace();
		String node = session.getNode();

		logger.debug("registerSession(%s,%s,%s,%s,%b,%b)", token, username, verifier, lang, isMobile, rememberMe, space, node);

		Connection connection = null;
		NamedParameterStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REGISTER_SESSION));
			statement.setString(DataRepository.PARAM_TOKEN, token);
			statement.setString(DataRepository.PARAM_USERNAME, username);
			statement.setBoolean(DataRepository.PARAM_REMEMBER_ME, rememberMe);
			statement.setString(DataRepository.PARAM_VERIFIER, verifier);
			statement.setString(DataRepository.PARAM_LANG, lang);
			statement.setBoolean(DataRepository.PARAM_IS_MOBILE, isMobile);
			statement.setTimestamp(DataRepository.PARAM_LAST_USE, this.getDateAsTimestamp(new Date()));
			statement.setString(DataRepository.PARAM_SPACE, space);
			statement.setString(DataRepository.PARAM_NODE, node);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void saveSession(Session session) {
		String token = session.getToken();
		String username = session.getUsername();
		String lang = session.getLang();
		boolean isMobile = session.isMobile();
		boolean rememberMe = session.isRememberMe();
		Date lastUse = session.getLastUse();
		String space = session.getSpace();
		String node = session.getNode();

		logger.debug("saveSession(%s,%s)", token, username);

		Connection connection = null;
		NamedParameterStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.SAVE_SESSION));
			statement.setString(DataRepository.PARAM_TOKEN, token);
			statement.setString(DataRepository.PARAM_LANG, lang);
			statement.setBoolean(DataRepository.PARAM_IS_MOBILE, isMobile);
			statement.setBoolean(DataRepository.PARAM_REMEMBER_ME, rememberMe);
			statement.setTimestamp(DataRepository.PARAM_LAST_USE, this.getDateAsTimestamp(lastUse));
			statement.setString(DataRepository.PARAM_SPACE, space);
			statement.setString(DataRepository.PARAM_NODE, node);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void unregisterSession(String token) {
		logger.debug("unregisterSession(%s)", token);

		Connection connection = null;
		NamedParameterStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.UNREGISTER_SESSION));
			statement.setString(DataRepository.PARAM_TOKEN, token);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void unregisterSessions(Date to) {
		logger.debug("unregisterSession(%s)", to.getTime());

		Connection connection = null;
		NamedParameterStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.UNREGISTER_SESSIONS));
			statement.setTimestamp(DataRepository.PARAM_LAST_USE, this.getDateAsTimestamp(to));
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public DeviceList loadMobileDevices() {
		this.logger.debug("loadMobileDevices()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		DeviceList deviceList = new DeviceList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_MOBILE_DEVICES));

			result = statement.executeQuery();

			while (result.next()) {
				Device device = new Device();
				this.fillDevice(device, result);
				deviceList.add(device);
			}

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return deviceList;
	}

	@Override
	public DeviceList loadMobileDevices(String userId) {
		this.logger.debug("loadMobileDevices(%s)", userId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;
		DeviceList deviceList = new DeviceList();

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.LOAD_USER_MOBILE_DEVICES));
			statement.setString(DataRepository.PARAM_ID_USER, userId);

			result = statement.executeQuery();

			while (result.next()) {
				Device device = new Device();
				this.fillDevice(device, result);
				deviceList.add(device);
			}

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}

		return deviceList;
	}

	@Override
	public boolean existsMobileDevice(String userId, String deviceId) {
		this.logger.debug("existsMobileDevice(%s,%s)", userId, deviceId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet result = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.EXISTS_MOBILE_DEVICE));
			statement.setString(DataRepository.PARAM_ID, deviceId);
			statement.setString(DataRepository.PARAM_ID_USER, userId);
			result = statement.executeQuery();

			return result.next();

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(result);
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void registerMobileDevice(String userId, String deviceId) {
		this.logger.debug("registerMobileDevice(%s,%s)", userId, deviceId);

		if (this.existsMobileDevice(userId, deviceId))
			return;

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.REGISTER_MOBILE_DEVICE));
			statement.setString(DataRepository.PARAM_ID, deviceId);
			statement.setString(DataRepository.PARAM_ID_USER, userId);
			statement.executeUpdate();
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	@Override
	public void unRegisterMobileDevice(String userId, String deviceId) {
		this.logger.debug("unRegisterDevice(%s,%s)", userId, deviceId);

		if (!this.existsMobileDevice(userId, deviceId))
			return;

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, (String) this.queries.get(DataRepository.UNREGISTER_MOBILE_DEVICE));
			statement.setString(DataRepository.PARAM_ID, deviceId);
			statement.setString(DataRepository.PARAM_ID_USER, userId);
			statement.executeUpdate();
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			DBHelper.close(statement);
			DBHelper.close(connection);
		}
	}

	private boolean isSystemFederation(String name) {
		return this.configuration.getName().equals(name);
	}

	private Federation loadSystemFederation() {
		Federation federation = new Federation();

		federation.setId(null);
		federation.setName(this.configuration.getName());
		federation.setLabel(this.configuration.getLabel());
		federation.setTrusted(true);

		return federation;
	}

	private void fillFederation(Federation federation, ResultSet result) throws SQLException, URISyntaxException {
		federation.setId(result.getString(DataRepository.PARAM_ID));
		federation.setName(result.getString(DataRepository.PARAM_NAME));
		federation.setLabel(result.getString(DataRepository.PARAM_LABEL));
		federation.setUri(new URI(result.getString(DataRepository.PARAM_URI)));
		federation.setTrusted(result.getBoolean(DataRepository.PARAM_TRUSTED));
	}

	private void fillBusinessUnit(BusinessUnit businessUnit, ResultSet result) throws SQLException, URISyntaxException {
		businessUnit.setId(result.getString(DataRepository.PARAM_ID));
		businessUnit.setName(result.getString(DataRepository.PARAM_NAME));
		businessUnit.setLabel(result.getString(DataRepository.PARAM_LABEL));
		businessUnit.setFederationId(result.getString(DataRepository.PARAM_ID_FEDERATION));
		businessUnit.setType(result.getString(DataRepository.PARAM_TYPE));
		businessUnit.setUri(new URI(result.getString(DataRepository.PARAM_URI)));
		businessUnit.setEnable(result.getBoolean(DataRepository.PARAM_ENABLE));
		businessUnit.setVisible(result.getBoolean(DataRepository.PARAM_VISIBLE));

		if (!(businessUnit instanceof FederationUnit))
			businessUnit.setSecret(result.getString(DataRepository.PARAM_SECRET));
	}

	private void fillService(Service service, ResultSet result) throws SQLException {
		service.setBusinessUnitId(result.getString(DataRepository.PARAM_ID_BUSINESS_UNIT));
		service.setName(result.getString(DataRepository.PARAM_NAME));
		service.setLabel(result.getString(DataRepository.PARAM_LABEL));
		service.setOntology(result.getString(DataRepository.PARAM_ONTOLOGY));
		service.setIsEnable(result.getBoolean(DataRepository.PARAM_ENABLE));
	}

	private void fillFeeder(Feeder feeder, ResultSet result) throws SQLException {
		feeder.setBusinessUnitId(result.getString(DataRepository.PARAM_ID_BUSINESS_UNIT));
		feeder.setName(result.getString(DataRepository.PARAM_NAME));
		feeder.setLabel(result.getString(DataRepository.PARAM_LABEL));
		feeder.setOntology(result.getString(DataRepository.PARAM_ONTOLOGY));
		feeder.setIsEnable(result.getBoolean(DataRepository.PARAM_ENABLE));
	}

	private void fillUser(User user, ResultSet result) throws SQLException {
		user.setId(result.getString(DataRepository.PARAM_ID));
		user.setUsername(result.getString(DataRepository.PARAM_USERNAME));
		user.setFullname(result.getString(DataRepository.PARAM_FULLNAME));
		user.setEmail(result.getString(DataRepository.PARAM_EMAIL));
		user.setLang(result.getString(DataRepository.PARAM_LANG));
		user.setHuman(result.getBoolean(DataRepository.PARAM_IS_HUMAN));
		user.setMode(LoginMode.from(result.getString(DataRepository.PARAM_MODE)));
	}

	private void fillSession(Session session, ResultSet result) throws SQLException {
		session.setToken(result.getString(DataRepository.PARAM_TOKEN));
		session.setUsername(result.getString(DataRepository.PARAM_USERNAME));
		session.setVerifier(result.getString(DataRepository.PARAM_VERIFIER));
		session.setLang(result.getString(DataRepository.PARAM_LANG));
		session.setMobile(result.getBoolean(DataRepository.PARAM_IS_MOBILE));
		session.setRememberMe(result.getBoolean(DataRepository.PARAM_REMEMBER_ME));
		session.setLastUse(result.getTimestamp(DataRepository.PARAM_LAST_USE));
		session.setSpace(result.getString(DataRepository.PARAM_SPACE));
		session.setNode(result.getString(DataRepository.PARAM_NODE));
	}

	private void fillDevice(Device device, ResultSet result) throws SQLException {
		device.setId(result.getString(DataRepository.PARAM_ID));
		device.setUserId(result.getString(DataRepository.PARAM_ID_USER));
	}

	private boolean existsTable(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet result = null;
		try {
			result = metaData.getTables(connection.getCatalog(), null, tableName, null);
			if (result.next())
				return true;
		} finally {
			DBHelper.close(result);
		}

		try {
			result = metaData.getTables(connection.getCatalog(), null, tableName.toUpperCase(), null);
			if (result.next())
				return true;
		} finally {
			DBHelper.close(result);
		}

		try {
			result = metaData.getTables(connection.getCatalog(), null, tableName.toLowerCase(), null);
			return result.next();
		} finally {
			DBHelper.close(result);
		}
	}

	private Timestamp getDateAsTimestamp(Date date) {
		return date != null ? new Timestamp(date.getTime()) : null;
	}

}
