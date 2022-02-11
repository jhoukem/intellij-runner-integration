package com.game;

import com.core.AppProperties;

import javax.inject.Inject;
import java.io.IOException;

public class GameApp {

    @Inject
    public GameApp() {
    }

    public void displayAppInfo() {
        try {
            System.out.println("The app version is " + AppProperties.getVersion());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
