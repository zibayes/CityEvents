# README

## Руководство пользователя
Данное приложения предоставляет возможность пользователям как узнать информацию о готовящемся мероприятии в городе Красноярске, так и разместить её в данном приложении.
При заходе в приложение пользователь находится на главном экране, который представляет собой список опубликованных пероприятий. В данном списке отображается название мероприятия, дата и время его проведения. 

<img src="/images/MainPage.jpg" alt="Скриншот главного экрана" style="width:350px;"/>

Для того, чтобы узнать более подробную информацию о мероприятии, необходимо на него нажать в списке, после чего откроется страница с подробной информацией о мероприятии. 

<img src="/images/MainPageChoose.jpg" alt="Скриншот главного экрана с указанием, как выбрать мероприятие" style="width:350px;"/>

На данной странице будет отображено местоположение данного мероприятия на карте, его название, дата и время его проведения, стоимость билета, продолжительность мероприятия, прогноз погоды на день его проведения, а также ссылка на сайт мероприятия. 

<img src="/images/Event.jpg" alt="Скриншот экрана с мероприятием" style="width:350px;"/>

Для того, чтобы вернуться на главный экран или перейти на другие вкладки, необходимо нажать на иконку меню в левом верхнем углу или сделать swipe пальцем от левого края экрана вправо. 

<img src="/images/MenuChoose.jpg" alt="Скриншот экрана с выбором меню" style="width:350px;"/>

В открывшемся меню можно перейти на список мероприятий города, на карту мероприятий и во вкладку "о нас". 

<img src="/images/Menu.jpg" alt="Скриншот экрана с меню" style="width:350px;"/>

При переходе на карту мероприятий, на экране появится карта с метками всех опубликованных в приложении мероприятий. 

<img src="/images/Map.jpg" alt="Скриншот экрана с картой мероприятий" style="width:350px;"/>

При переходе во вкладку "о нас" на экране отобразится информация о разработчике приложения и ссылка на сайт разработчка. 

<img src="/images/AboutUs.jpg" alt="Скриншот экрана с информацией о разработчике" style="width:350px;"/>

На главном экране также есть возможность опубликовать собственное мероприятие, нажав на значок плюсика в правом нижнем углу. 

<img src="/images/MainPageAddEvent.jpg" alt="Скриншот главного экрана с указанием, как добавить мероприятие" style="width:350px;"/>

После нажатия отпроется диалоговое окно, в котором необходимо ввести всю необходимую информацию о мероприятии. 

<img src="/images/AddEvent.jpg" alt="Скриншот добавления мероприятия" style="width:350px;"/>

После создания мероприятия оно отобразится в общем списке. Значок удаления мероприятия отображается только у созданных текущем пользователем мероприятий. 

<img src="/images/MainPageDeleteEvent.jpg" alt="Скриншот добавления мероприятия" style="width:350px;"/>

## План монетизации и публикации приложения
Для публикации приложения в Google Play будут предприняты следующие шаги:
1. Создание аккаунт в Google Play Developer Console.
2. Оформление privacy policy.
3. Подготовка маркетинговых материалов (иконка, скриншоты, APK, баннер, текст, проморолик).
4. Обеспечение сборки наличием сертификата цифровой подписи.
5. Настройка оплаты за пользование приложения.
6. Отправка сборки в Google Play.
Приложение планируется выпустить на бесплатной основе, но с монетизацией посредством размещения рекламных баннеров, использования нативной рекламы, а также интерактивных и видео-вставок.

## Инструкция по сборке приложения с использованием командной строки
Чтобы запустить задачу с оболочкой, используйте одну из следующих команд из окна терминала (в Android Studio выберите Вид > Окна инструментов > Терминал).:

- Windows:
``
gradlew task-name
``

- Mac или Linux:
``
./gradlew task-name
``

Чтобы просмотреть список всех доступных задач сборки для вашего проекта, выполните следующее:
``
gradlew tasks
``

## Решение возможных ошибок
Если при сборке приложения возникает ошибка, связанная с файлом local.properties, то необходимо задать в этом файле путь к расположению SDK на данном компьютере.
