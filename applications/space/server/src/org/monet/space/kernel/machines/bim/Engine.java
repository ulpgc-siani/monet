package org.monet.space.kernel.machines.bim;

import org.monet.metamodel.DashboardDefinition;
import org.monet.metamodel.DatastoreDefinition;
import org.monet.space.kernel.exceptions.CreateDatastoreSessionException;
import org.monet.space.kernel.exceptions.MountDatastoreException;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.kernel.model.Datastore;
import org.monet.space.kernel.model.DatastoreTransaction;

import java.util.List;

public interface Engine {

	Dashboard createDashboard(String code, OnCreate create);

	boolean existsDashboard(String code);

	Dashboard getDashboard(String code, OnLoad load);

	Dashboard updateDashboard(DashboardDefinition oldDefinition, DashboardDefinition definition);

	void removeDashboard(String code);

	Datastore createDatastore(String code);

	boolean existsDatastore(String code);

	Datastore getDatastore(String code);

	Datastore updateDatastore(DatastoreDefinition oldDefinition, DatastoreDefinition definition);

	void removeDatastore(String code);

	void mountDatastore(String code, List<DatastoreTransaction> transactions) throws CreateDatastoreSessionException, MountDatastoreException;

	void create();

	void reset();

	void destroy();

	interface OnCreate {
		void created(Dashboard dashboard);
	}

	interface OnLoad {
		void loaded(Dashboard dashboard);
	}
}
