package dev.langchain4j.model.embedding.onnx.bgelargeenv15;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;

public class BgeLargeEnV15EmbeddingModelFactory implements EmbeddingModelFactory {

    @Override
    public EmbeddingModel create() {
        return new BgeLargeEnV15EmbeddingModel();
    }
}
