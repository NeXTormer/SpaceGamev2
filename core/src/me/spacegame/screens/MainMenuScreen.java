package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.Console;

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
    private TextField textField;
    private Label usernameLabel;
    private Label infoLabel;

    private Preferences preferences = Gdx.app.getPreferences("sgusername");



    public MainMenuScreen(SpaceGame game) {
        this.game = game;


        FreeTypeFontGenerator ftfg2 = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = (int) Scale.getScaledSizeX(64);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = (int) Scale.getScaledSizeX(42);

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

        startbtn.setPosition(Scale.getScaledSizeX(625), Scale.getScaledSizeY(120));

        stage = new Stage();

        startbtn.setSize(Scale.getScaledSizeX(800), Scale.getScaledSizeY(300));
        stage.addActor(startbtn);

        usernameLabel = new Label("Username: ", new Label.LabelStyle(ftfg2.generateFont(parameter2), Color.RED));
        usernameLabel.setPosition(Scale.getScaledSizeX(550), Scale.getScaledSizeY(657));

        infoLabel = new Label("Click to edit username", new Label.LabelStyle(ftfg2.generateFont(parameter3), Color.RED));
        infoLabel.setPosition(Scale.getScaledSizeX(840), Scale.getScaledSizeY(610));


        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();

        tfs.font = ftfg2.generateFont(parameter2);
        tfs.fontColor = Color.RED;

        String prefuser = preferences.getString("username");

        String username;
        if (prefuser == "")
        {
            username = "DefaultUser";
        }
        else
        {
            username = prefuser;
        }

        textField = new TextField(username, tfs);
        textField.setPosition(Scale.getScaledSizeX(920), Scale.getScaledSizeY(645));
        textField.setSize(600, 100);

        textField.setMaxLength(16);


        if(username.equalsIgnoreCase("DefaultUser"))
        {
            stage.addActor(infoLabel);
        }

        stage.addActor(usernameLabel);
        stage.addActor(textField);

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
            String username = textField.getText();
            game.username = username;
            preferences.putString("username", username);
            preferences.flush();
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);

        batch.draw(logo, Scale.getScaledSizeX(400), Scale.getScaledSizeY(800), Scale.getScaledSizeX(920 * 1.4f), Scale.getScaledSizeY(102 * 1.4f));
        //batch.draw(logo, 40, 1200);
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
