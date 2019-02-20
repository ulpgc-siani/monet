/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.space.kernel.model.wrappers;

import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

import java.util.*;

public class NodeDocumentWrapper {
	private Node node;

	public static final String SIGNATURE_ID_PATTERN_SEPARATOR = "_:_";
	public static final String SIGNATURE_ID_PATTERN = "%s" + SIGNATURE_ID_PATTERN_SEPARATOR + "%s";
	public static final String SIGNATURE_CODE_PREFIX = "signature";
	public static final String SIGNATURE_CODE = SIGNATURE_CODE_PREFIX + "%d";

	public NodeDocumentWrapper(Node node) {
		this.node = node;
	}

	public static final String getSignatureCodeFromId(String signatureId) {
		if (!signatureId.contains(SIGNATURE_ID_PATTERN_SEPARATOR))
			return signatureId;

		return signatureId.substring(0, signatureId.indexOf("_:_"));
	}

	public static final int getSignaturePositionFromId(String signatureId) {
		if (!signatureId.contains(SIGNATURE_ID_PATTERN_SEPARATOR))
			return 0;

		return Integer.valueOf(signatureId.substring(signatureId.indexOf(SIGNATURE_ID_PATTERN_SEPARATOR) + SIGNATURE_ID_PATTERN_SEPARATOR.length()));
	}

	public int getCountSignaturesOfType(DocumentDefinitionBase.SignaturesProperty.SignatureProperty signatureDefinition) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES_COUNT);
		String count = attribute.getIndicatorValue(signatureDefinition.getCode());
		int result;

		if (count.isEmpty())
			result = (signatureDefinition.getAmount() != null) ? signatureDefinition.getAmount().intValue() : 1;
		else
			result = Integer.valueOf(count);

		return result;
	}

	public Map<String, Integer> getCountSignatures() {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES_COUNT);
		Map<String, Integer> result = new HashMap<>();
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		Map<String, DocumentDefinitionBase.SignaturesProperty.SignatureProperty> signaturesDefinitionMap = new HashMap<>();

		if (definition.getSignatures() != null)
			signaturesDefinitionMap = definition.getSignatures().getSignatureMap();

		for (String signature : signaturesDefinitionMap.keySet()) {
			DocumentDefinitionBase.SignaturesProperty.SignatureProperty signatureDefinition = signaturesDefinitionMap.get(signature);
			Indicator indicator = attribute.getIndicator(signatureDefinition.getCode());
			int count;

			if (indicator != null)
				count = Integer.valueOf(indicator.getData());
			else
				count = (signatureDefinition.getAmount() != null) ? signatureDefinition.getAmount().intValue() : 1;

			result.put(signatureDefinition.getCode(), count);
		}

		return result;
	}

	public void setCountSignaturesOfType(String signatureCode, int count) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES_COUNT);
		attribute.addOrSetIndicatorValue(signatureCode, 0, String.valueOf(count));
	}

	public Map<String, Map<Integer, SignatureUserRestriction>> getSignatureUserRestrictions() {
		Map<String, Map<Integer, SignatureUserRestriction>> result = new HashMap<>();
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES);

		for (final Attribute signatureAttribute : attribute.getAttributeList())
			result.put(signatureAttribute.getCode(), getSignaturesUserRestrictionsWithSameCode(signatureAttribute));

		return result;
	}

	public Map<Integer, SignatureUserRestriction> getSignaturesUserRestrictionsWithSameCode(Attribute attribute) {
		Map<Integer, SignatureUserRestriction> result = new HashMap<>();

		for (final Attribute signatureAttribute : attribute.getAttributeList()) {
			int signaturePosition = Integer.valueOf(signatureAttribute.getCode().replace(SIGNATURE_CODE_PREFIX, ""));

			result.put(signaturePosition, new SignatureUserRestriction() {
				@Override
				public String getCode() {
					return signatureAttribute.getCode();
				}

				@Override
				public List<String> getUserIds() {
					List<String> result = new ArrayList<>();

					for (Attribute userAttribute : signatureAttribute.getAttributeList())
						result.add(userAttribute.getIndicatorValue(Indicator.CODE));

					return result;
				}

				@Override
				public List<String> getUserFullNames() {
					List<String> result = new ArrayList<>();

					for (Attribute userAttribute : signatureAttribute.getAttributeList())
						result.add(userAttribute.getIndicatorValue(Indicator.VALUE));

					return result;
				}
			});
		}

		return result;
	}

	public void clearSignatureUsersRestrictions(String signature, int signaturePos) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES);
		Attribute signatureAttributes = node.createAttribute(attribute.getAttributeList(), signature);
		Attribute signatureAttribute = signatureAttributes.getAttributeList().get(String.format(SIGNATURE_CODE, signaturePos));

		if (signatureAttribute == null)
			return;

		signatureAttribute.getAttributeList().clear();
	}

	public void addSignatureUserRestriction(String signature, int signaturePos, User user) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES);
		Attribute signatureAttributes = node.createAttribute(attribute.getAttributeList(), signature);
		Attribute signatureAttribute = signatureAttributes.getAttributeList().get(String.format(SIGNATURE_CODE, signaturePos));

		if (signatureAttribute == null) {
			signatureAttribute = node.createAttribute(signatureAttributes.getAttributeList(), signature);
			signatureAttribute.setCode(String.format(SIGNATURE_CODE, signaturePos));
		}

		Attribute newSignature = new Attribute();
		newSignature.addOrSetIndicatorValue(Indicator.CODE, 0, user != null ? user.getId() : "-1");
		newSignature.addOrSetIndicatorValue(Indicator.VALUE, 0, user != null ? user.getInfo().getFullname() : "");

		signatureAttribute.getAttributeList().add(newSignature);
	}

	public void addSignature(Node node, String signatureCode, int signaturePosition, String label, String reason, String location, String contact, Date date) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), signatureCode);
		Attribute signatureAttribute = new Attribute();

		signatureAttribute.setCode(String.format(SIGNATURE_CODE, signaturePosition));
		signatureAttribute.addOrSetIndicatorValue(Indicator.CODE, -1, signatureCode);
		signatureAttribute.addOrSetIndicatorValue(Indicator.CODE, -1, signatureCode);
		signatureAttribute.addOrSetIndicatorValue(Indicator.VALUE, -1, label);
		signatureAttribute.addOrSetIndicatorValue(Indicator.VALID, -1, "0");
		signatureAttribute.addOrSetIndicatorValue(Indicator.REASON, -1, reason);
		signatureAttribute.addOrSetIndicatorValue(Indicator.LOCATION, -1, location);
		signatureAttribute.addOrSetIndicatorValue(Indicator.CONTACT, -1, contact);
		signatureAttribute.addOrSetIndicatorValue(Indicator.DETAILS, -1, "");
		signatureAttribute.addOrSetIndicatorValue(Indicator.DATE, -1, String.valueOf(date.getTime()));

		attribute.getAttributeList().add(signatureAttribute);
	}

	public void deleteSignature(Node node, String signatureCode, int signaturePosition) {
		Attribute attribute = node.getAttributeList().get(signatureCode);
		attribute.getAttributeList().delete(signaturePosition);
	}

	public Signature getSignature(final String signatureCode, final int signaturePosition) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), signatureCode);
		final Attribute signatureAttribute = attribute.getAttributeList().get(String.format(SIGNATURE_CODE, signaturePosition));

		if (signatureAttribute == null)
			return null;

		return new Signature() {
			@Override
			public String getCode() {
				return signatureCode;
			}

			@Override
			public int getPosition() {
				return signaturePosition;
			}

			@Override
			public String getId() {
				return String.format(SIGNATURE_ID_PATTERN, signatureCode, signaturePosition);
			}

			@Override
			public boolean isValid() {
				String value = signatureAttribute.getIndicatorValue(Indicator.VALID);
				return value.equals("1");
			}

			@Override
			public String getReason() {
				return signatureAttribute.getIndicatorValue(Indicator.REASON);
			}

			@Override
			public String getLocation() {
				return signatureAttribute.getIndicatorValue(Indicator.LOCATION);
			}

			@Override
			public String getContact() {
				return signatureAttribute.getIndicatorValue(Indicator.CONTACT);
			}

			@Override
			public String getDetails() {
				return signatureAttribute.getIndicatorValue(Indicator.DETAILS);
			}

			@Override
			public Long getDate() {
				return Long.valueOf(signatureAttribute.getIndicatorValue(Indicator.DATE));
			}
		};
	}

	public boolean isSignatureMade(final String signatureCode, final int signaturePosition) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), signatureCode);
		return attribute.getAttributeList().get(String.format(SIGNATURE_CODE, signaturePosition)) != null;
	}

	public boolean isSignatureDisabled(String signatureCode, int signaturePosition) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.SIGNATURES);
		Attribute signatureAttributes = node.createAttribute(attribute.getAttributeList(), signatureCode);
		Map<Integer, SignatureUserRestriction> userRestrictions = getSignaturesUserRestrictionsWithSameCode(signatureAttributes);

		if (!userRestrictions.containsKey(signaturePosition))
			return false;

		List<String> userIds = userRestrictions.get(signaturePosition).getUserIds();
		return userIds.size() == 1 && userIds.get(0).equals("-1");
	}

	public void markSignatureAsValid(String signature, int signaturePosition, String details) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), signature);
		Attribute signatureAttribute = attribute.getAttributeList().get(String.format(SIGNATURE_CODE, signaturePosition));

		signatureAttribute.getIndicatorList().get(Indicator.VALID).setData("1");
		signatureAttribute.getIndicatorList().get(Indicator.DETAILS).setData(details);
	}

	public interface Signature {
		String getCode();
		int getPosition();
		String getId();
		boolean isValid();
		String getReason();
		String getLocation();
		String getContact();
		String getDetails();
		Long getDate();
	}

	public interface SignatureUserRestriction {
		String getCode();
		List<String> getUserIds();
		List<String> getUserFullNames();
	}

}
