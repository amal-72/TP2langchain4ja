
package ma.emsi.amal.tp2.tests;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.service.AiServices;
import ma.emsi.amal.tp2.tests.outil.meteo.MeteoTool;

import java.time.Duration;
import java.util.Scanner;

public class Test7Outils {

    public static void main(String[] args) {

        String llmKey = System.getenv("GEMINI_KEY");

        if (llmKey == null || llmKey.isBlank()) {
            System.out.println("La variable d'environnement GEMINI_KEY n'est pas définie.");
            return;
        }

        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .maxRetries(0)
                .timeout(Duration.ofMinutes(5))
                .logRequestsAndResponses(true)
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

                if ("fin".equalsIgnoreCase(question)) {
                    break;
                }

                System.out.println("==================================================");

                String reponse = assistant.chat(question);

                System.out.println("Assistant : " + reponse);
                System.out.println("==================================================");
            }
        }
    }
}
