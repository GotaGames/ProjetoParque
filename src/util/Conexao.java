package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    private static final String URL =
            "jdbc:mysql://localhost:3306/parque_estacionamento?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection abrir() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ligar à BD: " + e.getMessage());
        }
    }
}