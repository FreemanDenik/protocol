package com.execute.protocol.core.enums;

public enum EmTarget {
    GOLD("Золото"),
    REPUTATION("Репутация"),
    INFLUENCE("Влияние"),
    SHADOW("Скрытность"),
    LUCK("Удача"),
    THING("Вещь инвентаря");
    String title;
    EmTarget(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}