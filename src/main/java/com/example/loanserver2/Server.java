package com.example.loanserver2;

/*
 * Exercise 33.1
 * Loan server. Write a server for a client. The client sends loan information (annual interest rate, number og years,
 * and loan amount) to the server.
 * The server computes monthly payment and total payment, and sends them back to the client.
 * */

import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application{

    @Override
    public void start(Stage stage) {

        // Text area for displaying contents
        TextArea content = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(content), 450, 200);
        stage.setTitle("Server"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage

        new Thread (() -> {

            try {
                //Step 1 create serverSocket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        content.appendText("Server started at " + new Date() + '\n' + "Server waiting for request..." + '\n'));

                //Step 2 make it listen for requests
                Socket socket = serverSocket.accept();

                //Step 3 establish connections to and from the server
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                System.out.println("Accepting connection on port 8000");
                System.out.println("Connection established " + socket.getRemoteSocketAddress().toString());

                while (true){

                    //Step 4 data calculation   |   kn = k0*(1+r)^n
                    double r = (inputFromClient.readDouble())/100; //input from client
                    System.out.println("Annunal interest rate (r) from client: " + r);

                    double n = inputFromClient.readDouble(); //input from client
                    System.out.println("Number of years (n) from client: " + n);

                    double k0 = inputFromClient.readDouble(); //input from client
                    System.out.println("Loan amount (k0) from client: " + k0);

                    double kn = k0 * Math.pow((1+r),n);
//                    double kn = Math.pow((k0*(1+r)),n);
//                    double kn = k0 * Math.pow(1+ r, n);
                    //    double kn = k0 * Math.pow(1 + (r / mp), mp * n);

                    outputToClient.writeDouble(kn); //output kn to client
                    System.out.println("The amount with rates in " + n + " years: " + kn);

                    Platform.runLater(() -> content.appendText("Loan amount (k0) from client: " + k0 + "\n" +
                            "Annunal interest rate (r) from client: " + r+ "\n" +
                            "Number of years (n) from client: " + n + "\n" +
                            "The amount with rates in " + n + "years: " + kn + "\n"));
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
