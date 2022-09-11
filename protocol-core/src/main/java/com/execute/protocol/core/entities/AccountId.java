package com.execute.protocol.core.entities;

import com.execute.protocol.core.enums.EnumProviders;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class AccountId implements Serializable {
    private static final long serialVersionUID = 3004441549811525440L;
    @Column
    private Long clientId;
    @Column
    @Enumerated(EnumType.STRING)
    private EnumProviders provider;

    public AccountId(Long clientId, EnumProviders provider) {
        this.clientId = clientId;
        this.provider = provider;
    }

    public AccountId() {

    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public EnumProviders getProvider() {
        return provider;
    }

    public void setProvider(EnumProviders provider) {
        this.provider = provider;
    }
}
