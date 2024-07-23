## Выпускной проект "Вклады" Финтех.Академия от МТС
### Реализовала:
####  - Микросервисное приложение для открытия вкладов, используя паттерн Агрегатор (Aggregator)
####  - Разработала архитектуру приложения и баз данных микросервисов
####  - Реализовала межсервисное взаимодействие через HTTP запросы (RestTemplate)
####  - Сервис Account отвечает за операции с банковскими счетами: получение счета, уменьшение счета, увеличение счета, перевод суммы между счетами, проверка наличия необходимой суммы, закрытие счета
####  - Сервис CustomerService отвечает за операции с клиентами: получение клиента, отправка смс-кода и его подтверждение, получение всех банковских счетов клиента, добавление счета клиенту
####  - Сервис Deposit отвечает за операции с заявками и вкладами: создание заявки, подтверждение по смс-коду, одобрение заявки, получение всех отклоненных заявок, удаление заявки, создание вклада по заявке, получение всех активных вкладов, получение полной информации по вкладу, закрытие вклад
####  - Сервис Aggregator: собирает информацию из функциональных сервисов, обрабатывает её и выводит пользователю
####  - Аутентификацию по номеру телефона и смс-коду
####  - Реализовала стартер для внедрения логирования c помощью AOP
####  - Выполнила отображение UI с помощью Thymeleaf
####  - Осуществила миграцию с помощью Flyway
####  - Разработала ExceptionHandler c помощью ControllerAdvice
####  - Покрыла тестами Controller, Service, DAO слой(TestContainers)


### Технологический стек: Java, ООП, Spring Boot, AOP, Flyway, Thymeleaf,PostgreSQL, JUnit, TestContainers