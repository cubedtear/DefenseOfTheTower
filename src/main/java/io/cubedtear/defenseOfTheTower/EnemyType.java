package io.cubedtear.defenseOfTheTower;

import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;

import java.io.IOException;

/**
 * @author Aritz Lopez
 */
public enum EnemyType {

    SIMPLE("enemy1.png", 1.0, 100); // TODO Add some types

    public final Sprite left, right, up, down;
    public final double speed;
    public final int health;

    EnemyType(String s, double speed, int health) {
        try {
            Sprite sprite = new Sprite(s);
            this.speed = speed;
            this.health = health;
            this.left = sprite;
            this.right = SpriteUtil.flipV(sprite);
            this.down = SpriteUtil.rotate90(sprite, SpriteUtil.Rotation._90);
            this.up = SpriteUtil.flipH(this.down);
        } catch (IOException e) {
            throw new IllegalArgumentException("Sprite \"" + s + "\" could not be found!");
        }
    }
}
