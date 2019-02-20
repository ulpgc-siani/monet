package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.LoadNewGlossariesRequest;
import org.monet.mobile.service.results.DownloadGlossariesResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class ActionDoLoadNewGlossaries extends AuthenticatedTypedAction<LoadNewGlossariesRequest, DownloadGlossariesResult> {

	SourceLayer sourceLayer;

	public ActionDoLoadNewGlossaries() {
		super(LoadNewGlossariesRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadGlossariesResult onExecute(LoadNewGlossariesRequest request) throws ActionException {
		DownloadGlossariesResult result = new DownloadGlossariesResult();

		Dictionary dictionary = Dictionary.getInstance();

		HashSet<String> usedThesaurus = new HashSet<String>();
		for (org.monet.metamodel.JobDefinition definition : dictionary.getJobDefinitions()) {
			for (String name : definition.getUsedThesaurus())
				usedThesaurus.add(dictionary.getDefinitionCode(name));
		}

		HashMap<String, ArrayList<String>> glossariesMap = new HashMap<String, ArrayList<String>>();

		for (Source<?> source : this.sourceLayer.loadSourceList()) {
			String code = source.getCode();
			if (!usedThesaurus.contains(code))
				continue;

			if (source.getUpdateDate().getTime() <= request.SyncMark)
				continue;

			String partnerContext = source.getPartnerName();
			if (BusinessUnit.getInstance().getName().equals(partnerContext))
				partnerContext = null;
			ArrayList<String> contexts = glossariesMap.get(code);
			if (contexts == null) {
				contexts = new ArrayList<>();
				glossariesMap.put(code, contexts);
			}
			contexts.add(partnerContext);
		}

		HashMap<String, String[]> glossaries_ = new HashMap<String, String[]>();
		for (Entry<String, ArrayList<String>> entry : glossariesMap.entrySet()) {
			glossaries_.put(entry.getKey(), entry.getValue().toArray(new String[]{}));
		}
		result.Glossaries = glossaries_;
		result.SyncMark = (new Date()).getTime();

		return result;
	}

}
