package org.monet.space.kernel.model;


import com.vividsolutions.jts.geom.Polygon;

import java.util.List;

public class NodeDataRequest extends DataRequest {
	private String codeView;
	private String[] codeNodes;
	private String codeReference;
	private String codeDomainNode;
	private String codeSourceNode;
	private Polygon boundingBox;
	private String locationId;
	private List<String> nodeIds;

	public NodeDataRequest() {
		this.setCodeView(null);
		this.setCodeNodes(null);
		this.setCodeReference(null);
		this.setCodeDomainNode(null);
		this.setCodeSourceNode(null);
		this.setBoundingBox(null);
		this.setLocationId(null);
		this.setNodeIds(null);
	}

	public String getCodeView() {
		return codeView;
	}

	public void setCodeView(String codeView) {
		this.codeView = codeView;
	}

	public String[] getCodeNodes() {
		return codeNodes;
	}

	public void setCodeNodes(String[] codeNodes) {
		this.codeNodes = codeNodes;
	}

	public String getCodeReference() {
		return codeReference;
	}

	public void setCodeReference(String codeReference) {
		this.codeReference = codeReference;
	}

	public String getCodeDomainNode() {
		return codeDomainNode;
	}

	public void setCodeDomainNode(String codeDomainNode) {
		this.codeDomainNode = codeDomainNode;
	}

	public String getCodeSourceNode() {
		return codeSourceNode;
	}

	public void setCodeSourceNode(String codeSourceNode) {
		this.codeSourceNode = codeSourceNode;
	}

	public Polygon getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Polygon boundingBox) {
		this.boundingBox = boundingBox;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public List<String> getNodeIds() {
		return nodeIds;
	}

	public void setNodeIds(List<String> nodeIds) {
		this.nodeIds = nodeIds;
	}
}
