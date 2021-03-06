package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import me.spacegame.screens.GameScreen;

/**
 * Created by Felix on 19-Jun-17.
 */

public class MainMenu extends MenuTemplate {

    private ImageButton resumeButton;

    private Texture startbuttonup;
    private Texture startbuttondown;

    private SpriteDrawable startButtonUpDrawable;
    private SpriteDrawable startButtonDownDrawable;

    private ImageButton retryButton;
    private long[] timers = new long[2];
    public boolean vibration = true;


    private FreeTypeFontGenerator ftfg;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    //private Label vibrationText;

    public MainMenu(MenuManager menuManager)
    {
        super(menuManager);
        stage.getBatch().setProjectionMatrix(menuManager.camera.combined);
    }

    @Override
    public void create() {
        startbuttonup = menuManager.getGameScreen().getGame().getTexture("resumeButtonMenu");
        startbuttondown = menuManager.getGameScreen().getGame().getTexture("resumeButtonMenu");

        startButtonUpDrawable = new SpriteDrawable();
        startButtonDownDrawable = new SpriteDrawable();

        startButtonUpDrawable.setSprite(new Sprite(startbuttonup));
        startButtonDownDrawable.setSprite(new Sprite(startbuttondown));

        resumeButton = new ImageButton(startButtonUpDrawable, startButtonDownDrawable);

        retryButton = new ImageButton(new SpriteDrawable(new Sprite(menuManager.getGameScreen().getGame().getTexture("retryButton"))), new SpriteDrawable(new Sprite(menuManager.getGameScreen().getGame().getTexture("retryButton"))));
        retryButton.setPosition(900, 500);

        resumeButton.setPosition(895, 180);

        stage.addActor(resumeButton);
        stage.addActor(retryButton);

        ftfg = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 140;
    }

    @Override
    public MenuTemplate activate()
    {
        Gdx.input.setInputProcessor(stage);
        return this;
    }

    @Override
    public void draw()
    {
        stage.act();
        stage.draw();

        if(retryButton.isPressed())
        {
            if(System.currentTimeMillis() - timers[0] > 1000)
            {
                timers[0] = System.currentTimeMillis();
                menuManager.getGameScreen().getGame().setScreen(new GameScreen(menuManager.getGameScreen().getGame()));
            }
        }
        if(resumeButton.isPressed())
        {
            if(System.currentTimeMillis() - timers[1] > 1000)
            {
                menuManager.currentMenu = menuManager.screens.get("empty").activate();
                menuManager.getGameScreen().paused = false;
                timers[1] = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
