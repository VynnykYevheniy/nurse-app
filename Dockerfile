# Используйте официальный образ Maven, который уже содержит JDK
FROM maven:3.8-openjdk-17-slim

# Установка переменных среды
ENV DATABASE_URL=jdbc:postgresql://dpg-d0c8cupr0fns73e5mc0g-a.frankfurt-postgres.render.com/nurse_db
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=fWrQCBenYxJmiy9GwSDLU3wBbHa3HAZB

# Установите рабочую директорию внутри контейнера
WORKDIR /nurse-api

# Копируйте исходный код приложения в контейнер
COPY / /nurse-api

# Копируйте файлы из api/src/main/resources/exercise в папку ресурсов внутри контейнера
COPY src/main/resources/exercise /nurse-api/src/main/resources/exercise

# Соберите JAR-файл внутри контейнера
RUN mvn clean package

# Команда для запуска приложения (замените "nurse-api-0.1.jar" на имя вашего JAR-файла)
CMD ["java", "-jar", "target/nurse-api-0.1.jar"]