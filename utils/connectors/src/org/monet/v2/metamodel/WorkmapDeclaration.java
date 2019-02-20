package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// WorkmapDeclaration
// Declaración que se utiliza para modelar un workmap

public class WorkmapDeclaration extends WorkmapDeclarationBase {

  protected HashMap<String, WorkplaceDeclaration> workplaceMap     = new HashMap<String, WorkplaceDeclaration>();
  protected HashMap<String, WorkstopDeclaration>  workstopMap      = new HashMap<String, WorkstopDeclaration>();
  protected HashMap<String, WorklineDeclaration>  worklineMap      = new HashMap<String, WorklineDeclaration>();
  protected HashMap<String, WorklineDeclaration>  worklineStartMap = new HashMap<String, WorklineDeclaration>();
  protected HashMap<String, WorklockDeclaration>  worklockMap      = new HashMap<String, WorklockDeclaration>();
  protected ArrayList<WorkplaceDeclaration>       goals            = new ArrayList<WorkplaceDeclaration>();
  protected WorkplaceDeclaration                  initial;

  @Commit
  public void commit() {
    for (WorkplaceDeclaration workplace : this._workplaceDeclarationList) {
      this.workplaceMap.put(workplace.getCode(), workplace);
      this.workplaceMap.put(workplace.getName(), workplace);
      if (workplace.getType() != null) {
        switch (workplace.getType()) {
          case EVENT:
            this.initial = workplace;
            break;
          case GOAL:
            this.goals.add(workplace);
            break;
        }
      }
    }
    for (WorklineDeclaration workline : this._worklineDeclarationList) {
      this.worklineMap.put(workline.getCode(), workline);
      this.worklineMap.put(workline.getName(), workline);
      for (WorkstopDeclaration workstop : workline.getWorkstopDeclarationList()) {
        this.workstopMap.put(workstop.getCode(), workstop);
        this.workstopMap.put(workstop.getName(), workstop);
      }

      this.worklineStartMap.put(workline.getFrom().getWorkplace(), workline);
      this.worklineStartMap.put(this.workplaceMap.get(workline.getFrom().getWorkplace()).getCode(), workline);

      boolean hasMain = false;
      for (WorklockDeclaration worklock : workline.getWorklockDeclarationList()) {
        this.worklockMap.put(worklock.getCode(), worklock);
        this.worklockMap.put(worklock.getName(), worklock);
        if (worklock.isMain())
          hasMain = true;
      }
      if (!hasMain && workline.getWorklockDeclarationList().size() > 0)
        workline.getWorklockDeclarationList().get(0)._isMain = new WorklockDeclaration.IsMain();
    }
  }

  public WorkplaceDeclaration getWorkplace(String key) {
    return this.workplaceMap.get(key);
  }

  public WorkstopDeclaration getWorkstop(String key) {
    return this.workstopMap.get(key);
  }

  public WorklineDeclaration getWorkline(String key) {
    return this.worklineMap.get(key);
  }

  public WorkplaceDeclaration getInitial() {
    return this.initial;
  }

  public WorklineDeclaration getWorklineThatStartsIn(WorkplaceDeclaration workplace) {
    return this.worklineStartMap.get(workplace.getCode());
  }

  public List<WorkplaceDeclaration> getGoals() {
    return this.goals;
  }

  @SuppressWarnings("unchecked")
  public <T extends WorklockDeclaration> T getWorklockDeclaration(String key) {
    return (T) this.worklockMap.get(key);
  }

}
