package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import me.spacegame.screens.GameScreen;

/**
 * Created by Iris on 25-Jun-17.
 */

public class GameOverMenu extends TemplateMenu {


    private ImageButton retryBtn;
    private static SpriteDrawable retryBtnDrawable;
    private Label gameoverText;
    private Image vignetteImage;

    public GameOverMenu(Menu menu)
    {
        super(menu);
        //stage.getBatch().setProjectionMatrix(menu.camera.combined);

        Skin skin = new Skin();

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 270;


        vignetteImage = new Image(new Texture(Gdx.files.internal("ui/vignette.png")));


        gameoverText = new Label("Game Over", new Label.LabelStyle(ftfg.generateFont(parameter), Color.RED));
        stage.getBatch().enableBlending();
        stage.addActor(vignetteImage);
    }

    static
    {
        retryBtnDrawable = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/retrybutton.png"))));
    }

    @Override
    public void create() {
        retryBtn = new ImageButton(retryBtnDrawable);
        retryBtn.setPosition(600, 300);

        stage.addActor(retryBtn);
        gameoverText.setPosition(300, 600);
        stage.addActor(gameoverText);
    }

    @Override
    public TemplateMenu activate()
    {
        Gdx.input.setInputProcessor(stage);
        stage.getBatch().setProjectionMatrix(menu.getGameScreen().getCamera().combined);
        return this;
    }

    @Override
    public void draw() {
        stage.act();
        stage.draw();

        if(retryBtn.isPressed())
        {
            menu.getGameScreen().getGame().setScreen(new GameScreen(menu.getGameScreen().getGame()));
        }

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
