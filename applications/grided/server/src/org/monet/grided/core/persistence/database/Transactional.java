package org.monet.grided.core.persistence.database;

public interface Transactional {
    public void beginTransaction();
    public void commit();
    public void rollback();
}

