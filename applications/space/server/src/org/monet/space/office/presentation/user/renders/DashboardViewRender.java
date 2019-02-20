package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.DashboardDefinition;
import org.monet.metamodel.DashboardDefinitionBase.DashboardViewProperty;
import org.monet.metamodel.DashboardDefinitionBase.DashboardViewProperty.ShowProperty;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.kernel.model.Dashboard;

import java.util.HashMap;

public class DashboardViewRender extends OfficeRender {
	Dashboard dashboard;

	@Override
	public void setTarget(Object target) {
		this.dashboard = (Dashboard) target;
	}

	public DashboardViewRender() {
		super();
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Configuration configuration = Configuration.getInstance();
		String codeView = this.getParameterAsString("view");
		String view = "";

		loadCanvas("view.dashboard");

		if (codeView.isEmpty()) {
			map.put("codeView", codeView);
			map.put("labelDefinition", this.dashboard.getLabel());
			addMark("view", block("view.undefined", map));
		}

		DashboardDefinition definition = this.dashboard.getDefinition();
		DashboardViewProperty viewDefinition = definition.getView(codeView);
		ShowProperty showDefinition = viewDefinition.getShow();

		map.put("id", definition.getCode());
		map.put("code", definition.getCode());
		map.put("codeView", codeView);
		map.put("analyticsUrl", configuration.getAnalyticsUrl());

		if (showDefinition != null && showDefinition.getOlap() != null)
			view = block("view.olap", map);
		else if (showDefinition != null && showDefinition.getReport() != null)
			view = block("view.report", map);

		addMark("view", view);
	}
	
	/*
	private void initLayout(HashMap<String, Object> viewMap) {
	  int layoutSize = this.getLayoutSize();
	  
	  this.initLayoutTop(viewMap, layoutSize);
	  this.initLayoutMiddle(viewMap, layoutSize);
	  this.initLayoutBottom(viewMap, layoutSize);
	}

  private void initLayoutTop(HashMap<String, Object> viewMap, int layoutSize) {
    List<ViewProperty> topViews = this.definition.getTopViews();
    StringBuilder topBuilder = new StringBuilder();
    
    for (ViewProperty viewDefinition : topViews)
      topBuilder.append(this.initRegion(viewDefinition));
    
    viewMap.put("regionTop", topBuilder.toString());
  }
  
  private void initLayoutMiddle(HashMap<String, Object> viewMap, int layoutSize) {
    List<ViewProperty> middleViews = this.definition.getMiddleViews();
    StringBuilder middleBuilder = new StringBuilder();
    
    for (ViewProperty viewDefinition : middleViews)
      middleBuilder.append(this.initRegion(viewDefinition));
    
    viewMap.put("regionMiddle", middleBuilder.toString());
  }
  
  private void initLayoutBottom(HashMap<String, Object> viewMap, int layoutSize) {
    List<ViewProperty> bottomViews = this.definition.getBottomViews();
    StringBuilder bottomBuilder = new StringBuilder();
    
    for (ViewProperty viewDefinition : bottomViews)
      bottomBuilder.append(this.initRegion(viewDefinition));

    viewMap.put("regionBottom", bottomBuilder.toString());
  }
  
  private String initRegion(ViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    RegionProperty regionDefinition = viewDefinition.getRegion();
    Configuration configuration = Configuration.getInstance();
    Long size = regionDefinition.getSize();
    IndicatorProperty indicatorDefinition = this.dictionary.getIndicatorDefinition(viewDefinition.getShow().getIndicator().getValue());
    RangeProperty rangeDefinition = viewDefinition.getRange();
    ModeEnumeration mode = viewDefinition.getMode();
    ReferenceProperty referenceDefinition = viewDefinition.getReference();
    
    if (size == 0) 
      size = 1L;
    
    map.put("size", size);
    map.put("asset", this.renderLink.getAssetName());
    map.put("analyticsBrowserUrl", configuration.getAnalyticsBrowserUrl());
    map.put("view", viewDefinition.getCode());
    map.put("language", Language.getCurrent());
    map.put("indicator", indicatorDefinition.getCode());
    map.put("rangeMin", rangeDefinition!=null?rangeDefinition.getMin():0);
    map.put("rangeMax", rangeDefinition!=null?rangeDefinition.getMax():1);
    map.put("referenceMin", referenceDefinition!=null?referenceDefinition.getMin():0);
    map.put("referenceMax", referenceDefinition!=null?referenceDefinition.getMax():1);
    map.put("gauge", viewDefinition.getGauge().toString());
    map.put("mode", mode!=null?mode.toString():ModeEnumeration.VERTICAL);
    map.put("scale", this.definition.getScale().toString());
    
    return block("region$view", map); 
  }
  
  private int getLayoutSize() {
    HashMap<PositionEnumeration, Integer> sizes = this.definition.getRegionSizes();
    int maxSize = 0;
    
    for (int size : sizes.values())
      if (size > maxSize) maxSize = size;
    
    return maxSize;
  }
	*/
}