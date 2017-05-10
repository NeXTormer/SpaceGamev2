package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.SpaceGame;
import me.spacegame.gameobjects.Background;
import me.spacegame.gameobjects.Meteor;

/**
 * Created by Felix on 09-May-17.
 */

public class GameScreen implements Screen {


    private SpaceGame game;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Background background;
    private Meteor meteor;

    public GameScreen(SpaceGame game)
    {
        this.game = game;
        background = new Background("background.png");
        meteor = new Meteor();
        batch = new SpriteBatch(20);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        System.err.println(Gdx.gl.glGetError());

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(delta, batch);
        meteor.render(delta, batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        background.dispose();
        meteor.dispose();
    }
}
