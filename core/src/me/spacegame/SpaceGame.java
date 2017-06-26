package me.spacegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.PerformanceCounter;

import java.util.HashMap;

import me.spacegame.screens.MainMenuScreen;

public class SpaceGame extends Game {


	public static int VIEWPORTWIDTH;
	public static int VIEWPORTHEIGHT;

	private AssetManager manager;
	private HashMap<String, String> assetKeys = new HashMap<String, String>();

	@Override
	public void create ()
	{
		VIEWPORTWIDTH = Gdx.graphics.getWidth();
		VIEWPORTHEIGHT = Gdx.graphics.getHeight();

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		manager = new AssetManager();

		Texture.setAssetManager(manager);
		loadAssets();
		manager.finishLoading();
		setScreen(new MainMenuScreen(this));

	}


	private void loadAssets()
	{
		FileHandle assetFile = Gdx.files.internal("assetmanager.txt");
		String assetString = assetFile.readString();
		String[] uniqueAssets = assetString.split("\n");
		long starttime = System.currentTimeMillis();
		for(int i = 0; i < uniqueAssets.length; i++)
		{
			if(uniqueAssets[i].startsWith("#")) continue;
			String[] data = uniqueAssets[i].split(";");
			String path = data[1].split("\r")[0];
			assetKeys.put(data[0], path);
			manager.load(path, Texture.class);
		}

		System.out.println("Load Time: " + (System.currentTimeMillis() - starttime));

	}

	public Texture getTexture(String name)
	{
		return manager.get(assetKeys.get(name));
	}

	@Override
	public void render ()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose ()
	{
		manager.clear();
		manager.dispose();
	}

}
