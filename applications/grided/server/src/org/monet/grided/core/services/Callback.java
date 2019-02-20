package org.monet.grided.core.services;

public interface Callback<T> {    
    public void onResult(T result);    
    public void onFailure(Throwable throwable);
}

