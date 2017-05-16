package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
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
import me.spacegame.gameobjects.Rocket;

/**
 * Created by Felix on 09-May-17.
 */

public class GameScreen implements Screen, InputProcessor {


    private SpaceGame game;

    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private List<Meteor> meteors = new ArrayList<Meteor>();
    private List<Rocket> rockets = new ArrayList<Rocket>();


    private Background background;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadskin;
    private Drawable touchknob;
    private Drawable touchbackground;
    private Player player;
    InputMultiplexer inputMultiplexer;


    public GameScreen(SpaceGame game) {
        this.game = game;
        background = new Background("gameobjects/background.png");


        batch = new SpriteBatch();
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);


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

        for (int i = 0; i < 10; i++) {
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


        for (int i = 0; i < meteors.size(); i++) {
            if (Intersector.overlaps(meteors.get(i).box, player.box)) {
                player.health -= meteors.get(i).damage;
                if (player.health <= 0) {
                    gameOver();
                }
            }

            if (meteors.get(i).x < -meteors.get(i).radius) {
                meteors.get(i).dispose();
                meteors.remove(i);
                meteors.add(new Meteor());
            }
        }
        for (int i = 0; i < rockets.size(); i++) {
            if (rockets.get(i).x >= SpaceGame.VIEWPORTWIDTH) {
                rockets.get(i).dispose();
                rockets.remove(rockets.get(i));
            }
        }

        for (int i = 0; i < rockets.size(); i++) {
            for (int j = 0; j < meteors.size(); j++) {
                if (Intersector.overlaps(meteors.get(j).box, rockets.get(i).box)) {
                    rockets.get(i).dispose();
                    rockets.remove(rockets.get(i));
                    meteors.get(j).health -= 30;
                    if (meteors.get(j).health <= 0) {
                        meteors.get(j).dispose();
                        meteors.remove(j);
                        meteors.add(new Meteor());
                    }
                    break;
                }
            }
        }


        //render
        batch.begin();
        background.render(delta, batch);
        for (Meteor m : meteors) {
            m.render(delta, batch);
        }
        for (Rocket r : rockets) {
            r.render(delta, batch);

        }
        player.render(delta, batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void gameOver() {
        System.err.println("Game Over!");


    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX >= SpaceGame.VIEWPORTWIDTH / 2) {
            if (rockets.size() < 5) {
                rockets.add(new Rocket(player));
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
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
    public void dispose() {
        batch.dispose();
        background.dispose();

        for (int i = 0; i < meteors.size(); i++) {
            meteors.get(i).dispose();
        }
        for (int i = 0; i < rockets.size(); i++) {
            rockets.get(i).dispose();
        }
    }

    public void shakeCam()
    {
        double x = Math.sin(System.currentTimeMillis());
    }
}
