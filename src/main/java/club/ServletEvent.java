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

@WebServlet("/EvenementServlet")
public class ServletEvent extends HttpServlet {

    // Méthode pour afficher le message d'information
    private void afficherMessage(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h3>" + message + "</h3>");
        response.getWriter().println("<a href='index.jsp'>Retour à la liste des événements</a>");
        response.getWriter().println("</body></html>");
    }

    // Ajouter un événement
    private void ajouterEvenement(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String dateEvent = request.getParameter("date_event");
        String lieu = request.getParameter("lieu");
        String categorie = request.getParameter("categorie");
        int capacite = Integer.parseInt(request.getParameter("capacite"));
        int clubId = Integer.parseInt(request.getParameter("club_id"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO evenement (titre, description, date_event, lieu, categorie, capacite, club_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, titre);
            stmt.setString(2, description);
            stmt.setString(3, dateEvent);
            stmt.setString(4, lieu);
            stmt.setString(5, categorie);
            stmt.setInt(6, capacite);
            stmt.setInt(7, clubId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Rediriger vers la page de confirmation
                response.sendRedirect("confirmation.jsp?success=true");
            } else {
                // Rediriger vers la page de confirmation avec un message d'erreur
                response.sendRedirect("confirmation.jsp?success=false");
            }
        }
    }

    // Supprimer un événement
    private void supprimerEvenement(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM evenement WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                afficherMessage(response, "Événement supprimé avec succès !");
            } else {
                afficherMessage(response, "Erreur lors de la suppression de l'événement.");
            }
        }
    }

    // Modifier un événement
    private void modifierEvenement(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String dateEvent = request.getParameter("date_event");
        String lieu = request.getParameter("lieu");
        String categorie = request.getParameter("categorie");
        int capacite = Integer.parseInt(request.getParameter("capacite"));
        int clubId = Integer.parseInt(request.getParameter("club_id"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE evenement SET titre = ?, description = ?, date_event = ?, lieu = ?, categorie = ?, capacite = ?, club_id = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, titre);
            stmt.setString(2, description);
            stmt.setString(3, dateEvent);
            stmt.setString(4, lieu);
            stmt.setString(5, categorie);
            stmt.setInt(6, capacite);
            stmt.setInt(7, clubId);
            stmt.setInt(8, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                afficherMessage(response, "Événement modifié avec succès !");
            } else {
                afficherMessage(response, "Erreur lors de la modification de l'événement.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                ajouterEvenement(request, response);
            } else if ("supprimer".equals(action)) {
                supprimerEvenement(request, response);
            } else if ("modifier".equals(action)) {
                modifierEvenement(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherMessage(response, "Erreur de base de données : " + e.getMessage());
        }
    }
}
