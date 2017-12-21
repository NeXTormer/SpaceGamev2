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
    private SpriteDrawable retryBtnDrawable;
    //private static Label gameoverText;
    private Image gameoverText;
    private Label scoreText;

    public static void loadText()
    {
        //FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        //FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //parameter.size = 270;



        long peta = System.currentTimeMillis();
        //gameoverText = new Label("Game Over", new Label.LabelStyle(ftfg.generateFont(parameter), Color.RED));
        System.out.println("Game Over label loading time: " + (System.currentTimeMillis() - peta));
    }

    private Image vignetteImage;

    public GameOverMenu(Menu menu)
    {
        super(menu);
        //stage.getBatch().setProjectionMatrix(menu.camera.combined);

        gameoverText = new Image(menu.getGameScreen().getGame().getTexture("gameover"));

        vignetteImage = new Image(menu.getGameScreen().getGame().getTexture("vignetteEffect"));

        retryBtnDrawable = new SpriteDrawable(new Sprite(menu.getGameScreen().getGame().getTexture("retryButton")));


        vignetteImage.setSize(menu.getGameScreen().getCamera().viewportWidth, menu.getGameScreen().getCamera().viewportHeight);

        stage.getBatch().enableBlending();
        stage.addActor(vignetteImage);
    }




    @Override
    public void create() {
        retryBtn = new ImageButton(retryBtnDrawable);
        retryBtn.setPosition(getScaledSizeX(600), getScaledSizeY(150));
        retryBtn.setSize(getScaledSizeX(), getScaledSizeY());

        stage.addActor(retryBtn);
        gameoverText.setPosition(getScaledSizeX(350), getScaledSizeY(600));
        stage.addActor(gameoverText);
    }

    @Override
    public TemplateMenu activate()
    {
        Gdx.input.setInputProcessor(stage);
        stage.getBatch().setProjectionMatrix(menu.getGameScreen().getCamera().combined);

        FreeTypeFontGenerator ftfg2 = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 80;

        int highScore = menu.getGameScreen().preferences.getInteger("highscore");
        int score = menu.getGameScreen().player.score;
        System.err.println("highscore: " + highScore + ", score: " + score);
        if(score > highScore)
        {
            highScore = score;
        }
        menu.getGameScreen().preferences.putInteger("highscore", highScore);
        menu.getGameScreen().preferences.flush();
        scoreText = new Label("Score: " + score + ", Highscore: " + highScore ,new Label.LabelStyle(ftfg2.generateFont(parameter2), Color.RED));
        scoreText.setPosition(getScaledSizeX(500), getScaledSizeY(500));
        stage.addActor(scoreText);
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

    public float getScaledSizeX(float defaultSize)
    {
        return (defaultSize / 1080.0f) * menu.getGameScreen().getCamera().viewportHeight;
    }

    public float getScaledSizeY(float defaultSize)
    {
        return (defaultSize / 1920.0f) * menu.getGameScreen().getCamera().viewportWidth;
    }


}
