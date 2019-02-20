/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.agents;

import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.upgrade.DefaultUpgradeScript;
import org.monet.space.kernel.agents.upgrade.UpgradeScript;
import org.monet.space.kernel.agents.upgrade.UpgradeScriptClassLoader;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.utils.Resources;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

public abstract class AgentUpgrade {
    private static final AgentLogger agentLogger = AgentLogger.getInstance();

    public static boolean update() {
        Kernel kernel = Kernel.getInstance();
        String spaceVersion = kernel.getVersion();
        String databaseVersion = kernel.getDatabaseVersion();
	    String spaceName = Context.getInstance().getFrameworkName();

        agentLogger.info(String.format("Checking both space and database version for %s...", spaceName));

        if (spaceVersion.equals(databaseVersion)) {
            agentLogger.info("Versions OK!");
            return true;
        }

        agentLogger.info(String.format("%s VERSION (%s) IS DISTINCT FROM DATABASE VERSION (%s)! Finding for scripts to update database...", spaceName, spaceVersion, databaseVersion));
        boolean result = executeUpgradeScripts(databaseVersion, spaceVersion);
        agentLogger.info("DATABASE UPDATE FINISHED!");

        return result;
    }

    private static boolean executeUpgradeScripts(String databaseVersion, String spaceVersion) {
        String updateScriptsDir = getDatabaseUpdateScriptsDir();
        String[] files = AgentFilesystem.listDir(updateScriptsDir);
        AgentDatabase agentDatabase = AgentDatabase.getInstance();
        Configuration configuration = Configuration.getInstance();
        AgentLogger logger = AgentLogger.getInstance();
        Connection connection = null;

        Arrays.sort(files, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length()) return -1;
                if (o1.length() > o2.length()) return 1;
                return o1.compareTo(o2);
            }
        });

        try {

            UpgradeScriptClassLoader classLoader = new UpgradeScriptClassLoader(Resources.getFullPath("/"), logger);
            connection = agentDatabase.getConnection();
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            for (int i=0; i<files.length; i++) {
                String scriptFilename = files[i];
                String scriptVersion = LibraryFile.getFilename(scriptFilename);
                UpgradeScript script;

                if (!new File(updateScriptsDir + "/" + scriptFilename).isDirectory())
                    continue;

                try {
                    Class<?> clazz = Class.forName("kernel.database.upgrades." + scriptVersion + ".Main", true, classLoader);
                    Constructor constructor = clazz.getConstructor(AgentLogger.class, Configuration.class);
                    script = (UpgradeScript) constructor.newInstance(logger, configuration);
                }
                catch (ClassNotFoundException exception) {
                    script = new DefaultUpgradeScript(scriptVersion, logger, configuration);
                }

                if (!script.canExecute(spaceVersion, databaseVersion))
                    continue;

                if (!script.execute(connection))
                    return false;
            }

            Kernel.updateDatabaseVersion(connection, spaceVersion);

            if (autoCommit) {
                connection.commit();
                connection.setAutoCommit(true);
            }

        } catch (SQLException exception) {
            logger.error("Error getting database autoCommit variable", exception);
            return false;
        } catch (InvocationTargetException exception) {
            logger.error("Could not load script class", exception);
            return false;
        } catch (InstantiationException exception) {
            logger.error("Could not load script class", exception);
            return false;
        } catch (IllegalAccessException exception) {
            logger.error("Could not load script class", exception);
            return false;
        } catch (NoSuchMethodException exception) {
            logger.error("Could not load script class", exception);
            return false;
        } catch (Exception exception) {
            logger.error("Could not load script class", exception);
            return false;
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
            }
        }

        return true;
    }

    private static String getDatabaseUpdateScriptsDir() {
        return Configuration.getInstance().getUpgradesScriptsDir();
    }
}
