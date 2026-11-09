
package ma.emsi.amal.tp2.tests.outil.meteo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class MeteoTool {

    @Tool("""
        Retourne true si la probabilité d'avoir de la pluie pour la ville dont on donne la latitude et la longitude
        est importante durant une période de nbJours jours. La période commence nbJoursDebut jours après aujourd'hui.
        Par exemple, si nbJoursDebut = 1 et nbJours = 2, la période de 2 jours commence demain.
        Si nbJoursDebut = 0 et nbJours = 3, la période de 3 jours commence aujourd'hui.
        Retourne false sinon.
        """)
    public boolean isPluiePrevue(
            @P(value = "Latitude", name = "latitude") double latitude,
            @P(value = "Longitude", name = "longitude") double longitude,
            @P(value = "Nombre de jours après aujourd'hui pour indiquer le début de la période", name = "nbJoursDebut") int nbJoursDebut,
            @P(value = "Nombre de jours de la période", name = "nbJours") int nbJours) {

        double seuilPluie = 25.0;

        if (nbJoursDebut < 0 || nbJours < 1 || nbJoursDebut + nbJours > 7) {
            throw new IllegalArgumentException(
                    "La période doit être comprise dans les 7 prochains jours."
            );
        }

        try {
            String baseUrl = "https://api.open-meteo.com/v1/forecast";

            int finPeriode = nbJoursDebut + nbJours;

            String apiUri = String.format(
                    Locale.US,
                    "%s?latitude=%f&longitude=%f&daily=precipitation_probability_max&forecast_days=%d",
                    baseUrl, latitude, longitude, finPeriode
            );

            HttpURLConnection connection =
                    (HttpURLConnection) new URI(apiUri).toURL().openConnection();

            connection.setRequestMethod("GET");

            String response;
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                response = scanner.useDelimiter("\\A").next();
            } finally {
                connection.disconnect();
            }

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

            JsonObject daily = jsonResponse.getAsJsonObject("daily");

            int[] precipitationProbabilities = gson.fromJson(
                    daily.get("precipitation_probability_max"),
                    int[].class
            );

            for (int i = nbJoursDebut; i < finPeriode; i++) {
                if (precipitationProbabilities[i] > seuilPluie) {
                    return true;
                }
            }

            return false;

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(
                    "Erreur lors de la récupération de la météo pour ("
                            + latitude + ", " + longitude + ") : " + e.getMessage(),
                    e
            );
        }
    }

    @Tool("""
        Retourne la latitude et la longitude d'une ville à partir de son nom.
        """)
    public double[] getCoordonnees(
            @P(value = "Nom de la ville", name = "ville") String ville) {

        try {
            String nomVille = URLEncoder.encode(
                    ville,
                    StandardCharsets.UTF_8
            );

            String apiUri = "https://geocoding-api.open-meteo.com/v1/search?name="
                    + nomVille;

            HttpURLConnection connection =
                    (HttpURLConnection) new URI(apiUri).toURL().openConnection();

            connection.setRequestMethod("GET");

            String response;
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                response = scanner.useDelimiter("\\A").next();
            } finally {
                connection.disconnect();
            }

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

            JsonArray results = jsonResponse.getAsJsonArray("results");

            if (results == null || results.isEmpty()) {
                return null;
            }

            JsonObject villeTrouvee = results.get(0).getAsJsonObject();

            double latitude = villeTrouvee.get("latitude").getAsDouble();
            double longitude = villeTrouvee.get("longitude").getAsDouble();

            return new double[]{latitude, longitude};

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(
                    "Erreur lors de la recherche des coordonnées de " + ville,
                    e
            );
        }
    }
}
