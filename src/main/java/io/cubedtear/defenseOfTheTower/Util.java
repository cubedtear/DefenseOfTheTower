package io.cubedtear.defenseOfTheTower;

import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;
import io.github.cubedtear.jcubit.math.Vec2i;
import io.github.cubedtear.jcubit.util.ARGBColorUtil;

import java.util.List;

/**
 * @author Aritz Lopez
 */
public class Util {

    private static final int LINE_WIDTH = 4;
    private static final int LINE_COLOR = 0xFF00FF00;

    private static final Sprite HOR_LINE = new Sprite(Level.CELL_SIZE, Level.CELL_SIZE, 0x00000000);
    private static final Sprite VER_LINE = new Sprite(Level.CELL_SIZE, Level.CELL_SIZE, 0x00000000);
    private static final Sprite TARGET = SpriteUtil.circle(Level.CELL_SIZE, LINE_COLOR, Level.CELL_SIZE);

    static {
        for (int w = 0; w < LINE_WIDTH; w++) {
            for (int i = 0; i < Level.CELL_SIZE; i++) {
                VER_LINE.getPixels()[VER_LINE.getWidth() / 2 - w + w / 2 + (i) * VER_LINE.getWidth()] = LINE_COLOR;
                HOR_LINE.getPixels()[i + (HOR_LINE.getWidth() / 2 - w + w / 2) * HOR_LINE.getWidth()] = LINE_COLOR;
            }
        }
    }

    public static double aimTo(double px, double py, double tx, double ty) {
        return Math.atan2(ty - py, tx - px) * 180 / Math.PI + 90;
    }

    public static Sprite getGrid(int width, int height, int cellWidth, int cellHeight, int lineWidth, int lineHeight, int color) {
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((x + lineWidth / 2) % cellWidth < lineWidth || (y + lineHeight / 2) % cellHeight < lineHeight) {
                    pixels[x + y * width] = color;
                }
            }
        }
        return new Sprite(width, height, pixels);
    }

    public static Sprite combine(Sprite bg, Sprite fg, int xOffset, int yOffset, boolean blend) {
        int width = bg.getWidth();
        int height = bg.getHeight();
        if (width != fg.getWidth() || height != fg.getHeight())
            throw new IllegalArgumentException("The two sprites are not of the same size!");
        int[] px = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x + xOffset < 0 || y + yOffset < 0 || x + xOffset >= fg.getWidth() || y + yOffset >= fg.getHeight()) {
                    px[x + y * width] = bg.getPixels()[x + y * width];
                } else if (!blend) {
                    int fgColor = fg.getPixels()[x + xOffset + (y + yOffset) * width];
                    if (ARGBColorUtil.getAlpha(fgColor) != 0x00) px[x + y * width] = fgColor;
                    else px[x + y * width] = bg.getPixels()[x + y * width];
                } else {
                    px[x + y * width] = ARGBColorUtil.composite(fg.getPixels()[x + y * width], bg.getPixels()[x + xOffset + (y + yOffset) * width]);
                }
            }
        }
        return new Sprite(width, height, px);
    }

    public static Sprite rotateNoResize(Sprite original, double angle) {
        if (angle == 0) return original;
        if (angle == 90.0) return SpriteUtil.rotate90(original, SpriteUtil.Rotation._90);
        else if (angle == 180.0) return SpriteUtil.rotate90(original, SpriteUtil.Rotation._180);
        else if (angle == 270.0 || angle == -90.0) return SpriteUtil.rotate90(original, SpriteUtil.Rotation._270);

        final double radians = Math.toRadians(angle);
        final double cos = Math.cos(radians);
        final double sin = Math.sin(radians);

        int newWidth = original.getWidth();
        int newHeight = original.getHeight();

        final int[] pixels2 = new int[newWidth * newHeight];

        int centerX = original.getWidth() / 2;
        int centerY = original.getHeight() / 2;

        for (int x = 0; x < newWidth; x++)
            for (int y = 0; y < newHeight; y++) {
                int m = x - centerX;
                int n = y - centerY;
                int j = (int) (m * cos + n * sin + centerX);
                int k = (int) (n * cos - m * sin + centerY);

                if (j >= 0 && j < original.getWidth() && k >= 0 && k < original.getHeight()) {
                    pixels2[y * newWidth + x] = original.getPixels()[k * original.getWidth() + j];
                }
            }
        return new Sprite(newWidth, newHeight, pixels2);
    }

    public static void drawPath(IRender r, List<Vec2i> path) {
        int length = path.size();
        for (int i = 0; i < length; i++) {
            Vec2i pos = path.get(i);
            Sprite s;
            if (i == length - 1) {
                s = TARGET;
            } else {
                Vec2i next = path.get(i + 1);
                double angle = Math.toDegrees(Math.atan2(next.y - pos.y, next.x - pos.x)) % 180;
                s = angle == 0 ? HOR_LINE : VER_LINE;
            }
            r.draw((pos.x + 1) * Level.CELL_SIZE, (pos.y + 1) * Level.CELL_SIZE, s);
        }
    }
}
