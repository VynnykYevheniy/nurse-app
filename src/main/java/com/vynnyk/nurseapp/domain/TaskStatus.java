package com.vynnyk.nurseapp.domain;

public enum TaskStatus {
    PENDING,         // Ожидает обработки
    ACCEPTED,        // Принята медсестрой
    IN_PROGRESS,     // Медсестра в пути или оказывает помощь
    COMPLETED,       // Визит завершен
    CANCELLED,       // Отменена клиентом или системой
    REJECTED         // Отклонена (например, не найдена доступная медсестра)
}
