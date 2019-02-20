@messages
<div style="display: none;" class="message update">This element has changed... <a class="command" href="refreshnode(::idNode::)">update</a></div>

@tabs
<div class="body">
  <div class="tabs::hide| hide::">
    <div class="def default">::defaultTab::</div>
    ::tabsList::
  </div>
</div>

@tab
<div class="tab" id="::code::">
  <div class="def label">::label::</div>
  <div class="def visible">::visible::</div>
  ::render(view.node)::
</div>
