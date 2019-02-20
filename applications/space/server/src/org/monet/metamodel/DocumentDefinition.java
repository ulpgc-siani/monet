package org.monet.metamodel;

import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.IsInitiable;

import java.util.HashMap;

// DocumentDefinition
// Declaración que se utiliza para modelar un documento

public class DocumentDefinition extends DocumentDefinitionBase implements IsInitiable, HasMappings {
	private HashMap<String, SignatureProperty> signaturesMap = new HashMap<String, SignatureProperty>();

	public void init() {
		if (_signaturesProperty != null) {
			for (SignatureProperty signature : _signaturesProperty.getSignatureMap().values()) {
				this.signaturesMap.put(signature.getCode(), signature);
				if (signature.getName() != null)
					this.signaturesMap.put(signature.getName(), signature);
			}
		}
	}

	public SignatureProperty getSignature(String key) {
		return this.signaturesMap.get(key);
	}

	@Override
	public Class<?> getMappingClass(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
