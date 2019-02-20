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
import com.google.inject.Injector;
import org.monet.space.explorer.control.dialogs.LoadTaskListIndexFilterOptionsDialog;
import org.monet.space.explorer.control.displays.FilterOptionDisplay;
import org.monet.space.explorer.injection.InjectorFactory;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.explorer.model.Filter;
import org.monet.space.explorer.model.Language;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadTaskListIndexFilterOptionsAction extends Action<LoadTaskListIndexFilterOptionsDialog, FilterOptionDisplay> {

    public void execute() throws IOException {
        Filter filter = dialog.getFilter();
        Injector injector = InjectorFactory.get();
        OptionsLoader loader = injector.getInstance(loaders.get(filter.getName()));
        List<Filter.Option> options = loader.getOptions(filter);

        display.writeList(toExplorerList(options));
    }

    private ExplorerList<Filter.Option> toExplorerList(List<Filter.Option> options) {
        ExplorerList<Filter.Option> result = new ExplorerList<>();

        result.setTotalCount(options.size());

        for (Filter.Option option : options)
            result.add(option);

        return result;
    }

    private static Map<String, Class<? extends OptionsLoader>> loaders = new HashMap<String, Class<? extends OptionsLoader>>() {{
        put("type", TypeOptionsLoader.class);
        put("state", StateOptionsLoader.class);
        put("role", RoleOptionsLoader.class);
        put("owner", OwnerOptionsLoader.class);
        put("sender", SenderOptionsLoader.class);
    }};

    private interface OptionsLoader {
        public List<Filter.Option> getOptions(Filter filter);
    }

    private static class TypeOptionsLoader implements OptionsLoader {
        private LayerProvider layerProvider;
        private Language language;

        @Inject
        public void inject(LayerProvider layerProvider) {
            this.layerProvider = layerProvider;
        }

        @Inject
        public void inject(Language language) {
            this.language = language;
        }

        @Override
        public List<Filter.Option> getOptions(Filter filter) {
            TaskFilters taskFilters = layerProvider.getTaskLayer().loadTasksFilters(org.monet.space.explorer.model.Language.getCurrent());
            List<Filter.Option> result = new ArrayList<>();

            for (final TaskType type : taskFilters.types)
                result.add(new Filter.Option() {
                    @Override
                    public String getValue() {
                        return type.getCode();
                    }

                    @Override
                    public String getLabel() {
                        return type.getLabel();
                    }
                });

            return result;
        }
    }

    private static class StateOptionsLoader implements OptionsLoader {
        private LayerProvider layerProvider;
        private Language language;

        @Inject
        public void inject(LayerProvider layerProvider) {
            this.layerProvider = layerProvider;
        }

        @Inject
        public void inject(Language language) {
            this.language = language;
        }

        @Override
        public List<Filter.Option> getOptions(Filter filter) {
            TaskFilters taskFilters = layerProvider.getTaskLayer().loadTasksFilters(org.monet.space.explorer.model.Language.getCurrent());
            List<Filter.Option> result = new ArrayList<>();

            for (final String state : taskFilters.states)
                result.add(new Filter.Option() {
                    @Override
                    public String getValue() {
                        return state;
                    }

                    @Override
                    public String getLabel() {
                        return language.getTaskStateLabel(state);
                    }
                });

            return result;
        }
    }

    private static class RoleOptionsLoader implements OptionsLoader {
        private LayerProvider layerProvider;

        @Inject
        public void inject(LayerProvider layerProvider) {
            this.layerProvider = layerProvider;
        }

        @Override
        public List<Filter.Option> getOptions(Filter filter) {
            RoleList roleList = layerProvider.getTaskLayer().loadTasksRoleList();
            List<Filter.Option> result = new ArrayList();

            for (final Role role : roleList)
                result.add(new Filter.Option() {
                    @Override
                    public String getValue() {
                        return role.getCode();
                    }

                    @Override
                    public String getLabel() {
                        return role.getLabel();
                    }
                });

            return result;
        }
    }

    private static abstract class UserOptionsLoader implements OptionsLoader {
        protected LayerProvider layerProvider;

        @Inject
        public void inject(LayerProvider layerProvider) {
            this.layerProvider = layerProvider;
        }

        public List<Filter.Option> toList(UserList userList) {
            List<Filter.Option> result = new ArrayList();

            for (final User user : userList)
                result.add(new Filter.Option() {
                    @Override
                    public String getValue() {
                        return user.getId();
                    }

                    @Override
                    public String getLabel() {
                        return user.getInfo().getFullname();
                    }
                });

            return result;
        }
    }

    private static class OwnerOptionsLoader extends UserOptionsLoader {
        @Override
        public List<Filter.Option> getOptions(Filter filter) {
            return toList(layerProvider.getTaskLayer().loadTasksOwnerList());
        }
    }

    private static class SenderOptionsLoader extends UserOptionsLoader {
        @Override
        public List<Filter.Option> getOptions(Filter filter) {
            return toList(layerProvider.getTaskLayer().loadTasksSenderList());
        }
    }

}