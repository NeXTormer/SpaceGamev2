package me.spacegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;

import java.util.Arrays;
import java.util.HashMap;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;
import me.spacegame.databases.Database;
import me.spacegame.screens.MainMenuScreen;

public class SpaceGame extends Game {


	public static int VIEWPORTWIDTH;
	public static int VIEWPORTHEIGHT;

	public String username = "";
	public Database database;

	public boolean vibrationEnabled = true;
	public float soundVolume = 1.0f;

	private AssetManager manager;


	private HashMap<String, String> assetKeys = new HashMap<String, String>();

	public IGameServiceClient gsClient;

	@Override
	public void create ()
	{
		if(gsClient == null)
		{
			gsClient = new NoGameServiceClient();
		}

		VIEWPORTWIDTH = Gdx.graphics.getWidth();
		VIEWPORTHEIGHT = Gdx.graphics.getHeight();

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		manager = new AssetManager();

		Texture.setAssetManager(manager);
		loadAssets();

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
			String f[] = path.split("\\.");
			String format = f[f.length-1];
			if(format.equals("mp3") || format.equals("wav"))
			{
				manager.load(path, Sound.class);
			}
			else
			{
				manager.load(path, Texture.class);
			}
		}
		manager.load("ui/fragezeichen.obj", Model.class);
		manager.update();
		System.out.println("Load Time: " + (System.currentTimeMillis() - starttime));
	}

	public Texture getTexture(String name)
	{
		return manager.get(assetKeys.get(name));
	}

	public Sound getSound(String name)
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

	public void finishLoadingAssets()
	{
		manager.finishLoading();
	}
	
	@Override
	public void dispose ()
	{
		manager.clear();
		manager.dispose();
		database.CloseConnection();
	}

	public AssetManager getAssetManager()
	{
		return manager;
	}

}
