<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException, club.DatabaseConnection" %>
<%
// Récupération des données du formulaire
String eventId = request.getParameter("event_id");
String fullname = request.getParameter("fullname");
String email = request.getParameter("email");
String phone = request.getParameter("phone");

if(eventId == null || fullname == null || email == null || phone == null) {
    response.sendRedirect("events.jsp");
    return;
}

Connection conn = null;
PreparedStatement stmt = null;
ResultSet rs = null;

try {
    conn = DatabaseConnection.getConnection();
    conn.setAutoCommit(false); // Démarrer une transaction
    
    // 1. Vérifier la capacité disponible
    String checkQuery = "SELECT capacite, titre, date_event, heure, lieu FROM club.evenement WHERE id = ? FOR UPDATE";
    stmt = conn.prepareStatement(checkQuery);
    stmt.setInt(1, Integer.parseInt(eventId));
    rs = stmt.executeQuery();
    
    if(!rs.next() || rs.getInt("capacite") <= 0) {
        response.sendRedirect("register.jsp?id=" + eventId + "&error=no_capacity");
        return;
    }
    
    // Récupérer les infos de l'événement pour le ticket
    String eventTitle = rs.getString("titre");
    String eventDate = rs.getString("date_event");
    String eventTime = rs.getString("heure");
    String eventLocation = rs.getString("lieu");
    
    // 2. Décrémenter la capacité
    String updateQuery = "UPDATE club.evenement SET capacite = capacite - 1 WHERE id = ?";
    stmt = conn.prepareStatement(updateQuery);
    stmt.setInt(1, Integer.parseInt(eventId));
    int updated = stmt.executeUpdate();
    
    if(updated == 0) {
        conn.rollback();
        response.sendRedirect("register.jsp?id=" + eventId + "&error=update_failed");
        return;
    }
    
    // 3. Enregistrer l'inscription
    String insertQuery = "INSERT INTO club.registrations (event_id, fullname, email, phone, registration_date) " +
                         "VALUES (?, ?, ?, ?, NOW())";
    stmt = conn.prepareStatement(insertQuery);
    stmt.setInt(1, Integer.parseInt(eventId));
    stmt.setString(2, fullname);
    stmt.setString(3, email);
    stmt.setString(4, phone);
    stmt.executeUpdate();
    
    // 4. Récupérer le nom du club
    String clubQuery = "SELECT c.nom FROM club.club c " +
                      "JOIN club.evenement e ON c.id = e.club_id " +
                      "WHERE e.id = ?";
    stmt = conn.prepareStatement(clubQuery);
    stmt.setInt(1, Integer.parseInt(eventId));
    rs = stmt.executeQuery();
    String clubName = rs.next() ? rs.getString("nom") : "Inconnu";
    
    conn.commit();
    
    // Stocker les infos dans la session pour afficher le ticket
    session.setAttribute("ticket_event", eventTitle);
    session.setAttribute("ticket_club", clubName);
    session.setAttribute("ticket_date", eventDate);
    session.setAttribute("ticket_time", eventTime);
    session.setAttribute("ticket_location", eventLocation);
    session.setAttribute("ticket_name", fullname);
    
    // Rediriger vers la même page avec un paramètre de succès
    response.sendRedirect("register.jsp?id=" + eventId + "&success=true");
    
} catch (Exception e) {
    if(conn != null) conn.rollback();
    e.printStackTrace();
    response.sendRedirect("register.jsp?id=" + eventId + "&error=database");
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) { /* Ignorer */ }
    if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* Ignorer */ }
    if (conn != null) try { conn.close(); } catch (SQLException e) { /* Ignorer */ }
}
%>