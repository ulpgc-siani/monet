@tabs
<div class="body">
  <div class="content">
    <div class="tabs::hide| hide::">
      <div class="def default">::defaultTab::</div>
      ::tabsList::
    </div>
  </div>
</div>

@tab
<div class="tab" id="::code::">
  <div class="def label">::label::</div>
  <div class="def visible">::visible::</div>
  ::render(view.node)::
</div>