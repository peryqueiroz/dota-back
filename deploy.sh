#!/bin/bash

echo "🚀 Iniciando deploy para Railway..."

# Verificar se o Railway CLI está instalado
if ! command -v railway &> /dev/null; then
    echo "❌ Railway CLI não encontrado. Instalando..."
    npm install -g @railway/cli
fi

# Login (se não estiver logado)
railway login

# Build do projeto
echo "📦 Buildando projeto..."
./gradlew clean build -x test

# Deploy
echo "☁️ Fazendo deploy no Railway..."
railway up

echo "✅ Deploy concluído!"
echo "🌐 URL: $(railway domain)"