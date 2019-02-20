package org.monet.v2;

import org.monet.bpi.BPISchema;
import org.monet.bpi.java.BPIFieldSectionImpl;
import org.monet.bpi.java.locator.AnnotationBuilder;
import org.monet.bpi.java.locator.AnnotationScanner;
import org.monet.bpi.java.locator.PackageReader;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.model.Dictionary;

import java.util.HashMap;

public class BPIClassLocator {
	private final String businessModelClassesDir;
	private final Dictionary dictionary;

	private AnnotationScanner annotationScanner = null;
	private HashMap<String, Class<?>> definitionClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> schemaClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> operationClassCache = new HashMap<String, Class<?>>();

	private HashMap<String, Class<?>> sectionClassCache = new HashMap<String, Class<?>>();

	private HashMap<String, Class<?>> worklineClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> worklockClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> worklockFormClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> worklockServiceClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> workplaceClassCache = new HashMap<String, Class<?>>();
	private HashMap<String, Class<?>> workstopClassCache = new HashMap<String, Class<?>>();

	public BPIClassLocator(String businessModelClassesDir, Dictionary dictionary) {
		this.businessModelClassesDir = businessModelClassesDir;
		this.dictionary = dictionary;
		this.reset();
	}

	@SuppressWarnings("unchecked")
	public synchronized <T> T getDefinitionInstance(String name) {
		T classInstance = null;
		Class<?> definitionClass = this.definitionClassCache.get(name);
		if (definitionClass == null) {
			definitionClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildDefinition(name));
			if (definitionClass == null)
				return null;
			this.definitionClassCache.put(name, definitionClass);
		}
		try {
			classInstance = (T) definitionClass.newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		}
		return classInstance;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public synchronized <T extends BPISchema> T getSchemaInstance(String name) {
		T classInstance = null;
		Class<?> definitionClass = (Class) this.getSchemaClass(name);
		try {
			classInstance = (T) definitionClass.newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		}
		return classInstance;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getSchemaClass(String name) {
		Class<?> schemaClass = this.schemaClassCache.get(name);
		if (schemaClass == null) {
			schemaClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildSchema(name));
			if (schemaClass == null)
				return null;
			this.schemaClassCache.put(name, schemaClass);
		}
		return (Class<T>) schemaClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPIFieldSectionImpl> T getSectionInstance(String definition, String section) {
		String key = definition + "_" + section;
		T classInstance = null;
		Class<?> sectionClass = this.sectionClassCache.get(key);
		if (sectionClass == null) {
			Definition def = this.dictionary.getDefinition(definition);
			do {
				sectionClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildSection(def.getName(), section));
				if (sectionClass == null && def.getExtends() != null)
					def = this.dictionary.getDefinition(def.getExtends());
				else
					break;
			} while (sectionClass == null);
			if (sectionClass == null)
				return null;
			this.sectionClassCache.put(key, sectionClass);
		}

		try {
			classInstance = (T) sectionClass.newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		}
		return classInstance;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getOperationClass(String name) {
		Class<?> operationClass = this.operationClassCache.get(name);
		if (operationClass == null) {
			operationClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildOperation(name));
			if (operationClass == null)
				return null;
			this.operationClassCache.put(name, operationClass);
		}
		return (Class<T>) operationClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getWorkLineClass(String name) {
		Class<?> worklineClass = this.worklineClassCache.get(name);
		if (worklineClass == null) {
			worklineClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildWorkLine(name));
			if (worklineClass == null)
				return null;
			this.worklineClassCache.put(name, worklineClass);
		}
		return (Class<T>) worklineClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getWorkLockClass(String name) {
		Class<?> worklockClass = this.worklockClassCache.get(name);
		if (worklockClass == null) {
			worklockClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildWorkLock(name));
			if (worklockClass == null)
				return null;
			this.worklockClassCache.put(name, worklockClass);
		}
		return (Class<T>) worklockClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getWorkLockFormClass(String name) {
		Class<?> worklockFormClass = this.worklockFormClassCache.get(name);
		if (worklockFormClass == null) {
			worklockFormClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildWorkLockForm(name));
			if (worklockFormClass == null)
				return null;
			this.worklockFormClassCache.put(name, worklockFormClass);
		}
		return (Class<T>) worklockFormClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getWorkLockServiceClass(String name) {
		Class<?> worklockServiceClass = this.worklockServiceClassCache.get(name);
		if (worklockServiceClass == null) {
			worklockServiceClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildWorkLockService(name));
			if (worklockServiceClass == null)
				return null;
			this.worklockServiceClassCache.put(name, worklockServiceClass);
		}
		return (Class<T>) worklockServiceClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getWorkPlaceClass(String name) {
		Class<?> workplaceClass = this.workplaceClassCache.get(name);
		if (workplaceClass == null) {
			workplaceClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildWorkPlace(name));
			if (workplaceClass == null)
				return null;
			this.workplaceClassCache.put(name, workplaceClass);
		}
		return (Class<T>) workplaceClass;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends BPISchema> Class<T> getWorkStopClass(String name) {
		Class<?> workstopClass = this.workstopClassCache.get(name);
		if (workstopClass == null) {
			workstopClass = this.annotationScanner.getClassAnnotatedWith(AnnotationBuilder.buildWorkStop(name));
			if (workstopClass == null)
				return null;
			this.workstopClassCache.put(name, workstopClass);
		}
		return (Class<T>) workstopClass;
	}

	public synchronized void reset() {
		PackageReader reader = new PackageReader(this.businessModelClassesDir);
		this.annotationScanner = new AnnotationScanner(reader);
		this.annotationScanner.injectBusinessModelClassesDir(businessModelClassesDir);

		this.definitionClassCache.clear();
		this.schemaClassCache.clear();
		this.operationClassCache.clear();

		this.sectionClassCache.clear();

		this.worklineClassCache.clear();
		this.worklockClassCache.clear();
		this.workplaceClassCache.clear();
		this.workstopClassCache.clear();
	}

}
