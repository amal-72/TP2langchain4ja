package ma.emsi.amal.tp2.tests;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;
import java.util.List;

public class Test4Embeddings {

    public static void main(String[] args) {

        // Les 6 couples de phrases à comparer
        List<String> phrases = List.of(
                "Le chat dort sur le canapé.",
                "Un chat est en train de dormir sur le sofa.",

                "J'aime beaucoup le soleil.",
                "Je déteste la pluie.",

                "La voiture roule rapidement sur la route.",
                "Un véhicule avance vite sur une autoroute.",

                "Le chien joue dans le jardin.",
                "Le chat mange dans la cuisine.",

                "L'intelligence artificielle transforme le monde.",
                "Les technologies d'IA changent notre société.",

                "Il fait très froid aujourd'hui.",
                "La température est basse aujourd'hui."
        );

        // 1. Modèle local BGE
        System.out.println("========== MODELE BGE LOCAL ==========");

        EmbeddingModel modeleBGE =
                new BgeSmallEnV15QuantizedEmbeddingModel();

        afficherSimilarites(modeleBGE, phrases);

        // 2. Modèle externe Google Gemini
        System.out.println("\n========== MODELE GOOGLE GEMINI ==========");

        String cle = System.getenv("GEMINI_KEY");

        if (cle == null || cle.isBlank()) {
            throw new RuntimeException(
                    "La variable GEMINI_KEY est introuvable."
            );
        }

        EmbeddingModel modeleGoogle = GoogleGenAiEmbeddingModel.builder()
                .apiKey(cle)
                .modelName("gemini-embedding-001")
                .taskType(
                        GoogleGenAiEmbeddingModel.TaskTypeEnum.SEMANTIC_SIMILARITY
                )
                .timeout(Duration.ofSeconds(30))
                .build();

        afficherSimilarites(modeleGoogle, phrases);
    }

    private static void afficherSimilarites(
            EmbeddingModel modele,
            List<String> phrases) {

        // Calcul des embeddings
        List<TextSegment> segments = phrases.stream()
                .map(TextSegment::from)
                .toList();

        List<Embedding> embeddings = modele.embedAll(segments).content();

        // Calcul de la similarité cosinus pour chaque couple
        for (int i = 0; i < phrases.size(); i += 2) {

            Embedding embedding1 = embeddings.get(i);
            Embedding embedding2 = embeddings.get(i + 1);

            double similarite = CosineSimilarity.between(
                    embedding1,
                    embedding2
            );

            System.out.println("\nPhrase 1 : " + phrases.get(i));
            System.out.println("Phrase 2 : " + phrases.get(i + 1));

            System.out.printf(
                    "Similarité cosinus : %.4f%n",
                    similarite
            );
        }
    }
}
