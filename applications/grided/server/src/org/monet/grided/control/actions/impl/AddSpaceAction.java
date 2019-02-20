package org.monet.grided.control.actions.impl;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONSpaceSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.core.services.space.SpaceService;

import com.google.inject.Inject;

public class AddSpaceAction extends BaseAction {

	private GridedService gridedService;
	private JSONSpaceSerializer serializer;
	private DeployService deployService;
	private SpaceService spaceService;

	@Inject
	public AddSpaceAction(GridedService gridedService, DeployService deployService, SpaceService spaceService, JSONSpaceSerializer serializer) {
		this.gridedService = gridedService;
		this.deployService = deployService;
		this.spaceService = spaceService;
		this.serializer = serializer;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Space space = null;
		String serverId = request.getParameter(Params.SERVER_ID);
		String federationId = request.getParameter(Params.FEDERATION_ID);
		String name = request.getParameter(Params.NAME);
		String url = request.getParameter(Params.URL);
		String modelId = request.getParameter(Params.MODEL_ID);

		try {
			Model model = this.gridedService.loadModel(Long.valueOf(modelId));
			ModelVersion modelVersion = model.getLatestVersion();

			// String serverLocation =
			// space.getFederation().getServer().getIp();
			// this.deployService.createSpace(serverLocation, space);

			space = this.gridedService.createSpace(Long.valueOf(serverId), Long.valueOf(federationId), name, url, modelVersion);

			String serverLocation = space.getFederation().getServer().getIp();
			this.deployService.createSpace(serverLocation, space);
			
			byte[] bytes = this.gridedService.loadVersionFile(model.getId(), modelVersion.getId());
			this.spaceService.updateModel(space.getData().getUrl(), new ByteArrayInputStream(bytes));

		} catch (Exception ex) {
			sendErrorResponse(response, JSONErrorResponse.ERROR);
			return;
		}

		sendResponse(response, this.serializer.serialize(space));
	}

}
