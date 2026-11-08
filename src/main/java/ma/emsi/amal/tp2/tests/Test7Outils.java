package ma.emsi.amal.tp2.tests;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.service.AiServices;
import ma.emsi.amal.tp2.tests.outil.meteo.MeteoTool;
import java.util.Scanner;
import java.time.Duration;

public class Test7Outils {

    public static void main(String[] args) {

        String llmKey = System.getenv("GEMINI_KEY");

        if (llmKey == null) {
            System.out.println("La variable d'environnement GEMINI_KEY n'est pas définie.");
            return;
        }
        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-3.8-flash")
                .temperature(0.7)
                .maxRetries(0)
                .timeout(Duration.ofMinutes(5))                .logRequestsAndResponses(true)
                .build();

        AssistantMeteo assistant =
                AiServices.builder(AssistantMeteo.class)
                        .chatModel(model)
                        .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                        .tools(new MeteoTool())
                        .build();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question : ");
                String question = scanner.nextLine();

                if (question.isBlank()) {
                    continue;
                }

                System.out.println("==================================================");

                if ("fin".equalsIgnoreCase(question)) {
                    break;
                }

                String reponse = assistant.chat(question);
                System.out.println("Assistant : " + reponse);
                System.out.println("==================================================");
            }
        }
    }
}
