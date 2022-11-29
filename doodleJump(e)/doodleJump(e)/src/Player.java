
// Braden James
// CSCI3331
// HW4
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Player extends Pane {

    Image doodleImg = new Image(getClass().getClassLoader().getResourceAsStream("images/doodler.png"));
    // doodler image
    ImageView img = new ImageView(doodleImg);
    // velocity of the doodler
    public Point2D doodleVelocity = new Point2D(0, 0);
    private boolean jumpable = true;

    public Player(int character) {
        img.setFitHeight(76);
        img.setFitWidth(76);

        getChildren().addAll(this.img);
    }

    public void moveX(int value) {
        boolean movingRight = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    public void moveY(int value) {
        boolean Xdown = 0 < value;
        for (int i = 0; i < Math.abs(value); i++) {
            for (LandingPad platform : Game.platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (Xdown) {
                        if (this.getTranslateY() + this.getHeight() == platform.getTranslateY()
                                && platform.id == 1) {
                            this.setTranslateY(this.getTranslateY() - 1);
                            jumpable = true;
                            return;
                        }
                        if (this.getTranslateY() + this.getHeight() == platform.getTranslateY() && platform.id == 2
                                && !platform.brokenPlatform()) {
                            platform.brokenBrownPlatform();
                            platform.setDestroy(true);
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (Xdown ? 1 : -1));
            ifFalls();
        }
    }

    public boolean ifFalls() {
        boolean falls = false;
        if (this.getTranslateY() > 700) {
            falls = true;
        }

        return falls;
    }

    public void jump() {
        if (jumpable) {
            doodleVelocity = doodleVelocity.add(1, -30);
            jumpable = false;
        }
    }

    public void setjumpable(boolean jumpable) {
        this.jumpable = jumpable;
    }
}