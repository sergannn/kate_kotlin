# Настройка Supabase и Google Sign In

## 1. Настройка Google OAuth в Supabase

1. Перейдите в [Supabase Dashboard](https://supabase.com/dashboard)
2. Выберите ваш проект
3. Перейдите в **Authentication** → **Providers**
4. Включите **Google** провайдер
5. Добавьте:
   - **Client ID** (из Google Cloud Console)
   - **Client Secret** (из Google Cloud Console)

## 2. Получение Google Client ID

1. Перейдите в [Google Cloud Console](https://console.cloud.google.com/)
2. Создайте новый проект или выберите существующий
3. Включите **Google+ API**
4. Перейдите в **Credentials** → **Create Credentials** → **OAuth client ID**
5. Выберите **Android** как тип приложения
6. Укажите:
   - **Package name**: `com.drivenext.app`
   - **SHA-1 certificate fingerprint**: (получите через `keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android`)
7. Также создайте **Web application** OAuth client ID (для Supabase)

## 3. Настройка в приложении

1. Откройте `app/src/main/res/values/strings.xml`
2. Замените `YOUR_GOOGLE_CLIENT_ID` на ваш **Web application Client ID** из Google Cloud Console

```xml
<string name="default_web_client_id">ВАШ_WEB_CLIENT_ID.apps.googleusercontent.com</string>
```

## 4. Настройка Redirect URL в Supabase

В настройках Google провайдера в Supabase добавьте Redirect URL:
```
https://algfbtuheljugjyxuyge.supabase.co/auth/v1/callback
```

## 5. Тестирование

После настройки:
- Кнопка "Войти через Google" будет видна на экране Login
- При нажатии откроется диалог выбора Google аккаунта
- После успешного входа пользователь будет аутентифицирован через Supabase

## Текущие настройки Supabase

- **URL**: https://algfbtuheljugjyxuyge.supabase.co
- **Key**: sb_publishable_NglaLvWPGuQIxEq1F8O0gg_uELz__H1

Эти настройки уже добавлены в `SupabaseClient.kt`

