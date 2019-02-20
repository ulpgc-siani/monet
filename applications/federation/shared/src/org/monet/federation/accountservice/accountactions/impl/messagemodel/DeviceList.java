package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Root(name = "devicelist")
public class DeviceList implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Serializer persister = new Persister();

	@ElementList(inline = true, required = false)
	private List<Device> items = new ArrayList<Device>();
	@Attribute(name = "totalCount")
	private int totalCount;

	public List<Device> getAll() {
		return this.items;
	}

	public void add(Device device) {
		this.items.add(device);
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<String> getUsers() {
		ArrayList<String> result = new ArrayList<>();

		for (Device device : this.items)
			if (!result.contains(device.getUserId()))
				result.add(device.getUserId());

		return result;
	}

	public List<String> getDevices(String userId) {
		ArrayList<String> result = new ArrayList<>();

		for (Device device : this.items)
			if (device.getUserId().equals(userId) && !result.contains(device.getId()))
				result.add(device.getId());

		return result;
	}

	public List<String> getDevices() {
		ArrayList<String> result = new ArrayList<>();

		for (Device device : this.items)
			result.add(device.getId());

		return result;
	}

	public String serialize() throws Exception {
		StringWriter writer = new StringWriter();
		persister.write(this, writer);
		return writer.toString();
	}
}
