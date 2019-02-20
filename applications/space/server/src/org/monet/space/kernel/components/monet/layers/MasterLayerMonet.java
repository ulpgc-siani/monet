package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.kernel.components.monet.federation.ComponentFederationMonet;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.model.Master;
import org.monet.space.kernel.model.UserInfo;
import org.monet.space.kernel.producers.ProducerMaster;
import org.monet.space.kernel.producers.ProducerMasterList;
import org.monet.space.kernel.producers.ProducersFactory;

import java.util.LinkedHashMap;

public class MasterLayerMonet extends LayerMonet implements MasterLayer {
	protected final ProducersFactory producersFactory = ProducersFactory.getInstance();

	public MasterLayerMonet(ComponentFederationMonet componentFederationMonet) {
	}

	protected boolean isStarted() {
		return ComponentFederationMonet.started();
	}

	@Override
	public boolean exists(String username, String certificateAuthority) {
		ProducerMaster producerMaster = (ProducerMaster) this.producersFactory.get(Producers.MASTER);
		return producerMaster.exists(username, certificateAuthority);
	}

	@Override
	public Master load(String username, String certificateAuthority) {
		ProducerMaster producerMaster = (ProducerMaster) this.producersFactory.get(Producers.MASTER);
		return producerMaster.load(username, certificateAuthority);
	}

	@Override
	public Master addMaster(String username, String certificateAuthority, boolean colonizer, UserInfo info) {
		ProducerMaster producerMaster = (ProducerMaster) this.producersFactory.get(Producers.MASTER);
		return producerMaster.create(username, certificateAuthority, colonizer, info);
	}

	@Override
	public void deleteMaster(String id) {
		ProducerMaster producerMaster = (ProducerMaster) this.producersFactory.get(Producers.MASTER);
		producerMaster.remove(id);
	}

	@Override
	public LinkedHashMap<String, Master> requestMasterListItems() {
		ProducerMasterList producerMasterList = (ProducerMasterList) this.producersFactory.get(Producers.MASTERLIST);
		return producerMasterList.load();
	}

	@Override
	public boolean colonized() {
		ProducerMasterList producerMasterList = (ProducerMasterList) this.producersFactory.get(Producers.MASTERLIST);
		return producerMasterList.loadCount() > 0;
	}

}
