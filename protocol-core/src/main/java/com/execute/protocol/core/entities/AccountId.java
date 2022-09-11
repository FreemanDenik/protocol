package com.execute.protocol.core.entities;

import com.execute.protocol.core.enums.EnumProviders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class AccountId implements Serializable {
    private static final long serialVersionUID = 3004441549811525440L;
    @Column
    private Long clientId;
    @Column
    @Enumerated(EnumType.STRING)
    private EnumProviders provider;

    public String toString(){
        return clientId + "." + provider.name();
    }
    public AccountId stringTo(String str){
        String[] arr = str.split("\\.");
        clientId = Long.parseLong(arr[0]);
        provider = EnumProviders.valueOf(arr[1]);
        return this;
    }
}
