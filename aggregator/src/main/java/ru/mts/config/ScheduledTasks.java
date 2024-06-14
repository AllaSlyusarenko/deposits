package ru.mts.config;

import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledTasks {

    //проверка каждый день:
    // 1) закрыть ли вклад сегодня
    // 2) надо ли сегодня перечислить проценты - в конце срока и капитализация
    // 3) надо ли сегодня перечислить ли проценты - ежемесячно


    //раз в сутки (86400000 миллисекунд в сутках)
    @Scheduled(fixedDelayString = "${application.scheduled.time}")
    public void doRepositoryTasks() {
    }

}