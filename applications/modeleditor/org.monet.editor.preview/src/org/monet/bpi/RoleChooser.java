package org.monet.bpi;

import java.util.List;

public interface RoleChooser {

  List<Role> getCandidates();
  void choose(Role role);
  
}
