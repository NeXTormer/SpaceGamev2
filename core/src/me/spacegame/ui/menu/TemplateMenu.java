package me.spacegame.ui.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Felix on 19-Jun-17.
 */

public abstract class TemplateMenu {

    protected Stage stage;
    protected Menu menu;


    protected TemplateMenu(Menu menu) {
        stage = new Stage();
        this.menu = menu;
    }

    public abstract void create();

    public abstract void draw();

    public abstract void dispose();

}
