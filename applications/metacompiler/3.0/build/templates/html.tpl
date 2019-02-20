<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
<style>
body {
  font-family: cambria, times new roman;
  font-size: 11pt;
  margin:10;
  padding:0;
  border: 0;
}

a {
  text-decoration: none;
  color: #115;
}
a:hover {
  color: red;
}
.header {
  font-family: calibri, arial;
}

.tag {
  font-family: courier new, courier;
  font-weight: bold;
  color: #722;
}
.inherited {
  margin-top:10px;
  font-weight: normal;
  font-style: italic;
  font-size: 10pt;
}
.description {
  font-style: italic;
}
.hint {
  color: #669;
  font-style: italic;
  font-size: 10pt;
}
.stereotype {
  color: #722;
  font-style: italic;
  font-size: 14px;
}

.stereotype  span {
  padding-left: 5px;
}
.include .stereotype  span {
  padding: 0;
  padding-right: 5px;
}


.metamodel {
  margin-bottom: 20px;
}
.metamodel .header {
  font-size: 28pt;
  font-weight: bold
  border: 0;
  margin:0;
  border-bottom: 2px dashed #227;
  margin-bottom: 15px;
}

.metaclass {
  padding-top: 10px;
  padding-bottom: 10px;
  margin-bottom: 1000px;
}
.metaclass .header {
  margin: 0px;
  font-size: 22pt;
}

.metaclass .text {
  margin: 0px;
  margin-left: 20px;
}

.metaclass .section {
}
.metaclass .section .header {
  font-size: 16pt;
  border-bottom: 1px solid black;
  margin-top: 30px;
}
.metaclass .section .text {
  margin: 0;
}

.metaclass .section .attribute  {
  margin-top: 10px;  
}

.metaclass .section .attribute .header {
  font-size: 12pt;
  margin: 0;
  padding: 0;
  border: 0;
}
.metaclass .section  .attribute .text {
  padding-left: 20px;
}

.metaclass .section .include {
  margin-top: 10px;
  padding: 5px;
  border: 1px dotted silver;
  background-color: #EED;
}
.metaclass .section .include .header {
  font-size: 14pt;
  padding:0;
  border:0;
  margin:0;
}
.metaclass .section .include .text {
  margin: 0;
  padding-left: 20px;
}

.metaclass .section .property {
  margin-top: 10px;
  padding: 5px;
  border: 1px dotted silver;
}
.metaclass .section .property .header {
  font-size: 14pt;
  padding:0;
  border:0;
  margin:0;
  padding-left: 5px;
}

.metaclass .section .property .text{
  border:0;
  margin:0;
  padding-left: 20px;
}

.metaclass .section .property .attribute  {
  padding:0;
  border:0;
  margin:0;
  margin-top: 10px;  
}
.metaclass .section .property .attribute .header {
  font-size: 12pt;
  padding:0;
  border:0;
  margin:0;
}

.metaclass .section .property .attribute .text {
  margin:0;
}

.code {
  font-family: courier;
}

.reference .reference {
  margin-left: 10px;
  padding-left: 10px;
  border-left: 1px solid silver;
}
</style>
</head>

<body>
  <div class="metamodel">
    <div class="header"><a name="top">::name::</div>  
    <div class="text">Version\: ::version::</div>
    <div class="text">Released on\: ::date::</div>  
  </div>
  ::definitionMetaClass::
  ::metaClasses::
</body>
</html>

@metaClass
<div class="metaclass" id="::name::">
  <a name="::name::"></a>
  <div>
    <span class="stereotype">::stereotype::</span>
  </div>
  <div class="header">::label::</div>  
  <div class="text">
    ::description|<div class="description">*</div>::
    ::hint|<div class="hint">*</div>::
    ::attributes::
    ::properties::
    ::includes::
    ::parents::
    ::children::
    ::linkClasses::
    ::relatedClasses::
  </div>
</div>
 
@stereotype
::content::

@stereotype.main
<span>::main::</span>

@stereotype.alt
<span>::alt::</span>

@label
::label::

@label.abstract
<u>::label::</u>

@attributes
<div class="section">
  <div class="header">attributes</div>
  <div class="text">::items::</div>
</div>

@attribute
<div class="attribute">
  <div class="header">::name::\: ::type:: ::relation:: ::value|<span> = *</span>:: 
    <span class="stereotype">::stereotype::</span>
  </div>
  <div class="text">
    ::description|<div class="description">*</div>::
    ::hint|<div class="hint">*</div>::
    ::owner|<div class='inherited'>inherited from <a href='\#*'>*</a></div>::
  </div>
</div>

@attribute$relation
&gt;<a href='#::link::'>::link::</a>::restriction|\:<a href='\#*'>*</a>::

@properties
<div class="section">
  <div class="header">properties</div>
  <div class="text">::items::</div>
</div>

@property
<div class="property">
  <div class="header">::type::
    <span class="stereotype">
      ::multipleStereotype::
      ::requiredStereotype::
    </span>
  </div>
  <div class="text">
    ::description|<div class="description">*</div>::
    ::hint|<div class="hint">*</div>::
    ::attributes::
    ::contentType|<div class="attribute">content: *</div>::
    ::owner|<div class='inherited'>inherited from <a href='\#*'>*</a></div>::
  </div>
</div>

@includes
<div class="section">
  <div class="header">includes</div>
  <div class="text">::items::</div>
</div>

@include
<div class="include">
  <div>
    <span class="stereotype">
      ::multipleStereotype::
      ::requiredStereotype::
    </span>
  </div>
  <div class="header">
    ::classTree::
    <div class="text">
      ::owner|<div class='inherited'>inherited from <a href='\#*'>*</a></div>::
    </div>
  </div>
</div>

@parents
<div class="section">
  <div class="header">parent classes</div>
  <div class="text">::classList::</div>
</div>

@children
<div class="section">
  <div class="header">derived classes</div>
  <div class="text">::classTree::</div>
</div>

@linkClasses
<div class="section">
  <div class="header">can be used in</div>
  <div class="text">::classTree::</div>
</div>

@relatedClasses
<div class="section">
  <div class="header">related classes</div>
  <div class="text">::classList::</div>
</div>

@classTree
::links::

@classTree.link
<div class="reference">
  <a href="#::type::">::label::</a>
  ::children::
</div>

@classList
::items::

@classList.item
<div class="reference"><a href="#::type::">::label::</a></div>