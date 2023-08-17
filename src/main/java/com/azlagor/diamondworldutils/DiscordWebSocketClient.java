package com.azlagor.diamondworldutils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordWebSocketClient extends WebSocketClient {
    private boolean heartbeatIntervalStart = false;
    public static String token = "BOT TOKEN";
    private int heartbeatInterval;
    private Timer heartbeatTimer;

    private static String channelId = "1090743247137165402";
    private static String messageId = "1091333039050731552";
    public DiscordWebSocketClient(URI serverUri) {
        super(serverUri);
    }
    public static void init() throws Exception
    {
        URI uri = new URI("wss://gateway.discord.gg/?v=9&encoding=json");
        DiscordWebSocketClient client = new DiscordWebSocketClient(uri);
        client.connect();
        getMessage();
    }

    private static void c2sData() throws IOException {
        new Thread(() -> {
            try {
                System.out.println("c2sData 1");
                String url = "https://discord.com/api/v9/channels/"+ channelId +"/messages/" + messageId;
                String content = "```asciidoc\n";
                for(String name : BossDataWorker.bossTimesDiscord.keySet())
                {
                    content += name + " [" + BossDataWorker.lvlMap.get(name) + "]" + " :: " + BossDataWorker.bossTimesDiscord.get(name) + "\n";
                }
                content += "```";
                System.out.println("c2sData 2");

                HttpPatch httpPatch = new HttpPatch(url);
                httpPatch.addHeader("Authorization", "Bot " + token);
                httpPatch.addHeader("Content-Type", "application/json");

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("content", content);

                StringEntity entity = new StringEntity(jsonInput.toString(), ContentType.APPLICATION_JSON);
                httpPatch.setEntity(entity);
                System.out.println("Request body: " + httpPatch.toString());

                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(httpPatch)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Response code: " + statusCode);
                    System.out.println("Response body: " + responseBody);

                }
                catch (Exception e)
                {
                    System.out.println(e);
                }


            } catch (Exception e) {
                System.out.println(e);
            }
        }).start();

    }


    public static void checkDataClient(String name) throws IOException {
        System.out.println("checkDataClient start: " + name);
        long ut = System.currentTimeMillis() / 1000;
        long clientDataTime = BossDataWorker.bossTimes.get(name);
        if(ut > clientDataTime) return;
        System.out.println("checkDataClient start 2: " + name);
        if(BossDataWorker.bossTimesDiscord.containsKey(name))
        {
            System.out.println("checkDataClient start 3: " + name);
            long serverDataTime = BossDataWorker.bossTimesDiscord.get(name);
            if(clientDataTime - serverDataTime > 5 || clientDataTime - serverDataTime < -5)
            {
                System.out.println("checkDataClient start 4: " + name);
                BossDataWorker.bossTimesDiscord.put(name, clientDataTime);
                System.out.println(BossDataWorker.bossTimesDiscord.get(name));
                c2sData();
            }
        }
        else
        {

        }
    }

    private static void checkDataServer()
    {
        long ut = System.currentTimeMillis() / 1000;
        for(String name : BossDataWorker.bossTimesDiscord.keySet())
        {
            long serverDataTime = BossDataWorker.bossTimesDiscord.get(name);
            if(ut > serverDataTime) continue;
            if(BossDataWorker.bossTimes.containsKey(name))
            {
                long clientDataTime = BossDataWorker.bossTimes.get(name);
                if(clientDataTime - serverDataTime > 5 || clientDataTime - serverDataTime < -5)
                {
                    BossDataWorker.bossTimes.put(name, serverDataTime);
                }
            }
            else
            {
                BossDataWorker.bossTimes.put(name, serverDataTime);
            }

        }
    }

    public static void parseContent(String content)
    {

        String[] lines = content.split("\n");
        for(String contentLine : lines)
        {
            try {
                if(contentLine.startsWith("`")) continue;
                int lastSpaceIndex = contentLine.indexOf(":: ");
                int SpaceIndex = contentLine.indexOf(" [");
                Long time = Long.parseLong(contentLine.substring(lastSpaceIndex + 3).replaceAll(" ",""));
                String word = contentLine.substring(0, SpaceIndex);
                BossDataWorker.bossTimesDiscord.put(word, time);
                checkDataServer();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

        }
    }

    public static void getMessage() throws IOException {

        new Thread(() -> {
            try {
                String BASE_URL = "https://discord.com/api/";

                URL url = new URL(BASE_URL + "channels/" + channelId + "/messages/" + messageId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bot " + token);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String content = jsonObject.getString("content");
                parseContent(content);



                return;
            } catch (Exception e) {
                // Handle the exception here
            }
        }).start();
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // Соединение успешно установлено
        System.out.println("Соединение успешно установлено");
        JSONObject authMessage = new JSONObject();
        JSONObject properties = new JSONObject().put("$os", "linux");
        authMessage.put("op", 2);

        authMessage.put("d", new JSONObject().put("token", token).put("intents", 512).put("properties",properties));
        send(authMessage.toString());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Сообщение от сервера: " + message);
        JSONObject jsonObject = new JSONObject(message);
        int op = jsonObject.getInt("op");
        if(op == 10)
        {
            if(!heartbeatIntervalStart)
            {
                heartbeatIntervalStart = true;
                int heartbeatInterval = jsonObject.getJSONObject("d").getInt("heartbeat_interval");
                startHeartbeat(heartbeatInterval);
                return;
            }
        }
        if(op == 7)
        {
            System.out.println("Производится рекконект");
            new Thread(() -> {
                try {
                    this.reconnect();
                } catch (Exception e) {
                    // Handle the exception here
                }
            }).start();
            return;
        }
        if(op == 0)
        {
            String t = jsonObject.getString("t");
            if(t.equals("MESSAGE_UPDATE"))
            {
                String content = jsonObject.getJSONObject("d").getString("content");
                String messageID = jsonObject.getJSONObject("d").getString("id");
                if(messageID.equals(messageID)) parseContent(content);
            }
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Соединение закрыто
        System.out.println("Соединение закрыто\n" + reason + "\n" + code);
    }

    @Override
    public void onError(Exception ex) {
        // Ошибка соединения
        System.err.println("Ошибка соединения: " + ex.getMessage());
    }

    public void startHeartbeat(int interval) {
        this.heartbeatInterval = interval;
        // Отправляем первое сообщение heartbeat
        sendHeartbeat();
        System.out.println("Heartbeat запущен с интервалом " + String.valueOf(interval));
        // Запускаем таймер для последующих heartbeat
        this.heartbeatTimer = new Timer();
        this.heartbeatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendHeartbeat();
            }
        }, interval, interval);
    }

    public void sendHeartbeat() {
        JSONObject payload = new JSONObject();
        payload.put("op", 1);
        payload.put("d", "");
        send(payload.toString());
    }
}
