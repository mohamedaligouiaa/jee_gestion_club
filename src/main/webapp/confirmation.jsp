<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmation</title>
    <script type="text/javascript">
        // Fonction pour afficher le popup en fonction du param�tre success
        window.onload = function() {
            var success = '<%= request.getParameter("success") %>';
            if (success === 'true') {
                alert("�v�nement ajout� avec succ�s !");
            } else {
                alert("Erreur lors de l'ajout de l'�v�nement.");
            }
        };
    </script>
</head>
<body>
    <h2>Confirmation de l'ajout de l'�v�nement</h2>
    <!-- Contenu qui peut �tre ajust� ou laiss� vide si le popup suffit -->
</body>
</html>
