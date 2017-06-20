package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

    private ImageButton optionsbutton;
    private long[] timers = new long[2];

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

        optionsbutton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/optionsbtn.png")))));
        optionsbutton.setPosition(900, 500);

        startbtn.setPosition(895, 180);

        stage.addActor(startbtn);
        stage.addActor(optionsbutton);
        //Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void draw() {
        stage.act();
        stage.draw();

        if(optionsbutton.isPressed())
        {
            if(System.currentTimeMillis() - timers[0] > 100)
            {
                menu.currentMenu = menu.screens.get("options").activate();
                timers[0] = System.currentTimeMillis();
            }
        }
        if(startbtn.isPressed())
        {
            if(System.currentTimeMillis() - timers[1] > 100)
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
