package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    private boolean muted = false;

    public MainMenu(Menu menu)
    {
        super(menu);
        stage.getBatch().setProjectionMatrix(menu.camera.combined);
        //menu.getGameScreen().getInputMultiplexer().addProcessor(stage);
        //Gdx.input.setInputProcessor(menu.getGameScreen().getInputMultiplexer());
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

        mutebutton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/mutebutton.png")))), new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/mutebuttonon.png")))));
        mutebutton.setPosition(900, 500);

        startbtn.setPosition(895, 180);

        stage.addActor(startbtn);
        stage.addActor(mutebutton);
        //Gdx.input.setInputProcessor(stage);
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
                muted = !muted;
                mutebutton.setChecked(muted);
                timers[0] = System.currentTimeMillis();
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
