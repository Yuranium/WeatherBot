package ru.weather.bot.weatherbot.models;

import ru.weather.bot.weatherbot.enums.BotLanguage;

public interface Messages
{
    static String weatherForecast(BotLanguage language, String cityName, String description, double temp, double feels_like, int clouds, double speed)
    {
        return switch (language)
        {
            case RUSSIAN -> "\uD83C\uDFD9\uFE0F В регионе " + cityName + " " + description + ", \uD83C\uDF21\uFE0F средняя температура на улице " + temp +
                    "°C, ощущается как " + feels_like + "°C, ☁ облачность составляет " + clouds + "%, \uD83D\uDCA8 скорость ветра достигает " + speed + " м/c.";
            case ENGLISH -> "\uD83C\uDFD9\uFE0F In the city of " + cityName + " " + description + ", \uD83C\uDF21\uFE0F the average temperature outside is " +
                    temp + "°C, feels like " +  feels_like + "°C, ☁ cloudiness is " + clouds + "%, \uD83D\uDCA8 wind speeds up to " + speed + " m/s.";
            case CHINESE -> "\uD83C\uDFD9\uFE0F 在" + cityName + description + "城市，\uD83C\uDF21\uFE0F，室外平均气温为 " + temp + "℃，感觉温度为" + feels_like +
                    "℃，☁ ，云量为 " + clouds + "，\uD83D\uDCA8 风速达到 " + speed + " 米/秒。";
            case GERMAN -> "\uD83C\uDFD9\uFE0F In der Region " + cityName + " " +  description + ", \uD83C\uDF21\uFE0F beträgt die durchschnittliche Außentemperatur " +
                    temp + "°C, gefühlt " + feels_like + "°C, ☁ beträgt die Bewölkung " + clouds + "%, \uD83D\uDCA8 windgeschwindigkeiten bis zu " + speed + " m/s.";
        };
    }

    static String detailedWeatherForecast(BotLanguage language, String cityName, String description, double temp, double feels_like,
                                          double temp_min, double temp_max, double pressure, double humidity, double visibility, double speed,
                                          double gust, String windDirection, int clouds)
    {
        return switch (language)
        {
            case RUSSIAN -> "\uD83C\uDFD9 Хорошо! Более <b>подробный прогноз погоды</b> в регионе <b>" + cityName +
                    "</b> сейчас:\n\n\uD83C\uDF26 Описание: <b>" + description + "</b>" +
                    "\n\n\uD83C\uDF21 Средняя температура: <b>" + temp + "°C</b>" +
                    "\n\n\uD83D\uDC64 Ощущается как: <b>" + feels_like + "°C</b>" +
                    "\n\n\uD83E\uDDCA Минимальная температура: <b>" + temp_min + "°C</b>" +
                    "\n\n\uD83D\uDD25 Максимальная температура: <b>" + temp_max + "°C</b>" +
                    "\n\n\uD83D\uDDDC Давление: <b>" + (pressure * 0.75) + " мм рт. ст.</b>" +
                    "\n\n\uD83D\uDCA6 Влажность: <b>" + humidity + "%</b>" +
                    "\n\n\uD83D\uDD2D Видимость: <b>" + visibility + "км</b>" +
                    "\n\n\uD83D\uDCA8 Скорость ветра: <b>" + speed + "м/c</b>" +
                    "\n\n\uD83C\uDF2C Порывы ветра (кратковременное усиление ветра): <b>" + gust + "м/с</b>" +
                    "\n\n\uD83E\uDDED Направление ветра: <b>" + windDirection + "</b>" +
                    "\n\n☁ Облачность неба: <b>" + clouds + "%</b>";
            case ENGLISH -> "\uD83C\uDFD9 Good! More <b>detailed weather forecast</b> for the region <b>" + cityName + "</b> now:" +
                    "\n\n\uD83C\uDF26 Description: <b>" + description + "</b>" +
                    "\n\n\uD83C\uDF21 Average temperature: <b>" + temp + "°C</b>" +
                    "\n\n\uD83D\uDC64 Feels like: <b>" + feels_like + "°C</b>" +
                    "\n\n\uD83E\uDDCA Minimum temperature: <b>" + temp_min + "°C</b>" +
                    "\n\n\uD83D\uDD25 Maximum temperature: <b>" + temp_max + "°C</b>" +
                    "\n\n\uD83D\uDDDC Pressure: <b>" + (pressure * 0.75) + " mmHg</b>" +
                    "\n\n\uD83D\uDCA6 Humidity: <b>" + humidity + "%</b>" +
                    "\n\n\uD83D\uDD2D Visibility: <b>" + visibility + "km</b>" +
                    "\n\n\uD83D\uDCA8 Wind speed: <b>" + speed + "m/sec</b>" +
                    "\n\n\uD83C\uDF2C Wind gusts (short-term wind strengthening): <b>" + gust + "m/sec</b>" +
                    "\n\n\uD83E\uDDED Wind direction: <b>" + windDirection + "</b>" +
                    "\n\n☁ Sky cloudiness: <b>" + clouds + "%</b>";
            case CHINESE -> "\uD83C\uDFD9 好! 该地区更详细的天气预报 <b>" + cityName + "</b> 现在：" +
                    "\n\n\uD83C\uDF26 描述： <b>" + description + "</b>" +
                    "\n\n\uD83C\uDF21 平均气温： <b>" + temp + "°C</b>" +
                    "\n\n\uD83D\uDC64 感觉： <b>" + feels_like + "°C</b>" +
                    "\n\n\uD83E\uDDCA 最低气温： <b>" + temp_min + "°C</b>" +
                    "\n\n\uD83D\uDD25 最高气温： <b>" + temp_max + "°C</b>" +
                    "\n\n\uD83D\uDDDC 气压： <b>" + (pressure * 0.75) + " mmHg。</b>" +
                    "\n\n\uD83D\uDCA6 湿度： <b>" + humidity + "%</b>" +
                    "\n\n\uD83D\uDD2D 能见度： <b>" + visibility + "公里</b>" +
                    "\n\n\uD83D\uDCA8 风速： <b>" + speed + "米/秒。</b>" +
                    "\n\n\uD83C\uDF2C 阵风（短期风力增强）： <b>" + gust + "米/秒。</b>" +
                    "\n\n\uD83E\uDDED 风向： <b>" + windDirection + "</b>" +
                    "\n\n☁ 天空云量： <b>" + clouds + "%</b>";
            case GERMAN -> "\uD83C\uDFD9 Gut! <b>Ausführlichere Wettervorhersage</b> für die Region <b>" + cityName + "</b> jetzt:" +
                    "\n\n\uD83C\uDF26 Beschreibung: <b>" + description + "</b>" +
                    "\n\n\uD83C\uDF21 Durchschnittliche Temperatur: <b>" + temp + "°C</b>" +
                    "\n\n\uD83D\uDC64 Fühlt sich an wie: <b>" + feels_like + "°C</b>" +
                    "\n\n\uD83E\uDDCA Tiefsttemperatur: <b>" + temp_min + "°C</b>" +
                    "\n\n\uD83D\uDD25 Höchsttemperatur: <b>" + temp_max + "°C</b>" +
                    "\n\n\uD83D\uDDDC Luftdruck: <b>" + (pressure * 0.75) + " mmHg</b>" +
                    "\n\n\uD83D\uDCA6 Luftfeuchtigkeit: <b>" + humidity + "%</b>" +
                    "\n\n\uD83D\uDD2D Sichtweite: <b>" + visibility + "km</b>" +
                    "\n\n\uD83D\uDCA8 Windgeschwindigkeit: <b>" + speed + "m/sec</b>" +
                    "\n\n\uD83C\uDF2C Windböen (kurzfristige Windverstärkung): <b>" + gust + "m/sec</b>" +
                    "\n\n\uD83E\uDDED Windrichtung: <b>" + windDirection + "</b>" +
                    "\n\n☁ Bewölkungsgrad: <b>" + clouds + "%</b>";
        };
    }

    static String weatherForecastGeneralInfo(BotLanguage language, String cityName, int quantityDays) {
        String daysFormat;
        if (quantityDays == 1)
            daysFormat = switch (language)
            {
                case RUSSIAN -> "день";
                case ENGLISH -> "day";
                case CHINESE -> "天";
                case GERMAN -> "Tag";
            };
        else if (quantityDays >= 2 && quantityDays < 5)
            daysFormat = switch (language)
            {
                case RUSSIAN -> "дня";
                case ENGLISH -> "days";
                case CHINESE -> "天";
                case GERMAN -> "Tage";
            };
        else daysFormat = switch (language)
            {
                case RUSSIAN -> "дней";
                case ENGLISH -> "days";
                case CHINESE -> "天";
                case GERMAN -> "Tage";
            };
        return switch (language)
        {
            case RUSSIAN -> "❄ \uD83D\uDCC5\n\n\uD83D\uDC4D Хорошо! Прогноз погоды на " + quantityDays +
                    " " + daysFormat + " вперёд для региона " + cityName + ", начиная с сегодня:";
            case ENGLISH -> "❄ \uD83D\uDCC5\n\n\uD83D\uDC4D Good! Weather forecast for " + quantityDays +
                    " " + daysFormat + " ahead for the region of " + cityName + " starting today:";
            case CHINESE -> "❄ \uD83D\uDCC5\n\n\uD83D\uDC4D 好！从今天起，" + quantityDays + "市未来" + cityName + "天的天气预报：";
            case GERMAN -> "❄ \uD83D\uDCC5\n\n\uD83D\uDC4D Gut! Wettervorhersage für " + quantityDays + " " + daysFormat +
                    " voraus in der Region " + cityName + " ab heute:";
        };
    }
    String RU_THE_LANGUAGE_IS_ALREADY_THERE = "⚠ Язык уже выбран как русский.";
    String EN_THE_LANGUAGE_IS_ALREADY_THERE = "⚠ The language is already selected as English.";
    String CN_THE_LANGUAGE_IS_ALREADY_THERE = "⚠ 语言已选择为中文。";
    String DE_THE_LANGUAGE_IS_ALREADY_THERE = "⚠ Die Sprache ist bereits als Deutsch ausgewählt.";
    String RU_FURTHER_COMMUNICATION = "\uD83D\uDC4C Хорошо! Теперь дальнейшее общение будет на русском языке.\n\n" +
        "⚙ Чтобы изменить язык общения, выберите нужный с помощью команды: /lang\n\n" +
            "☀ Теперь Вы можете узнать погоду в любом городе, который существует на карте. " +
            "Просто введите название города без ошибок (регистр букв не важен) \uD83D\uDE03";
    String EN_FURTHER_COMMUNICATION = "\uD83D\uDC4C Good! Now further communication will be in English.\n\n" +
        "⚙ To change the language of communication, select the desired language with the command: /lang\n\n" +
            "☀ Now you can find out the weather in any city that exists on the map. " +
            "Just enter the name of the city without mistakes (case is not important) \uD83D\uDE03";
    String CN_FURTHER_COMMUNICATION = "\uD83D\uDC4C 很好！现在，我们将用德语进行进一步交流。\n\n⚙ 要更改通信语言，请使用 /lang\n\n 命令选择所需的语言。" +
            "☀ 现在，您可以查询地图上任何城市的天气情况。只需准确无误地输入城市名称（大小写并不重要）\uD83D\uDE03。";
    String DE_FURTHER_COMMUNICATION = "\uD83D\uDC4C Sehr gut! Nun wird die weitere Kommunikation auf Deutsch erfolgen.\n\n" +
        "⚙ Um die Sprache der Kommunikation zu ändern, wählen Sie die gewünschte Sprache mit dem Befehl: /lang\n\n" +
            "☀ Jetzt können Sie das Wetter in jeder beliebigen Stadt, die auf der Karte existiert, herausfinden. " +
            "Geben Sie einfach den Namen der Stadt ohne Fehler ein (Groß- und Kleinschreibung ist nicht wichtig) \uD83D\uDE03.";
    String RU_HELP = "Я предназначен для удобного информирования Вас о погодных условиях во всём мире! \uD83D\uDCE3\n" +
        "В мой функционал входит выполнение различных задач, например \uD83D\uDD00\n\n" +
        "\uD83D\uDD38 Для начала общения со мной, можно отправить команду: /start. ▶\n\n" +
        "\uD83D\uDD38 Если что-то непонятно, можно отправить команду /help. Выведется краткая информация по использованию. ℹ\n\n" +
        "\uD83D\uDD38 Если хотите изменить язык общения со мной, можно выбрать 1 из 4 предложенных языков здесь: /lang. По умолчанию будет выбран английский язык. \uD83C\uDDEC\uD83C\uDDE7\n\n" +
        "\uD83D\uDD38 Для того, чтобы узнать погоду в определённом городе, просто отправьте название города в любом формате, главное без орфографических ошибок! \uD83D\uDD8C\n\n" +
        "\uD83D\uDD38 Чтобы узнать прогноз погоды на несколько дней, просто отправьте сообщение в таком формате: <b><i>'Название региона' 'количество дней'</i></b>\n" +
        "\uD83C\uDF06 Например, чтобы узнать прогноз погоды в Москве на 4 дня, можно отправить: Москва 4\n<b><i>Количество дней ограничено пятью ❕</i></b>\n\n" +
        "\uD83D\uDD38 Можно узнать более подробные сведения о погоде, они будут предложены после вывода общей информации о погоде. ☔";
    String EN_HELP = "I am designed to keep you conveniently informed about weather conditions around the world! \uD83D\uDCE3\n" +
            "My functionality includes performing various tasks such as \uD83D\uDD00\n\n" +
            "\uD83D\uDD38 To start communicating with me, you can send the command: /start. ▶\n\n" +
            "\uD83D\uDD38 If something is unclear, you can send the command /help. A summary of usage information will be displayed. ℹ\n\n" +
            "\uD83D\uDD38 If you want to change the language of communication with me, you can choose 1 of 4 languages here: /lang. English will be selected by default. \uD83C\uDDEC\uD83C\uDDE7\n\n" +
            "\uD83D\uDD38 In order to find out the weather in a certain city, just send the name of the city in any format, the main thing is without spelling mistakes! \uD83D\uDD8C\n\n" +
            "\uD83D\uDD38 To find out the weather forecast for several days, just send a message in this format: <b><i>'Name of region' 'number of days'</i></b>\n" +
            "\uD83C\uDF06 For example, to get the weather forecast for Moscow for 4 days, you can send: Moscow 4\n<b><i>The number of days is limited to five ❕</i></b>\n\n" +
            "\uD83D\uDD38 You can find out more details about the weather, they will be offered after the general weather information is displayed. ☔";
    String CN_HELP = "我旨在让您方便地了解世界各地的天气情况！\uD83D\uDCE3\n我的功能包括执行各种任务，例如 \uD83D\uDD00\n\n" +
            "\uD83D\uDD38 要开始与我通信，您可以发送命令： /start。▶\n\n" +
            "\uD83D\uDD38 如果有不清楚的地方，可以发送 /help 命令。系统将显示使用信息摘要。ℹ\n\n" +
            "\uD83D\uDD38 如果您想更改与我交流的语言，可以从 4 种语言中选择一种：/lang。默认将选择英语。\uD83C\uDDEC\uD83C\uDDE7\n\n" +
            "\uD83D\uDD38 要想知道某个城市的天气情况，只需以任何格式发送城市名称，不要有拼写错误！\uD83D\uDD8C\n\n" +
            "\uD83D\uDD38 要查询几天的天气预报，只需发送以下格式的信息： <b><i>'城市名称' '天数'</i></b>\n" +
            "\uD83C\uDF06 例如，要查询莫斯科 4 天的天气预报，您可以发送： Moscow 4\n<b><i>天数以 5 天为限 ❕</i></b>\n\n" +
            "\uD83D\uDD38 您可以查找有关天气的更多详细信息，它们将在显示一般天气信息后提供。☔";
    String DE_HELP = "Ich wurde entwickelt, um Sie bequem über die Wetterbedingungen auf der ganzen Welt zu informieren! \uD83D\uDCE3\n" +
            "Zu meinen Funktionen gehört die Ausführung verschiedener Aufgaben, wie z. B. \uD83D\uDD00\n\n" +
            "\uD83D\uDD38 Um die Kommunikation mit mir zu starten, kannst du den Befehl: /start senden. ▶\n\n" +
            "\uD83D\uDD38 Wenn etwas unklar ist, können Sie den Befehl /help senden. Es wird eine Zusammenfassung der Nutzungsinformationen angezeigt. ℹ\n\n" +
            "\uD83D\uDD38 Wenn Sie die Sprache der Kommunikation mit mir ändern möchten, können Sie hier 1 von 4 Sprachen auswählen: /lang. Standardmäßig ist Englisch ausgewählt. \uD83C\uDDEC\uD83C\uDDE7\n\n" +
            "\uD83D\uDD38 Um das Wetter in einer bestimmten Stadt herauszufinden, senden Sie einfach den Namen der Stadt in einem beliebigen Format, ohne Rechtschreibfehler! \uD83D\uDD8C\n\n" +
            "\uD83D\uDD38 Um die Wettervorhersage für mehrere Tage zu erfahren, senden Sie einfach eine Nachricht in diesem Format: <b><i>'Name der Region' 'Anzahl der Tage'</i></b>\n" +
            "\uD83C\uDF06 Um zum Beispiel die Wettervorhersage für Moskau für 4 Tage herauszufinden, können Sie senden: Moskau 4\n<b><i>Die Anzahl der Tage ist auf fünf begrenzt ❕</i></b>\n\n" +
            "\uD83D\uDD38 Sie können mehr Details über das Wetter herausfinden, diese werden nach der Anzeige der allgemeinen Wetterinformationen angeboten. ☔";
    String SET_LANG_RU = "\uD83C\uDF0D Чтобы сменить язык, выберите необходимый из списка предложенных." +
        " После все сообщения будут на выбранном языке \uD83D\uDE09";
    String SET_LANG_EN = "\uD83C\uDF0D To change the language, select the desired language from the list of available languages." +
        " All messages will then be in the selected language \uD83D\uDE09";
    String SET_LANG_CN = "\uD83C\uDF0D 要更改语言，请从可用语言列表中选择所需的语言。所有信息都将使用所选语言 \uD83D\uDE09";
    String SET_LANG_DE = "\uD83C\uDF0D Um die Sprache zu ändern, wählen Sie die gewünschte Sprache aus der" +
        "Liste der verfügbaren Sprachen aus. Alle Meldungen werden dann in der ausgewählten Sprache angezeigt \uD83D\uDE09";
    String RU_UNSUPPORTED_COMMAND = "\uD83D\uDE1E Извините, эта команда в данный момент не поддерживается или неправильный ввод.";
    String EN_UNSUPPORTED_COMMAND = "\uD83D\uDE1E Sorry, this command is not currently supported or incorrect input.";
    String CN_UNSUPPORTED_COMMAND = "\uD83D\uDE1E 对不起，当前不支持此命令或输入不正确。";
    String DE_UNSUPPORTED_COMMAND = "\uD83D\uDE1E Sorry, dieser Befehl wird derzeit nicht unterstützt oder ist falsch eingegeben.";
    String RU_CITY_INPUT_ERROR = "❌ В ходе выполнения геокодирования введённого города, не было получено ни одного положительного ответа. " +
            "Убедитесь, что введённый Вами город написан без ошибок и существует.\n\n" +
            "Если это не помогло, возможно наблюдаются технические неполадки на стороне сервера, стоит повторить попытку позже.";
    String EN_CITY_INPUT_ERROR = "❌ While performing the geocoding of the entered city, no positive responses were received. " +
            "Make sure that the city you entered is not misspelled and exists.\n\n" +
            "If this does not help, there may be technical problems on the server side, you should try again later.";
    String CN_CITY_INPUT_ERROR = "❌ 对输入的城市进行地理编码时未收到肯定的回复。请确保您输入的城市没有拼写错误并且存在。\n\n" +
            "如果没有帮助，可能是服务器方面的技术问题，请稍后再试。";
    String DE_CITY_INPUT_ERROR = "❌ Bei der Geokodierung der eingegebenen Stadt wurden keine positiven Antworten erhalten. " +
            "Stellen Sie sicher, dass die eingegebene Stadt nicht falsch geschrieben ist und existiert.\n\n" +
            "Wenn dies nicht hilft, liegt möglicherweise ein technisches Problem auf der Serverseite vor, Sie sollten es später erneut versuchen.";
    String RU_DETAILED_FORECAST = "Подробный прогноз";
    String EN_DETAILED_FORECAST = "Detailed Forecast";
    String CN_DETAILED_FORECAST = "详细预测";
    String DE_DETAILED_FORECAST = "Detaillierte Vorhersage";
    String RU_UNSUCCESSFUL_EVENT_HANDLING = "⚠ Время ожидания истекло, повторите запрос.";
    String EN_UNSUCCESSFUL_EVENT_HANDLING = "⚠ Waiting time has expired, repeat the request.";
    String CN_UNSUCCESSFUL_EVENT_HANDLING = "⚠ 等待时间已过，请重复请求。";
    String DE_UNSUCCESSFUL_EVENT_HANDLING = "⚠ Die Wartezeit ist abgelaufen, wiederholen Sie die Anfrage.";
    String START_COMMAND_DESCRIPTION = "Initial command to start the bot";
    String HELP_COMMAND_DESCRIPTION = "Allows you to get a brief overview of the possibilities";
    String LANG_COMMAND_DESCRIPTION = "Allows you to change the language of communication with the bot";
}