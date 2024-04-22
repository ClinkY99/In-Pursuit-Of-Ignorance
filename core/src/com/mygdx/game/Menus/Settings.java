package com.mygdx.game.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CPTGame;
import com.mygdx.game.Menus.Interactive.MenuButton;
import com.mygdx.game.Menus.Interactive.SelectionBar;

import java.awt.*;

public class Settings implements Screen {
    final CPTGame game;

    Texture SettingsBackground;
    Stage stage;
    SelectionBar selectionMenu;
    private Array<Button> otherButtons;


    public Settings(CPTGame Game){
        game = Game;

        otherButtons = new Array<>();

        SettingsBackground = new Texture(Gdx.files.internal("Menu/Settings.png"));

        stage = new Stage(new FitViewport(1920,1080), game.batch);
        Gdx.input.setInputProcessor(stage);

        selectionMenu = new SelectionBar(1080/3, 100,"Graphics", "Audio", "Controls", "Multiplayer");

        selectionMenu.getTable().setPosition((float) (1920/2)+110, 1080/8*7);

        Array<Button> buttons = selectionMenu.getButtons();

        for(Button button: buttons){
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                }
            });
        }

        Button backButton = new MenuButton("back", .8f);
        otherButtons.add(backButton);

        backButton.setPosition(1920/4.5f, 30);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(selectionMenu.getTable());
        stage.addActor(backButton);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        stage.act(delta);

        stage.getBatch().begin();
        stage.getBatch().draw(SettingsBackground, 0,0,1920,1080);
        stage.getBatch().end();

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
    public void dispose() {

    }
}
