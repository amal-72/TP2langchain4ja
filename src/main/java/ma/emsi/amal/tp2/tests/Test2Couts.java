package ma.emsi.amal.tp2.tests;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;


public class Test2Couts {

    public static void main(String[] args) {

        // Récupération de la clé API
        String cle = System.getenv("GEMINI_KEY");

        if (cle == null || cle.isBlank()) {
            throw new RuntimeException("La variable GEMINI_KEY est introuvable.");
        }

        // Création du modèle
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.8-flash")
                .temperature(0.7)
                .maxRetries(0)
                .build();

        // Même question que dans le Test1
        String question = "Quelle heure est-il actuellement ?";

        // Envoi de la question et récupération de la réponse
        ChatResponse reponse = modele.chat(UserMessage.from(question));

        // Affichage de la réponse
        System.out.println("Question : " + question);
        System.out.println("Réponse : " + reponse.aiMessage().text());

        // Récupération des tokens utilisés
        int tokensEntree = reponse.tokenUsage().inputTokenCount();
        int tokensSortie = reponse.tokenUsage().outputTokenCount();
        int tokensTotal = reponse.tokenUsage().totalTokenCount();

        System.out.println("\n--- Utilisation des tokens ---");
        System.out.println("Tokens en entrée : " + tokensEntree);
        System.out.println("Tokens en sortie : " + tokensSortie);
        System.out.println("Total des tokens : " + tokensTotal);

        // Tarifs Flash-Lite utilisés pour l'exercice
        // Attention : ce ne sont pas nécessairement les tarifs de gemini-3.8-flash
        double prixEntree = 0.30; // dollars par million de tokens
        double prixSortie = 2.50; // dollars par million de tokens

        // Calcul du coût de la requête
        double cout = (tokensEntree * prixEntree / 1_000_000)
                + (tokensSortie * prixSortie / 1_000_000);

        System.out.println("\n--- Calcul du coût ---");
        System.out.printf("Coût de la requête : %.10f $%n", cout);

        // Nombre de requêtes similaires pour atteindre 1 dollar
        if (cout > 0) {
            long nombreRequetes = (long) Math.ceil(1.0 / cout);
            System.out.println(
                    "Nombre de requêtes similaires pour atteindre 1 $ : "
                            + nombreRequetes
            );
        }
    }
}
