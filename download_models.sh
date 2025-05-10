#!/bin/bash

# Download the model file
curl -L "https://huggingface.co/BAAI/bge-large-en-v1.5/resolve/main/onnx/model.onnx?download=true" \
     -o "langchain4j-embeddings-bge-large-en-v15/src/main/resources/bge-large-en-v1.5.onnx"

echo "Model downloaded successfully!" 
