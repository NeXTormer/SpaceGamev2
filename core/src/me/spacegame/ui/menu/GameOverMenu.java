package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import me.spacegame.databases.API;
import me.spacegame.screens.GameScreen;
import me.spacegame.screens.MainMenuScreen;
import me.spacegame.util.Scale;

/**
 * Created by Iris on 25-Jun-17.
 */

public class GameOverMenu extends MenuTemplate {


    private ImageButton retryBtn;
    private ImageButton mainMenuButton;
    private ImageButton scoreBtn;

    private SpriteDrawable scoreButtonDrawable;
    private SpriteDrawable retryBtnDrawable;
    private SpriteDrawable mainMenuButtonDrawable;
    private Label gameoverText;
    private Label scoreText;

    private BitmapFont f_font1;
    private BitmapFont f_font2;

    private Image vignetteImage;

    private long lastButtonPress = 0;


    public GameOverMenu(MenuManager menuManager)
    {
        super(menuManager);
        //stage.getBatch().setProjectionMatrix(menuManager.camera.combined);

        vignetteImage = new Image(menuManager.getGameScreen().getGame().getTexture("vignetteEffect"));

        retryBtnDrawable = new SpriteDrawable(new Sprite(menuManager.getGameScreen().getGame().getTexture("retryButton")));
        mainMenuButtonDrawable = new SpriteDrawable(new Sprite(menuManager.getGameScreen().getGame().getTexture("homebutton")));

        scoreButtonDrawable = new SpriteDrawable(new Sprite(menuManager.getGameScreen().getGame().getTexture("highscorebutton")));
        scoreBtn = new ImageButton(scoreButtonDrawable);

        scoreBtn.setSize(Scale.getScaledSizeX(170), Scale.getScaledSizeY(170));
        scoreBtn.setPosition(Scale.getScaledSizeX(1700), Scale.getScaledSizeY(300));

        vignetteImage.setSize(menuManager.getGameScreen().getCamera().viewportWidth, menuManager.getGameScreen().getCamera().viewportHeight);

        stage.getBatch().enableBlending();
        stage.addActor(vignetteImage);
        stage.addActor(scoreBtn);
    }




    @Override
    public void create() {
        retryBtn = new ImageButton(retryBtnDrawable);
        retryBtn.setPosition(Scale.getScaledSizeX(600), Scale.getScaledSizeY(150));
        retryBtn.setSize(Scale.getScaledSizeX(800), Scale.getScaledSizeY(300));

        mainMenuButton = new ImageButton(mainMenuButtonDrawable);
        mainMenuButton.setPosition(Scale.getScaledSizeX(1700), Scale.getScaledSizeY(50));
        mainMenuButton.setSize(Scale.getScaledSizeX(170), Scale.getScaledSizeX(170));

        stage.addActor(mainMenuButton);
        stage.addActor(retryBtn);

        FreeTypeFontGenerator ftfg2 = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = (int) Scale.getScaledSizeY(80);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = (int) Scale.getScaledSizeY(130);

        f_font1 = ftfg2.generateFont(parameter2);
        f_font2 = ftfg2.generateFont(parameter3);
    }

    @Override
    public MenuTemplate activate()
    {
        Gdx.input.setInputProcessor(stage);
        stage.getBatch().setProjectionMatrix(menuManager.getGameScreen().getCamera().combined);



        int highScore = menuManager.getGameScreen().preferences.getInteger("highscore");
        int score = menuManager.getGameScreen().player.score;
        System.err.println("highscore: " + highScore + ", score: " + score);
        if(score > highScore)
        {
            highScore = score;
        }
        menuManager.getGameScreen().preferences.putInteger("highscore", highScore);
        menuManager.getGameScreen().preferences.flush();
        scoreText = new Label("Score: " + score + ", Highscore: " + highScore, new Label.LabelStyle(f_font1, Color.RED));
        scoreText.setPosition(Scale.getScaledSizeX(455), Scale.getScaledSizeY(500));

        gameoverText = new Label("Game Over", new Label.LabelStyle(f_font2, Color.RED));
        gameoverText.setPosition(Scale.getScaledSizeX(540), Scale.getScaledSizeY(600));

        stage.addActor(gameoverText);
        stage.addActor(scoreText);

        //API.send("https://games.htl-klu.at/anyway/addscore/9F412BDFA1D49B0D80/" + menuManager.getGameScreen().getGame().username + "/" + score + "/spacegame");

        return this;
    }

    @Override
    public void draw() {
        stage.act();
        stage.draw();

        if(retryBtn.isPressed())
        {
            menuManager.getGameScreen().getGame().setScreen(new GameScreen(menuManager.getGameScreen().getGame()));
        }
        if(mainMenuButton.isPressed())
        {
            menuManager.getGameScreen().getGame().setScreen(new MainMenuScreen(menuManager.getGameScreen().getGame()));
        }
        if(scoreBtn.isPressed())
        {
            if(System.currentTimeMillis() - lastButtonPress > 3000)
            {
                Gdx.net.openURI("https://games.htl-klu.at/anyway/playerhighscores.html");
                lastButtonPress = System.currentTimeMillis();
            }
        }

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
