package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Felix on 19-Jun-17.
 */

public class OptionsMenu extends TemplateMenu {

    private ImageButton backButton;

    public OptionsMenu(Menu menu)
    {
        super(menu);
    }

    @Override
    public void create() {
        backButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/backbtn.png")))));
        backButton.setPosition(900, 500);
        stage.addActor(backButton);
    }


    @Override
    public void draw() {
        stage.act();
        stage.draw();
        if(backButton.isPressed())
        {
            System.out.println("WERNER");
            menu.currentMenu = menu.screens.get("main").activate();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
