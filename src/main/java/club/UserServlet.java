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
import org.mindrot.jbcrypt.BCrypt;
/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    private void ajoutUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "INSERT INTO users (email, username, password, full_name, gender, city,role) VALUES (?, ?,?,?,?,?,?);";  
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullname = request.getParameter("full_name");
        String gender = request.getParameter("gender");
        String city = request.getParameter("city");
        String role = request.getParameter("role");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, username);
            pst.setString(3, hashedPassword);
            pst.setString(4, fullname);
            pst.setString(5, gender);
            pst.setString(6, city);
            pst.setString(7, role);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void modifieruser (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String sql ="UPDATE users SET email=? ,username=?, password=?, full_name=?, gender=?, city=?,role=? WHERE id=? ;";
    	int id = Integer.parseInt(request.getParameter("id"));
    	String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullname = request.getParameter("full_name");
        String gender = request.getParameter("gender");
        String city = request.getParameter("city");
        String role = request.getParameter("role");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    	try {
    		Connection c = DatabaseConnection.getConnection();
    		PreparedStatement pst = c.prepareStatement(sql);
    		
    		pst.setString(1, email);
            pst.setString(2, username);
            pst.setString(3, hashedPassword);
            pst.setString(4, fullname);
            pst.setString(5, gender);
            pst.setString(6, city);
            pst.setString(7, role);
            pst.setInt(7, id);
    		pst.executeUpdate();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    private void supprimeruser (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String sql ="DELETE FROM users WHERE id=? ;";
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
	    	 supprimeruser(request, response);
		} 
			 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("ajouter".equals(action)) {
		    ajoutUser(request, response);
		} else if ("modifier".equals(action)) {
		    modifieruser(request, response);
		}
    }


}
