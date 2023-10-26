# Independent Education Spring

Этот проект представляет собой семестровую работу, выполненную Елизаветой Бельской, студенткой ИТИС КФУ. 
Эта работа была выполнена в рамках второго семестра второго курса.

## Задумка проекта

Целью проекта является создание веб-приложения, которое позволит ученикам и репетиторам находить друг друга для
организации образовательных занятий. Идея проекта аналогична функционалу популярных платформ, таких как Профи.ру.

## Использованные технологии

Проект использует следующие технологии:

- **Spring Boot** Для разработки веб-приложения и управления бизнес-логикой.

- **PostgreSQL, Hibernate, Spring Data Jpa:** Для хранения данных используется реляционная база данных PostgreSQL. 
Hibernate и Spring Data JPA облегчают взаимодействие с базой данных. 

- **Spring Security:** Для обеспечения регистрации и аутентификации пользователей. 
Добавлен собственный AuthenticationProvider для аутентификации по трем параметрам

- **REST API:** Для управления сущностью Order и взаимодействия с данными.

- **Шаблонизатор JSP, JSTL, собственные теги:** Для создания динамических веб-страниц и отображения данных.

- **HTML5, CSS3, Bootstrap:** Для создания пользовательского интерфейса и визуального оформления.

- **Javascript:** Для добавления интерактивности, включая оценку репетиторов в виде "звездочек".

- **AJAX (fetch):** для отправки асинхронных запросов на вышеупомянутый REST API.

- **Работы с внешним API:** Для проверки соответствия номера телефона стране.

- **Конвертеры между DTO и сущностями:** Для удобного взаимодействия с данными.

- **Логирование:** Возникающие исключения логируются с использованием аспектов и JUL.

- **Отлов исключений:** Также предусмотрено отлов исключений с помощью ControllerAdvice.

## Функционал

Проект обладает следующими ключевыми функциями:

- **Регистрация и авторизация:**
  - Ученики и репетиторы могут зарегистрироваться и войти в систему для доступа к функционалу.

- **Ученические заявки:**
  - Ученики могут создавать заявки о поиске репетитора и управлять ими, включая просмотр, редактирование и удаление.

- **Отклик репетиторов:**
  - Репетиторы имеют возможность откликаться на заявки учеников и отказываться от них.

- **Просмотр и выбор репетитора:**
  - Ученики могут просматривать список своих репетиторов и кандидатов, подавших заявки.
  - Ученики могут просматривать страницы репетиторов со всеми отзывами.

- **Утверждение репетитора:**
  - Ученики могут утверждать репетитора-кандидата в качестве своего репетитора, получая доступ к контактам репетитора.

- **Оценки и отзывы:**
  - Ученики могут оставлять оценки и отзывы своим репетиторам, что помогает в развитии и улучшении качества обучения.

- **Удаление аккаунта:**
  - Пользователи могут удалить свои аккаунты. 



