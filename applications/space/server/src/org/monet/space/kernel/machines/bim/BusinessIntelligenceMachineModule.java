package org.monet.space.kernel.machines.bim;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Language;
import org.sumus.DataWarehouse;
import org.sumus.ext.DatawareHouseDelegate;
import org.sumus.ext.filelocator.UriFS;
import sumus.builder.Builder;
import sumus.builder.ext.BuilderDelegate;

public class BusinessIntelligenceMachineModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Engine.class).to(EngineMonet.class);
		bind(Configuration.class).toInstance(Configuration.getInstance());
	}

	@Provides
	public Dictionary dictionaryProducer() {
		return Dictionary.getInstance();
	}

	@Provides
	public Language languageProducer() {
		return Language.getInstance();
	}

	@Provides
	public DashboardLayer dashboardLayerProducer() {
		return ComponentDatawareHouse.getInstance().getDashboardLayer();
	}

	@Provides
	public DatastoreLayer datastoreLayerProducer() {
		return ComponentDatawareHouse.getInstance().getDatastoreLayer();
	}

	@Provides
	public DataWarehouse getDatawareHouse() {
		return new DatawareHouseWrapper().get();
	}

	@Provides
	public Builder getBuilder() {
		return new BuilderWrapper().get();
	}

	static class DatawareHouseWrapper {

		public DatawareHouseWrapper() {
		}

		public DataWarehouse get() {
			return createDatawareHouse();
		}

		private DataWarehouse createDatawareHouse() {
			String datawareHouseDir = Configuration.getInstance().getDatawareHouseDir();
			DatawareHouseDelegate delegate = new DatawareHouseDelegate(new UriFS(datawareHouseDir));
			return DataWarehouse.newInstance(delegate);
		}
	}

	static class BuilderWrapper {
		private Builder builder;

		public BuilderWrapper() {
			String datawareHouseDir = Configuration.getInstance().getDatawareHouseDir();
			BuilderDelegate delegate = new BuilderDelegate(datawareHouseDir);
			this.builder = new Builder(delegate);
		}

		public Builder get() {
			return this.builder;
		}
	}

}
