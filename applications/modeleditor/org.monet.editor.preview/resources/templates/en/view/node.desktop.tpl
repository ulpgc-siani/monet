@content
::links::
::shows::

@link.entity
<div class="_link ::entityCode::">
  <div class="title"><a class="command" href="javascript:showPage('::entityName::')" title="::entityLabel::">::entityLabel::</a></div>
  <div class="description">::entityDescription::</div>
</div>

@link.entity.cube
<div class="_link ::entityCode::">
  <div class="title"><a class="command" href="javascript:showPage('::entityName::')" title="Abrir cubo de ::entityLabel::">::entityLabel::</a></div>
  <div class="description">::entityDescription::</div>
</div>

@link.entity.thesaurus
<div class="_link ::entityCode::">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="::entityLabel::">::entityLabel::</a> <span class="message">&nbsp;(link not available on preview)</span></div>
  <div class="description">::entityDescription::</div>
</div>

@link.news
<div class="_link">
  <div class="title"><a class="command" href="javascript:void(null)" title="News">News</a> <span class="unreachable">&nbsp;(link not available on preview)</span></div>
  <div class="description">Current information about the business unit</div>
</div>

@link.tasks
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Tareas">Tasks</a> <span class="message">&nbsp;(link not available on preview)</span></div>
  <div class="description">State of the asigned tasks to the business unit</div>
</div>

@link.tasks.pending
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Tareas">Tareas pendientes</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">Podrá ver el estado de los trabajos pendientes generados en el sistema</div>
</div>

@link.tasks.active
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Tareas">Tareas activas</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">Podrá ver el estado de los trabajos activos generados en el sistema</div>
</div>

@link.trash
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Trash">Trash</a> <span class="message">&nbsp;(link not available on preview)</span></div>
  <div class="description">Elements that have been deleted from the system</div>
</div>

@link.roles
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Roles">Roles</a> <span class="message">&nbsp;(link not available on preview)</span></div>
  <div class="description">List of the asigned roles to the users</div>
</div>

@show.news
<div class="system news">Here you can see business unit news</div>

@show.tasks
<div class="system tasks">Here you can see business unit tasks</div>

@show.trash
<div class="system trash">Here you can see trash</div>

@show.roles
<div class="system roles">Here you can see the list of roles assigned to the users</div>