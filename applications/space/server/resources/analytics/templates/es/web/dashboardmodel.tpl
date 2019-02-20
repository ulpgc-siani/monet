model = new Object();
model.id = "::name::";
model.name = "::name::";
model.label = "::label::";
model.report = "::report::";
model.timeDimension = "::timeDimension::";
model.resolution = ::resolution::;
model.range = { "min"\: Date.timeToLocale(::min::), "max"\: Date.timeToLocale(::max::) };
model.scale = ::scale::;
::folderList::
::folderMap::
::indicatorList::
::indicatorMap::
::measureUnitList::
::measureUnitMap::
::dimensionList::
::dimensionMap::

@elementList
model.::list:: = new Array();
::items::

@elementList$item
model.::list::.push(::element::);

@elementMap
model.::map:: = new Object();
::items::

@elementMap$item
model.::map::["::name::"] = ::element::;

@indicator
{
id:"::id::",
name:"::name::",
label:"::label::",
description:"::description::",
metric:"::metric::",
resolutions:[::resolutions::]
}

@indicator$resolution
::comma|,::"::resolution::"

@folder
{
name:"::name::",
label:"::label::",
folders:[::folders::],
indicators:[::indicators::]
}::comma|,::

@folder$indicator
::comma|,::"::id::"

@measureUnit
{
id:"::id::",
name:"::name::",
label:"::label::"
}

@dimension
{
id:"::id::",
name:"::name::",
label:"::label::",
taxonomies:[::taxonomies::]
}

@dimension$taxonomy
::comma|,::{
id:"::id::",
name:"::name::",
label:"::label::"
}