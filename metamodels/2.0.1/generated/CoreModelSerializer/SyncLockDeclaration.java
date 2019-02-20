package org.monet.kernel.model.definition;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

// SyncLockDeclaration
// Declaración de un bloqueo de sincronización de tareas. Estos bloqueos se resuelven automáticamente cuando una tarea termina
// Antes se llamaban wait-task

@Root(name="sync-lock")
public  class SyncLockDeclaration extends WorklockDeclaration 
 {
@Root(name="wait")
public static class Wait {
protected @Attribute(name="task") String _task;
public String getTask() { return _task; }
}

protected @Element(name="wait") Wait _wait;

public Wait getWait() { return _wait; }

}







































