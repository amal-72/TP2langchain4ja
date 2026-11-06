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

/**
 * Le RAG facile !
 */
public class Test5Rag1 {

    // Assistant conversationnel
    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        String llmKey = System.getenv("GEMINI_KEY");

        if (llmKey == null || llmKey.isBlank()) {
            System.out.println("La variable d'environnement GEMINI_KEY n'est pas définie.");
            return;
        }

        // Création du modèle Gemini
        // Température <= 0.3 pour favoriser des réponses précises.
        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-3.8-flash")
                .temperature(0.3)
                .build();

        // Chargement du document
        String nomDocument = "infos.txt";
        Document document = FileSystemDocumentLoader.loadDocument(nomDocument);

        // Base de données vectorielle en mémoire
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // Phase 1 : ingestion du document et création des embeddings
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        // Création de l'assistant conversationnel
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(
                        EmbeddingStoreContentRetriever.from(embeddingStore)
                )
                .build();

        // Phase 2 : question avec RAG
        String question = "Comment s'appelle le chat de Pierre ?";

        String reponse = assistant.chat(question);

        System.out.println("Question : " + question);
        System.out.println("Réponse : " + reponse);
    }
}
