package shared;

import java.nio.ByteBuffer;
import java.util.Objects;

public class Vector2 {
    public float x, y;

    public Vector2() {}

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(byte[] data) {
        this(data, 0);
    }

    public Vector2(byte[] data, int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.position(offset);
        this.x = buffer.getFloat();
        this.y = buffer.getFloat();
    }

    public byte[] getData() {
        return ByteBuffer.allocate(8).putFloat(x).putFloat(4, y).array();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2 vector) {
            return x == vector.x && y == vector.y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void sub(float x, float y) {
        this.x -= x;
        this.y -= y;
    }

    public void mul(float x, float y) {
        this.x *= x;
        this.y *= y;
    }

    public void scale(float s) {
        this.x *= s;
        this.y *= s;
    }

    public float length() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public void normalize() {
        float length = length();
        x /= length;
        y /= length;
    }

    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2 mul(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x * v2.x, v1.y * v2.y);
    }

    public static Vector2 scale(Vector2 v1, float scale) {
        return new Vector2(v1.x * scale, v1.y * scale);
    }
}
