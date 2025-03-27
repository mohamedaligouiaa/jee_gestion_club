package club;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;




public class Club {
	private int id;
	private String nom;
	private String description;
	public Club(int id, String nom, String description) {
		this.id = id;
		this.nom = nom;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public static ArrayList<Club> afficherClubs() {
        ArrayList<Club> liste = new ArrayList<>();
        Connection c = DatabaseConnection.getConnection(); 

        if (c == null) { 
            System.out.println("Erreur : Connexion à la base de données non établie !");
            return liste;
        }

        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM club");

            while (rs.next()) {
                Club cl = new Club(rs.getInt("idclub"), rs.getString("nom"), rs.getString("description"));
                liste.add(cl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }
	
}
