package com.carsim;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.Match;
import javafx.css.Stylesheet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SecondaryController implements Initializable{

    @FXML
    Rectangle platform;

    @FXML
    AnchorPane car;

    @FXML
    Button secondaryButton;

    @FXML 
    ImageView frontTyreImg;

    @FXML 
    ImageView backTyreImg;

    @FXML
    ImageView frontTyreImg1;

    @FXML 
    ImageView backTyreImg1;

    @FXML
    AnchorPane car1;

    @FXML
    ImageView frontTyreImg11;

    @FXML 
    ImageView backTyreImg11;

    @FXML
    AnchorPane car11;

    @FXML
    Rectangle layered;


    private static final double CLAMP_VALUE = 1.0d;  
    private static final double STATIC_FRICTION = 2.0d;

    private Vector2 pos;
    private Vector2 speed;
    private DoubleProperty rotation;
    KeyHandler<KeyEvent> keyHandler;

    double duration = 1.d;
    double estimate = 0;
    private double deltaTime = 1d/60d;
    

    protected double angularSpeed;

    protected double start;

    private boolean sync;

    protected double inc;

    private int sign;

    private DoubleProperty jumpSpeed;

    private int jumpPower; 
    

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    private double interpolate(double a, double e, double t) {
        return a + (e - a) * t;
    }

    public double cubicEaseInOut(double t) {
        return t < CLAMP_VALUE / 2 ? CLAMP_VALUE * 4 * polynomialEaseIn(t, 3) :
            CLAMP_VALUE - (Math.pow(-CLAMP_VALUE * 2 * t + 2, 3) / 2);
    }

    public double polynomialEaseOut(double t, double grade) {
        return 1 - Math.pow(1 - t, grade);
    }

    public double polynomialEaseIn(double t, double grade) {
        return Math.pow(t, grade);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Thread  c = new Thread(() -> {
            while(true) {
            estimate += deltaTime;
                
                double percentage = Double.min(estimate / duration, CLAMP_VALUE);
                
                double prevRotation = rotation.getValue();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                var start1 = System.currentTimeMillis();
                Platform.runLater(() -> {backTyreImg.setRotate(interpolate(start, 1000d, 
                    cubicEaseInOut(percentage)));
                    System.out.println("DIFF → " + (System.currentTimeMillis() - start1));
                });
                if(percentage >= CLAMP_VALUE){
                    estimate = 0;
                    start = 0;
                    duration = 3d;
                }
                jumpSpeed.set(interpolate(jumpSpeed.getValue(), jumpPower,
                    polynomialEaseIn(0.06, 2)));
                
                
                jumpPower -= jumpPower < 0 ? -1 : 0;
            }
        });
        
        AnimationTimer t = new AnimationTimer() {

            private int gravity;

            @Override
            public void handle(long now) {
                
                estimate += deltaTime;
                
                double percentage = Double.min(estimate / duration, CLAMP_VALUE);
                
                double prevRotation = rotation.getValue();

                var start1 = System.currentTimeMillis();
                backTyreImg.setRotate(interpolate(start, 1000d, 
                    cubicEaseInOut(percentage)));
                    System.out.println("DIFF → " + (System.currentTimeMillis() - start1));
                
                if(percentage >= CLAMP_VALUE){
                    estimate = 0;
                    start = 0;
                    duration = 3d;
                }
                // jumpSpeed.set(interpolate(jumpSpeed.getValue(), jumpPower,
                //     polynomialEaseIn(0.06, 2)));
                
                
                jumpPower -= jumpPower < 0 ? -1 : 0;
            }
            
        };
        Platform.runLater(car::requestFocus);
        secondaryButton.setFocusTraversable(false);
        pos = new Vector2(0d, car.getLayoutY());
        speed = new Vector2(1d, 0d);
        rotation = new SimpleDoubleProperty();
        jumpSpeed = new SimpleDoubleProperty();
        rotation.bindBidirectional(backTyreImg.rotateProperty());
        frontTyreImg.rotateProperty().bind(rotation);
        frontTyreImg1.rotateProperty().bind(rotation);
        backTyreImg1.rotateProperty().bind(rotation);
        frontTyreImg11.rotateProperty().bind(rotation);
        backTyreImg11.rotateProperty().bind(rotation);
        
        
        pos.getXProprety().bindBidirectional(car.translateXProperty());
        pos.getYProprety().bindBidirectional(car.translateYProperty());
        car1.translateXProperty().bindBidirectional(pos.getXProprety());
        car1.translateYProperty().bindBidirectional(pos.getYProprety());
        car11.translateXProperty().bindBidirectional(pos.getXProprety());
        car11.translateYProperty().bindBidirectional(pos.getYProprety());
        pos.getXProprety().bind(speed.getXProprety().divide(1d));
        pos.getYProprety().bind(speed.getYProprety());
        speed.getXProprety().bindBidirectional(rotation);
        speed.getYProprety().bindBidirectional(jumpSpeed);
        // t.start();
        car.setOnKeyReleased(e -> {
            // angularSpeed = rotation.get() + (30d * sign); 
        });
        c.start();
    }

    @FXML 
    private void handleInput(KeyEvent e) {
        sync = true;
        if(e.getCode().equals(KeyCode.D) || e.getCode().equals(KeyCode.RIGHT)) {
            // System.out.println(deltaTime * STATIC_FRICTION);
            // duration += deltaTime * STATIC_FRICTION;
            angularSpeed+= 100;
            sign = 1;
            
            
        }
        else if(e.getCode().equals(KeyCode.W) || e.getCode().equals(KeyCode.UP)) {
            System.out.println(jumpSpeed.get());
            jumpPower = -70;
        }
        if(e.getCode().equals(KeyCode.A) || e.getCode().equals(KeyCode.LEFT)) {
            duration += deltaTime * STATIC_FRICTION;
            angularSpeed-=100d;
            sign = -1;
            
        }
    }
}