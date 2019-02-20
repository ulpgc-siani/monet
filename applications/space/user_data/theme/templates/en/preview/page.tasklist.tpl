<div class="page tasklist">

  <div class="def configuration">
    var configuration = new Object();
    configuration.toolbar = new Object();
    configuration.toolbar.operationSet = new Array();
    configuration.sidebar = new Object();
    configuration.sidebar.operationSet = new Array();
  </div>

  <div class="info">
    <table style="width:100%;">
      <tr>
        <td class="content">
          <div class="breadcrumbs"><span>::label::</span></div>
          <div class="title">::label::</div>
        </td>
      </tr>
    </table>
    <div class="toolbar"></div>
  </div>
  
  <div class="content">
    <div class="tabs">
      <div class="def default">::codeView::</div>
      <div class="tab" id="::codeView::">
        <div class="def label">::label::</div>
        ::render(view.tasklist)::
      </div>
    </div>
  </div>
</div>

@label
No label

@label.tasktray
My tasks

@label.taskboard
Task board

@addList$item
Options.AddList.Items["::code::"] = {"Code": "::code::", "Label": "::label::", "Description": "::description::"};
