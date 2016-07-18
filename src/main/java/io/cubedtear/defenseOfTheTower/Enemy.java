package io.cubedtear.defenseOfTheTower;

import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.math.Vec2i;

import java.util.List;

/**
 * @author Aritz Lopez
 */
public class Enemy {

    private final EnemyType type;
    private int x;
    private int y;
    private Dir2D dir;
    private List<Vec2i> path;
    private int progress;

    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void updatePath(Level level) {
        path = level.goTo(new Vec2i(this.x / Level.CELL_SIZE, this.y / Level.CELL_SIZE), level.getTarget());
    }

    public void update(Level level) {
        progress++;
        // TODO Move slowly
    }

    public void render(IRender r) {

    }
}
