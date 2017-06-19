package me.spacegame.ui.menu;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.HashMap;

/**
 * Created by Felix on 19-Jun-17.
 */

public class Menu {

    public OrthographicCamera camera;

    private HashMap<String, TemplateMenu> screens;
    private TemplateMenu currentMenu;

    public Menu()
    {
        camera = new OrthographicCamera(1920, 1080);
        screens = new HashMap<String, TemplateMenu>();


        //load all menuscreens
        screens.put("main", new MainMenu(this));

        currentMenu = screens.get("main");
    }

    public void draw()
    {
        currentMenu.draw();
    }

    public void dispose()
    {
        for(TemplateMenu menu : screens.values())
        {
            menu.dispose();
        }
    }

}
