package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Felix on 19-Jun-17.
 */

public abstract class MenuTemplate {

    protected Stage stage;
    protected MenuManager menuManager;


    protected MenuTemplate(MenuManager menuManager) {
        stage = new Stage();
        this.menuManager = menuManager;
    }

    public abstract void create();

    public MenuTemplate activate()
    {
        Gdx.input.setInputProcessor(stage);
        return this;
    }

    public abstract void draw();

    public abstract void dispose();

}
