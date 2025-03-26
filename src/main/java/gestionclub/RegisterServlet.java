package club;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les paramètres du formulaire
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String city = request.getParameter("city");

        // Vérification que les mots de passe correspondent
        if (password.equals(confirmPassword)) {
            // Insérer les données dans la base de données
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO users (email, username, password, full_name, gender, city) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, email);
                    statement.setString(2, username);
                    statement.setString(3, password);  // Note : N'oubliez pas de hasher le mot de passe pour la production
                    statement.setString(4, fullName);
                    statement.setString(5, gender);
                    statement.setString(6, city);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        // Enregistrement réussi, rediriger vers la page de connexion ou succès
                        response.sendRedirect("login.jsp");
                    } else {
                        // Enregistrement échoué
                        request.setAttribute("error", "Erreur lors de l'enregistrement. Essayez à nouveau.");
                        request.getRequestDispatcher("/register.jsp").forward(request, response);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "Erreur de base de données. Essayez plus tard.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } else {
            // Les mots de passe ne correspondent pas
            request.setAttribute("error", "Les mots de passe ne correspondent pas.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}

