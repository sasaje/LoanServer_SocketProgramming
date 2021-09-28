package com.example.loanserver2;

/*
 * Exercise 33.1
 * Loan server. Write a server for a client. The client sends loan information (annual interest rate, number og years,
 * and loan amount) to the server.
 * The server computes monthly payment and total payment, and sends them back to the client.
 * */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client2 extends Application{
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    Label loanserverLabel = new Label("Loan Server");
    Label hb1Label = new Label("Annual Interest Rate: ");
    Label hb2Label = new Label("Number of years: ");
    Label hb3Label = new Label("Total loan amount");
    TextField hb1Textfield = new TextField();
    TextField hb2Textfield = new TextField();
    TextField hb3Textfield = new TextField();
    Button submitButton = new Button("Submit");
    TextArea textArea = new TextArea();

    @Override
    public void start(Stage stage) throws IOException {

        loanserverLabel.setStyle("-fx-font-size: 30px");
        hb1Label.setPadding(new Insets(5,0,0,0));
        hb2Label.setPadding(new Insets(5,0,0,0));
        hb3Label.setPadding(new Insets(5,0,0,0));

        HBox hb1 = new HBox(hb1Label, hb1Textfield);
        HBox hb2 = new HBox(hb2Label, hb2Textfield);
        HBox hb3 = new HBox(hb3Label, hb3Textfield);

        hb1.setPadding(new Insets(5,0,0,0));
        hb2.setPadding(new Insets(5,0,0,0));
        hb3.setPadding(new Insets(5,0,0,0));

        VBox vb2 = new VBox(submitButton);
        vb2.setPadding(new Insets(10,10,10,10));
        VBox vb3 = new VBox(textArea);
        vb3.setPadding(new Insets(10,10,10,10));

        VBox vBox = new VBox(loanserverLabel, hb1, hb2, hb3, vb2, vb3);
        vBox.setPadding(new Insets(10,10,10,10));

        BorderPane vboxPane = new BorderPane();
        // Text area to display contents
        vboxPane.setCenter(new AnchorPane(vBox));

        // Create a scene and place it in the stage
        Scene scene = new Scene(vboxPane, 500, 350);
        stage.setTitle("Client"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage

        //SERVER CONNECTION
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

        }
        catch (IOException ex) {
            System.out.println("Client error!");
            textArea.appendText(ex.toString() + '\n');
        }

        new Thread (() -> submitButton.setOnAction(e -> {
            try {
                //Get Annual Interest rate r
                double r = Double.parseDouble(hb1Textfield.getText().trim());
                //Send to server
                System.out.println("R er " + r);
                toServer.writeDouble(r);
                toServer.flush();

                //Get Number of years
                double n = Double.parseDouble(hb2Textfield.getText().trim());
                System.out.println("N er " + n);
                //Send to server
                toServer.writeDouble(n);
                toServer.flush();

                //Get Loan Amount k0
                double k0 = Double.parseDouble(hb3Textfield.getText().trim());
                System.out.println("k0 er " + k0);
                //Send to server
                toServer.writeDouble(k0);
                toServer.flush();

                //Get kn from the server
                double kn = fromServer.readDouble();
                System.out.println("THIS IS kn: " + kn);
//                    fromServer.close();

                Platform.runLater(() -> {
                    // Display to the text area
                    textArea.appendText("Annual Interest Rate is: " + r + "\n" +
                            "Number of years is: " + n + "\n" + "Total loan amount is: " + k0 + "\n" +
                            "The total amount with annual interest rates in " + n + " years is: " + kn + "\n");
                });
            }
            catch (IOException ex) {
                System.out.println("An error has occurred!");
            }
        })).start();
    }

    public static void main(String[] args) {
        launch();
    }
}


