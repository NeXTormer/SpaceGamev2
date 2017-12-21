package me.spacegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Timer;
import java.util.TimerTask;

import me.spacegame.SpaceGame;
import me.spacegame.powerups.PowerUp;
import me.spacegame.powerups.PowerUpObject;
import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 19-May-17.
 */

public class HealthBar {

    private GameScreen game;

    private Texture mainTexture;
    private Texture healthTexture;
    private Texture powerupCooldownTexture;

    private FileHandle healthbarFrag;
    private FileHandle healthbarVert;

    private SpriteBatch healthBatch;
    private ShaderProgram healthbarProgram;

    // Health percentage
    private float healthPercent = 100;
    private float healthPixel = 0;
    private float newHealthPercent = 100;
    private float healthbarSmoothing = 5.42f;

    private float powerUpCooldown = 0;

    //Questionmark
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;
    private ModelLoader loader = new ObjLoader();
    public FrameBuffer fbo;
    public Environment environment;

    private float defaultRotationSpeed = 1.9f;


    public HealthBar(GameScreen game)
    {
        this.game = game;
        healthbarFrag = Gdx.files.internal("shader/ui/healthbar.frag");
        healthbarVert = Gdx.files.internal("shader/ui/healthbar.vert");
        mainTexture = game.getGame().getTexture("healthbarmain");
        healthTexture = game.getGame().getTexture("healthTexture");
        powerupCooldownTexture = game.getGame().getTexture("poweruptimer");


        healthbarProgram = new ShaderProgram(healthbarVert, healthbarFrag);
        if (!healthbarProgram.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + healthbarProgram.getLog());


        healthBatch = new SpriteBatch();

        healthBatch.setShader(healthbarProgram);

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 200, 200, true);
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, -2f, 0);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        model = loader.loadModel(Gdx.files.internal("ui/fragezeichen.obj"));

        instance = new ModelInstance(model);
        instance.transform.scl(4);
        instance.transform.rotate(0, 0, 1, 180);

        healthPixel = convertPercentToPixel(healthPercent);

    }

    public void draw(SpriteBatch batch)
    {
        if(game.currentPowerUp != null && game.currentPowerUp.texture != null)
        {
            batch.draw(game.currentPowerUp.texture, Scale.getScaledSizeX(105), Scale.getScaledSizeX(857), Scale.getScaledSizeY(160), Scale.getScaledSizeX(160));
        }
        else
        {
            batch.draw(fbo.getColorBufferTexture(), Scale.getScaledSizeX(87), Scale.getScaledSizeY(833), Scale.getScaledSizeX(200), Scale.getScaledSizeY(200));
        }

        batch.draw(mainTexture, Scale.getScaledSizeX(62), Scale.getScaledSizeY(740), Scale.getScaledSizeX(800), Scale.getScaledSizeY(400));

    }

    public float getHealth()
    {
        return healthPercent;
    }

    public void update()
    {
        updateHealth();

        instance.transform.rotate(new Vector3(0, 1, 0), defaultRotationSpeed);

        healthPixel = convertPercentToPixel(healthPercent);
    }

    public void draw()
    {
        //render to fbo
        fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, 200, 200);

        modelBatch.begin(cam);
        modelBatch.render(instance);
        modelBatch.end();
        fbo.end();

        healthbarProgram.begin();
        healthbarProgram.setUniformf("health_t", healthPixel);
        //healthbarProgram.setUniformf("powerupCooldown", powerUpCooldown);
        healthbarProgram.end();

        healthBatch.begin();

        healthBatch.draw(healthTexture, Scale.getScaledSizeX(62), Scale.getScaledSizeY(740), Scale.getScaledSizeX(800), Scale.getScaledSizeY(400));
        //healthBatch.draw(powerupCooldownTexture, Scale.getScaledSizeX(62), Scale.getScaledSizeY(740), Scale.getScaledSizeX(800), Scale.getScaledSizeY(400));
        healthBatch.end();
    }

    public void setProjectionMatrix(Matrix4 pr_matrix)
    {
        healthBatch.setProjectionMatrix(pr_matrix);
    }

    public void updateHealth()
    {
        this.healthPercent = Interpolation.linear.apply(healthPercent, newHealthPercent, Gdx.graphics.getDeltaTime() * healthbarSmoothing);
    }

    public void setAbsuloteHealth(float health)
    {
        if(health > 100) health = 100;
        if(health < 0) health = 0;
        newHealthPercent = health;
        updateHealth();
    }

    public void changeHalth(float dh)
    {
        newHealthPercent = healthPercent - dh;
        if(newHealthPercent <= 0)
        {
            newHealthPercent = 0;
        }
        if(newHealthPercent > 100) newHealthPercent = 100;

        updateHealth();
    }

    public void dispose()
    {
        model.dispose();
        modelBatch.dispose();
        healthbarProgram.dispose();
        fbo.dispose();
    }

    private float convertPercentToPixel(float percent)
    {
        return Scale.getScaledSizeX(255) + ((percent) * Scale.getScaledSizeX(5.7f));
    }

    /**
     * Sets the powerup cooldownbar (circle in healthbar) to the desired height
     * @param px the amount of pixels in screen space where the cooldown texture should be drawn
     */
    public void setPowerUpCooldown(float px)
    {
        powerUpCooldown = px;
    }

    public float getPowerUpCooldown() { return powerUpCooldown; }

}
