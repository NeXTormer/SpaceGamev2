package me.spacegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.screens.MainMenuScreen;

public class SpaceGame extends Game {

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {

	}
}
