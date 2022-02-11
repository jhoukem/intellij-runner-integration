package com.game;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component()
public interface GameComponent {

    GameApp build();

}
