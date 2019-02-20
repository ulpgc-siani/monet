package org.monet.space.backservice.control.actions;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.Term;

public class ActionGetSourceParentTerm extends Action {

	public ActionGetSourceParentTerm() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String code = (String) this.parameters.get(Parameter.CODE);

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		Source<SourceDefinition> source = sourceLayer.loadSource(id);

		Term term = sourceLayer.loadSourceTerm(source, code);
		if (term.getParent() == null)
			return "";

		Term parentTerm = sourceLayer.loadSourceTerm(source, term.getParent());
		if (parentTerm == null)
			return "";

		return parentTerm.serializeToXML();
	}

}
