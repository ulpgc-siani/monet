grammar org.monet.editor.dsl.MonetModelingLanguage with org.eclipse.xtext.xbase.Xbase

import "http://www.eclipse.org/emf/2002/Ecore" as ecore

generate monetModelingLanguage "http://www.monet.org/editor/dsl/MonetModelingLanguage"

DomainModel\:
	DefinitionModel | ProjectModel | DistributionModel
;

DistributionModel\:
	'distribution' name=ValidID 'extends' superType=[ProjectModel|QualifiedName] '{'
		features+=ManifestFeature*
	'}'
;

ProjectModel\:
	'project' name=ValidID '{'
		features+=ManifestFeature*
	'}'
;

ManifestFeature\:
	Attribute | Property | Define | Method
;

DefinitionModel\:
	elements+=Import*
	elements+= PackageDeclaration;

Import\:
	'import' importedNamespace=QualifiedNameWithWildcard;

Code\:
	'[' value = STRING ']'
;

PackageDeclaration\:
	'package' name=QualifiedName '{'
		
		definition=Definition
	'}';

DefinitionType\:
::metaClassListDefinitionsNotAbstract::
;

PropertyTypes\:
::metaProperties::
;

MethodTypes\:
::metaMethods::
;

AttributeTypes\:
::metaAttributes::
;

Definition\:
	code=Code?
	abstract?='abstract'? extensible?='extensible'? 'definition' name=ValidID 'is' definitionType=DefinitionType ('extends' superType=[Definition|QualifiedName])? ('replace' replaceSuperType=[Definition|QualifiedName])? '{'
		features+=Feature*
	'}'
;

Feature\:
	Attribute | Definition | Property | Schema | Method | Function | Variable
;

Attribute:
id=AttributeTypes '=' value=AttributeValue
;

AttributeValue:
StringLiteral | IntLiteral | FloatLiteral | DoubleLiteral | EnumLiteral | XTReference | LocalizedText | Resource | ExpressionLiteral | TimeLiteral
;

LocalizedText:
'text' value=(ID | ENUM) ';'
;

Resource:
  type=('data' | 'help' | 'image' | 'template' | 'layout') value=(ID | ENUM) ';'
;

StringLiteral:
value=STRING ';';

TimeLiteral:
	value=TIME ';'
;

IntLiteral:
	negative?='-'? value=INT ';'
;

FloatLiteral:
	negative?='-'? value=FLOAT ';'
;

DoubleLiteral:
	negative?='-'? value=DOUBLE ';'
;

ExpressionLiteral
:value=XBlockExpression;

Referenciable:
Property | Definition
;

XTReference:
'ref' value=[Referenciable|QualifiedName] ';'
;

EnumLiteral:
value=ENUM ';'
;

terminal ENUM \: ('A'..'Z'|'_')('0'..'9'|'A'..'Z'|'_')*;
terminal FLOAT returns ecore\:\:EFloat \: INT '.' INT 'F';
terminal DOUBLE returns ecore\:\:EDouble \: INT '.' INT;
terminal TIME : ((('0'..'9')+ ':')? ((('0'..'1')('0'..'9')) | (('2')('0'..'3'))) ':')? ('0'..'5')('0'..'9') ':' ('0'..'5')('0'..'9') (',' ('0'..'9')('0'..'9')('0'..'9'))?;

Property\:
	code=Code?
	id=PropertyTypes name=ValidID? ('{' 
		features+=PropertyFeature*
	'}' | ';')
;

PropertyFeature\:
	Attribute | Property | Method
;

Method\:
	id=MethodTypes '(' (params+=FullJvmFormalParameter (',' params+=FullJvmFormalParameter)*)? ')' body=XBlockExpression
;

Variable:
	'var' name=ValidID 'as' type=JvmTypeReference ';'
;

Define:
	'define' name=ValidID '=' value=STRING ';'
;

Schema\:
	id='schema' '{'  
	 	properties+=SchemaFeature*
	 '}'
;

SchemaFeature\:
	SchemaSection | SchemaProperty
;

SchemaSection\:
	(many ?= 'many')? id=ID '{' features += SchemaFeature* '}'
;

SchemaProperty\:
	(many ?= 'many')? id=ID source=SchemaPropertyOfValue
;

SchemaPropertyOfValue\:
	type = JvmParameterizedTypeReference (body = XBlockExpression | ';')
;
		
Function\:
	'function' name=ValidID '(' (params+=FullJvmFormalParameter (',' params+=FullJvmFormalParameter)*)? ')' (':' type=JvmTypeReference)? 
		body=XBlockExpression;

QualifiedNameWithWildcard\:
	QualifiedName  ('.' '*')?;


@metaClassListDefinitionsNotAbstract
::or| * ::'::token::'

@metaProperty
::or| * ::'::name::'

@metaMethod
::or| * ::'::token::'

@metaAttribute
::or| * ::'::name::'