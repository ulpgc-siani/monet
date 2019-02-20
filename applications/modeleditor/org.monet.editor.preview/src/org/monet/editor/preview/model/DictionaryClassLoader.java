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

package org.monet.editor.preview.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.monet.editor.preview.utils.StreamHelper;

public class DictionaryClassLoader extends ClassLoader {
  private String pathBase = null;

  private static HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
  private static DictionaryClassLoader instance;
  
  private DictionaryClassLoader() {
  }
  
  public static DictionaryClassLoader getInstance() {
    if (instance == null)
      instance = new DictionaryClassLoader();
    return instance;
  }

  byte[] getClassBytes(InputStream is) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedInputStream bis = new BufferedInputStream(is);
    boolean eof = false;
    
    while (!eof) {
      try {
        int i = bis.read();
        if (i == -1)
          eof = true;
        else baos.write(i);
      } catch (IOException e) {
        System.out.println(e);
        return null;
      }
    }
    
    return baos.toByteArray();
  }

  public Class<?> loadClass(String name) throws ClassNotFoundException {
    byte buf[];
    Class<?> resultClass;
    File file;
    InputStream is = null;

    try {
      file = new File(this.getPathBase(), name.replace(".", "/") + ".class");
      
      if (!file.exists()) return this.getClass().getClassLoader().loadClass(name);
      if (DictionaryClassLoader.classes.containsKey(name)) return DictionaryClassLoader.classes.get(name);
      
      is = new FileInputStream(file);
      buf = getClassBytes(is);
      resultClass = defineClass(name, buf, 0, buf.length, null);
      DictionaryClassLoader.classes.put(name, resultClass);
      
      return resultClass;
    } catch (FileNotFoundException e) {
      System.out.println(e);
    } finally {
      StreamHelper.close(is);
    }
    return null;
  }
  
  public static void reset() {
    DictionaryClassLoader.classes.clear();
  }

  public String getPathBase() {
    return pathBase;
  }

  public void setPathBase(String pathBase) {
    this.pathBase = pathBase;
  }
}