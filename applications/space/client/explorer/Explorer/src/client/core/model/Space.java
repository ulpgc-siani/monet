package client.core.model;

import client.core.model.definition.Dictionary;
import client.core.model.factory.EntityFactory;

import java.util.ArrayList;

import static client.core.model.Instance.ClassName;

public interface Space {

	ClassName CLASS_NAME = new ClassName("Space");

	String getName();
	String getTitle();
	String getSubTitle();
	String getModelLogoUrl();
	String getLanguage();
	String getTheme();
	String getInstanceId();
	Action getInitialAction();
	Federation getFederation();
	Account getAccount();
	Configuration getConfiguration();
	List<Tab> getTabs();
	ClassName getInstanceClass();
	EntityFactory getEntityFactory();
	Dictionary getDictionary();

	interface Federation {
		String getLabel();
		String getLogoUrl();
		String getUrl();
	}

	interface Configuration {
		String getDomain();
		String getUrl();
		String getApiUrl();
		String getPushUrl();
		String getAnalyticsUrl();
		String getDigitalSignatureUrl();
		String getImagesPath();
	}

	interface Tab {
		enum Type {
			ENVIRONMENT, DASHBOARD, TASK_BOARD, TASK_TRAY, NEWS, ROLES, TRASH;

			public static Type fromString(String type) {
				return Type.valueOf(type.toUpperCase());
			}
		}

		<T extends Entity> T getEntity();
		String getLabel();
		Type getType();
		View getView();
		boolean isActive();
	}

	class Action {
		protected java.util.List<String> parameters = new ArrayList<>();

		public static final ClassName CLASS_NAME = new ClassName("Action");

		public static final Action SHOW_HOME = new ShowHomeAction();

		public static Action parse(String link) {
			clean(link);

			int leftBracket = link.indexOf("(");
			int rightBracket = link.indexOf(")");

			if (leftBracket == -1)
				return Action.build(link);

			String parameters = link.substring(leftBracket+1, rightBracket);
			parameters = parameters.replaceAll("\\/", "@bar45@");
			link = link.substring(0, leftBracket+1) + parameters + link.substring(rightBracket);

			int pos = -1;
			while ((pos = link.indexOf("/")) != -1)
				link = link.substring(pos+1, link.length());

			if ((pos = link.indexOf("(")) == -1)
				return Action.build(link);

			if (link.charAt(link.length()-1) != ')') return null;

			Action action = Action.build(link.substring(0, pos));

			link = link.substring(pos+1, link.length()-1);
			link = link.replaceAll("@bar45@", "/");

			String[] resultArray = link.split(",");
			for (pos=0; pos < resultArray.length; pos++) {
				action.add(resultArray[pos]);
			}

			return action;
		}

		private static Action build(String name) {
			if (name == null)
				return new VoidAction();

			switch (name) {
				case ShowTaskAction.NAME: return new ShowTaskAction();
				case ShowHomeAction.NAME: return new ShowHomeAction();
				case ShowNodeAction.NAME: return new ShowNodeAction();
			}

			return null;
		}

		private static String clean(String link) {
			link = link.replaceAll("%20", "");
			link = link.replaceAll("%28", "(");
			link = link.replaceAll("%29", ")");
			return link;
		}

		private void add(String parameter) {
			this.parameters.add(parameter);
		}
	}

	class UpdateTaskStateAction extends Action {
		public static final String NAME = "updatetaskstate";

		public String getTaskId() {
			return parameters.get(0);
		}
	}

	class ShowTaskAction extends Action {
		public static final String NAME = "showtask";

		public String getTaskId() {
			return parameters.get(0);
		}
	}

	class ShowHomeAction extends Action {
		public static final String NAME = "showhome";
	}

	class ShowNodeAction extends Action {
		public static final String NAME = "shownode";

		public String getNodeId() {
			return parameters.get(0);
		}
	}

	class VoidAction extends Action {
	}

}
