#!/bin/bash
  echo "Parando o container atual..."
  docker container stop $(docker ps -a -q)

  echo "Deletando o container atual..."
  docker container rm $(docker ps -a -q)

  echo "Atualizando main.."
  git pull origin main

  echo "Buildando projeto ..."
  ./gradlew build -x test

  echo "Buildando imagem docker do projeto  ..."
  docker build -t dotaslz .

  echo "Rodando docker do projeto  ..."
  docker run -p 8081:8081 -d dotaslz

  echo "Rodando logs do container  ..."
  docker logs -f $(docker ps -a -q)