package ru.weather.bot.weatherbot.models;

public interface Messages
{
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
        "\uD83D\uDD38 Можно узнать более подробные сведения о погоде, они будут предложены после вывода общей информации о погоде. ☔";
    String EN_HELP = "I am designed to keep you conveniently informed about weather conditions around the world! \uD83D\uDCE3\n" +
            "My functionality includes performing various tasks such as \uD83D\uDD00\n\n" +
            "\uD83D\uDD38 To start communicating with me, you can send the command: /start. ▶\n\n" +
            "\uD83D\uDD38 If something is unclear, you can send the command /help. A summary of usage information will be displayed. ℹ\n\n" +
            "\uD83D\uDD38 If you want to change the language of communication with me, you can choose 1 of 4 languages here: /lang. English will be selected by default. \uD83C\uDDEC\uD83C\uDDE7\n\n" +
            "\uD83D\uDD38 In order to find out the weather in a certain city, just send the name of the city in any format, the main thing is without spelling mistakes! \uD83D\uDD8C\n\n" +
            "\uD83D\uDD38 You can find out more details about the weather, they will be offered after the general weather information is displayed. ☔";
    String CN_HELP = "我旨在让您方便地了解世界各地的天气情况！\uD83D\uDCE3\n我的功能包括执行各种任务，例如 \uD83D\uDD00\n\n" +
            "\uD83D\uDD38 要开始与我通信，您可以发送命令： /start。▶\n\n" +
            "\uD83D\uDD38 如果有不清楚的地方，可以发送 /help 命令。系统将显示使用信息摘要。ℹ\n\n" +
            "\uD83D\uDD38 如果您想更改与我交流的语言，可以从 4 种语言中选择一种：/lang。默认将选择英语。\uD83C\uDDEC\uD83C\uDDE7\n\n" +
            "\uD83D\uDD38 要想知道某个城市的天气情况，只需以任何格式发送城市名称，不要有拼写错误！\uD83D\uDD8C\n\n" +
            "\uD83D\uDD38 您可以查找有关天气的更多详细信息，它们将在显示一般天气信息后提供。☔";
    String DE_HELP = "Ich wurde entwickelt, um Sie bequem über die Wetterbedingungen auf der ganzen Welt zu informieren! \uD83D\uDCE3\n" +
            "Zu meinen Funktionen gehört die Ausführung verschiedener Aufgaben, wie z. B. \uD83D\uDD00\n\n" +
            "\uD83D\uDD38 Um die Kommunikation mit mir zu starten, kannst du den Befehl: /start senden. ▶\n\n" +
            "\uD83D\uDD38 Wenn etwas unklar ist, können Sie den Befehl /help senden. Es wird eine Zusammenfassung der Nutzungsinformationen angezeigt. ℹ\n\n" +
            "\uD83D\uDD38 Wenn Sie die Sprache der Kommunikation mit mir ändern möchten, können Sie hier 1 von 4 Sprachen auswählen: /lang. Standardmäßig ist Englisch ausgewählt. \uD83C\uDDEC\uD83C\uDDE7\n\n" +
            "\uD83D\uDD38 Um das Wetter in einer bestimmten Stadt herauszufinden, senden Sie einfach den Namen der Stadt in einem beliebigen Format, ohne Rechtschreibfehler! \uD83D\uDD8C\n\n" +
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
}