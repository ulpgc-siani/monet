package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.LogBookNode;
import org.monet.space.kernel.model.LogHistory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionSearchEvent extends Action {

	public ActionSearchEvent() {
	}

	@Override
	public String execute() {
		String type = (String) this.parameters.get(Parameter.TYPE);
		String fromValue = (String) this.parameters.get(Parameter.FROM);
		String toValue = (String) this.parameters.get(Parameter.TO);
		String result;
		LogBookNode logBookNode;
		LogHistory logHistory;
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
		Date from = null, to = null;

		try {
			if (type == null) return ErrorCode.WRONG_PARAMETERS;
			if ((fromValue != null) && (!fromValue.equals(Strings.EMPTY))) from = dateFormat.parse(fromValue);
			if ((toValue != null) && (!toValue.equals(Strings.EMPTY))) to = dateFormat.parse(toValue);
		} catch (ParseException oException) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		logBookNode = ComponentPersistence.getInstance().getNodeLayer().loadNodeBook();
		logHistory = new LogHistory();
		logHistory.setEntryList(logBookNode.search(Integer.valueOf(type), from, to));

		result = logHistory.serializeToXML(true).toString();

		return result;
	}

}
