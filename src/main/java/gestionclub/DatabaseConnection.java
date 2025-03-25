package gestionclub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection connect() {
        if (connection == null) {
            String nomDB = "gestion_club";
            String urlDB = "jdbc:mysql://localhost:3306/" + nomDB;
            String login = "root";
            String mot_de_passe = "magdell21102018";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Charger le driver MySQL
                
                connection = DriverManager.getConnection(urlDB, login, mot_de_passe);
                System.out.println("Connexion établie !");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver MySQL introuvable !");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Connexion non établie !");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static Connection getConnection() {
        return connect();
    }
}

