package me.spacegame.ui.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by Felix on 19-Jun-17.
 */

public class MainMenu extends TemplateMenu {

    private TextArea text;
    private TextField.TextFieldStyle style;

    public MainMenu(Menu menu)
    {
        super(menu);
        style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();

        text = new TextArea("peta", style);
        stage.getBatch().setProjectionMatrix(menu.camera.combined);
    }

    @Override
    public void create() {
        stage.addActor(text);
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
