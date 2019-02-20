package org.monet.grided.core.persistence.filesystem;

public interface HasData<T extends Data> {    
    public long getId();
    public T getData();  
    public  void setData(T data); 
}

