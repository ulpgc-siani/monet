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
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="::entityLabel::">::entityLabel::</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">::entityDescription::</div>
</div>

@link.news
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Noticias">Noticias</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">Podrá ver la información de actualidad del sistema</div>
</div>

@link.tasks
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Tareas">Tareas</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">Podrá ver el estado de los trabajos generados en el sistema</div>
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
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Papelera">Papelera</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">Podrá ver los elementos que han sido eliminados del sistema</div>
</div>

@link.roles
<div class="_link">
  <div class="title unreachable"><a class="command" href="javascript:void(null)" title="Roles">Roles</a> <span class="message">&nbsp;(enlace no disponible en vista previa)</span></div>
  <div class="description">Podrá ver la lista de roles del sistema</div>
</div>

@show.news
<div class="system news">Aquí se mostrarán las noticias de sistema</div>

@show.tasks
<div class="system tasks">Aquí se mostrarán las tareas de sistema</div>

@show.trash
<div class="system trash">Aquí se mostrará la papelera de sistema</div>

@show.roles
<div class="system roles">Aquí se mostrarán los roles definidos en el modelo</div>