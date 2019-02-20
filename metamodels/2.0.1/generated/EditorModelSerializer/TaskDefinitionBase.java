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

// TaskDefinition
// Declaración que se utiliza para modelar una tarea

@Root(name="task")
public  class TaskDefinitionBase extends EntityDefinition 
 {
@Root(name = "is-private"
)
public static class IsPrivate {
}
@Root(name = "execution"
)
public static class Execution {
public enum ModeEnumeration { MANUAL,SERVICE,TIMER }
protected @Attribute(name="mode") ModeEnumeration _mode;
protected @Attribute(name="timer",required=false) String _timer;
public ModeEnumeration getMode() { return _mode; }
public void setMode(ModeEnumeration value) { _mode = value; }
public String getTimer() { return _timer; }
public void setTimer(String value) { _timer = value; }
}
@Root(name = "target"
)
public static class Target {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}
@Root(name = "input"
)
public static class Input {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}
@Root(name = "output"
)
public static class Output {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}
@Root(name = "implements"
)
public static class Implements {
protected @Attribute(name="cube") String _cube;
public String getCube() { return _cube; }
public void setCube(String value) { _cube = value; }
}

protected @Element(name="is-private",required=false) IsPrivate _isPrivate;
protected @Element(name="execution") Execution _execution;
protected @Element(name="target") Target _target;
protected @Element(name="input",required=false) Input _input;
protected @Element(name="output",required=false) Output _output;
protected @ElementList(inline=true,required=false) ArrayList<Implements> _implementsList = new ArrayList<Implements>();
protected @Element(name="workmap",required=false) WorkmapDeclaration _workmapDeclaration;
protected @Element(name="view",required=false) TaskViewDeclaration _taskViewDeclaration;

public boolean isPrivate() { return (_isPrivate != null); }
public void setIsPrivate(boolean value) { if(value) _isPrivate = new IsPrivate(); else _isPrivate = null;}
public Execution getExecution() { return _execution; }
public void setExecution(Execution value) { _execution = value; }
public Target getTarget() { return _target; }
public void setTarget(Target value) { _target = value; }
public Input getInput() { return _input; }
public void setInput(Input value) { _input = value; }
public Output getOutput() { return _output; }
public void setOutput(Output value) { _output = value; }
public ArrayList<Implements> getImplementsList() { return _implementsList; }
public WorkmapDeclaration getWorkmapDeclaration() { return _workmapDeclaration; }
public TaskViewDeclaration getTaskViewDeclaration() { return _taskViewDeclaration; }

}







































