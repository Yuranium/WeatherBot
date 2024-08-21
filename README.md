### **Погодный телеграмм бот с использованием Java Spring Boot.**
#### *Описание:*
Данный проект представляет собой реализацию бот-сервиса по прогнозу погоды, используя технологии логирования (в будущем),
HTTP запросы для получения данных о погоде сегодня и на несколько дней в заданном городе, технологию десериализации объектов с помощью библиотеки Jackson, 
упрощение разработки с помощью Lombok, представление данных о погоде пользователю.
---
#### *Внешнее окружение*
* Spring Framework
* Spring Boot
* Telegram Bot API
* Hibernate
* PostgreSQL
* SLF4J (в процессе)
* OpenWeatherMap
* Jackson Json
* Lombok
---
#### *Запуск*
Для запуска из _Docker_ нужно сначала в классе **ru.weather.bot.weatherbot.json.ReceiveData** в методе **fetchWeatherMap()** изменить путь, 
по которому сохраняется изображение на: `/app/images/temp.png` (для запуска из _IDE_ оставляем путь: `temp.png`),
также в _**application.properties**_ и _**docker-compose.yaml**_ файлах нужно указать: 
* Корректный пароль от базы данных.
* Название самой базы данных.
* Имя текущего пользователя для корректного подключения к базе данных приложением.

Далее собрать _jar_ файл с проектом (находится в папке target), проверить в Dockerfile, правильно ли указано название _jar_ файла, далее ввести,
находясь в корне данного проекта команду для запуска приложения на Spring Boot.\
Сначала выполняем команду по сборке образа: `docker build -t spring-boot-weather .`

После сборки образа, нужно собрать сервис из двух контейнеров командой: `docker-compose build`, далее можно запускать приложение Spring Boot командой: `docker-compose up`.
Для удаления двух контейнеров и всех данных внутри можно воспользоваться командой: `docker-compose down`.