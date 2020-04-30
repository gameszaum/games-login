package com.gameszaum.login.spigot.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class DatabaseCredentials {

    private String host, db, port, user, pass;

}