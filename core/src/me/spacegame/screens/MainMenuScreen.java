package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

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
        background = new Texture(Gdx.files.internal("mainmenu/background1.png"));
        logo = new Texture(Gdx.files.internal("mainmenu/logo.png"));
        startbuttonup = new Texture(Gdx.files.internal("mainmenu/startbtnup.png"));
        startbuttondown = new Texture(Gdx.files.internal("mainmenu/startbtndown.png"));

        startButtonUpDrawable = new SpriteDrawable();
        startButtonDownDrawable = new SpriteDrawable();

        startButtonUpDrawable.setSprite(new Sprite(startbuttonup));
        startButtonDownDrawable.setSprite(new Sprite(startbuttondown));

        startbtn = new ImageButton(startButtonUpDrawable, startButtonDownDrawable);

        startbtn.setPosition(580, 250);

        stage = new Stage();
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
            dispose();
        }

        batch.setProjectionMatrix(camera.combined);
        //stage.getBatch().setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(logo, 282, 700);
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
    }




}
