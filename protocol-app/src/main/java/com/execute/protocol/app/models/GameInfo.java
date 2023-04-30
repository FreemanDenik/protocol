package com.execute.protocol.app.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    private int answer;
    private int event;
}