<html>
<head>
  <title>::pageTitle:: Analytics</title>
  <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>
  <style type="text/css">
    html, body, input, table, td, select, options, textarea {
      font-family: Roboto, Verdana, Arial, sans-serif;
      font-size: 12px;
    }

    h1 {
      color: \#254061;
    }

    ul {
      list-style-type: none;
    }

    li {
      margin-bottom: 10px;
    }

    li .title {
      font-size: 14px;
      font-style: italic;
      margin-bottom: 3px;
    }
  </style>
</head>
<body>
<div class="header">
  <a href="api"><img src="::imagesUrl::/logo.gif" title="Monet analytics"/></a>
</div>
<div class="content">
  <h1>Applications interface. Available operations</h1>

  <div class="notes">The obteined resultset for a calling to these operations returns in json format</div>
  <ul>
    <li>
      <div class="title">/cubelist</div>
      <div class="help">Returns the available list of cubes</div>
      <div class="params">
        <div class="param"><b>start</b>. The list is paginated. This parameter shows the start indication of the first
          element to return.
        </div>
        <div class="param"><b>limit</b>. The list is paginated. This parameter shows the number of elements to return
          from the start.
        </div>
      </div>
    </li>
    <li>
      <div class="title">/cube</div>
      <div class="help">Return the indicated cube with name</div>
      <div class="params">
        <div class="param"><b>name</b>. Cube name to obtain</div>
      </div>
    </li>
    <li>
      <div class="title">/members</div>
      <div class="help">It returns the available values of the indicate dimension</div>
      <div class="params">
        <div class="param"><b>cube</b>. Cube name which contains the values</div>
        <div class="param"><b>key</b>. Key which represents the dimension and level. It is represented by the next form:
          dimension.level
        </div>
        <div class="param"><b>selection</b>. Selectors list to apply in serializer form in json. Each filter will have
          the hierarchy-path pair
        </div>
      </div>
    </li>
    <li>
      <div class="title">/tuples</div>
      <div class="help">It returns the measure values for the indicate key in the cube</div>
      <div class="params">
        <div class="param"><b>cube</b>. Cube name which contains the hierarchy</div>
        <div class="param"><b>key</b>. Key which represents dimension and level.It is represented by the next form:
          dimension.level
        </div>
        <div class="param"><b>filters</b>. Filters list to apply serializer in json. Each filter will have the
          hierarchy-path pair
        </div>
      </div>
    </li>
  </ul>
  <h1>Inteface Web. Ways to invocate the application</h1>
  <ul>
    <li>
      <div class="title">/showcube</div>
      <div class="help">Show a full view of the cube</div>
      <div class="params">
        <div class="param"><b>cube</b>. Cube name.</div>
        <div class="param"><b>view</b>. View name with you want to show the cube.</div>
      </div>
    </li>
    <li>
      <div class="title">/showindicators</div>
      <div class="help">Show the cube indicators to the indicate view</div>
      <div class="params">
        <div class="param"><b>cube</b>. Cube name.</div>
        <div class="param"><b>view</b>. View name with you want to show the cube.</div>
      </div>
    </li>
    <li>
      <div class="title">/showmembers</div>
      <div class="help">Show the cube members. There are three ways to show the members: full, in a line chart or in a
        table.
      </div>
      <div class="params">
        <div class="param"><b>cube</b>. Cube name.</div>
        <div class="param"><b>view</b>. View name with you want to show the cube.</div>
        <div class="param"><b>mode</b>. It allows to choose the way to show the members:
          <ul>
            <li>all: all the members</li>
            <li>chart: all the represented members in the line charts. One line per indicator.</li>
            <li>table: all the disaggregated members by some dimension. The dimension will indicate in the rest of the
              parameters
            </li>
          </ul>
        </div>
        <div class="param"><b>dimension</b>. It represents the dimension for which disaggregate. Optional. It will only
          indicate in the table mode.
        </div>
        <div class="param"><b>hierarchy</b>. It represents the hierarchy for which disaggregate. Optional. It will only
          indicate in the table mode.
        </div>
        <div class="param"><b>level</b>. It represents the level for which disaggregate. Optional. It will only indicate
          in the table mode.
        </div>
        <div class="param"><b>measure</b>. It indicates the measure to show. The measure-operator pair represents the
          indicator. Optional. It will only indicate in the table mode.
        </div>
        <div class="param"><b>operator</b>. It indicates the operator to show. Optional. It will only indicate in the
          table mode.
        </div>
      </div>
    </li>
  </ul>
</div>
<div class="footer">Copyright &copy; 2012</div>
</body>
</html>