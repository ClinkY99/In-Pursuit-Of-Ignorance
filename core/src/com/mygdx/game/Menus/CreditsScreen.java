package com.mygdx.game.Menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CPTGame;
import com.mygdx.game.Menus.subMenus.HostGame;

public class CreditsScreen implements Screen {
    Texture credits;
    CPTGame game;
    Music music;
    public CreditsScreen( CPTGame game, Music music) {
        this.credits = new Texture(Gdx.files.internal("Images/credits.png"));
        this.game =  game;
        this.music = music;

        music.setLooping(true);
        music.play();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0,1);
        game.batch.begin();
        game.batch.draw(credits,0,0,1920 , 1080);
        game.batch.end();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            music.stop();
            game.setScreen(new MainMenu(game,Gdx.audio.newMusic(Gdx.files.internal("Music/Menus/mainMenu.wav"))));
        }
    }

    @Override
    public void resize(int i, int i1) {

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
