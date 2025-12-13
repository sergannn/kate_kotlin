# DriveNext - Android App

Android приложение для аренды автомобилей, реализованное на Kotlin с использованием XML layouts.

## Структура проекта

Проект содержит 14 экранов из Figma дизайна:

### Экраны онбординга
1. **SplashActivity** - Экран загрузки
2. **OnboardingActivity** (3 экрана) - Онбординг с тремя страницами:
   - Аренда автомобилей
   - Безопасно и удобно
   - Лучшие предложения

### Экраны авторизации
3. **GettingStartedActivity** - Стартовый экран с кнопками входа и регистрации
4. **LoginActivity** - Экран входа в аккаунт

### Экраны регистрации
5. **SignUp1Activity** - Регистрация: Email и пароль
6. **SignUp2Activity** - Регистрация: Личные данные (ФИО, дата рождения, пол)
7. **SignUp3Activity** - Регистрация: Документы (фото профиля, водительское удостоверение, паспорт)

### Дополнительные экраны
8. **CongratulationsActivity** - Экран поздравления после успешной регистрации
9. **NoConnectionActivity** - Экран отсутствия подключения к интернету

## Технологии

- **Kotlin** - основной язык программирования
- **XML Layouts** - верстка экранов
- **Material Design Components** - компоненты UI
- **ConstraintLayout** - для позиционирования элементов

## Установка

1. Клонируйте репозиторий
2. Откройте проект в Android Studio
3. Синхронизируйте Gradle файлы
4. Запустите приложение

## Особенности

- Простой и понятный код
- Соответствие дизайну из Figma
- XML layouts для всех экранов
- Навигация между экранами через Intent
- Все строки вынесены в resources

## Цветовая схема

- Primary: #2A1246
- Text: #1A1A1A
- Label: #404040
- Gray: #667085
- Stroke: #D0D5DD

## Структура файлов

```
app/src/main/
├── java/com/drivenext/app/
│   ├── MainActivity.kt
│   ├── SplashActivity.kt
│   ├── OnboardingActivity.kt
│   ├── GettingStartedActivity.kt
│   ├── LoginActivity.kt
│   ├── SignUp1Activity.kt
│   ├── SignUp2Activity.kt
│   ├── SignUp3Activity.kt
│   ├── CongratulationsActivity.kt
│   └── NoConnectionActivity.kt
└── res/
    ├── layout/          # XML layouts для всех экранов
    ├── values/
    │   ├── colors.xml   # Цветовая палитра
    │   ├── strings.xml # Все строки
    │   └── dimens.xml  # Размеры и отступы
    └── drawable/        # Drawable ресурсы
```
