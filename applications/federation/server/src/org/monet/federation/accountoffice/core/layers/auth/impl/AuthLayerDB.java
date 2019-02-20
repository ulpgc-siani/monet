package org.monet.federation.accountoffice.core.layers.auth.impl;

import com.google.inject.Inject;
import org.monet.exceptions.DatabaseException;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.database.DataRepositorySource;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.DBHelper;
import org.monet.federation.accountoffice.utils.MD5;
import org.monet.federation.accountoffice.utils.NamedParameterStatement;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthLayerDB extends FederationAuthLayer implements AuthLayer {

    private static final String AUTHDB_CREATE_USER = "AUTHDB_CREATE_USER";
    private static final String AUTHDB_LOGIN_USER = "AUTHDB_LOGIN_USER";
    private static final String AUTHDB_LOAD_USER = "AUTHDB_LOAD_USER";
    private static final String AUTHDB_LOAD_USER_BY_EMAIL = "AUTHDB_LOAD_USER_BY_EMAIL";
    private static final String AUTHDB_SAVE_USER = "AUTHDB_SAVE_USER";
    private static final String AUTHDB_EXISTS_USER_PASSWORD = "AUTHDB_EXISTS_USER_PASSWORD";
    private static final String AUTHDB_CREATE_USER_PASSWORD = "AUTHDB_CREATE_USER_PASSWORD";
    private static final String AUTHDB_UPDATE_USER_PASSWORD = "AUTHDB_UPDATE_USER_PASSWORD";

    private static final String PARAM_ID_USER = "id_user";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_FULLNAME = "fullname";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_LANG = "lang";
    private static final String PARAM_IS_HUMAN = "human";

    private Logger logger;
    private boolean isReadOnly;
    private DataRepositorySource dataRepositorySource;

    @Inject
    public AuthLayerDB(Logger logger, Configuration configuration, DataRepositorySource dataRepositorySource) {
        try {
            this.isReadOnly = configuration.getUserPasswordAuth().getDatabaseAuth().isReadOnly();
            this.dataRepositorySource = dataRepositorySource;
            this.logger = logger;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createUser(User user, String password) {
        logger.debug("createUser(%s)", user);

        this.checkIsReadOnly();

        Connection connection = null;
        NamedParameterStatement statement = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_CREATE_USER));
            statement.setString(PARAM_ID_USER, user.getId());
            statement.setString(PARAM_PASSWORD, MD5.calculate(password));
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
    }

    public User login(HttpServletRequest request) {
        logger.debug("login(%s)", request);

        String username = requestParam(request, "username");
        String password = requestParam(request, "password");

        return login(username, password);
    }

    @Override
    public boolean isValidLogin(String username, String password) {
        logger.debug("isValidLogin(%s, %s)", username, "****");

        return this.login(username, password) != null;
    }

    private User login(String username, String password) {
        logger.debug("login(%s, %s)", username, "****");

        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet resultSet = null;

        try {

            User user = this.loadUser(username);

            if (user == null)
                return null;

            if (password.isEmpty() && !this.existsPassword(user))
                return user;

            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_LOGIN_USER));
            statement.setString(PARAM_USERNAME, username);
            statement.setString(PARAM_PASSWORD, MD5.calculate(password));
            resultSet = statement.executeQuery();

            if (resultSet != null && resultSet.next())
                return this.fillUser(resultSet);

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(resultSet);
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
        return null;
    }

    @Override
    public User loadUser(String username) {
        logger.debug("loadUser(%s)", username);

        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_LOAD_USER));
            statement.setString(PARAM_USERNAME, username);
            resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                return this.fillUser(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(resultSet);
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
        return null;
    }

    @Override
    public User loadUserFromEmail(String email) {
        logger.debug("loadUserFromEmail(%s)", email);

        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_LOAD_USER_BY_EMAIL));
            statement.setString(PARAM_EMAIL, email);
            resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                return this.fillUser(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(resultSet);
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
        return null;
    }

    private User fillUser(ResultSet resultSet) throws SQLException {
        logger.debug("fillUser(%s)", resultSet);

        String language = resultSet.getString(PARAM_LANG);
        language = language != null ? language.toLowerCase() : "es";

        User user = new User();
        user.setId(resultSet.getString(DataRepository.PARAM_ID));
        user.setUsername(resultSet.getString(PARAM_USERNAME));
        user.setFullname(resultSet.getString(PARAM_FULLNAME));
        user.setEmail(resultSet.getString(PARAM_EMAIL));
        user.setLang(language);
        user.setHuman(resultSet.getBoolean(DataRepository.PARAM_IS_HUMAN));
        user.setMode(LoginMode.PASSWORD);

        return user;
    }

    @Override
    public void saveUser(User user) {
        logger.debug("updateUser(%s)", user);

        this.checkIsReadOnly();

        Connection connection = null;
        NamedParameterStatement statement = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();

            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_SAVE_USER));
            statement.setString(PARAM_USERNAME, user.getUsername());
            statement.setString(PARAM_FULLNAME, user.getFullname());
            statement.setString(PARAM_EMAIL, user.getEmail());
            statement.setString(PARAM_LANG, user.getLang().toLowerCase());
            statement.setBoolean(PARAM_IS_HUMAN, user.isHuman());

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
    public boolean updateUserPassword(User user, String newPassword) {
        logger.debug("updateUserPassword(%s, %s)", user.getId(), "****");

        this.checkIsReadOnly();

        if (!this.existsPassword(user))
            return this.createPassword(user, newPassword);

        return this.updatePassword(user, newPassword);
    }

    private void checkIsReadOnly() {
        if (this.isReadOnly)
            throw new RuntimeException("Authentication store is read only.");
    }

    private boolean existsPassword(User user) {
        Connection connection = null;
        NamedParameterStatement statement = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_EXISTS_USER_PASSWORD));
            statement.setString(PARAM_ID_USER, user.getId());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
    }

    private boolean createPassword(User user, String newPassword) {
        Connection connection = null;
        NamedParameterStatement statement = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_CREATE_USER_PASSWORD));
            statement.setString(PARAM_ID_USER, user.getId());
            statement.setString(PARAM_PASSWORD, MD5.calculate(newPassword));

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
    }

    private boolean updatePassword(User user, String newPassword) {
        Connection connection = null;
        NamedParameterStatement statement = null;

        try {
            connection = this.dataRepositorySource.getDataSource().getConnection();
            statement = new NamedParameterStatement(connection, (String) this.dataRepositorySource.getQueries().get(AUTHDB_UPDATE_USER_PASSWORD));
            statement.setString(PARAM_ID_USER, user.getId());
            statement.setString(PARAM_PASSWORD, MD5.calculate(newPassword));

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        } finally {
            DBHelper.close(statement);
            DBHelper.close(connection);
        }
    }

}
