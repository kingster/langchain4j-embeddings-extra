package dev.langchain4j.model.embedding.onnx.bgelargeenv15;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.RelevanceScore;
import org.junit.jupiter.api.Test;

import static dev.langchain4j.internal.Utils.repeat;
import static dev.langchain4j.model.embedding.onnx.internal.VectorUtils.magnitudeOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

class BgeLargeEnV15EmbeddingModelIT {

    @Test
    void should_embed() {

        EmbeddingModel model = new BgeLargeEnV15EmbeddingModel();

        Embedding first = model.embed("hi").content();
        assertThat(first.vector()).hasSize(1024);

        Embedding second = model.embed("hello").content();
        assertThat(second.vector()).hasSize(1024);

        double cosineSimilarity = CosineSimilarity.between(first, second);
        assertThat(RelevanceScore.fromCosineSimilarity(cosineSimilarity)).isGreaterThan(0.96);
    }

    // @Test
    void embedding_should_have_the_same_values_as_embedding_produced_by_sentence_transformers_python_lib() {

        EmbeddingModel model = new BgeLargeEnV15EmbeddingModel();

        Embedding embedding = model.embed("I love cool flags!").content();

        assertThat(embedding.vector()[0]).isCloseTo(-0.030576343f, withPercentage(1));
        assertThat(embedding.vector()[1]).isCloseTo(0.058405522f, withPercentage(1));
        assertThat(embedding.vector()[382]).isCloseTo(-0.008137911f, withPercentage(1));
        assertThat(embedding.vector()[383]).isCloseTo(-0.02132941f, withPercentage(1));
    }

    @Test
    void should_embed_1510_token_long_text() {

        EmbeddingModel model = new BgeLargeEnV15EmbeddingModel();

        String oneToken = "hello ";

        Embedding embedding = model.embed(repeat(oneToken, 1510)).content();

        assertThat(embedding.vector()).hasSize(1024);
    }

    @Test
    void should_embed_text_longer_than_1510_tokens_by_splitting_and_averaging_embeddings_of_splits() {

        EmbeddingModel model = new BgeLargeEnV15EmbeddingModel();

        String oneToken = "hello ";

        Embedding embedding1510 = model.embed(repeat(oneToken, 1510)).content();
        assertThat(embedding1510.vector()).hasSize(1024);

        Embedding embedding1511 = model.embed(repeat(oneToken, 1511)).content();
        assertThat(embedding1511.vector()).hasSize(1024);

        double cosineSimilarity = CosineSimilarity.between(embedding1510, embedding1511);
        assertThat(RelevanceScore.fromCosineSimilarity(cosineSimilarity)).isGreaterThan(0.99);
    }

    @Test
    void should_produce_normalized_vectors() {

        EmbeddingModel model = new BgeLargeEnV15EmbeddingModel();

        String oneToken = "hello ";

        assertThat(magnitudeOf(model.embed(oneToken).content()))
                .isCloseTo(1, withPercentage(0.01));
        assertThat(magnitudeOf(model.embed(repeat(oneToken, 999)).content()))
                .isCloseTo(1, withPercentage(0.01));
    }

    @Test
    void should_return_correct_dimension() {

        EmbeddingModel model = new BgeLargeEnV15EmbeddingModel();

        assertThat(model.dimension()).isEqualTo(384);
    }
}