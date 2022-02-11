package com.game;

public class Main {
    public static void main(String[] args) {
        GameApp gameApp = DaggerGameComponent.create().build();
        gameApp.displayAppInfo();
    }
}
