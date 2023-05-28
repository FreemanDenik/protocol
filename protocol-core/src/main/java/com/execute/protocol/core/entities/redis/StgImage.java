package com.execute.protocol.core.entities.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("StgImage")
public class StgImage implements Serializable {

    private String id;
    private String base64;
}
