package client.services.http.builders.types;

import client.core.model.types.CompositeCheck;
import client.core.system.types.Check;
import client.core.system.types.CheckCategory;
import client.core.system.types.CheckList;
import client.core.system.types.SuperCheck;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

import java.util.HashMap;
import java.util.Map;

public class CheckBuilder implements Builder<client.core.model.types.Check, client.core.model.types.CheckList> {

	private final Map<String, Builder> builders = new HashMap<>();

	public CheckBuilder() {
		builders.put("category", new CheckBuilder.Builder() {
			@Override
			public client.core.model.types.Check build(HttpInstance instance) {
				return buildCategory(instance);
			}
		});
		builders.put("super_check", new CheckBuilder.Builder() {
			@Override
			public client.core.model.types.Check build(HttpInstance instance) {
				return buildSuperCheck(instance);
			}
		});
		builders.put("check", new CheckBuilder.Builder() {
			@Override
			public client.core.model.types.Check build(HttpInstance instance) {
				return buildCheck(instance);
			}
		});
	}

	@Override
	public client.core.model.types.Check build(HttpInstance instance) {
		if (instance == null || (instance.getString("value").isEmpty() && instance.getString("label").isEmpty()))
			return null;

		if (builders.containsKey(instance.getString("type")))
			return builders.get(instance.getString("type")).build(instance);
		return buildCheck(instance);
	}

	@Override
	public void initialize(client.core.model.types.Check object, HttpInstance instance) {
		Check check = (Check)object;
		check.setValue(instance.getString("value"));
		check.setLabel(instance.getString("label"));
		check.setChecked(instance.getBoolean("checked"));
	}

	@Override
	public client.core.model.types.CheckList buildList(HttpList instances) {
		CheckList result = new CheckList();
		result.setTotalCount(instances.getTotalCount());

		for (int i = 0; i < instances.getItems().length(); i++)
			result.add(build(instances.getItems().get(i)));

		return result;
	}

	private client.core.model.types.Check buildCheck(HttpInstance instance) {
		Check check = new Check();
		initialize(check, instance);
		return check;
	}

	private client.core.model.types.Check buildSuperCheck(HttpInstance instance) {
		client.core.model.types.CompositeCheck check = new SuperCheck();
		initializeCompositeCheck(instance, check);
		return check;
	}

	private client.core.model.types.CompositeCheck buildCategory(HttpInstance instance) {
		client.core.model.types.CompositeCheck category = new CheckCategory();
		initializeCompositeCheck(instance, category);
		return category;
	}

	private void initializeCompositeCheck(HttpInstance instance, CompositeCheck compositeCheck) {
		initialize(compositeCheck, instance);
		compositeCheck.setChecks(buildList(instance.getList("terms")));
	}

	private interface Builder {
		client.core.model.types.Check build(HttpInstance instance);
	}
}
