package org.monet.editor.dsl.ui.descriptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.monet.editor.Constants;
import org.monet.editor.library.StreamHelper;

public class DefinitionContentDescriber implements IContentDescriber {
	
	private static class ContentHeader {
		private static final String TOKEN_LINE_COMMENT = "//";
		private static final String TOKEN_BLOCK_COMMENT_OPEN = "/*";
		private static final String TOKEN_BLOCK_COMMENT_CLOSE = "*/";
		
		private InputStream input = null;
	    private StringBuffer stringBuffer = null;
	    private BufferedReader bufferedReader = null;
	    private InputStreamReader reader = null;
		private String header;
		private boolean isBlockComment = false;
		
		private ContentHeader(InputStream input) throws IOException {
		    this.stringBuffer = new StringBuffer();
		    this.input = input;
		    this.reader = new InputStreamReader(input, "UTF-8");
		    this.bufferedReader = new BufferedReader(reader);
			
		    try {
		    	this.header = this.execute();
		    } finally {
		    	StreamHelper.close(this.bufferedReader);
		    	StreamHelper.close(this.reader);
		    	StreamHelper.close(this.input);
		    }
		}
		
		public String removeLineComment(String line) {
			int commentPos = line.indexOf(TOKEN_LINE_COMMENT);
			if (commentPos >= 0) {
				return line.substring(0, commentPos);
			} else {
				return line;
			}
		}
		
		public String removeBlockComment(String line) {
			if (this.isBlockComment) {
				int pos = line.indexOf(TOKEN_BLOCK_COMMENT_CLOSE);
				if (pos < 0) {
					return "";
				} 
					
				this.isBlockComment = false;
				line = line.substring(pos + TOKEN_BLOCK_COMMENT_CLOSE.length());
			} 
			
			int pos = line.indexOf(TOKEN_BLOCK_COMMENT_OPEN);
			if (pos < 0)
				return line;
			
			line = line.substring(0, pos);
			this.isBlockComment = true;
			
			return removeBlockComment(line);
		}
		  
		public String execute() throws IOException {
			String line;
		    int bracketsFound = 0;

		    while ((line = bufferedReader.readLine()) != null) {

		    	line = this.removeLineComment(line);
		    	
		    	line = this.removeBlockComment(line); 
		    	  
		    	if (!line.equals("")) {
		    		stringBuffer.append(line);
		    		if(line.indexOf("{") > -1) {
		    			if(bracketsFound == 1)
		    				break;
		    			else
		    				bracketsFound++;
		    		}
		    	}
		    }

		    return this.stringBuffer.toString();
		}
	}

	private String token;
  
	public DefinitionContentDescriber(String definitionToken) {
		this.token = definitionToken;
	}
  
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		if(description == null || description.getContentType().getId().startsWith(Constants.MML_CONTENT_TYPE)) {
      
			ContentHeader contentHeader = new ContentHeader(contents);
			String content = contentHeader.header;
      
			Pattern pattern = Pattern.compile("((\\ is\\ " + token + "[{|\\ ])|(\\ is\\ " + token + "$))");
			java.util.regex.Matcher matcher = pattern.matcher(content);
			//TODO Chequear bien esta expresion regular
			if (matcher.find())
				return VALID;
			else
				return INVALID;
		}
		return INVALID;
	}
  
	public QualifiedName[] getSupportedOptions() {
		return null;
	}

  /* Definition describers */
  
  public static class Catalog extends DefinitionContentDescriber {
    public Catalog() { super("catalog"); }
  }
  
  public static class Collection extends DefinitionContentDescriber {
    public Collection() { super("collection"); }
  }

  public static class Container extends DefinitionContentDescriber {
    public Container() { super("container"); }
  }
  
  public static class Dashboard extends DefinitionContentDescriber {
    public Dashboard() { super("dashboard"); }
  }
  
  public static class DataStore extends DefinitionContentDescriber {
    public DataStore() { super("datastore"); }
  }

  public static class MeasureUnit extends DefinitionContentDescriber {
    public MeasureUnit() { super("measure-unit"); }
  }

  public static class Desktop extends DefinitionContentDescriber {
    public Desktop() { super("desktop"); }
  }
  
  public static class Document extends DefinitionContentDescriber {
    public Document() { super("document"); }
  }
  
  public static class Exporter extends DefinitionContentDescriber {
    public Exporter() { super("exporter"); }
  }
  
  public static class DatastoreBuilder extends DefinitionContentDescriber {
    public DatastoreBuilder() { super("datastore-builder"); }
  }

  public static class Form extends DefinitionContentDescriber {
    public Form() { super("form"); }
  }
  
  public static class Importer extends DefinitionContentDescriber {
    public Importer() { super("importer"); }
  }

  public static class Index extends DefinitionContentDescriber {
    public Index() { super("index"); }
  }
  
  public static class Role extends DefinitionContentDescriber {
    public Role() { super("role"); }
  }

  public static class ServiceProvider extends DefinitionContentDescriber {
    public ServiceProvider() { super("service-provider"); }
  }
  
  public static class Service extends DefinitionContentDescriber {
    public Service() { super("service"); }
  }
  
  public static class Activity extends DefinitionContentDescriber {
    public Activity() { super("activity"); }
  }
  
  public static class Thesaurus extends DefinitionContentDescriber {
    public Thesaurus() { super("thesaurus"); }
  }
  
  public static class Glossary extends DefinitionContentDescriber {
    public Glossary() { super("glossary"); }
  }
  
  public static class Job extends DefinitionContentDescriber {
    public Job() { super("job"); }
  }
  
  public static class Sensor extends DefinitionContentDescriber {
    public Sensor() { super("sensor"); }
  }
  
}
