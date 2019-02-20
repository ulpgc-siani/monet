package org.monet.metamodel;

import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.IsInitiable;

import java.util.HashMap;

// DocumentDefinition
// Declaración que se utiliza para modelar un documento

public class DocumentDefinition extends DocumentDefinitionBase implements IsInitiable, HasMappings {
	private HashMap<String, SignaturesProperty.SignatureProperty> signaturesMap = new HashMap<String, SignaturesProperty.SignatureProperty>();

	public void init() {
		if (_signaturesProperty != null) {
			for (SignaturesProperty.SignatureProperty signature : _signaturesProperty.getSignatureMap().values()) {
				this.signaturesMap.put(signature.getCode(), signature);
				if (signature.getName() != null)
					this.signaturesMap.put(signature.getName(), signature);
			}
		}
	}

	public SignaturesProperty.SignatureProperty getSignature(String key) {
		return this.signaturesMap.get(key);
	}

	@Override
	public Class<?> getMappingClass(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
