package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;

/**
 * Created by Felix on 19-Jun-17.
 */

public class EmptyMenu extends TemplateMenu {


    public EmptyMenu(Menu menu) { super(menu); }

    @Override
    public void create() { }

    @Override
    public TemplateMenu activate()
    {
        Gdx.input.setInputProcessor(menu.getGameScreen().getInputMultiplexer());
        return this;
    }

    @Override
    public void draw() { }

    @Override
    public void dispose() { }
}
