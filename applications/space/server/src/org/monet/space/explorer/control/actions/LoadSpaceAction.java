/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2014  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

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

package org.monet.space.explorer.control.actions;


import com.google.inject.Inject;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.dialogs.Dialog;
import org.monet.space.explorer.control.displays.SpaceDisplay;
import org.monet.space.explorer.model.*;
import org.monet.space.explorer.model.Language;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Federation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class LoadSpaceAction extends Action<Dialog, SpaceDisplay> {
    private LayerProvider layerProvider;
    private BusinessUnit businessUnit;
    private Dictionary dictionary;
    private Language languageInstance;
    private org.monet.space.kernel.configuration.Configuration kernelConfiguration;

    @Inject
    public void inject(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Inject
    public void inject(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    @Inject
    public void inject(Language language) {
        this.languageInstance = language;
    }

    @Inject
    public void inject(org.monet.space.kernel.configuration.Configuration configuration) {
        this.kernelConfiguration = configuration;
    }

    public void execute() throws IOException {
        Language.fillCurrentTimeZone(/*dialog.getTimeZone()*/BusinessUnit.getTimeZone());
        display.write(createSpace());
    }

    private Space createSpace() {
        Distribution distribution = businessUnit.getDistribution();
        Project project = businessUnit.getBusinessModel().getProject();

        Space space = new Space(businessUnit.getName());
        space.setInstanceId(UUID.randomUUID().toString());
        space.setTitle(BusinessUnit.getTitle(distribution, project));
        space.setSubTitle(businessUnit.getLabel());
        space.setLanguage(Language.getCurrent());
        space.setTheme(configuration.getTheme());
        space.setInitialAction(getInitialAction());
        space.setFederation(getFederation());
        space.setAccount(getAccount());
        space.setConfiguration(createConfiguration());
        space.setTabs(getTabs());
        space.setDictionary(dictionary);

        return space;
    }

    protected String getInitialAction() {
        Account account = getAccount();
        MonetLink link = null;

        Task initializerTask = layerProvider.getTaskLayer().getCurrentInitializerTask();
        if (initializerTask != null && account.canResolveInitializerTask(initializerTask))
            link = new MonetLink(MonetLink.Type.Task, initializerTask.getId());

        return link!=null?link.toString():null;
    }

    protected org.monet.space.explorer.model.Federation getFederation() {
        Federation federation = businessUnit.getFederation();
        return new org.monet.space.explorer.model.Federation(federation.getLabel(), getFederationLogo(), federation.getUri());
    }

    protected String getFederationLogo() {
        org.monet.space.kernel.model.Federation federation = businessUnit.getFederation();

        if (AgentFilesystem.existFile(kernelConfiguration.getFederationLogoFile()))
            return configuration.getFederationLogoUrl();

        return federation.getLogoUrl();
    }

    private Tab[] getTabs() {
        Distribution distribution = businessUnit.getDistribution();
        Distribution.ShowProperty showDefinition = distribution.getShow();
        List<Tab> tabs = new ArrayList<>();

        if (showDefinition == null)
            return tabs.toArray(new Tab[tabs.size()]);

        addEnvironmentTabs(showDefinition, tabs);
        addDashboardTabs(showDefinition, tabs);
        addTaskBoardTab(showDefinition, tabs);
        addTaskTrayTab(showDefinition, tabs);
        addNewsTab(showDefinition, tabs);
        addRolesTab(showDefinition, tabs);
        addTrashTab(showDefinition, tabs);

        return tabs.toArray(new Tab[tabs.size()]);
    }

    private void addEnvironmentTabs(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        Account account = getAccount();
        Node rootNode = account.getRootNode();

        for (Ref definitionRef : showDefinition.getTabEnvironment()) {
            Node node = getEnvironmentNode(account, definitionRef.getValue());

            if (node == null)
                continue;

            boolean isRootNode = rootNode.getId().equals(node.getId());
            NodeDefinition definition = node.getDefinition();
            boolean visible = false;

            if (!definition.isEnvironment())
                continue;

            List<Tab.EntityView> views = new ArrayList<>();
            if (definition.isDesktop()) {
                DesktopDefinition desktopDefinition = (DesktopDefinition) definition;
                visible = (desktopDefinition.getFor() != null) ? this.isLinkVisible(dictionary, desktopDefinition.getFor().getRole()) : true;
                views = getTabViews(desktopDefinition.getViewDefinitionList(), desktopDefinition.getDefaultView(), isRootNode);
            } else if (definition.isContainer() && definition.isEnvironment()) {
                ContainerDefinition containerDefinition = (ContainerDefinition) definition;
                visible = (containerDefinition.getFor() != null) ? this.isLinkVisible(dictionary, containerDefinition.getFor().getRole()) : true;
                views = getTabViews(containerDefinition.getViewDefinitionList(), containerDefinition.getDefaultView(), isRootNode);
            }

            if (visible)
                for (Tab.EntityView view : views)
                    tabs.add(createTab(node, Tab.Type.ENVIRONMENT, view, false));
        }
    }

    private List<Tab.EntityView> getTabViews(Collection<NodeViewProperty> viewList, final NodeViewProperty defaultView, boolean isRootNode) {
        List<Tab.EntityView> result = new ArrayList<>();

        if (isRootNode)
            for (final NodeViewProperty viewProperty : viewList)
                result.add(new Tab.EntityView() {
                    @Override
                    public String getCode() {
                        return viewProperty.getCode();
                    }

                    @Override
                    public String getName() {
                        return viewProperty.getName();
                    }

                    @Override
                    public Type getType() {
                        return Type.NODE_VIEW;
                    }

                    @Override
                    public String getLabel() {
                        return languageInstance.getModelResource(viewProperty.getLabel());
                    }
                });
        else
            result.add(new Tab.EntityView() {
                @Override
                public String getCode() {
                    return defaultView.getCode();
                }

                @Override
                public String getName() {
                    return defaultView.getName();
                }

                @Override
                public Type getType() {
                    return Type.NODE_VIEW;
                }

                @Override
                public String getLabel() {
                    return languageInstance.getModelResource(defaultView.getLabel());
                }
            });

        return result;
    }

    private void addTrashTab(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        if (showDefinition.getTabTrash() != null) {
            boolean visible = (showDefinition.getTabTrash().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabTrash().getFor()) : true;
            if (visible)
                tabs.add(createTab(new Trash(), Tab.Type.TRASH, null, false));
        }
    }

    private void addRolesTab(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        if (showDefinition.getTabRoles() != null) {
            boolean visible = (showDefinition.getTabRoles().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabRoles().getFor()) : true;
            if (visible)
                tabs.add(createTab(new Roles(), Tab.Type.ROLES, null, false));
        }
    }

    private void addNewsTab(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        if (showDefinition.getTabNews() != null){
            boolean visible = (showDefinition.getTabNews().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabNews().getFor()) : true;
            if (visible)
                tabs.add(createTab(new News(), Tab.Type.NEWS, null, false));
        }
    }

    private void addTaskTrayTab(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        if (showDefinition.getTabTasktray() != null) {
            boolean visible = (showDefinition.getTabTasktray().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabTasktray().getFor()) : true;
            if (visible)
                tabs.add(createTab(new TaskTray(), Tab.Type.TASK_TRAY, null, false));
        }
    }

    private void addTaskBoardTab(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        if (showDefinition.getTabTaskboard() != null) {
            boolean visible = (showDefinition.getTabTaskboard().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabTaskboard().getFor()) : true;
            if (visible)
                tabs.add(createTab(new TaskBoard(), Tab.Type.TASK_BOARD, null, false));
        }
    }

    private void addDashboardTabs(Distribution.ShowProperty showDefinition, List<Tab> tabs) {
        for (Ref dashboardRef : showDefinition.getTabDashboard()) {
            Dashboard dashboard = layerProvider.getDashboardLayer().load(dictionary.getDefinitionCode(dashboardRef.getDefinition()));
            final DashboardDefinition definition = dashboard.getDefinition();
            boolean visible = (definition.getFor() != null) ? this.isLinkVisible(dictionary, definition.getFor().getRole()) : true;
            final DashboardDefinitionBase.DashboardViewProperty viewDefinition = definition.getView(dashboardRef.getValue());
            Tab.EntityView view = new Tab.EntityView() {
                @Override
                public String getCode() {
                    return viewDefinition.getCode();
                }

                @Override
                public String getName() {
                    return viewDefinition.getName();
                }

                @Override
                public Type getType() {
                    return Type.DASHBOARD_VIEW;
                }

                @Override
                public String getLabel() {
                    return definition.getLabelString();
                }
            };

            if (visible)
                tabs.add(createTab(dashboard, Tab.Type.DASHBOARD, view, false));
        }
    }

    private boolean isLinkVisible(Dictionary dictionary, List<Ref> roleRefs) {
        boolean visible = false;

        for (Ref role : roleRefs) {
            String roleCode = dictionary.getDefinitionCode(role.getValue());
            if (layerProvider.getRoleLayer().existsNonExpiredUserRole(roleCode, this.getAccount().getUser())) {
                visible = true;
                break;
            }
        }

        return visible;
    }

    private Configuration createConfiguration() {
        Configuration result = new Configuration();
        result.setDomain(kernelConfiguration.getDomain());
        result.setUrl(configuration.getUrl());
        result.setApiUrl(configuration.getApiUrl());
        result.setPushUrl(configuration.getPushApiUrl());
        result.setAnalyticsUrl(configuration.getAnalyticsUrl());
        result.setDigitalSignatureUrl(configuration.getDigitalSignatureUrl());
        result.setImagesPath(configuration.getImagesPath());
        return result;
    }

    protected Tab createTab(final Entity entity, final Tab.Type type, final Tab.EntityView view, final boolean isActive) {
        Tab result = new Tab();
        result.setEntity(entity);
        result.setType(type);
        result.setActive(isActive);
        result.setView(view);
        return result;
    }

    private Node getEnvironmentNode(Account account, String key) {
        NodeDefinition definition = dictionary.getNodeDefinition(key);
        String code = definition.getCode();
        Node result = null;

        if (definition.isDesktop())
            result = layerProvider.getNodeLayer().locateNode(code);
        else if (definition.isEnvironment()) {
            ArrayList<Node> environmentNodes = account.getEnvironmentNodes();
            for (Node environmentNode : environmentNodes) {
                if (environmentNode.getDefinition().getCode().equals(code))
                    return environmentNode;
            }
        }

        return result;
    }

}