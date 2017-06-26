package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.spacegame.SpaceGame;
import me.spacegame.animations.ExclaimationPoint;
import me.spacegame.animations.Explosion;
import me.spacegame.gameobjects.Background;
import me.spacegame.gameobjects.Enemy;
import me.spacegame.gameobjects.EnemyRocket;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.gameobjects.Rocket;
import me.spacegame.powerups.PowerUp;
import me.spacegame.powerups.PowerUpClear;
import me.spacegame.powerups.PowerUpControl;
import me.spacegame.powerups.PowerUpFreeze;
import me.spacegame.powerups.PowerUpHealth;
import me.spacegame.powerups.PowerUpHelper;
import me.spacegame.powerups.PowerUpObject;
import me.spacegame.powerups.PowerUpRapidFire;
import me.spacegame.ui.HealthBar;
import me.spacegame.ui.menu.Menu;

/**
 * Created by Felix on 09-May-17.
 */

public class GameScreen implements Screen, InputProcessor {

    //TMP
    private Menu menu;

    private static final int SHAKETIME = 150;

    public SpaceGame game;

    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;


    public List<Meteor> meteors = new ArrayList<Meteor>();
    public List<Rocket> rockets = new ArrayList<Rocket>();
    public List<Explosion> explosions = new ArrayList<Explosion>();
    public List<Enemy> enemies = new ArrayList<Enemy>();
    private List<PowerUp> activePowerUps = new ArrayList<PowerUp>();
    private List<PowerUpObject> powerUpObjects = new ArrayList<PowerUpObject>();

    private Background background;

    public static Random random = new Random();
    public PowerUp currentPowerUp;
    public boolean vibration = true;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadskin;
    private Drawable touchknob;
    private Drawable touchbackground;
    public Player player;
    private Enemy enemy0;
    private Enemy enemy1;
    public HealthBar healthBar;
    private TextureRegion lastFrameBuffer;
    private Image lastFrameBufferImage;

    private InputMultiplexer inputMultiplexer;
    private long shakeCamTimer = 0;
    private long pausebtnLastTimePressed;
    private Explosion ex;

    public boolean paused = false;

    //richtiger Pfusch!! (OOP)
    private double enemy0SpawnTimer;
    private double enemy1SpawnTimer;
    private double enemy0Spawner;
    private double enemy1Spawner;
    private double meteorSpawnTimer;
    private double meteorSpawner;
    private double enemy0SpawnerSubtract;
    private double enemy0SpawnSubtractTimer;
    private double enemy0SpawnerSubtractValue;

    //Pause Button
    public ImageButton settingsbtn;
    private Texture pausebtnup;
    private Texture pausebtndown;
    private SpriteDrawable pausebtnupdrawable;
    private SpriteDrawable pausebtndowndrawable;

    public GameScreen(SpaceGame game) {
        this.game = game;
        background = new Background(this);

        batch = new SpriteBatch();
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SpaceGame.VIEWPORTWIDTH, SpaceGame.VIEWPORTHEIGHT);

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

        for (int i = 0; i < 4; i++) {
            meteors.add(new Meteor());
        }


        enemy0SpawnTimer = System.currentTimeMillis();
        enemy0SpawnSubtractTimer = System.currentTimeMillis();
        enemy1SpawnTimer = System.currentTimeMillis();
        meteorSpawnTimer = System.currentTimeMillis();

        //Timer, when a new Enemy0 will spawn
        enemy0Spawner = 9000;
        //Timer, when the enemy0Spawner gets lower
        enemy0SpawnerSubtract = 10000;
        //Value, which is subtstracted form enemy0Spawner after enemy0SpawnerSubtract+enemy0Spawner
        enemy0SpawnerSubtractValue = 500;
        //Timer, when enemy1 will spawn
        enemy1Spawner = 16000;
        //Timer, when new meteor will spawn
        meteorSpawner = 15000;


        player = new Player();
        //enemy0 = new Enemy(0);
        //enemy1 = new Enemy(1, player);
        //enemies.add(enemy0);
        //enemies.add(enemy1);

        currentPowerUp = null;

        healthBar = new HealthBar(this);

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        menu = new Menu(this);

        //Pause Button
        pausebtnup = new Texture(Gdx.files.internal("ui/settingsbtn.png"));
        pausebtndown = new Texture(Gdx.files.internal("ui/settingsbtn.png"));

        pausebtnupdrawable= new SpriteDrawable();
        pausebtndowndrawable = new SpriteDrawable();

        pausebtnupdrawable.setSprite(new Sprite(pausebtnup));
        pausebtndowndrawable.setSprite(new Sprite(pausebtndown));

        settingsbtn = new ImageButton(pausebtnupdrawable, pausebtndowndrawable);

        settingsbtn.setPosition(1650, 928);
        stage.addActor(settingsbtn);

        lastFrameBufferImage = new Image();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {



        if(!paused)
        {

            player.score+=1;

            //update
            if (System.currentTimeMillis() - shakeCamTimer < SHAKETIME)
            {
                float dx = (float) Math.sin(System.currentTimeMillis());
                float dy = (float) Math.sin(2 * System.currentTimeMillis());

                camera.position.set(SpaceGame.VIEWPORTWIDTH/2 + dx * 10 , SpaceGame.VIEWPORTHEIGHT/2 + dy * 10 , 0);
                camera.update();
                batch.setProjectionMatrix(camera.combined);
                healthBar.setProjectionMatrix(camera.combined);
            }
            camera.position.set(SpaceGame.VIEWPORTWIDTH/2, SpaceGame.VIEWPORTHEIGHT/2, 0);

            player.updatePosition(touchpad);

            //Spawn Enemy0 after Seconds
            if((System.currentTimeMillis()-enemy0SpawnTimer)>enemy0Spawner)
            {
                enemies.add(new Enemy(0));
                enemy0SpawnTimer=System.currentTimeMillis();
            }

            //Spawn Enemy1 after Seconds
            outerloop:
            if((System.currentTimeMillis()-enemy1SpawnTimer)>enemy1Spawner)
            {
                for(int i = 0; i<enemies.size(); i++)
                {
                    if(enemies.get(i).type==1)
                    {
                        enemy1SpawnTimer=System.currentTimeMillis();
                        break outerloop;
                    }
                }
                enemies.add(new Enemy(1, player));
                enemy1SpawnTimer=System.currentTimeMillis();
            }

            //Spawn new Meteors over time
            if((System.currentTimeMillis()-meteorSpawnTimer)>meteorSpawner)
            {
                meteors.add(new Meteor());
                meteorSpawnTimer=System.currentTimeMillis();
            }

            //Spawn Enemy0 more frequently
            if((System.currentTimeMillis()-enemy0SpawnSubtractTimer)>enemy0SpawnerSubtract+enemy0Spawner)
            {
                if(enemy0Spawner>1000)
                {
                    enemy0Spawner-=enemy0SpawnerSubtractValue;
                    enemy0SpawnSubtractTimer=System.currentTimeMillis();
                }
            }


            //Remove offscreen enemies
            for(int i = 0; i<enemies.size(); i++)
            {
                if(enemies.get(i).type==0)
                {
                    if(enemies.get(i).enemyX<=-enemies.get(i).enemyWidth)
                    {
                        enemies.remove(enemies.get(i));
                    }
                }
            }

            //Meteor - Enemy Collision
            outerloop:
            for (int i = 0; i < meteors.size(); i++)
            {
                for(int j = 0; j<enemies.size(); j++)
                {
                    if (Intersector.overlaps(meteors.get(i).box, enemies.get(j).box) && enemies.get(j).type==1) {
                        enemies.get(j).health -= 50;
                        explosions.add(new Explosion((int) enemies.get(j).enemyX, (int) (enemies.get(j).enemyY)));
                        if (enemies.get(j).health <= 0) {
                            enemies.remove(enemies.get(j));
                            break outerloop;
                        }
                    }
                }
            }

            //Meteor  -  Player Collision
            for (int i = 0; i < meteors.size(); i++) {
                if (Intersector.overlaps(meteors.get(i).box, player.box)) {
                    // System.out.println(System.currentTimeMillis() - meteors.get(i).lastTimeHit);
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

            //Rocket  -  Meteor collision
            outerloop:
            for (int i = 0; i < rockets.size(); i++) {
                for (int j = 0; j < meteors.size(); j++) {
                    if (Intersector.overlaps(meteors.get(j).box, rockets.get(i).box)) {
                        meteors.get(j).health -= rockets.get(i).damage;
                        rockets.remove(rockets.get(i));
                        meteors.get(j).updateTexture();
                        explosions.add(new Explosion((int) meteors.get(j).x - 70, (int) (meteors.get(j).y - 20)));
                        if (meteors.get(j).health <= 0) {
                            if(random.nextInt(6)==0)
                            {
                                powerUpObjects.add(new PowerUpObject(meteors.get(j), this));
                            }
                            meteors.remove(j);
                            meteors.add(new Meteor());
                            player.score+=100;
                        }
                        break outerloop;
                    }
                }

            }

            //Rocket   -  Enemy Collision
            outerloop:
            for (int i = 0; i < rockets.size(); i++) {
                for (int j = 0; j<enemies.size(); j++) {
                    if (Intersector.overlaps(rockets.get(i).box, enemies.get(j).box)) {
                        rockets.remove(rockets.get(i));
                        enemies.get(j).health -= 50;
                        explosions.add(new Explosion((int) enemies.get(j).enemyX - 70, (int) (enemies.get(j).enemyY - 20)));
                        if (enemies.get(j).health <= 0) {
                            enemies.remove(enemies.get(j));
                            player.score+=500;
                        }
                        break outerloop;

                    }
                }
            }

            //EnemyRockets  -  Player/Meteor collision
            //Enemy - Player collision
            for(Enemy e : enemies)
            {
                for(EnemyRocket er : e.getRockets())
                {
                    if(Intersector.overlaps(er.box, player.box) )
                    {
                        //only hit player once
                        if(!er.hasHitPlayer)
                        {
                            er.hasHitPlayer = true;
                            damagePlayer(er.damage);
                        }
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
                    if((System.currentTimeMillis() - e.lastTimeHit) > 1200)
                    {
                        e.lastTimeHit = System.currentTimeMillis();
                        damagePlayer(e.damage);
                    }
                }
            }

            //Enemy Rocket - Enemy Collision
            outerloop:
            for(int i = 0; i<enemies.size(); i++)
            {
                for(int l = 0; l<enemies.size(); l++)
                {
                    for(int h = 0; h<enemies.get(l).getRockets().size(); h++)
                    {
                        if(Intersector.overlaps(enemies.get(i).box, enemies.get(l).getRockets().get(h).box))
                        {
                            explosions.add(new Explosion((int) enemies.get(i).enemyX - 70, (int) (enemies.get(i).enemyY - 20)));
                            enemies.remove(i);
                            break outerloop;
                        }
                    }
                }
            }

            //Player - PowerUpObject Collision
            outerloop:
            for(int i = 0; i<powerUpObjects.size(); i++)
            {
                if((Intersector.overlaps(powerUpObjects.get(i).box, player.box)) && currentPowerUp==null)
                {
                    System.out.println(powerUpObjects.get(i).box.toString() + " : "+ player.box.toString());
                    powerUpObjects.remove(powerUpObjects.get(i));
                    switch(random.nextInt(5))
                    {
                        case 4:
                            currentPowerUp = new PowerUpHelper(player, this);
                            break outerloop;
                        case 3:
                            currentPowerUp = new PowerUpClear(player, this);
                            break outerloop;
                        case 2:
                            currentPowerUp = new PowerUpControl(player, this);
                            break outerloop;
                        case 1:
                            currentPowerUp = new PowerUpHealth(player, this);
                            break outerloop;
                        case 0:
                            currentPowerUp = new PowerUpRapidFire(player, this);
                            break outerloop;
                        default:
                            currentPowerUp = new PowerUpClear(player, this);
                            break outerloop;
                    }

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

            //Render PowerUps
            outerloop:
            for(int i = 0; i<activePowerUps.size(); i++)
            {
                if(activePowerUps.get(i).render(delta, batch))
                {

                }
                else
                {
                    activePowerUps.remove(activePowerUps.get(i));
                    currentPowerUp=null;
                    break outerloop;
                }
            }

            for(int i = 0; i<powerUpObjects.size(); i++)
            {
                powerUpObjects.get(i).render(delta, batch);
            }

            player.render(delta, batch);

            healthBar.draw(batch);

            batch.end();

            healthBar.draw();


        }
        else
        {
            camera.position.set(SpaceGame.VIEWPORTWIDTH/2, SpaceGame.VIEWPORTHEIGHT/2, 0);
            camera.update();
            batch.begin();
            batch.draw(lastFrameBuffer, 0, 0);
            batch.end();
        }

        stage.act();
        stage.draw();

        menu.draw();

        if(settingsbtn.isPressed())
        {
            //Richtiger Pfusch, geht aber
            settingsbtn.remove();
            settingsbtn = new ImageButton(pausebtnupdrawable, pausebtndowndrawable);
            stage.addActor(settingsbtn);
            settingsbtn.setPosition(1650, 928);

            if(System.currentTimeMillis() - pausebtnLastTimePressed > 1000)
            {
                paused = !paused;
                if(paused)
                {
                    lastFrameBuffer = ScreenUtils.getFrameBufferTexture();
                }
                camera.position.set(SpaceGame.VIEWPORTWIDTH/2, SpaceGame.VIEWPORTHEIGHT/2, 0);
                menu.currentMenu = menu.screens.get("main").activate();
                pausebtnLastTimePressed = System.currentTimeMillis();
            }
        }


    }

    public void damagePlayer(int damage)
    {
        //damage = 4;
        player.health -= damage;
        healthBar.setHealth(player.health, true);
        shakeCam();
        if (healthBar.getHealthPX() <= 260) {
            gameOver();
        }
    }

    private void gameOver() {
        if(!player.dead)
        {
            player.setVisible(false);
            explosions.add(new Explosion((int) player.x, (int) player.y, true));
            System.err.println("Game Over!");
            menu.currentMenu = menu.screens.get("gameover").activate();
            player.dead = true;
        }

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
        if (screenX >= SpaceGame.VIEWPORTWIDTH / 2)
        {
            if(activePowerUps.size()>=1)
            {
                if(!(activePowerUps.get(0) instanceof PowerUpHelper))
                {
                    if(rockets.size() < 5)
                    {
                        rockets.add(new Rocket(player));
                    }
                }
                else
                {
                    rockets.add(new Rocket(player));
                }

            }
            else
            {
                if(rockets.size() < 5)
                {
                    rockets.add(new Rocket(player));
                }
            }
        }
        else
        {
            //float dx = (float) Math.pow(Math.abs(screenX-110), 2);
            //float dy = (float) Math.pow(Math.abs(screenY-880), 2);
            //System.out.println(screenX + " : "+screenY);
            if(screenX>91 && screenX<270 && screenY>45 && screenY<220)
            //if((dx+dy)>250)
            {
                if(currentPowerUp!=null && activePowerUps.size()==0)
                {
                    activePowerUps.add(currentPowerUp);
                }

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
        ExclaimationPoint.dispose();
        for (int i = 0; i < rockets.size(); i++) {
            rockets.get(i).dispose();
        }

    }

    public void shakeCam()
    {
        if(!player.dead)
        {
            shakeCamTimer = System.currentTimeMillis();
            if(vibration)
                Gdx.input.vibrate(SHAKETIME);
        }
    }

    public InputMultiplexer getInputMultiplexer()
    {
        return inputMultiplexer;
    }

    public SpaceGame getGame() { return game; }

    public Camera getCamera() { return camera; }

}
