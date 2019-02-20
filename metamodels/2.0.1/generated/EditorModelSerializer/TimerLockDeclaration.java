package org.monet.modelling.kernel.model;

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

// TimerLockDeclaration
// Declaración de un bloqueo de temporizador. Estos bloqueos se resuelven automáticamente al cabo del tiempo indicado

@Root(name="timer-lock")
public  class TimerLockDeclaration extends WorklockDeclaration 
 {
@Root(name = "timer"
)
public static class Timer {
protected @Attribute(name="delay") int _delay;
protected @Attribute(name="extend",required=false) int _extend;
public int getDelay() { return _delay; }
public void setDelay(int value) { _delay = value; }
public int getExtend() { return _extend; }
public void setExtend(int value) { _extend = value; }
}

protected @Element(name="timer") Timer _timer;

public Timer getTimer() { return _timer; }
public void setTimer(Timer value) { _timer = value; }

}







































