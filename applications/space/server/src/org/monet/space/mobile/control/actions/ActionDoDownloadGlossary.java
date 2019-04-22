package org.monet.space.mobile.control.actions;

import org.monet.metamodel.SourceDefinition;
import org.monet.mobile.service.requests.DownloadGlossaryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.utils.StreamHelper;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ActionDoDownloadGlossary extends AuthenticatedAction {

	@Override
	public void onExecute(org.monet.http.Request httpRequest, org.monet.http.Response httpResponse) throws Exception {
		DownloadGlossaryRequest request = this.getRequest(httpRequest, DownloadGlossaryRequest.class);

		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		String glossaryCode = request.Code;
		String glossaryContext = request.Context;

 		XmlSerializer serializer = XmlPullParserFactory.newInstance().newSerializer();
		serializer.setOutput(httpResponse.getOutputStream(), "UTF-8");

		try {
			SourceList sourceList = sourceLayer.loadSourceList(glossaryCode, glossaryContext);
			if (sourceList.getCount() > 0) {
				Source<SourceDefinition> source = sourceList.iterator().next();
				TermList terms = sourceLayer.loadSourceTerms(source, false);

				serializeToXML(serializer, terms);
			}
		} finally {
			serializer.flush();
			StreamHelper.close(httpResponse.getOutputStream());
		}

		this.writeResponse(httpResponse, "application/vnd.monet.glossary", null, "glossary_" + glossaryCode + "_" + glossaryContext);
	}

	public void serializeToXML(XmlSerializer serializer, TermList termList) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "term-list");

		for (Term term : termList.getAll())
			serializeToXML(serializer, term);

		serializer.endTag("", "term-list");
	}

	public void serializeToXML(XmlSerializer serializer, Term term) throws IllegalArgumentException, IllegalStateException, IOException {
		if (term.getCode() == null)
			return;
		serializer.startTag("", "term");
		serializer.attribute("", "code", term.getCode());
		if (term.getParent() != null)
			serializer.attribute("", "parent", term.getParent());
		serializer.attribute("", "type", String.valueOf(term.getType()));
		serializer.attribute("", "tags", SerializerData.serializeSet(term.getTags()));
		serializer.attribute("", "is-enable", Boolean.toString(term.isEnabled()));
		serializer.attribute("", "is-new", Boolean.toString(term.isNew()));
		serializer.attribute("", "is-leaf", Boolean.toString(term.isLeaf()));
		serializer.attribute("", "flatten", term.getFlattenLabel() != null ? term.getFlattenLabel() : "");
		serializer.text(term.getLabel() != null ? term.getLabel() : "");
		serializer.endTag("", "term");
	}

}
