package org.monet.grided.core.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;


public class ModelVersion {
    private static final String DATE_FORMAT = "dd MMM yyy (HH:mm:ss)";
    private static final DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    
    private long id;
    private String label;
    private long date;
    private MetaModelVersion metaModelVersion;
    private List<Space> spaces;
    private Model model;

    public ModelVersion(long id, Model model, String label, long date, MetaModelVersion metaModelVersion) {
        this.id = id;   
        this.model = model;
        this.date  =  DateUtils.truncate(new Date(date), Calendar.SECOND).getTime();
        this.label = label;
        this.metaModelVersion = metaModelVersion;
        this.spaces = new ArrayList<Space>();        
    }
    
    public ModelVersion(Model model, String label, long date, MetaModelVersion metaModelVersion) {
        this(-1, model, label, date, metaModelVersion);
    }
    
    public ModelVersion(Model model, long date, MetaModelVersion metaModelVersion) {
        this(-1, model, "", date, metaModelVersion);
        this.label = dateFormatter.format(new Date(date));
    }
    
    public ModelVersion(long id, String label, MetaModelVersion metaModelVersion) {
        this(id, null, label, 0, metaModelVersion);
    }
        
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;       
    }   
    
    public Model getModel() {
        return this.model;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public void addSpace(Space space) {
        this.spaces.add(space);    
    }
    
    public List<Space> getSpaces() {
        return this.spaces;
    }
    
    public long getDate() {
        return this.date;
    }
    
    public void setDate(long date) {
        this.date = DateUtils.truncate(new Date(date), Calendar.SECOND).getTime();
    }
    
    public MetaModelVersion getMetaModelVersion() {
        return this.metaModelVersion;
    }
    
    public void setMetaModelVersion(MetaModelVersion metaModelVersion) {
        this.metaModelVersion = metaModelVersion;
    }
    
     @Override
    public boolean equals(Object object) {
         if (! (object instanceof ModelVersion)) return false;
         if (this == object) return true;
        return equals((ModelVersion) object);                
     }

     @Override
     public int hashCode() {
         HashCodeBuilder hc = new HashCodeBuilder();
         hc.append(this.date);
        return hc.hashCode();         
     }
     
     @Override
     public String toString()  {
         ToStringBuilder sb = new ToStringBuilder(this);
         sb.append(this.label);
         sb.append(this.date);
         return sb.toString();
     }
          
     private boolean equals(ModelVersion version) {
         EqualsBuilder eq = new EqualsBuilder();
         eq.append(this.date, version.getDate());
         return eq.isEquals();
     }
}