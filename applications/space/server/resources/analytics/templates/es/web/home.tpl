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
  <h1>Interface de Aplicaciones. Operaciones disponibles</h1>

  <div class="notes">Los resultados obtenidos por la llamada a alguna de estas operaciones se devuelven en formato
    json
  </div>
  <ul>
    <li>
      <div class="title">/cubelist</div>
      <div class="help">Devuelve la lista de cubos disponibles</div>
      <div class="params">
        <div class="param"><b>start</b>. El listado está paginado. Este parámetro indica el índice de comienzo del
          primer elemento a devolver.
        </div>
        <div class="param"><b>limit</b>. El listado está paginado. Este parámetro indica el número de elementos a
          devolver desde start.
        </div>
      </div>
    </li>
    <li>
      <div class="title">/cube</div>
      <div class="help">Devuelve el cubo indicado con name</div>
      <div class="params">
        <div class="param"><b>name</b>. Nombre del cubo a obtener</div>
      </div>
    </li>
    <li>
      <div class="title">/members</div>
      <div class="help">Devuelve los valores de valores de la dimensión indicada</div>
      <div class="params">
        <div class="param"><b>cube</b>. Nombre del cubo que contiene los valores</div>
        <div class="param"><b>key</b>. Clave que representa la dimensión y level. Se representa de la siguiente forma:
          dimension.level
        </div>
        <div class="param"><b>selection</b>. Listado de selectores a aplicar serializados en json. Cada filtro tendrá el
          par jerarquía-path
        </div>
      </div>
    </li>
    <li>
      <div class="title">/tuples</div>
      <div class="help">Devuelve los valores de medida para el la clave indicada del cubo</div>
      <div class="params">
        <div class="param"><b>cube</b>. Nombre del cubo que contiene la jerarquía</div>
        <div class="param"><b>key</b>. Clave que representa la dimensión y level. Se representa de la siguiente forma:
          dimension.level
        </div>
        <div class="param"><b>filters</b>. Listado de filtros a aplicar serializados en json. Cada filtro tendrá el par
          jerarquía-path
        </div>
      </div>
    </li>
  </ul>
  <h1>Inteface Web. Modos en los que se puede invocar a la aplicación</h1>
  <ul>
    <li>
      <div class="title">/showcube</div>
      <div class="help">Muestra una vista completa del cubo</div>
      <div class="params">
        <div class="param"><b>cube</b>. Nombre del cubo.</div>
        <div class="param"><b>view</b>. Nombre de la vista que se quiere mostrar del cubo.</div>
      </div>
    </li>
    <li>
      <div class="title">/showindicators</div>
      <div class="help">Muestra los indicadores del cubo para la vista indicada</div>
      <div class="params">
        <div class="param"><b>cube</b>. Nombre del cubo.</div>
        <div class="param"><b>view</b>. Nombre de la vista que se quiere mostrar del cubo.</div>
      </div>
    </li>
    <li>
      <div class="title">/showmembers</div>
      <div class="help">Muestra los miembros del cubo. Existen tres formas de mostrar los miembros: al completo,
        mediante un gráfico de líneas o mediante una tabla.
      </div>
      <div class="params">
        <div class="param"><b>cube</b>. Nombre del cubo.</div>
        <div class="param"><b>view</b>. Nombre de la vista que se quiere mostrar del cubo.</div>
        <div class="param"><b>mode</b>. Permite seleccionar el modo en que se mostrarán los miembros:
          <ul>
            <li>all: todos los miembros</li>
            <li>chart: todos los miembros representados en una gráfica de líneas. Una línea por indicador.</li>
            <li>table: todos los miembros desagregados por alguna dimensión. La dimensión se indicará en el resto de
              parámetros
            </li>
          </ul>
        </div>
        <div class="param"><b>dimension</b>. Representa la dimensión por la que desagregar. Opcional. Solo se indicará
          si el modo es table.
        </div>
        <div class="param"><b>hierarchy</b>. Representa la jerarquía por la que desagregar. Opcional. Solo se indicará
          si el modo es table.
        </div>
        <div class="param"><b>level</b>. Representa el nivel por el que desagregar. Opcional. Solo se indicará si el
          modo es table.
        </div>
        <div class="param"><b>measure</b>. Indica la medida a mostrar. El par measure-operator representa el indicador.
          Opcional. Solo se indicará si el modo es table.
        </div>
        <div class="param"><b>operator</b>. Indica el operador a mostrar. Opcional. Solo se indicará si el modo es
          table.
        </div>
      </div>
    </li>
  </ul>
</div>
<div class="footer">Copyright &copy; 2012</div>
</body>
</html>