package ma.emsi.amal.tp2.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;

public class Test1 {

    public static void main(String[] args) {

        // Récupération de la clé API
        String cle = System.getenv("GEMINI_KEY");

        if (cle == null || cle.isBlank()) {
            throw new RuntimeException("La variable GEMINI_KEY est introuvable.");
        }
        // Création du modèle avec un builder
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.8-flash")
                .temperature(0.7)
                .maxRetries(0)
                .build();

        // Pose une question au modèle
        String reponse = modele.chat("Quelle heure est-il actuellement ?");
        // Affiche la réponse
        System.out.println("Réponse du modèle : " + reponse);
    }
}