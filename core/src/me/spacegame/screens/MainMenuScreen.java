package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import me.spacegame.SpaceGame;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 08-May-17.
 */

public class MainMenuScreen implements Screen {

    private SpaceGame game;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture background;
    private Texture logo;
    private Texture startbuttonup;
    private Texture startbuttondown;

    private SpriteDrawable startButtonUpDrawable;
    private SpriteDrawable startButtonDownDrawable;

    private Stage stage;
    private ImageButton startbtn;


    public MainMenuScreen(SpaceGame game)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        batch = new SpriteBatch(4);
        game.finishLoadingAssets();
        background = game.getTexture("background");
        logo = game.getTexture("logo");
        startbuttonup = game.getTexture("startButtonUp");
        startbuttondown = game.getTexture("startButtonDown");

        startButtonUpDrawable = new SpriteDrawable();
        startButtonDownDrawable = new SpriteDrawable();

        startButtonUpDrawable.setSprite(new Sprite(startbuttonup));
        startButtonDownDrawable.setSprite(new Sprite(startbuttondown));

        startbtn = new ImageButton(startButtonUpDrawable, startButtonDownDrawable);

        startbtn.setPosition(Scale.getScaledSizeX(580), Scale.getScaledSizeY(250));

        stage = new Stage();

        startbtn.setSize(Scale.getScaledSizeX(800), Scale.getScaledSizeY(300));
        stage.addActor(startbtn);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(startbtn.isPressed())
        {
            game.setScreen(new GameScreen(game));
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(logo, Scale.getScaledSizeX(23), Scale.getScaledSizeY(900), Scale.getScaledSizeX(1276 * 2), Scale.getScaledSizeY(142 * 2));
        batch.end();

        stage.draw();

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
        startbuttonup.dispose();
        startbuttondown.dispose();
        stage.dispose();
    }




}
