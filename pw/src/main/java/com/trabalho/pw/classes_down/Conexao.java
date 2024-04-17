package com.trabalho.pw.classes_down;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    /*
    Default env:
    DATABASE_HOST=localhost;DATABASE_PORT=5432;DATABASE_NAME=tarefa_db;DATABASE_USERNAME=postgres;DATABASE_PASSWORD=postgres
    */

    /*
    https://www.commandprompt.com/education/how-to-create-a-postgresql-database-in-docker/

    psql -h localhost -U postgres
    CREATE DATABASE tarefa_db;
    \l
    \c tarefa_db;
    CREATE TABLE tarefa_tbl(ID SERIAL PRIMARY KEY NOT NULL, TEXTO TEXT NOT NULL, PRIORIDADE INTEGER NOT NULL, DATA_CADASTRO BIGINT NOT NULL);

     */
    public static Connection getConnection() throws SQLException, URISyntaxException {
        String dbUri = "localhost";
        String dbPort = "5432";
        String dbName = "trabalho_pw";

        String username = "postgres";
        String password = "postgres";
        String dbUrl = "jdbc:postgresql://" + dbUri + ':' + dbPort + "/" + dbName + "?serverTimezone=UTC";

        System.out.println(dbUrl);

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
