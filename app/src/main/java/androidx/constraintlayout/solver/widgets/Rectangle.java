package androidx.constraintlayout.solver.widgets;

public class Rectangle {
    public int height;
    public int width;

    public int f19x;

    public int f20y;

    public void setBounds(int i, int i2, int i3, int i4) {
        this.f19x = i;
        this.f20y = i2;
        this.width = i3;
        this.height = i4;
    }

    void grow(int i, int i2) {
        this.f19x -= i;
        this.f20y -= i2;
        this.width += i * 2;
        this.height += i2 * 2;
    }

    boolean intersects(Rectangle rectangle) {
        int i;
        int i2;
        int i3 = this.f19x;
        int i4 = rectangle.f19x;
        return i3 >= i4 && i3 < i4 + rectangle.width && (i = this.f20y) >= (i2 = rectangle.f20y) && i < i2 + rectangle.height;
    }

    public boolean contains(int i, int i2) {
        int i3;
        int i4 = this.f19x;
        return i >= i4 && i < i4 + this.width && i2 >= (i3 = this.f20y) && i2 < i3 + this.height;
    }

    public int getCenterX() {
        return (this.f19x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.f20y + this.height) / 2;
    }
}
