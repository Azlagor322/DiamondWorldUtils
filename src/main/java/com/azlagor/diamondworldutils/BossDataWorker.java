package com.azlagor.diamondworldutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BossDataWorker {

    public static int x = 5;
    public static int y = 5;
    public static int scale = 1;
    public static int step = 10;
    public static String nowBossTarget = "";
    public static Map<String, Long> bossTimes = new HashMap<String, Long>();
    public static Map<String, Long> bossTimesDiscord = new HashMap<String, Long>();
    public static ArrayList<String> unkBoss = new ArrayList<>();
    public static Map<String, String> lvlMap = new HashMap<String, String>();
    public static Map<String, String> clanFightMap = new HashMap<String, String>();
    public static String upBoss = "";
    public static String downBoss = "";
    public static void init()
    {
        lvlMap.put("Кригер","§a15"); unkBoss.add("Кригер");
        lvlMap.put("Слизень","§a20"); unkBoss.add("Слизень");
        lvlMap.put("Стальной Страж", "§a25"); unkBoss.add("Стальной Страж");
        lvlMap.put("Кошмар", "§a30"); //unkBoss.add("Кошмар");
        lvlMap.put("Близнецы","§a35"); unkBoss.add("Близнецы");
        lvlMap.put("Повелитель Огня","§a40"); unkBoss.add("Повелитель Огня");
        lvlMap.put("Паучиха","§a45"); unkBoss.add("Паучиха");
        lvlMap.put("Утопленник","§a50"); unkBoss.add("Утопленник");
        lvlMap.put("Колдун","§a55"); unkBoss.add("Колдун");
        lvlMap.put("Смерть","§a60"); unkBoss.add("Смерть");
        lvlMap.put("Наездник","§a65"); unkBoss.add("Наездник");
        lvlMap.put("Разбойник","70"); unkBoss.add("Разбойник");
        lvlMap.put("Лавовый куб","§a75"); unkBoss.add("Лавовый куб");
        lvlMap.put("Варден","80"); unkBoss.add("Варден");
        lvlMap.put("Призрачный охотник","90"); unkBoss.add("Призрачный охотник");
        lvlMap.put("Чёрный дракон","§a95"); unkBoss.add("Чёрный дракон");
        lvlMap.put("Гигант","100"); unkBoss.add("Гигант");
        lvlMap.put("Проклятый легион","§a105"); unkBoss.add("Проклятый легион");
        lvlMap.put("Монстр","110"); unkBoss.add("Монстр");
        lvlMap.put("Некромант","§a115"); unkBoss.add("Некромант");
        lvlMap.put("Пожиратель тьмы","120"); unkBoss.add("Пожиратель тьмы");
        lvlMap.put("Чудовище","§a125"); unkBoss.add("Чудовище");
        lvlMap.put("Октопус","130"); unkBoss.add("Октопус");
        lvlMap.put("Кузнец","140"); unkBoss.add("Кузнец");
        lvlMap.put("Могущественный шалкер","§a150"); unkBoss.add("Могущественный шалкер");
        lvlMap.put("Заклинатель","160"); unkBoss.add("Заклинатель");
        lvlMap.put("Всадник","170"); unkBoss.add("Всадник");
        lvlMap.put("Кобольд","180"); unkBoss.add("Кобольд");
        lvlMap.put("Самурай","190"); unkBoss.add("Самурай");
        lvlMap.put("Повелитель мёртвых","§a200"); unkBoss.add("Повелитель мёртвых");
        lvlMap.put("Теневой лорд","210"); unkBoss.add("Теневой лорд");
        lvlMap.put("Гигантская черепаха","220"); unkBoss.add("Гигантская черепаха");
        lvlMap.put("Голиаф","230"); unkBoss.add("Голиаф");
        lvlMap.put("Разрушитель","§a240"); unkBoss.add("Разрушитель");
        lvlMap.put("Снежный Монстр","250"); unkBoss.add("Снежный Монстр");
        lvlMap.put("Крик","260"); unkBoss.add("Крик");
        lvlMap.put("Спектральный куб","270"); unkBoss.add("Спектральный куб");
        lvlMap.put("Тень","280"); unkBoss.add("Тень");
        lvlMap.put("Синтия","§a290"); unkBoss.add("Синтия");
        lvlMap.put("Магнус","300"); unkBoss.add("Магнус");
        lvlMap.put("Вестник Ада","310"); unkBoss.add("Вестник Ада");
        lvlMap.put("Цербер","§a320"); unkBoss.add("Цербер");
        lvlMap.put("Ифрит","330"); unkBoss.add("Ифрит");
        lvlMap.put("Бафомет","340"); unkBoss.add("Бафомет");
        lvlMap.put("Пиглин","350"); unkBoss.add("Пиглин");
        lvlMap.put("Королева Пиглинов","§a360"); unkBoss.add("Королева Пиглинов");
        lvlMap.put("Хоглин","370"); unkBoss.add("Хоглин");
        lvlMap.put("Зомби Пиглин","380"); unkBoss.add("Зомби Пиглин");
        lvlMap.put("Брутальный Пиглин","390"); unkBoss.add("Брутальный Пиглин");
        lvlMap.put("Магма","400"); unkBoss.add("Магма");
        lvlMap.put("Зоглин","410"); unkBoss.add("Зоглин");
        lvlMap.put("Адский Рыцарь","410"); unkBoss.add("Адский Рыцарь");

    }


}
