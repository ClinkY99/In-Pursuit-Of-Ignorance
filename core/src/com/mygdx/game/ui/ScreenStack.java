package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * Class responsible for handling level management and screenStacking
 */

public class ScreenStack implements Disposable{
    Stack<stackableScreen> stack;

    public ScreenStack(stackableScreen... screens) {
        stack = new Stack<>();
        push(screens);
    }

    public void push(@NotNull final stackableScreen screen) {
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stack.push(screen);
    }
    public void push(@NotNull stackableScreen... screens) {
        for (stackableScreen screen : screens) {
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stack.push(screen);
        }
    }

    public stackableScreen peek(){
        return stack.peek();
    }

    public void show () {
        for (stackableScreen screen : stack){
            if (screen == null) continue;
            screen.show();
        }
    }
    public void render(float delta) {
        for (stackableScreen screen : stack) {
            if (screen == null) continue;
            screen.render(delta,stack.peek()==screen);
        }
    }
    public void resize(int width, int height) {
        for (stackableScreen screen : stack) {
            if (screen == null) continue;
            screen.resize(width, height);
        }
    }

    public void hide(){
        for (stackableScreen screen : stack) {
            if (screen == null) continue;
            screen.hide();
        }
    }

    public void pause(){
        for (stackableScreen screen : stack) {
            if (screen == null) continue;
            screen.pause();
        }
    }

    public void resume(){
        for (stackableScreen screen : stack) {
            if (screen == null) continue;
            screen.resume();
        }
    }

    public void remove(@NotNull final stackableScreen screen){
        stack.remove(screen);
    }

    @Override
    public void dispose() {
        for (stackableScreen screen : stack) {
            if (screen == null) continue;
            screen.dispose();
        }
        stack.clear();
    }
}