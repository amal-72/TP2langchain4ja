# Utilisation de l'IA dans le TP 2

**Auteur : Amal Jawhari, groupe G2, n° 14**

J'ai utilisé ChatGPT (OpenAI) comme aide ponctuelle pendant la réalisation du TP 2 avec LangChain4j. J'ai suivi les consignes et les étapes données par le professeur. Cette page présente les tâches que j'ai réalisées moi-même et les situations dans lesquelles j'ai eu recours à l'IA.

## Ce que j'ai fait moi-même

- Création du projet Java Maven et du dépôt GitHub.
- Configuration de l'environnement de développement avec Java 25 et IntelliJ IDEA.
- Lecture de l'énoncé et suivi des étapes indiquées par le professeur.
- Réalisation des classes de test (Test1 à Test7, ainsi que TestMeteo).
- Lecture et compréhension du code fourni dans l'énoncé, notamment pour le RAG et la classe MeteoTool.
- Ajout et configuration des dépendances dans le fichier pom.xml.
- Exécution des différents tests, observation des résultats et conservation des réponses obtenues auprès de Gemini.
- Vérification du fonctionnement des outils météo et géolocalisation avec Open-Meteo.
- Analyse des résultats : tokens, similarités, réponses du modèle et échanges liés à l'utilisation des outils.
- Gestion du dépôt GitHub : vérification des modifications, commits et push.

## Aide de l'IA

J'ai principalement sollicité ChatGPT lorsque certaines parties du code ne fonctionnaient pas comme prévu ou lorsque je rencontrais des erreurs pendant l'exécution.

### Compréhension et explication

- Explication de certaines notions du cours : tokens, embeddings, similarité cosinus, RAG, annotations `@Tool` et `@P`.
- Aide à comprendre le fonctionnement de LangChain4j et l'utilisation des outils par le LLM.
- Explication de l'enchaînement des appels entre Gemini, l'outil de géolocalisation et l'outil météo.
- Aide à interpréter les messages affichés dans la console et les journaux de logging.

### Correction et débogage du code

- Aide à identifier les problèmes rencontrés dans certaines portions de code qui ne fonctionnaient pas correctement.
- Aide à comprendre et à rechercher l'origine d'une erreur HTTP 403 rencontrée pendant les tests.
- Aide à corriger certains éléments de la configuration Maven et des dépendances.
- Aide à comprendre les résultats des tests RAG et les problèmes rencontrés lors de l'utilisation du document PDF.

Les corrections proposées ont été examinées et testées dans mon environnement avant d'être conservées.

### Git et environnement

- Aide ponctuelle pour les commandes Git et PowerShell.
- Vérification des fichiers à inclure dans les commits et des modifications à envoyer sur GitHub.

## Limites et vérification

Je ne considère pas les propositions de l'IA comme exactes par défaut. J'ai vérifié les corrections suggérées, leur cohérence avec les consignes du professeur et leur fonctionnement en exécutant les programmes moi-même.

Les réponses de Gemini, les résultats météo, les coordonnées, les similarités et les consommations de tokens présentés dans le TP proviennent de mes propres tests.