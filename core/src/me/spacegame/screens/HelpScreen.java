package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.spacegame.SpaceGame;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 08-May-17.
 */

public class HelpScreen implements Screen {

    private MainMenuScreen menuscreen;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture slides[];

    private int index = 0;

    public HelpScreen(MainMenuScreen gamescreen) {
        this.menuscreen = gamescreen;
        this.batch = gamescreen.batch;
        slides = new Texture[3];

        slides[0] = SpaceGame.getInstance().getTexture("cs1");
        slides[1] = SpaceGame.getInstance().getTexture("cs2");
        slides[2] = SpaceGame.getInstance().getTexture("cs3");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Scale.getScaledSizeX(1920), Scale.getScaledSizeY(1080));
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta)
    {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(slides[index], 0, 0, Scale.getScaledSizeX(1920), Scale.getScaledSizeY(1080));
        batch.end();

        if(Gdx.input.justTouched())
        {
            index++;
            if(index == (slides.length))
            {
                menuscreen.getGame().setScreen(menuscreen);
            }
            index %= slides.length;
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }




}
