public class Coordinates {
    private final int MAX_X = 180;
    private final int MIN_X = -180;
    private final int MAX_Y = 90;
    private final int MIN_Y = -90;

    private int xUpper;
    private int xLower;
    private int yUpper;
    private int yLower;

    public Coordinates(int xU, int xL, int yU, int yL) {
        xUpper = xU;
        xLower = xL;
        yUpper = yU;
        yLower = yL;
    }

    public void moveDown(int step) {
        if (yLower - step < MIN_Y) {
            step = MIN_Y - yLower;
        }

        yLower -= step;
        yUpper -= step;
    }

    public void moveUp(int step) {
        if (yUpper + step > MAX_Y) {
            step = MAX_Y - yUpper;
        }

        yLower += step;
        yUpper += step;
    }

    public void moveLeft(int step) {
        if (xLower - step < MIN_X) {
            step = MIN_X - xLower;
        }

        xLower -= step;
        xUpper -= step;
    }

    public void moveRight(int step) {
        if (xUpper + step > MAX_X) {
            step = MAX_X - xUpper;
        }

        xLower += step;
        xUpper += step;
    }

    public void zoomIn(int step) {
        System.out.println("xU: " + xUpper + " xL" + xLower);
        if (xLower - xUpper < step) {
            xLower += step / 2;
            xUpper -= step / 2;
        }
        if (yLower - yUpper < step) {
            yLower += step / 2;
            yUpper -= step / 2;
        }
    }

    public void zoomOut(int step) {
        if (xLower - step / 2 < MIN_X) {
            xLower = MIN_X;
        } else {
            xLower -= step / 2;
        }
        if (xUpper + step / 2 > MAX_X) {
            xUpper = MAX_X;
        } else {
            xUpper += step / 2;
        }
        if (yLower - step / 2 < MIN_Y) {
            yLower = MIN_Y;
        } else {
            yLower -= step / 2;
        }
        if (yUpper + step / 2 > MAX_Y) {
            yUpper = MAX_Y;
        } else {
            yUpper += step / 2;
        }

    }

    public int getxUpper() {
        return xUpper;
    }

    public int getxLower() {
        return xLower;
    }

    public int getyUpper() {
        return yUpper;
    }

    public int getyLower() {
        return yLower;
    }
}
