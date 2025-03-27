package club;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class ClubServlet
 */
public class ClubServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ClubServlet() {
        // TODO Auto-generated constructor stub
    }
    private void ajoutClub(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "INSERT INTO club (nom, description) VALUES (?, ?);";  // Retrait de 'id'
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");

        try {
            Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setString(2, description);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifierclub (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String sql ="UPDATE club SET nom=? , description = ? WHERE id=? ;";
    	int id = Integer.parseInt(request.getParameter("id"));
    	String nom = request.getParameter("nom");
    	String description = request.getParameter("description");
    	try {
    		Connection c = DatabaseConnection.getConnection();
    		PreparedStatement pst = c.prepareStatement(sql);
    		
    		pst.setString(1, nom);
    		pst.setString(2, description);
    		pst.setInt(3, id);
    		pst.executeUpdate();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    private void supprimerclub (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String sql ="DELETE FROM club WHERE id=? ;";
    	int id = Integer.parseInt(request.getParameter("id"));
    	try {
    		Connection c = DatabaseConnection.getConnection();
    		PreparedStatement pst = c.prepareStatement(sql);
    		pst.setInt(1, id);
    		pst.executeUpdate();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String action = request.getParameter("action");

	     if ("supprimer".equals(action)) {
	    	 supprimerclub(request, response);
		} 
			 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("ajouter".equals(action)) {
		    ajoutClub(request, response);
		} else if ("modifier".equals(action)) {
		    modifierclub(request, response);
		}
    }

}
