package client.core.system;

import client.core.model.List;
import client.core.model.Node;

import static client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition;

public class NodeIndexEntry extends IndexEntry<Node> implements client.core.model.NodeIndexEntry {
	private boolean geoReferenced;
	private client.core.model.NodeIndexEntry.Attribute title;
	private client.core.model.NodeIndexEntry.Attribute picture;
	private client.core.model.NodeIndexEntry.Attribute icon;
	private client.core.model.List<client.core.model.NodeIndexEntry.Attribute> highlights = new MonetList<>();
	private client.core.model.List<client.core.model.NodeIndexEntry.Attribute> lines = new MonetList<>();
	private client.core.model.List<client.core.model.NodeIndexEntry.Attribute> linesBelow = new MonetList<>();
	private client.core.model.List<client.core.model.NodeIndexEntry.Attribute> footers = new MonetList<>();

	@Override
	public boolean isGeoReferenced() {
		return geoReferenced;
	}

	@Override
	public void setGeoReferenced(boolean geoReferenced) {
		this.geoReferenced = geoReferenced;
	}

	@Override
	public List<client.core.model.NodeIndexEntry.Attribute> getAttributes() {
		List<client.core.model.NodeIndexEntry.Attribute> attributes = new MonetList<>();
		if (getTitle() != null) attributes.add(getTitle());
		if (getPicture() != null) attributes.add(getPicture());
		if (getIcon() != null) attributes.add(getIcon());
		if (getHighlights() != null) attributes.addAll(getHighlights());
		if (getLines() != null) attributes.addAll(getLines());
		if (getLinesBelow() != null) attributes.addAll(getLinesBelow());
		if (getFooters() != null) attributes.addAll(getFooters());
		return attributes;

	}

	@Override
	public client.core.model.NodeIndexEntry.Attribute getTitle() {
		return this.title;
	}

	public void setTitle(client.core.model.NodeIndexEntry.Attribute title) {
		this.title = title;
	}

	@Override
	public client.core.model.NodeIndexEntry.Attribute getPicture() {
		return this.picture;
	}

	public void setPicture(client.core.model.NodeIndexEntry.Attribute picture) {
		this.picture = picture;
	}

	@Override
	public client.core.model.NodeIndexEntry.Attribute getIcon() {
		return this.icon;
	}

	public void setIcon(client.core.model.NodeIndexEntry.Attribute icon) {
		this.icon = icon;
	}

	@Override
	public client.core.model.List<client.core.model.NodeIndexEntry.Attribute> getHighlights() {
		return this.highlights;
	}

	public void setHighlights(client.core.model.List<client.core.model.NodeIndexEntry.Attribute> highlights) {
		this.highlights = highlights;
	}

	@Override
	public client.core.model.List<client.core.model.NodeIndexEntry.Attribute> getLines() {
		return this.lines;
	}

	public void setLines(client.core.model.List<client.core.model.NodeIndexEntry.Attribute> lines) {
		this.lines = lines;
	}

	@Override
	public client.core.model.List<client.core.model.NodeIndexEntry.Attribute> getLinesBelow() {
		return this.linesBelow;
	}

	public void setLinesBelow(client.core.model.List<client.core.model.NodeIndexEntry.Attribute> linesBelow) {
		this.linesBelow = linesBelow;
	}

	@Override
	public client.core.model.List<client.core.model.NodeIndexEntry.Attribute> getFooters() {
		return this.footers;
	}

	public void setFooters(client.core.model.List<client.core.model.NodeIndexEntry.Attribute> footers) {
		this.footers = footers;
	}

	@Override
	public final client.core.model.types.Link toLink() {
		return typeFactory.createLink(this.getEntity().getId(), this.getLabel());
	}

	public static class Attribute implements client.core.model.NodeIndexEntry.Attribute {
		private String code;
		private String value;
		private AttributeDefinition definition;
		private client.core.model.NodeIndexEntry.Operation link;

		public Attribute() {
		}

		public Attribute(String value) {
			this.value = value;
		}

		@Override
		public String getCode() {
			return this.code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Override
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public AttributeDefinition getDefinition() {
			return this.definition;
		}

		public void setDefinition(AttributeDefinition definition) {
			this.definition = definition;
		}

		@Override
		public boolean is(AttributeDefinition.Type type) {
			return getDefinition() != null && getDefinition().is(type);
		}

		@Override
		public client.core.model.NodeIndexEntry.Operation getLink() {
			return this.link;
		}

		public void setLink(client.core.model.NodeIndexEntry.Operation link) {
			this.link = link;
		}

	}

	public static class Operation implements client.core.model.NodeIndexEntry.Operation {
	}
}
