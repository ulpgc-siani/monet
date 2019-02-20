package org.monet.space.mobile.syncservice;


import org.monet.space.mobile.R;

public enum SynchronizationFailure {

    DEFAULT(R.string.sync_disabled_content), INCORRECT_CREDENTIALS(R.string.sync_incorrect_credentials);

    private final int cause;

    SynchronizationFailure(int cause) {
        this.cause = cause;
    }

    public int causeStringId() {
        return cause;
    }
}
