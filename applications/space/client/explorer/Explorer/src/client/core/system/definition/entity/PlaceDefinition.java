package client.core.system.definition.entity;

import client.core.model.definition.Ref;
import client.core.system.definition.Definition;

public class PlaceDefinition extends Definition implements client.core.model.definition.entity.PlaceDefinition {
	private client.core.model.definition.entity.PlaceDefinition.ActionDefinition actionDefinition;

	@Override
	public client.core.model.definition.entity.PlaceDefinition.ActionDefinition getAction() {
		return actionDefinition;
	}

	public void setAction(client.core.model.definition.entity.PlaceDefinition.ActionDefinition actionDefinition) {
		this.actionDefinition = actionDefinition;
	}

	public static class ActionDefinition extends Definition implements client.core.model.definition.entity.PlaceDefinition.ActionDefinition {
	}

	public static class DelegationActionDefinition extends ActionDefinition implements client.core.model.definition.entity.PlaceDefinition.DelegationActionDefinition {
		private Ref provider;

		@Override
		public Ref getProvider() {
			return provider;
		}

		public void setProvider(Ref provider) {
			this.provider = provider;
		}
	}

	public static class LineActionDefinition extends ActionDefinition implements client.core.model.definition.entity.PlaceDefinition.LineActionDefinition {
		private client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.TimeoutDefinition timeoutDefinition;
		private client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.LineStopDefinition[] stopDefinitions;

		@Override
		public client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.TimeoutDefinition getTimeout() {
			return timeoutDefinition;
		}

		public void setTimeoutDefinition(client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.TimeoutDefinition timeoutDefinition) {
			this.timeoutDefinition = timeoutDefinition;
		}

		@Override
		public client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.LineStopDefinition[] getStop() {
			return stopDefinitions;
		}

		public void setStopDefinitions(client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.LineStopDefinition[] stopDefinitions) {
			this.stopDefinitions = stopDefinitions;
		}

		public static class TimeoutDefinition implements client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.TimeoutDefinition {
			private Ref take;

			@Override
			public Ref getTake() {
				return take;
			}

			public void setTake(Ref take) {
				this.take = take;
			}
		}

		public static class LineStopDefinition implements client.core.model.definition.entity.PlaceDefinition.LineActionDefinition.LineStopDefinition {
			private String code;
			private String label;

			@Override
			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			@Override
			public String getLabel() {
				return label;
			}

			public void setLabel(String label) {
				this.label = label;
			}
		}

	}

	public static class EditionActionDefinition extends ActionDefinition implements client.core.model.definition.entity.PlaceDefinition.EditionActionDefinition {
	}

	public static class WaitActionDefinition extends ActionDefinition implements client.core.model.definition.entity.PlaceDefinition.WaitActionDefinition {
	}

}
