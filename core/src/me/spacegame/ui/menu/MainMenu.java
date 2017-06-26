package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Felix on 19-Jun-17.
 */

public class MainMenu extends TemplateMenu {

    private ImageButton startbtn;

    private Texture startbuttonup;
    private Texture startbuttondown;

    private SpriteDrawable startButtonUpDrawable;
    private SpriteDrawable startButtonDownDrawable;

    private ImageButton mutebutton;
    private long[] timers = new long[2];
    public boolean vibration = true;


    private FreeTypeFontGenerator ftfg;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Label vibrationText;

    public MainMenu(Menu menu)
    {
        super(menu);
        stage.getBatch().setProjectionMatrix(menu.camera.combined);
    }

    @Override
    public void create() {
        startbuttonup = new Texture(Gdx.files.internal("ui/resumebtn.png"));
        startbuttondown = new Texture(Gdx.files.internal("ui/resumebtn.png"));

        startButtonUpDrawable = new SpriteDrawable();
        startButtonDownDrawable = new SpriteDrawable();

        startButtonUpDrawable.setSprite(new Sprite(startbuttonup));
        startButtonDownDrawable.setSprite(new Sprite(startbuttondown));

        startbtn = new ImageButton(startButtonUpDrawable, startButtonDownDrawable);

        mutebutton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/camshakeoff.png")))), new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/camshakeoff.png")))));
        mutebutton.setPosition(900, 500);

        startbtn.setPosition(895, 180);

        stage.addActor(startbtn);
        stage.addActor(mutebutton);

        ftfg = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 140;

        vibrationText = new Label("Vibration", new Label.LabelStyle(ftfg.generateFont(parameter), Color.GREEN));
        vibrationText.setPosition(990, 570);
        stage.addActor(vibrationText);
    }

    @Override
    public TemplateMenu activate()
    {
        Gdx.input.setInputProcessor(stage);
        return this;
    }

    @Override
    public void draw() {
        stage.act();
        stage.draw();

        if(mutebutton.isPressed())
        {
            if(System.currentTimeMillis() - timers[0] > 1000)
            {
                vibration= !vibration;
                timers[0] = System.currentTimeMillis();

                //Pfusch, geht aber
                if(vibration)
                {
                    mutebutton.remove();
                    vibrationText.remove();
                    vibrationText = new Label("Vibration", new Label.LabelStyle(ftfg.generateFont(parameter), Color.GREEN));
                    vibrationText.setPosition(990, 570);
                    stage.addActor(vibrationText);
                    stage.addActor(mutebutton);

                    menu.getGameScreen().vibration = true;
                }
                else
                {
                    mutebutton.remove();
                    vibrationText.remove();
                    vibrationText = new Label("Vibration", new Label.LabelStyle(ftfg.generateFont(parameter), Color.RED));
                    vibrationText.setPosition(990, 570);
                    stage.addActor(vibrationText);
                    stage.addActor(mutebutton);

                    menu.getGameScreen().vibration = false;
                }
            }
        }
        if(startbtn.isPressed())
        {
            if(System.currentTimeMillis() - timers[1] > 1000)
            {
                menu.currentMenu = menu.screens.get("empty").activate();
                menu.getGameScreen().paused = false;
                timers[1] = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
