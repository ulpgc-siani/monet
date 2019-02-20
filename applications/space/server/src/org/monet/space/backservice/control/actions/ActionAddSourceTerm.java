package org.monet.space.backservice.control.actions;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.Term;

import java.util.Date;

public class ActionAddSourceTerm extends Action {

	public ActionAddSourceTerm() {
	}

	@Override
	public String execute() {
		String code = (String) this.parameters.get(Parameter.CODE);
		String parentCode = (String) this.parameters.get(Parameter.PARENT);
		int type = Integer.valueOf((String) this.parameters.get(Parameter.TYPE));
		String sourceId = (String) this.parameters.get(Parameter.SOURCE);
		String label = (String) this.parameters.get(Parameter.LABEL);
		String tagsValues = (String) this.parameters.get(Parameter.TAGS);

		if (code == null || label == null || sourceId == null)
			return ErrorCode.WRONG_PARAMETERS;

		Term term = new Term();
		term.setCode(code);
		term.setType(type);
		term.setLabel(LibraryEncoding.decode(label));
		term.setEnabled(true);
		term.setNew(true);

		String[] tagsArray = LibraryEncoding.decode(tagsValues).split(PARAMETER_SEPARATOR);
		for (String tagValue : tagsArray)
			term.addTag(tagValue);

		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		Source<SourceDefinition> source = sourceLayer.loadSource(sourceId);
		sourceLayer.addSourceTerm(source, term, parentCode);
		source.setUpdateDate(new Date());
		sourceLayer.saveSource(source);
		sourceLayer.publishSourceTerms(source, new String[] { term.getCode() });

		return MessageCode.SOURCE_TERM_ADDED;
	}

}
