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

    public MainMenu(Menu menu)
    {
        super(menu);
        stage.getBatch().setProjectionMatrix(menu.camera.combined);
    }

    @Override
    public void create() {
        startbuttonup = new Texture(Gdx.files.internal("mainmenu/startbtnup.png"));
        startbuttondown = new Texture(Gdx.files.internal("mainmenu/startbtndown.png"));

        startButtonUpDrawable = new SpriteDrawable();
        startButtonDownDrawable = new SpriteDrawable();

        startButtonUpDrawable.setSprite(new Sprite(startbuttonup));
        startButtonDownDrawable.setSprite(new Sprite(startbuttondown));

        startbtn = new ImageButton(startButtonUpDrawable, startButtonDownDrawable);

        startbtn.setPosition(580, 250);

        stage.addActor(startbtn);
        menu.getGameScreen().getInputMultiplexer().addProcessor(stage);
        //Gdx.input.setInputProcessor(menu.getGameScreen().getInputMultiplexer());
    }

    @Override
    public void draw() {
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
