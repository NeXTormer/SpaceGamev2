package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.SpaceGame;

/**
 * Created by Felix on 08-May-17.
 */

public class MainMenuScreen implements Screen {

    private SpaceGame game;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture background;
    private Texture logo;
    private Texture startbutton;

    public MainMenuScreen(SpaceGame game)
    {
        this.game = game;

        camera = new OrthographicCamera(1920, 1080);
        batch = new SpriteBatch(4);
        background = new Texture(Gdx.files.internal("mainmenu/background.png"));
        logo = new Texture(Gdx.files.internal("mainmenu/logo.png"));
        startbutton= new Texture(Gdx.files.internal("mainmenu/startbutton.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0);
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
        logo.dispose();
        startbutton.dispose();
    }




}
