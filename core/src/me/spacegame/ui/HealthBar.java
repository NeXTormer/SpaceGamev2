package me.spacegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.GdxRuntimeException;

import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 19-May-17.
 */

public class HealthBar
{
    private GameScreen game;

    private Texture mainTexture;
    private Texture healthTexture;
    private Texture powerupCooldownTexture;

    private FileHandle healthbarFrag;
    private FileHandle healthbarVert;

    private SpriteBatch healthBatch;
    private ShaderProgram healthbarProgram;

    private Label scoreLabel;

    // Health percentage
    private float healthPercent = 100;
    private float healthPixel = 0;
    private float newHealthPercent = 100;
    private float healthbarSmoothing = 5.69f;

    private float powerupCooldownPixel = 0;

    public int score = 0;

    //Questionmark
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private ModelLoader loader = new ObjLoader();
    private FrameBuffer fbo;
    private Environment environment;

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

        model = game.getGame().getAssetManager().get("ui/fragezeichen.obj");

        instance = new ModelInstance(model);
        instance.transform.scl(4);
        instance.transform.rotate(0, 0, 1, 180);

        healthPixel = convertPercentToPixel(healthPercent);

        FreeTypeFontGenerator ftfg2 = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = (int) Scale.getScaledSizeX(50);

        scoreLabel = new Label("Score: " + score, new Label.LabelStyle(ftfg2.generateFont(parameter2), Color.BLACK));

        scoreLabel.setPosition(Scale.getScaledSizeX(284), Scale.getScaledSizeY(894));
    }

    public void draw(SpriteBatch batch)
    {
        if(game.currentPowerUp != null && game.currentPowerUp.texture != null)
        {
            batch.draw(game.currentPowerUp.texture, Scale.getScaledSizeX(105), Scale.getScaledSizeY(857), Scale.getScaledSizeX(160), Scale.getScaledSizeY(160));
        }
        else
        {
            batch.draw(fbo.getColorBufferTexture(), Scale.getScaledSizeX(87), Scale.getScaledSizeY(833), Scale.getScaledSizeX(200), Scale.getScaledSizeY(200));
        }

        batch.draw(mainTexture, Scale.getScaledSizeX(62), Scale.getScaledSizeY(740), Scale.getScaledSizeX(800), Scale.getScaledSizeY(400));

        scoreLabel.draw(batch, 1);
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
        scoreLabel.setText("Score: " + score);
    }

    public void draw()
    {
        fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, 200, 200);

        modelBatch.begin(cam);
        modelBatch.render(instance);
        modelBatch.end();
        fbo.end();

        healthbarProgram.begin();
        healthbarProgram.setUniformf("health_t", healthPixel);
        healthbarProgram.setUniformf("powerupCooldown", powerupCooldownPixel);
        healthbarProgram.end();

        healthBatch.begin();

        healthBatch.draw(healthTexture, Scale.getScaledSizeX(62), Scale.getScaledSizeY(740), Scale.getScaledSizeX(800), Scale.getScaledSizeY(400));
        healthBatch.draw(powerupCooldownTexture, Scale.getScaledSizeX(62), Scale.getScaledSizeY(740), Scale.getScaledSizeX(800), Scale.getScaledSizeY(400));
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

    public void setPowerupCooldown(double percent)
    {
        if(percent > 98) percent = 200;
        powerupCooldownPixel = (convertPowerupCooldowntoPixel((float) (100 - percent)));
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

    private float convertPowerupCooldowntoPixel(float percent)
    {
        return Scale.getScaledSizeY(852) + ((percent) * Scale.getScaledSizeY(1.82f));
    }
}