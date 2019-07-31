package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.*;
import org.monet.space.office.core.model.Language;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

import java.util.ArrayList;

public abstract class OfficeRender extends Render {
	protected RenderLink renderLink;
	protected Account account;
	protected RendersFactory rendersFactory;
	protected Dictionary dictionary;

	protected static final Integer ELEMENTS_PER_PAGE = 10;

	public static final class Mode {
		public static final String PAGE = "page";
		public static final String VIEW = "view";
	}

	private static class Logger implements CanvasLogger {
		@Override
		public void debug(String message, Object... args) {
			AgentLogger.getInstance().debugInModel(message, args);
		}
	}

	public OfficeRender() {
		super(new Logger(), Configuration.getInstance().getThemeTemplatesDir(Language.getCurrent()));
		this.rendersFactory = RendersFactory.getInstance();
		this.dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
	}

	public void setTemplate(String template) {
		Integer pos = template.lastIndexOf("?");
		if (pos == -1) pos = template.length();
		this.template = template.substring(0, pos);
		this.template = this.template.replaceAll(".html", "");
		this.template = this.template.replaceAll(".js", "");
		if (pos < template.length()) this.setParameters(template.substring(pos + 1));
	}

	public void setRenderLink(RenderLink renderLink) {
		this.renderLink = renderLink;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	protected boolean isSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getLinksIn() != null || showDefinition.getLinksOut() != null ||
				showDefinition.getNotes() != null || showDefinition.getRevisions() != null || showDefinition.getTasks() != null || showDefinition.getLocation() != null;
		}

		if (viewDefinition instanceof SetViewProperty) {
			SetViewProperty.ShowProperty showDefinition = ((SetViewProperty) viewDefinition).getShow();
			return (showDefinition.getOwnedPrototypes() != null || showDefinition.getSharedPrototypes() != null);
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();

			return showDefinition.getLinksIn() != null || showDefinition.getLinksOut() != null ||
				showDefinition.getNotes() != null || showDefinition.getRevisions() != null || showDefinition.getTasks() != null ||
				showDefinition.getAttachments() != null || showDefinition.getLocation() != null;
		}

		return false;
	}

	protected boolean isNotesSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getNotes() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getNotes() != null;
		}

		return false;
	}

	protected boolean isLocationSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getLocation() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getLocation() != null;
		}

		return false;
	}

	protected boolean isLinksInSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getLinksIn() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getLinksIn() != null;
		}

		return false;
	}

	protected boolean isLinksOutSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getLinksOut() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getLinksOut() != null;
		}

		return false;
	}

	protected boolean isAttachmentsSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return (showDefinition.getAttachments() != null);
		}

		return false;
	}

	protected boolean isRevisionsSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getRevisions() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getRevisions() != null;
		}

		return false;
	}

	protected boolean isPrototypesSystemView(NodeViewProperty viewDefinition) {
		return this.isOwnedPrototypesSystemView(viewDefinition) || this.isSharedPrototypesSystemView(viewDefinition);
	}

	protected boolean isOwnedPrototypesSystemView(NodeViewProperty viewDefinition) {
		if (!(viewDefinition instanceof SetViewProperty)) return false;

		SetViewProperty setDefinition = ((SetViewProperty) viewDefinition);
		ShowProperty showDefinition = setDefinition.getShow();

		return showDefinition.getOwnedPrototypes() != null;
	}

	protected boolean isSharedPrototypesSystemView(NodeViewProperty viewDefinition) {
		if (!(viewDefinition instanceof SetViewProperty)) return false;

		SetViewProperty setDefinition = ((SetViewProperty) viewDefinition);
		ShowProperty showDefinition = setDefinition.getShow();

		return showDefinition.getSharedPrototypes() != null;
	}

	protected boolean isRecentTaskSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getRecentTask() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getRecentTask() != null;
		}

		return false;
	}

	protected boolean isTasksSystemView(NodeViewProperty viewDefinition) {

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			return showDefinition.getTasks() != null;
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			return showDefinition.getTasks() != null;
		}

		return false;
	}

	protected boolean isViewMapLayer(NodeViewProperty viewDefinition) {
		if (!(viewDefinition instanceof SetViewProperty)) return false;
		return ((SetViewProperty) viewDefinition).getShow().getLocations() != null;
	}

	protected ArrayList<Ref> mergeIndexShows(final IndexViewProperty.ShowProperty showDefinition) {
		ArrayList<Ref> showList = new ArrayList<Ref>();

		if (showDefinition.getTitle() != null)
			showList.add(showDefinition.getTitle());

		if (showDefinition.getPicture() != null)
			showList.add(showDefinition.getPicture());

		showList.addAll(showDefinition.getHighlight());
		showList.addAll(showDefinition.getLine());
		showList.addAll(showDefinition.getLineBelow());
		showList.addAll(showDefinition.getFooter());

		return showList;
	}

	protected String getDefaultWidgetView(Node node) {
		NodeViewProperty nodeViewDefinition = node.getDefinition().getDefaultView();

		if (nodeViewDefinition != null)
			return nodeViewDefinition.getCode();

		return "default";
	}

	protected boolean canExecuteOperation(Node node, NodeDefinitionBase.OperationProperty operationDefinition) {

		if (operationDefinition.getFor() == null)
			return true;

		boolean canExecute = false;
		for (Ref roleRef : operationDefinition.getFor().getRole()) {
			String roleCode = dictionary.getDefinitionCode(roleRef.getValue());
			Role role = account.getRoleList().get(roleCode);

			if (role == null)
				continue;

			if (role.getDefinition().getDisableEdition() != null)
				return false;
			else
				canExecute = true;
		}

		return canExecute;
	}

	public String getNodeType(Node node) {
		if (node.isForm()) return "form";
		else if (node.isDesktop()) return "desktop";
		else if (node.isContainer()) return "container";
		else if (node.isCollection()) return "collection";
		return "notype";
	}

	protected boolean allowEdition(Node node) {
		return !node.isLocked() && canEditWithRole();
	}

	protected boolean canEditWithRole() {
		for (Role role : account.getRoleList()) {
			if (role.getDefinition().getDisableEdition() != null)
				return false;
		}

		return true;
	}

	ArrayList<Ref> collectionDefinitionAdds(NodeDefinition definition) {
		if (!(definition instanceof CollectionDefinition)) return new ArrayList<>();
		CollectionDefinition collectionDefinition = (CollectionDefinition) definition;
		ArrayList<Ref> node = collectionDefinition.getAdd().getNode();
		if (collectionDefinition.getToolbar() != null && collectionDefinition.getToolbar().getAddOperation() != null)
			node = collectionDefinition.getToolbar().getAddOperation().getEnable();
		return node;
	}
}
