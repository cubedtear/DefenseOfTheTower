package io.cubedtear.defenseOfTheTower;

import io.github.cubedtear.jcubit.awt.gameEngine.core.IGame;
import io.github.cubedtear.jcubit.awt.gameEngine.input.InputHandler;
import io.github.cubedtear.jcubit.awt.gameEngine.test.TestEngine;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * @author Aritz Lopez
 */
public class Game implements IGame {
    private static final int WIDTH = Level.getWidth();
    private static final int HEIGHT = Level.getHeight();

    private TestEngine engine;
    private IRender render;
    private Sprite toolPanel;
    private Sprite turret = new Sprite("test.png");
    private Point mousePos = new Point(0, 0);
    private Level level = new Level();



    public Game() throws IOException {
        this.engine = new TestEngine(this, WIDTH, HEIGHT, null, null);
        this.render = this.engine.getRender();
        this.toolPanel = new Sprite(WIDTH - Level.getWidth(), HEIGHT, 0xFF555555);
        this.render.setBlend(true);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRender() {
        final int x = 5 * Level.CELL_SIZE + Level.CELL_SIZE / 2 - turret.getWidth() / 2;
        final int y = 5 * Level.CELL_SIZE + Level.CELL_SIZE / 2 - turret.getHeight() / 2;

        this.level.render(this.render);
        this.render.draw(Level.getWidth(), 0, toolPanel);

        render.draw(x, y, Util.rotateNoResize(turret, Util.aimTo(x + turret.getWidth() / 2, y + turret.getHeight() / 2, mousePos.x, mousePos.y)));
    }

    @Override
    public void onUpdate() {
        InputHandler ih = this.engine.getEngine().getInputHandler();
        mousePos = ih.getMousePos();

        while (!ih.getMouseEvents().isEmpty()) {
            InputHandler.MouseInputEvent mie = ih.getMouseEvents().remove();
            if (mie.getAction() == InputHandler.MouseAction.DRAGGED) {
                level.draggedAt(mie.getPosition());
            } else if (mie.getAction() == InputHandler.MouseAction.CLICKED) {
                level.clickedAt(mie.getPosition());
            }
        }
        ih.clearMouseEvents();

        if (ih.wasKeyTyped(KeyEvent.VK_SPACE)) level.print();

        this.level.update(ih);
    }

    @Override
    public void onUpdatePS() {
        this.engine.getEngine().setTitle(this.getGameName() + " - " + this.engine.getEngine().getFPS() + " FPS");
    }

    @Override
    public void onPostRender() {
        Graphics g = this.engine.getEngine().getGraphics();
    }

    @Override
    public String getGameName() {
        return "Defense Of The Tower";
    }
}
