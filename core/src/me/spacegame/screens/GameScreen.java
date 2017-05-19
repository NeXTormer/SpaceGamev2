package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

import me.spacegame.SpaceGame;
import me.spacegame.animations.Explosion;
import me.spacegame.gameobjects.Background;
import me.spacegame.gameobjects.Enemy;
import me.spacegame.gameobjects.EnemyRocket;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.gameobjects.Rocket;

/**
 * Created by Felix on 09-May-17.
 */

public class GameScreen implements Screen, InputProcessor {


    private static final int SHAKETIME = 150;

    private SpaceGame game;
    private SpriteBatch batch;
    private Stage stage;


    private OrthographicCamera camera;
    private List<Meteor> meteors = new ArrayList<Meteor>();
    private List<Rocket> rockets = new ArrayList<Rocket>();
    private List<Explosion> explosions = new ArrayList<Explosion>();
    private List<Enemy> enemies = new ArrayList<Enemy>();

    private Background background;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadskin;
    private Drawable touchknob;
    private Drawable touchbackground;
    private Player player;
    private Enemy enemy;

    private InputMultiplexer inputMultiplexer;
    private long shakeCamTimer = 0;

    private Explosion ex;

    public GameScreen(SpaceGame game) {
        this.game = game;
        background = new Background("gameobjects/background.png");

        enemy = new Enemy();

        batch = new SpriteBatch();
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        ex = new Explosion(300, 300);

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
        enemy = new Enemy();
        enemies.add(enemy);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //update
        if (System.currentTimeMillis() - shakeCamTimer < SHAKETIME)
        {
            float dx = (float) Math.sin(System.currentTimeMillis());
            float dy = (float) Math.sin(2 * System.currentTimeMillis());

            camera.position.set(SpaceGame.VIEWPORTWIDTH/2 + dx * 10 , SpaceGame.VIEWPORTHEIGHT/2 + dy * 10 , 0);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
        }

        player.updatePosition(touchpad);

        //Remove offscreen enemies
        for(Enemy e : enemies)
        {
            if(e.enemyX<=-e.enemyWidth)
            {
                enemies.remove(e);
                enemies.add(new Enemy());
            }
        }


        //Meteor  -  Player Collision
        for (int i = 0; i < meteors.size(); i++) {
            if (Intersector.overlaps(meteors.get(i).box, player.box)) {
                System.out.println(System.currentTimeMillis() - meteors.get(i).lastTimeHit);
                if((System.currentTimeMillis() - meteors.get(i).lastTimeHit) > 1200)
                {
                    meteors.get(i).lastTimeHit = System.currentTimeMillis();
                    damagePlayer(meteors.get(i).damage);
                }
            }

            if (meteors.get(i).x < -meteors.get(i).radius) {
                meteors.remove(i);
                meteors.add(new Meteor());
            }
        }

        //Remove offscreen rockets
        for (int i = 0; i < rockets.size(); i++) {
            if (rockets.get(i).x >= SpaceGame.VIEWPORTWIDTH) {
                rockets.remove(rockets.get(i));
            }
        }

        //Rocket  -  Meteor/Enemy collision
        for (int i = 0; i < rockets.size(); i++) {
            for (int j = 0; j < meteors.size(); j++) {
                if (Intersector.overlaps(meteors.get(j).box, rockets.get(i).box)) {
                    rockets.remove(rockets.get(i));
                    meteors.get(j).health -= 30;
                    meteors.get(j).updateTexture();
                    explosions.add(new Explosion((int) meteors.get(j).x - 70, (int) (meteors.get(j).y - 20)));
                    if (meteors.get(j).health <= 0) {
                        meteors.remove(j);
                        meteors.add(new Meteor());
                    }
                    break;
                }
            }
            for(Enemy e : enemies)
            {
                if(Intersector.overlaps(rockets.get(i).box, e.box))
                {
                    rockets.remove(rockets.get(i));
                    e.health-=50;
                    explosions.add(new Explosion((int) e.enemyX - 70, (int) (e.enemyY - 20)));
                    if(e.health<=100)
                    {
                        enemies.remove(e);
                        shakeCam();
                        enemies.add(new Enemy());
                        break;
                    }

                }
            }
        }

        //Enemy  -  Player/Meteor collision
        for(Enemy e : enemies)
        {
            for(EnemyRocket er : e.getRockets())
            {
                if(Intersector.overlaps(er.box, player.box) )
                {
                    damagePlayer(er.damage);
                }
                for (int j = 0; j < meteors.size(); j++)
                {
                    if (Intersector.overlaps(meteors.get(j).box, er.box))
                    {
                        explosions.add(new Explosion((int) meteors.get(j).x - 70, (int) (meteors.get(j).y - 20)));
                        meteors.remove(j);
                        meteors.add(new Meteor());
                    }
                }
            }
            if(Intersector.overlaps(e.box, player.box))
            {
                damagePlayer(e.damage);
            }
        }

        //render
        batch.begin();
        background.render(delta, batch);

        for(Enemy e : enemies)
        {
            e.render(delta, batch);
        }
        for (Meteor m : meteors) {
            m.render(delta, batch);
        }
        for (Rocket r : rockets) {
            r.render(delta, batch);
        }
        for(int i = 0; i < explosions.size(); i++)
        {
            if(explosions.get(i).draw(delta, batch))
            {
                explosions.remove(i);
            }
        }

        player.render(delta, batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void damagePlayer(int damage)
    {
        player.health -= damage;
        shakeCam();
        if (player.health <= 0) {
            gameOver();
        }
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
        Meteor.dispose();
        Rocket.dispose();
        Explosion.dispose();
        for (int i = 0; i < rockets.size(); i++) {
            rockets.get(i).dispose();
        }

    }

    public void shakeCam()
    {
        shakeCamTimer = System.currentTimeMillis();
        Gdx.input.vibrate(SHAKETIME);
    }

}
