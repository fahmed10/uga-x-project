package com.example.ugaxproject;

public class GameView {

    Client client;

    public void initialize(Client client) {
        this.client = client;
        setupGame();
    }

    public void setupGame() {
        // Queries server for data to setup the game session. Server should return back any data needed for that:
        // Could be locations of objects that spawn on the map at the start of a game session, or anything else the
        // server should be authoritatively informing the client of when the game is set up.
    }
}
