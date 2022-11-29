
// Braden James
// CSCI3331
// HW4
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Game extends Application {

    public HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public static ArrayList<LandingPad> platforms = new ArrayList<>();
    public static int score = 0;
    public boolean playerPosition;
    public Player player;

    Image img = new Image(getClass().getResourceAsStream("images/background2.png"));

    // space between platforms
    public static final int spacing = 68;
    public static Pane appPane = new Pane();
    public static Pane gamePane = new Pane();
    Text gameText = new Text();
    Text scoreText = new Text();

    public void initialize() {
        ImageView background = new ImageView();
        background.setFitHeight(700);
        background.setFitWidth(450);
        int shiftY = 650;
        int Xmax = 160;
        int Xmin = 130;
        // Green Platforms
        for (int i = 0; i < 8; i++) {
            // set y random placement
            shiftY -= Xmin + (int) (Math.random() * ((Xmax - Xmin) + 1));
            platforms.add(new LandingPad(1, (int) (Math.random() * 5 * spacing), shiftY));
        }
        // Brown Platforms
        for (int i = 0; i < 4; i++) {
            // set y shift random placement
            shiftY -= Xmin + (int) (Math.random() * ((Xmax - Xmin) + 1));
            platforms.add(new LandingPad(2, (int) (Math.random() * 5 * spacing), shiftY));
        }
        addDoodler(background);
    }

    private void movingDoodler() {
        if (player.getTranslateY() >= 5) {
            player.jump();
            player.setjumpable(false);
        }
        if (keyPressed(KeyCode.LEFT)) {
            player.setScaleX(-1);
            player.moveX(-5);
        }
        if (keyPressed(KeyCode.RIGHT)) {
            player.setScaleX(1);
            player.moveX(5);
        }
        if (player.doodleVelocity.getY() < 10) {
            player.doodleVelocity = player.doodleVelocity.add(0, 1.1);
        } else
            player.setjumpable(false);
        player.moveY((int) player.doodleVelocity.getY());
    }

    public void addDoodler(ImageView background) {
        player = new Player(0);
        player.setTranslateX(185);
        player.setTranslateY(650);
        player.translateYProperty().addListener((obs, old, newValue) -> {
            playerPosition = false;
            if (player.getTranslateY() < 300) {
                playerPosition = true;
                for (LandingPad plat : Game.platforms) {
                    plat.setTranslateY(plat.getTranslateY() + 0.5);
                    if (plat.getTranslateY() == 701) {
                        if (plat.id == 2) {
                            plat.brownPlatform();
                            plat.setDestroy(false);
                        }
                        plat.setTranslateY(1);
                        plat.setTranslateX(Math.random() * 6 * spacing);
                    }
                }
            }
        });

        gamePane.getChildren().add(player);
        appPane.getChildren().addAll(background, gamePane);
    }

    private void checkFlip() {
        // check x, flip player right
        if (player.getTranslateX() < -59) {
            player.setTranslateX(520);
            // check x, flip player left
        } else if (player.getTranslateX() > 450 && player.getTranslateX() > 519) {
            player.setTranslateX(-59);
        }
    }

    private void playerScore() {
        gamePane.getChildren().remove(scoreText);

        if (playerPosition == true) {
            score += 1;
        }
        scoreText.setText("SCORE: " + score);
        scoreText.setTranslateY(30);
        scoreText.setTranslateX(50);
        scoreText.setScaleX(2);
        scoreText.setScaleY(2);
        gamePane.getChildren().add(scoreText);
    }

    public void update() {
        movingDoodler();
        playerScore();
        checkFlip();
        if (player.ifFalls()) {
            mainText();
            gameText.setText("Game over!. Press the spacebar to restart the game.");
            scoreText.setTranslateY(300);
            scoreText.setTranslateX(200);
            if (keyPressed(KeyCode.SPACE)) {
                restart();
            }
        }
    }

    private void mainText() {

        gamePane.getChildren().remove(gameText);
        gameText.setText(null);
        gameText.setTranslateX(150);
        gameText.setTranslateY(330);

        gameText.setScaleX(2);
        gameText.setScaleY(2);
        gamePane.getChildren().add(gameText);
    }

    public void restart() {
        platforms.clear();
        keys.clear();
        Game.score = 0;
        gamePane.getChildren().remove(gameText);
        gamePane.getChildren().clear();
        appPane.getChildren().removeAll(gamePane);
        gamePane.getChildren().clear();

        initialize();
    }

    private boolean keyPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        initialize();
        Scene scene = new Scene(appPane, 450, 700);

        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
        });
        primaryStage.setTitle("Doodle Jump");
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}