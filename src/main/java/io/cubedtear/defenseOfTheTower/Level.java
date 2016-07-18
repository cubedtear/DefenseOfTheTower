package io.cubedtear.defenseOfTheTower;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.awt.gameEngine.input.InputHandler;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;
import io.github.cubedtear.jcubit.math.Vec2i;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Aritz Lopez
 */
public class Level {

    public static final int CELL_SIZE = 40;
    public static final int TWIDE = 14;
    public static final int THIGH = 10;
    private Sprite grey;
    private Sprite bg;
    private Vec2i lastTp = null;
    private boolean[][] walls;

    private List<Vec2i> path = Lists.newArrayList();


    public Level() {
        Sprite grid1 = Util.getGrid(TWIDE * CELL_SIZE, THIGH * CELL_SIZE, CELL_SIZE, CELL_SIZE, 1, 1, 0xFF333333);
        Sprite grid2 = Util.getGrid(TWIDE * CELL_SIZE, THIGH * CELL_SIZE, CELL_SIZE, CELL_SIZE, 1, 1, 0xFFCCCCCC);
        bg = Util.combine(grid1, grid2, -1, -1, false);
        bg = Util.combine(new Sprite(TWIDE * CELL_SIZE, THIGH * CELL_SIZE, 0xFF3137FD), bg, 0, 0, false);
        grey = new Sprite(CELL_SIZE, CELL_SIZE, 0xFF444444);
        walls = new boolean[THIGH][TWIDE];
    }

    public void render(IRender r) {
        r.draw(CELL_SIZE - 1, CELL_SIZE - 1, bg);
        for (int y = 0; y < THIGH; y++) {
            for (int x = 0; x < TWIDE; x++) {
                if (walls[y][x]) r.draw((x + 1) * CELL_SIZE, (y + 1) * CELL_SIZE, grey);
            }
        }

        if (this.path != null && this.path.size() > 0) {
            Util.drawPath(r, this.path);
        }
    }

    public void clickedAt(Point pos) {
        try {
            Vec2i tp = convertCoord(pos);
            walls[tp.y][tp.x] = !walls[tp.y][tp.x];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private static Vec2i convertCoord(Point pos) {
        return new Vec2i(pos.x / CELL_SIZE - 1, pos.y / CELL_SIZE - 1);
    }

    public void draggedAt(Point pos) {
        Vec2i tp = convertCoord(pos);
        if (tp.x < 0 || tp.y < 0 || tp.x >= TWIDE || tp.y >= THIGH) return;
        if (tp.equals(lastTp)) return;
        lastTp = tp;
        walls[tp.y][tp.x] = !walls[tp.y][tp.x];
    }

    public void print() {
        StringBuilder sb = new StringBuilder(walls.length * walls[0].length);
        for (boolean[] wall : walls) {
            for (boolean aWall : wall) {
                sb.append(aWall ? '1' : '0');
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public List<Vec2i> goTo(Vec2i from, Vec2i to) {
        Map<Vec2i, List<Vec2i>> shortestPaths = Maps.newHashMap();
        Queue<Vec2i> q = Lists.newLinkedList();
        q.add(from);
        shortestPaths.put(from, Lists.newArrayList(from));
        while (!q.isEmpty()) {
            Vec2i current = q.remove();
            List<Vec2i> path = shortestPaths.get(current);
            if (to.equals(current)) {
                return path;
            } else {
                if (current.x < TWIDE - 1) {
                    Vec2i newPos = new Vec2i(current.x + 1, current.y);
                    updatePaths(shortestPaths, q, path, newPos);
                }

                if (current.x > 0) {
                    Vec2i newPos = new Vec2i(current.x - 1, current.y);
                    updatePaths(shortestPaths, q, path, newPos);
                }

                if (current.y < THIGH - 1) {
                    Vec2i newPos = new Vec2i(current.x, current.y + 1);
                    updatePaths(shortestPaths, q, path, newPos);
                }

                if (current.y > 0) {
                    Vec2i newPos = new Vec2i(current.x, current.y - 1);
                    updatePaths(shortestPaths, q, path, newPos);
                }
            }
        }
        return null;
    }

    private void updatePaths(Map<Vec2i, List<Vec2i>> shortestPaths, Queue<Vec2i> q, List<Vec2i> path, Vec2i newPos) {
        if (!shortestPaths.containsKey(newPos) && !walls[newPos.y][newPos.x]) {
            List<Vec2i> newPath = Lists.newArrayList(path);
            newPath.add(newPos);
            shortestPaths.put(newPos, newPath);
            q.add(newPos);
        }
    }

    public static int getWidth() {
        return (TWIDE + 1) * CELL_SIZE;
    }

    public static int getHeight() {
        return (THIGH + 2) * CELL_SIZE;
    }

    public void update(InputHandler ih) {
        if (ih.isKeyDown(KeyEvent.VK_U)) {
            this.path = goTo(new Vec2i(0, 0), new Vec2i(9, 9));
        }
    }
}
