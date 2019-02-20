package client.services.http.builders.types;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.types.Number;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import client.services.http.builders.EntityBuilder;

public class NumberBuilder<Type extends java.lang.Number> implements Builder<client.core.model.types.Number<Type>, List<client.core.model.types.Number<Type>>> {
	@Override
	public client.core.model.types.Number build(HttpInstance instance) {
		if (instance == null)
			return null;

		Number number = new Number();
		initialize(number, instance);
		return number;
	}

	@Override
	public void initialize(client.core.model.types.Number object, HttpInstance instance) {
		Number number = (Number)object;
		number.setValue(getValue(instance.getString("value")));
		number.setTypeFactory(new EntityBuilder<>());
	}

	private Type getValue(String value) {
		Object result = null;

		try {
			result = Double.parseDouble(value);
		} catch(NumberFormatException e) {
			try {
				result = Integer.parseInt(value);
			}
			catch (NumberFormatException e2) {
				return null;
			}
		}

		return (Type)result;
	}

	@Override
	public List<client.core.model.types.Number<Type>> buildList(HttpList instance) {
		return new MonetList<>();
	}

}
