package com.execute.protocol.core.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AccounId implements Serializable {
    private static final long serialVersionUID = 3004441549811525440L;
    private long Id;
    private String provider;

    public AccounId(long id, String provider) {
        Id = id;
        this.provider = provider;
    }

    public AccounId() {

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
