package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.List;

import me.spacegame.SpaceGame;
import me.spacegame.gameobjects.Background;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;

/**
 * Created by Felix on 09-May-17.
 */

public class GameScreen implements Screen {


    private SpaceGame game;

    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private List<Meteor> meteors = new ArrayList<Meteor>();


    private Background background;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadskin;
    private Drawable touchknob;
    private Drawable touchbackground;
    private Player player;


    public GameScreen(SpaceGame game)
    {
        this.game = game;
        background = new Background("gameobjects/background.png");


        batch = new SpriteBatch();
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        Gdx.input.setInputProcessor(stage);

        touchpadskin = new Skin();
        touchpadStyle = new Touchpad.TouchpadStyle();

        touchpadskin.add("touchBackground", new Texture("touchpad/touchbackground.png"));
        touchpadskin.add("touchKnob", new Texture("touchpad/touchknob.png"));

        touchbackground = touchpadskin.getDrawable("touchBackground");
        touchknob = touchpadskin.getDrawable("touchKnob");


        touchpadStyle.background = touchbackground;
        touchpadStyle.knob = touchknob;

        touchpad = new Touchpad(0, touchpadStyle);
        touchpad.setBounds(90, 90, 400, 400);

        stage.addActor(touchpad);

        for(int i = 0; i < 10; i++)
        {
            meteors.add(new Meteor());
        }


        player = new Player();
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //update
        batch.setProjectionMatrix(camera.combined);
        player.updatePosition(touchpad);

        for(int i = 0; i < meteors.size(); i++)
        {
            if(meteors.get(i).x < -meteors.get(i).radius)
            {
                meteors.get(i).dispose();
                meteors.remove(i);
                meteors.add(new Meteor());
            }
        }


        //render
        batch.begin();
        background.render(delta, batch);
        for(Meteor m : meteors)
        {
            m.render(delta, batch);
        }
        player.render(delta, batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        background.dispose();

        for(int i = 0; i < meteors.size(); i++)
        {
            meteors.get(i).dispose();
        }
    }
}
