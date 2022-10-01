package com.execute.protocol.app.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    private long answer;
    private long event;
}