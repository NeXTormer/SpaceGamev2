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
    public SpriteBatch batch;

    private Texture background;
    private Texture logo;
    private Texture startbuttonup;
    private Texture startbuttondown;
    private Texture helpButton;

    private SpriteDrawable startButtonUpDrawable;
    private SpriteDrawable startButtonDownDrawable;
    private SpriteDrawable helpButtonDrawable;

    public Stage stage;

    private ImageButton startbtn;
    private ImageButton helpbtn;
    private TextField textField;
    private Label usernameLabel;
    private Label infoLabel;

    private float logoscalex;
    private float logoscaley;


    private Preferences preferences = Gdx.app.getPreferences("sgusername");

    public MainMenuScreen(SpaceGame game) {
        this.game = game;


        FreeTypeFontGenerator ftfg2 = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = (int) Scale.getScaledSizeX(64);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = (int) Scale.getScaledSizeX(42);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Scale.getScaledSizeX(1920), Scale.getScaledSizeY(1080));
        batch = new SpriteBatch(4);
        game.finishLoadingAssets();
        background = game.getTexture("background");
        logo = game.getTexture("logo");
        startbuttonup = game.getTexture("startButtonUp");
        startbuttondown = game.getTexture("startButtonDown");
        helpButton = game.getTexture("helpbutton");

        startButtonUpDrawable = new SpriteDrawable();
        startButtonDownDrawable = new SpriteDrawable();
        helpButtonDrawable = new SpriteDrawable();

        startButtonUpDrawable.setSprite(new Sprite(startbuttonup));
        startButtonDownDrawable.setSprite(new Sprite(startbuttondown));
        helpButtonDrawable.setSprite(new Sprite(helpButton));

        startbtn = new ImageButton(startButtonUpDrawable, startButtonDownDrawable);
        helpbtn = new ImageButton(helpButtonDrawable);

        startbtn.setPosition(Scale.getScaledSizeX(625), Scale.getScaledSizeY(120));

        helpbtn.setPosition(Scale.getScaledSizeX(100), Scale.getScaledSizeY(550));
        helpbtn.setSize(Scale.getScaledSizeX(170), Scale.getScaledSizeY(170));

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

        float offset = ((Gdx.graphics.getHeight()/1080.0f) - 1) * 60.0f;

        textField.setPosition(Scale.getScaledSizeX(920), Scale.getScaledSizeY(645) + offset);
        textField.setSize(600, 100);

        textField.setMaxLength(16);


        if(username.equalsIgnoreCase("DefaultUser"))
        {
            stage.addActor(infoLabel);
        }

        stage.addActor(usernameLabel);
        stage.addActor(textField);

        Gdx.input.setInputProcessor(stage);

        if(Scale.getScaledSizeX(170 * 11f) > 1870)
        {
               logoscalex = 1870;
               logoscaley = 176;
        }
        else
        {
            logoscalex = Scale.getScaledSizeX(170 * 11f);
            logoscaley = Scale.getScaledSizeY(16 * 11f);
        }
        stage.addActor(helpbtn);
    }

    @Override
    public void show() {

    }

    public SpaceGame getGame() { return game; }

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
        if(helpbtn.isPressed())
        {
            game.setScreen(new HelpScreen(this));
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);

        batch.draw(logo, Scale.getScaledSizeX(100), Scale.getScaledSizeY(800), logoscalex, logoscaley);
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
