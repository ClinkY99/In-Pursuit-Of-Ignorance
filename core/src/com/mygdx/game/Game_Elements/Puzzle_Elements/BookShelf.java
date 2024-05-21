package com.mygdx.game.Game_Elements.Puzzle_Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.ui.ScreenStack;
import com.mygdx.game.ui.stackableScreen;
import com.ray3k.stripe.FreeTypeSkin;
import sun.font.ScriptRun;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.ui.ScreenStack;
import com.mygdx.game.ui.stackableScreen;

import java.awt.*;

import java.util.Random;

public class BookShelf extends ImagePuzzleButton {
    public PuzzleTable textHolder;
    public boolean isTextLoaded;
    Texture text;
    Texture shelf;
    TextureRegion region;
    bookFocused library;

    ImagePuzzleButton button;

    ScreenStack stack;

    public BookShelf(Texture shelfTexture, int scale,Texture textTexture, ScreenStack stack) {
       super(shelfTexture, scale);
        text = textTexture;
        shelf = shelfTexture;
        library = new bookFocused(textTexture,stack);
        region = new TextureRegion(shelfTexture);
        setBounds(region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("SUS");
                stack.push(library);

            }
        });

        this.stack = stack;
    }

    public BookShelf() {
        super(new FreeTypeSkin(Gdx.files.internal("Levels/PuzzleElements.json")), "Bookshelf");
    }

    public BookShelf(int type, String imgPath, ScreenStack stack){
        super(new FreeTypeSkin(Gdx.files.internal("Levels/PuzzleElements.json")), "BookshelfInteractable");

    }

    public Table generateRow(){
        int booksWidth = 0;
        Random random = new Random();

        Table table = new Table();

        TextureAtlas tex = new TextureAtlas(Gdx.files.internal("Images/tiles/Level1/Bookshelves/Books/books.atlas"));

        table.setWidth(200-28);

        while(booksWidth < table.getWidth()){
            Image image = new Image(tex.getRegions().get(random.nextInt(0,8)));
            image.setScale(.5f);
            table.add(image).pad(1);
            table.debug();
            booksWidth += image.getWidth();
        }

        table.align(Align.center);
        return table;
    }


    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);
//        Color color = getColor();
//        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
//        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
//            try {
//                stack.remove(library);
//            } catch (Exception e) {
//
//            }
//        }

    }

    class bookFocused implements stackableScreen {

        Stage stage;
        ImagePuzzleButton screenBlocker;
        ImagePuzzleButton text;
        public bookFocused(Texture textTexture, ScreenStack stack) {

            stage = new Stage(new FitViewport(1920,1080));
            Texture screenBlockerTexture = new Texture(Gdx.files.internal("Images/screenBlocker.png"));

            screenBlocker = new ImagePuzzleButton(screenBlockerTexture,0);
            screenBlocker.setPosition(0,0);
            screenBlocker.setSize(1928,1080);

            text = new ImagePuzzleButton(textTexture,2);
            text.setPosition(400,500);
            text.setSize(300,300);


            stage.addActor(screenBlocker);
            stage.addActor(text);
       }

        @Override
        public void show() {

        }

        @Override
        public void render(float delta, boolean top) {
            stage.act(delta);
            stage.draw();

            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                stack.remove(this);

            }
        }

        @Override
        public void resize(int width, int height) {
            stage.getViewport().update(width, height, true);
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
            stage.dispose();
        }

        @Override
        public void setStage(Stage stage) {
            this.stage = stage;
        }

        @Override
        public Stage getStage() {
            return stage;
        }
    }
}
