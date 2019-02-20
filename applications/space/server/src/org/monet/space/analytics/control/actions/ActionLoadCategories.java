package org.monet.space.analytics.control.actions;

import net.minidev.json.JSONObject;
import org.monet.space.analytics.model.Kernel;
import org.monet.space.analytics.serializers.CategoriesSerializer;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.model.ChartStore;
import org.monet.space.kernel.agents.AgentLogger;
import org.sumus.asset.Asset;
import org.sumus.dimension.Dimension;
import org.sumus.dimension.category.Category;
import org.sumus.query.Chart;
import org.sumus.taxonomy.Taxonomy;

public class ActionLoadCategories extends Action {

	public ActionLoadCategories() {
		super();
	}

	@Override
	public String execute() {
		Category[] categories = null;
		JSONObject result = new JSONObject();
		Asset asset = null;

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		try {
			String dashboardId = this.getParameterAsString(Parameter.DASHBOARD);
			String taxonomyId = this.getParameterAsString(Parameter.ID);
			String parentCategoryId = this.getParameterAsString(Parameter.CATEGORY);

			asset = (Asset) Kernel.Instance().getDashboard(dashboardId).getAsset();

			Taxonomy taxonomy = this.getTaxonomy(asset, taxonomyId);

			if (taxonomy == null)
				return null;

			Dimension dimension = this.getDimension(asset, taxonomy.getName());
			long[] components = this.getComponents(dimension);
            categories = this.getCategories(asset, taxonomy, components, parentCategoryId);

			result = CategoriesSerializer.toJson(categories, components, this.getDictionary(asset));

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return result.toString();
	}

	private Taxonomy getTaxonomy(Asset asset, String taxonomyId) {
		Taxonomy taxonomy = null;

		for (Dimension dimension : asset.getDimensions()) {
			taxonomy = dimension.getTaxonomy(taxonomyId);
			if (taxonomy != null)
				break;
		}

		return taxonomy;
	}

    private Dimension getDimension(Asset asset, String taxonomyId) {

        for (Dimension dimension : asset.getDimensions()) {
            Taxonomy taxonomy = dimension.getTaxonomy(taxonomyId);
            if (taxonomy != null)
                return dimension;
        }

        return null;
    }

    private long[] getComponents(Dimension dimension) {
        Chart chart = ChartStore.Instance().get(this.getSessionId());

        if (chart == null)
            return new long[0];

        return chart.getParticipants().get(dimension);
    }

    private Category[] getCategories(Taxonomy taxonomy, long[] components) {

        if (components == null)
          return new Category[0];

	    if (components.length <= 0)
		    return new Category[0];

        return taxonomy.getCategories(components);
    }

    private Category[] getCategories(Asset asset, Taxonomy taxonomy, long[] components, String parentCategoryId) {
        Category category = taxonomy.searchCategory(parentCategoryId);

        if (parentCategoryId == null)
            return this.getCategories(taxonomy, components);

	    if (components == null)
		    return new Category[0];

        if (components.length <= 0)
            return new Category[0];

        return category.getChildren(components);
    }

}
