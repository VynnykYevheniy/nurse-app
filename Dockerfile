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

# Соберите JAR-файл внутри контейнера
RUN mvn clean package

# Команда для запуска приложения
CMD ["java", "-jar", "target/nurse-app-0.1.jar"]