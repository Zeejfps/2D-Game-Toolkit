package me.zeejfps.tests.spritesnakegame;

import me.zeejfps.gametoolkit.math.Vec2i;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Zeejfps on 10/6/2016.
 */
public class Snake {

    public final Vec2i head;
    public final Queue<Vec2i> body;

    public Snake() {
        head = new Vec2i();
        body = new ArrayDeque<>();
    }

    public void grow() {
        body.add(new Vec2i(head.x, head.y));
    }

}
