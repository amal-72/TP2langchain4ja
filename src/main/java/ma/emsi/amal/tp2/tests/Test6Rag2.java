package ma.emsi.amal.tp2.tests;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.Scanner;

/**
 * RAG avec un document PDF et une conversation interactive.
 */
public class Test6Rag2 {

    interface Assistant {
        String chat(String userMessage);
    }Quelle est la différence entre le Machine Learning supervisé et le Machine Learning non supervisé ?

    public static void main(String[] args) {

        String llmKey = System.getenv("GEMINI_KEY");

        if (llmKey == null || llmKey.isBlank()) {
            System.out.println("La variable d'environnement GEMINI_KEY n'est pas définie.");
            return;
        }

        // Création du modèle Gemini
        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-3.8-flash")
                .temperature(0.3)
                .build();

        // Chargement du document PDF
        String nomDocument = "support-machine-learning.pdf";
        Document document = FileSystemDocumentLoader.loadDocument(nomDocument);

        // Base vectorielle en mémoire
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // Ingestion du document et création des embeddings
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        // Création de l'assistant avec mémoire et RAG
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(
                        EmbeddingStoreContentRetriever.from(embeddingStore)
                )
                .build();

        // Démarrage de la conversation interactive
        conversationAvec(assistant);
    }

    private static void conversationAvec(Assistant assistant) {

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
