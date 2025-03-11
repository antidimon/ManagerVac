#!/bin/bash

# Сборка JAR-файла
echo "Сборка JAR-файла..."
mvn clean install dependency:copy-dependencies

if [ $? -ne 0 ]; then
    echo "Ошибка при сборке JAR-файла. Проверьте вывод выше."
    exit 1
fi

echo "JAR-файл успешно собран."

# Запуск Docker Compose
echo "Запуск Docker Compose..."
docker-compose up --build -d

if [ $? -ne 0 ]; then
    echo "Ошибка при запуске Docker Compose. Проверьте вывод выше."
    exit 1
fi

echo "Docker Compose успешно запущен."
