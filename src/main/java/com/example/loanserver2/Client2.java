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
import javafx.scene.control.*;
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
    Label rLabel = new Label("Annual Interest Rate: ");
    Label nLabel = new Label("Number of years: ");
    Label k0Label = new Label("Total loan amount");
    TextField rTextfield = new TextField();
    TextField nTextfield = new TextField();
    TextField k0Textfield = new TextField();
    Button submitButton = new Button("Submit");
    TextArea textArea = new TextArea();

    @Override
    public void start(Stage stage) throws IOException {

        loanserverLabel.setStyle("-fx-font-size: 30px");
        rLabel.setPadding(new Insets(5,0,0,0));
        nLabel.setPadding(new Insets(5,0,0,0));
        k0Label.setPadding(new Insets(5,0,0,0));

        HBox rHbox = new HBox(rLabel, rTextfield);
        HBox nHbox = new HBox(nLabel, nTextfield);
        HBox k0Hbox = new HBox(k0Label, k0Textfield);

        rHbox.setPadding(new Insets(5,0,0,0));
        nHbox.setPadding(new Insets(5,0,0,0));
        k0Hbox.setPadding(new Insets(5,0,0,0));

        VBox vb2 = new VBox(submitButton);
        vb2.setPadding(new Insets(10,10,10,10));
        VBox vb3 = new VBox(textArea);
        vb3.setPadding(new Insets(10,10,10,10));

        VBox vBox = new VBox(loanserverLabel, rHbox, nHbox, k0Hbox, vb2, vb3);
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

        submitButton.setOnAction(e -> {
            try {
                //Get Annual Interest rate r, n Number of years and total loan amount k0
                double r = Double.parseDouble(rTextfield.getText());
                double n = Double.parseDouble(nTextfield.getText());
                double k0 = Double.parseDouble(k0Textfield.getText());

                //Send to server
                toServer.writeDouble(r);
                toServer.writeDouble(n);
                toServer.writeDouble(k0);
                //flush
                toServer.flush();

                System.out.println("R er " + r);
                System.out.println("N er " + n);
                System.out.println("k0 er " + k0);

                //Get mp and kn from the server
                double mp = fromServer.readDouble();
                double kn = fromServer.readDouble();

                System.out.println("This is mp: " + mp);
                System.out.println("This is kn: " + kn);

                // Display to the text area
                textArea.appendText("Annual Interest Rate is: " + r + "\n" +
                        "Number of years is: " + n + "\n" + "Total loan amount is: " + k0 + "\n" +
                        "The total amount with annual interest rates in " + n + " years is: " + kn + "\n");
            }
            catch (IOException ex) {
                System.out.println("An error has occurred!");
                System.err.println(ex);
            }
        });
    }
    public static void main(String[] args) {
        launch();
    }
}


