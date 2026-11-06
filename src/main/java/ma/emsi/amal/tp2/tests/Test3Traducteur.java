package ma.emsi.amal.tp2.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;

public class Test3Traducteur {

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        if (cle == null || cle.isBlank()) {
            throw new RuntimeException("La variable GEMINI_KEY est introuvable.");
        }

        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.8-flash")
                .temperature(0.7)
                .build();

        // 1. Template avec une seule variable : le texte à traduire
        PromptTemplate templateSimple = PromptTemplate.from(
                "Traduis le texte suivant en anglais : {{it}}"
        );

        Prompt promptSimple = templateSimple.apply(
                "Bonjour, comment vas-tu ?"
        );

        ChatResponse reponseSimple = modele.chat(
                promptSimple.toUserMessage()
        );

        System.out.println("=== Traduction avec une variable ===");
        System.out.println("Prompt : " + promptSimple.text());
        System.out.println("Réponse : " + reponseSimple.aiMessage().text());


        // 2. Template avec deux variables : langue et texte
        PromptTemplate templateDouble = PromptTemplate.from(
                "Traduis le texte suivant en {{langue}} : {{texte}}"
        );

        Prompt promptDouble = templateDouble.apply(Map.of(
                "langue", "espagnol",
                "texte", "Bonjour, comment vas-tu ?"
        ));

        ChatResponse reponseDouble = modele.chat(
                promptDouble.toUserMessage()
        );

        System.out.println("\n=== Traduction avec deux variables ===");
        System.out.println("Prompt : " + promptDouble.text());
        System.out.println("Réponse : " + reponseDouble.aiMessage().text());
    }
}
