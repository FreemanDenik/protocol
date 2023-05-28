package com.execute.protocol.core.models;

import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.User;

/**
 * Этот класс больше практика чем какое-то проф решение
 */
public class RestartGame implements Runnable{
    User user;
    public RestartGame(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        user.setTarget(Target.generate());
        user.setAddEvents(null);
        user.setCurrentEvent(0);
        user.setAddCategories(null);
        user.setOnceAnswer(null);
        user.setOnceEvents(null);
    }
}
