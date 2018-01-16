package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import me.spacegame.databases.Database;
import me.spacegame.screens.GameScreen;
import me.spacegame.screens.MainMenuScreen;
import me.spacegame.util.Scale;

/**
 * Created by Iris on 25-Jun-17.
 */

public class GameOverMenu extends TemplateMenu {


    private ImageButton retryBtn;
    private ImageButton mainMenuButton;
    private SpriteDrawable retryBtnDrawable;
    private SpriteDrawable mainMenuButtonDrawable;
    private Label gameoverText;
    private Label scoreText;

    private BitmapFont f_font1;
    private BitmapFont f_font2;

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

        vignetteImage = new Image(menu.getGameScreen().getGame().getTexture("vignetteEffect"));

        retryBtnDrawable = new SpriteDrawable(new Sprite(menu.getGameScreen().getGame().getTexture("retryButton")));
        mainMenuButtonDrawable = new SpriteDrawable(new Sprite(menu.getGameScreen().getGame().getTexture("homebutton")));


        vignetteImage.setSize(menu.getGameScreen().getCamera().viewportWidth, menu.getGameScreen().getCamera().viewportHeight);

        stage.getBatch().enableBlending();
        stage.addActor(vignetteImage);
    }




    @Override
    public void create() {
        retryBtn = new ImageButton(retryBtnDrawable);
        retryBtn.setPosition(Scale.getScaledSizeX(600), Scale.getScaledSizeY(150));
        retryBtn.setSize(Scale.getScaledSizeX(800), Scale.getScaledSizeY(300));

        mainMenuButton = new ImageButton(mainMenuButtonDrawable);
        mainMenuButton.setPosition(Scale.getScaledSizeX(1600), Scale.getScaledSizeY(880));
        mainMenuButton.setSize(Scale.getScaledSizeX(200), Scale.getScaledSizeX(200));

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
    public TemplateMenu activate()
    {
        Gdx.input.setInputProcessor(stage);
        stage.getBatch().setProjectionMatrix(menu.getGameScreen().getCamera().combined);



        int highScore = menu.getGameScreen().preferences.getInteger("highscore");
        int score = menu.getGameScreen().player.score;
        System.err.println("highscore: " + highScore + ", score: " + score);
        if(score > highScore)
        {
            highScore = score;
        }
        menu.getGameScreen().preferences.putInteger("highscore", highScore);
        menu.getGameScreen().preferences.flush();
        scoreText = new Label("Score: " + score + ", Highscore: " + highScore, new Label.LabelStyle(f_font1, Color.RED));
        scoreText.setPosition(Scale.getScaledSizeX(455), Scale.getScaledSizeY(500));

        gameoverText = new Label("Game Over", new Label.LabelStyle(f_font2, Color.RED));
        gameoverText.setPosition(Scale.getScaledSizeX(540), Scale.getScaledSizeY(600));

        stage.addActor(gameoverText);
        stage.addActor(scoreText);

        Database db = menu.getGameScreen().getGame().database;

        if(db.connectionEstablished)
        {
            int userid = db.GetUserID(menu.getGameScreen().getGame().username);

            db.AddScore(userid ,score);
        }
        else
        {
            System.out.println("[Error] Database not connected!");
        }

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
        if(mainMenuButton.isPressed())
        {
            menu.getGameScreen().getGame().setScreen(new MainMenuScreen(menu.getGameScreen().getGame()));
        }

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
