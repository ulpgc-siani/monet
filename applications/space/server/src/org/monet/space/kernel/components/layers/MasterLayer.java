package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.Master;
import org.monet.space.kernel.model.UserInfo;

import java.util.LinkedHashMap;

public interface MasterLayer extends Layer {
	public boolean exists(String username, String certificateAuthority);

	public Master load(String username, String certificateAuthority);

	public Master addMaster(String username, String certificateAuthority, boolean colonizer, UserInfo info);

	public void deleteMaster(String id);

	public LinkedHashMap<String, Master> requestMasterListItems();

	public boolean colonized();
}
