package org.monet.bpi.java.locator;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationScanner {

  private PackageReader reader;
  private boolean       areClassnamesLoaded;
  private List<String>  classnames;
	private String businessModelClassesDir;

	public AnnotationScanner(PackageReader reader) {
    this.reader = reader;
    this.areClassnamesLoaded = false;
  }

  public Class<?> getClassAnnotatedWith(Annotation expectedAnnotation) {

    try {
      if (!areClassnamesLoaded)
        this.classnames = this.reader.read();
      synchronized (this.classnames) {
        for (String classname : this.classnames) {
          try {
            if(classname.indexOf("$") != classname.lastIndexOf("$"))
              continue;
            Class<?> clazz = Class.forName(classname/*, true, new BusinessModelClassLoader(this.businessModelClassesDir)*/);

            for (Annotation annotation : clazz.getAnnotations()) {
              if (expectedAnnotation.equals(annotation)) {
                return clazz;
              }
            }
          } catch (ClassNotFoundException ex) {
            return null;
          } catch (Error ex) {
	          return null;
          }
        }
      }
    } catch (Exception e) {
	    return null;
    }
    return null;
  }

	public void injectBusinessModelClassesDir(String businessModelClassesDir) {
		this.businessModelClassesDir = businessModelClassesDir;
	}
}
