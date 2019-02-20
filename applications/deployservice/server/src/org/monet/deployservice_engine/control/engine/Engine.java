package org.monet.deployservice_engine.control.engine;

import org.monet.deployservice_engine.control.commands.*;
import org.monet.deployservice.xml.Item;

public class Engine {
	private static Factory factory;

	public Engine() {
		factory = Factory.getInstance();
		
		factory.register(OperationIDs.GetServers, GetServers.class);
		factory.register(OperationIDs.AddSpace, AddSpace.class);
		factory.register(OperationIDs.DeleteSpace, DeleteSpace.class);
		factory.register(OperationIDs.DeleteContainer, DeleteContainer.class);
		factory.register(OperationIDs.ResetSpace, ResetSpace.class);
		factory.register(OperationIDs.UpdateContainer, UpdateContainer.class);
		factory.register(OperationIDs.ResetContainer, ResetContainer.class);
		factory.register(OperationIDs.RestartContainer, RestartContainer.class);		
		factory.register(OperationIDs.ChangeServerName, ChangeServerName.class);
		factory.register(OperationIDs.ChangeContainerName, ChangeContainerName.class);
		factory.register(OperationIDs.UpdateApplication, UpdateApplication.class);
		factory.register(OperationIDs.ResetMonet, ResetMonet.class);
		factory.register(OperationIDs.ResetDocService, ResetDocService.class);
		factory.register(OperationIDs.ResetBiEngine, ResetBiEngine.class);
		factory.register(OperationIDs.UpdateWars, UpdateWars.class);
		factory.register(OperationIDs.ResetMonetContainer, ResetMonetContainer.class);		
		factory.register(OperationIDs.ResetDocServiceContainer, ResetDocServiceContainer.class);		
		factory.register(OperationIDs.ResetLocalContainer, ResetLocalContainer.class);						
		factory.register(OperationIDs.UpdateFederationConfig, UpdateFederationConfig.class);						
		factory.register(OperationIDs.UpdateSSHConfig, UpdateSSHConfig.class);						
		factory.register(OperationIDs.UpdateDeployServiceConfig, UpdateDeployServiceConfig.class);						
		factory.register(OperationIDs.UploadDistribution, UploadDistribution.class);						
		factory.register(OperationIDs.UpdateTime, UpdateTime.class);						
		factory.register(OperationIDs.RenewCertificates, RenewCertificates.class);						
		factory.register(OperationIDs.UpdateFederations, UpdateFederations.class);						
	}
	
	public Item getItem(Item command) {
		Item result = new Item();
		
		if (command.getProperty("id").equals(OperationIDs.GetServers)) result = factory.getCommand(OperationIDs.GetServers).execute(command);
		if (command.getProperty("id").equals(OperationIDs.AddSpace)) result = factory.getCommand(OperationIDs.AddSpace).execute(command);
		if (command.getProperty("id").equals(OperationIDs.DeleteSpace)) result = factory.getCommand(OperationIDs.DeleteSpace).execute(command);
		if (command.getProperty("id").equals(OperationIDs.DeleteContainer)) result = factory.getCommand(OperationIDs.DeleteContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateContainer)) result = factory.getCommand(OperationIDs.UpdateContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetContainer)) result = factory.getCommand(OperationIDs.ResetContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.RestartContainer)) result = factory.getCommand(OperationIDs.RestartContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ChangeServerName)) result = factory.getCommand(OperationIDs.ChangeServerName).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ChangeContainerName)) result = factory.getCommand(OperationIDs.ChangeContainerName).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetSpace)) result = factory.getCommand(OperationIDs.ResetSpace).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateApplication)) result = factory.getCommand(OperationIDs.UpdateApplication).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetMonet)) result = factory.getCommand(OperationIDs.ResetMonet).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetDocService)) result = factory.getCommand(OperationIDs.ResetDocService).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetBiEngine)) result = factory.getCommand(OperationIDs.ResetBiEngine).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateWars)) result = factory.getCommand(OperationIDs.UpdateWars).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetMonetContainer)) result = factory.getCommand(OperationIDs.ResetMonetContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetDocServiceContainer)) result = factory.getCommand(OperationIDs.ResetDocServiceContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.ResetLocalContainer)) result = factory.getCommand(OperationIDs.ResetLocalContainer).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateFederationConfig)) result = factory.getCommand(OperationIDs.UpdateFederationConfig).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateSSHConfig)) result = factory.getCommand(OperationIDs.UpdateSSHConfig).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateDeployServiceConfig)) result = factory.getCommand(OperationIDs.UpdateDeployServiceConfig).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UploadDistribution)) result = factory.getCommand(OperationIDs.UploadDistribution).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateTime)) result = factory.getCommand(OperationIDs.UpdateTime).execute(command);
		if (command.getProperty("id").equals(OperationIDs.RenewCertificates)) result = factory.getCommand(OperationIDs.RenewCertificates).execute(command);
		if (command.getProperty("id").equals(OperationIDs.UpdateFederations)) result = factory.getCommand(OperationIDs.UpdateFederations).execute(command);
						
		return result;
	}
	
}
