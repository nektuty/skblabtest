package ru.tshagaev.skblabtest.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Утилиты для многопотока
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Slf4j
public class ExecutorUtils {
    /**
     * Выполнение однотипных задач в несколько потоков.
     *
     * @param list     Список входных данных
     * @param task     Функция обработки элемента данных
     * @param executor Менеджер выполнения
     * @param <T>      Тип элемента входных данных
     */
    public static <T> void runTaskForList(List<T> list, Consumer<T> task, ExecutorService executor) {
        if (CollectionUtils.isEmpty(list))
            return;

        var futures = new ArrayList<Future<?>>();
        // Запускаем задачу для каждого элемента списка
        for (T elem : list) {
            futures.add(executor.submit(() -> task.accept(elem)));
        }

        // Ждем завершения всех задач
        for (var future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
