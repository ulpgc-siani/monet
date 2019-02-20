package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// TimerLockDeclaration
// Declaración de un bloqueo de temporizador. Estos bloqueos se resuelven automáticamente al cabo del tiempo indicado

@Root(name="timer-lock")
public  class TimerLockDeclaration extends WorklockDeclaration 
 {
@Root(name="timer")
public static class Timer {
protected @Attribute(name="delay") int _delay;
protected @Attribute(name="extend",required=false) int _extend;
public int getDelay() { return _delay; }
public int getExtend() { return _extend; }
}

protected @Element(name="timer") Timer _timer;

public Timer getTimer() { return _timer; }

}







































