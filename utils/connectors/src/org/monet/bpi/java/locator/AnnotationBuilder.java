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
package org.monet.bpi.java.locator;

import org.monet.bpi.java.annotations.*;

import java.lang.annotation.Annotation;

public class AnnotationBuilder {

	public static Definition buildDefinition(final String name) {
		Definition definition = new Definition() {

			@Override
			public Class<? extends Annotation> annotationType() {				
				return Definition.class;
			}

			@Override
			public String name() {
				return name;				
			}
			
			@Override
			public boolean equals(Object object) {
				if (!(object instanceof Definition)) return false;
				if (this == object) return true;
				return equals((Definition) object);
			}
			
			private boolean equals(Definition definition) {
				return definition.name().equals(this.name());
			}
			
		};
							
		return definition;
	}
	
	 public static DefinitionSchema buildSchema(final String name) {
	   DefinitionSchema definitionSchema = new DefinitionSchema() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return DefinitionSchema.class;
      }

      @Override
      public String name() {
        return name;        
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof DefinitionSchema)) return false;
        if (this == object) return true;
        return equals((DefinitionSchema) object);
      }
      
      private boolean equals(DefinitionSchema definitionSchema) {
        return definitionSchema.name().equals(this.name());
      }
            
    };
              
    return definitionSchema;
  }

  public static Section buildSection(final String definition, final String section) {
    Section sectionDefinition = new Section() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return Section.class;
      }

      @Override
      public String definition() {
        return definition;
      }
      
      @Override
      public String name() {
        return section;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof Section)) return false;
        if (this == object) return true;
        return equals((Section) object);
      }
      
      private boolean equals(Section sectionDefinition) {
        return sectionDefinition.name().equals(this.name()) && sectionDefinition.definition().equals(this.definition());
      }
      
    };
              
    return sectionDefinition;
  }

  public static Annotation buildOperation(final String name) {
    DefinitionOperation annotation = new DefinitionOperation() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return DefinitionOperation.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof DefinitionOperation)) return false;
        if (this == object) return true;
        return equals((DefinitionOperation) object);
      }
      
      private boolean equals(DefinitionOperation annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
  
  public static Annotation buildWorkLine(final String name) {
    TaskWorkLine annotation = new TaskWorkLine() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return TaskWorkLine.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof TaskWorkLine)) return false;
        if (this == object) return true;
        return equals((TaskWorkLine) object);
      }
      
      private boolean equals(TaskWorkLine annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
  
  public static Annotation buildWorkLock(final String name) {
    TaskWorkLock annotation = new TaskWorkLock() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return TaskWorkLock.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof TaskWorkLock)) return false;
        if (this == object) return true;
        return equals((TaskWorkLock) object);
      }
      
      private boolean equals(TaskWorkLock annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
  
  public static Annotation buildWorkLockForm(final String name) {
    TaskWorkLockForm annotation = new TaskWorkLockForm() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return TaskWorkLockForm.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof TaskWorkLockForm)) return false;
        if (this == object) return true;
        return equals((TaskWorkLockForm) object);
      }
      
      private boolean equals(TaskWorkLockForm annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
  
  public static Annotation buildWorkLockService(final String name) {
    TaskWorkLockService annotation = new TaskWorkLockService() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return TaskWorkLockService.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof TaskWorkLockService)) return false;
        if (this == object) return true;
        return equals((TaskWorkLockService) object);
      }
      
      private boolean equals(TaskWorkLockService annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
  
  public static Annotation buildWorkPlace(final String name) {
    TaskWorkPlace annotation = new TaskWorkPlace() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return TaskWorkPlace.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof TaskWorkPlace)) return false;
        if (this == object) return true;
        return equals((TaskWorkPlace) object);
      }
      
      private boolean equals(TaskWorkPlace annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
  
  public static Annotation buildWorkStop(final String name) {
    TaskWorkStop annotation = new TaskWorkStop() {

      @Override
      public Class<? extends Annotation> annotationType() {       
        return TaskWorkStop.class;
      }

      @Override
      public String name() {
        return name;
      }
      
      @Override
      public boolean equals(Object object) {
        if (!(object instanceof TaskWorkStop)) return false;
        if (this == object) return true;
        return equals((TaskWorkStop) object);
      }
      
      private boolean equals(TaskWorkStop annotation) {
        return annotation.name().equals(this.name());
      }
      
    };
              
    return annotation;
  }
}
