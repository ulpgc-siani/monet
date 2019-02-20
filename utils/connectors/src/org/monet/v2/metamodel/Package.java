package org.monet.v2.metamodel;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "definition")
@Namespace(reference = "http://www.monetproject.com/schemas/model", prefix = "m")
public class Package {

  @Element(required = false)
  private ThesaurusDefinition         thesaurus;

  @Element(required = false)
  private ReferenceDefinition         reference;

  @Element(required = false)
  private DocumentDefinition          document;
  @Element(required = false)
  private CatalogDefinition           catalog;
  @Element(required = false)
  private ContainerDefinition         container;
  @Element(required = false)
  private CollectionDefinition        collection;
  @Element(required = false)
  private FormDefinition              form;

  @Element(required = false)
  private DesktopDefinition           desktop;

  @Element(required = false)
  private RoleDefinition              role;

  @Element(required = false)
  private TaskDefinition              task;

  @Element(name = "service", required = false)
  private ServiceDefinition           service;

  @Element(name = "service-provider", required = false)
  private ServiceProviderDefinition   serviceProvider;
  @Element(name = "thesaurus-provider", required = false)
  private ThesaurusProviderDefinition thesaurusProvider;
  @Element(name = "map-provider", required = false)
  private MapProviderDefinition       mapProvider;
  @Element(name = "cube-provider", required = false)
  private CubeProviderDefinition      cubeProvider;

  @Element(required = false)
  private CubeDefinition              cube;

  @Element(required = false)
  private ImporterDefinition          importer;
  @Element(required = false)
  private ExporterDefinition          exporter;

  public Package() {
  }

  public Package(CubeDefinition definition) {
    this.cube = definition;
  }

  public Definition getDefinition() {
    if (this.thesaurus != null)
      return this.thesaurus;
    else if (this.reference != null)
      return this.reference;
    else if (this.document != null)
      return this.document;
    else if (this.catalog != null)
      return this.catalog;
    else if (this.container != null)
      return this.container;
    else if (this.collection != null)
      return this.collection;
    else if (this.form != null)
      return this.form;
    else if (this.desktop != null)
      return this.desktop;
    else if (this.role != null)
      return this.role;
    else if (this.task != null)
      return this.task;
    else if (this.service != null)
      return this.service;
    else if (this.mapProvider != null)
      return this.mapProvider;
    else if (this.thesaurusProvider != null)
      return this.thesaurusProvider;
    else if (this.serviceProvider != null)
      return this.serviceProvider;
    else if (this.cubeProvider != null)
      return this.cubeProvider;
    else if (this.cube != null)
      return this.cube;
    else if (this.importer != null)
      return this.importer;
    else if (this.exporter != null)
      return this.exporter;
    else
      return null;
  }
}
