package com.execute.protocol.auth.models;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;


import javax.persistence.Id;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("StgToken")
public class StgToken implements Serializable {

    private String id;
    private String refreshToken;
}
