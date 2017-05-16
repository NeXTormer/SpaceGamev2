package me.spacegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.screens.MainMenuScreen;

public class SpaceGame extends Game {

	public final static int VIEWPORTWIDTH = 1920;
	public final static int VIEWPORTHEIGHT = 1080;

	SpriteBatch batch;
	Texture img;


	@Override
	public void create () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {

	}
}
