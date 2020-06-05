package com.gameszaum.login.core.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class DatabaseCredentials {

    private String host, db, user, pass;
    private int port;

}