package com.company;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/*
1) Собрать номера городов (не менее 10)
2) Зарегистрироваться на сайте и получить APPID
3) Используя заготовку написать программу, собирающую
сведения о температуре в перечисленных городах
4) Вывести названия городов и температуры в них
от более тёплых более холодным
5) Получить названия городов можно из JSON-данных,
для этого придётся дополнить класс Weather
 */
class M {
    double temp;
    String name;

    @Override
    public String toString() {
        return " " + temp;
    }
}
class Weather {
    M main;
    String name;

    @Override
    public String toString() {
        return name + " - " + new DecimalFormat("#0.00").format(main.temp - 273) + "°C";
    }
}

public class Main {
    final static String API_URL = "https://api.openweathermap.org/data/2.5/weather?id=%d&appid=6699ff91adc36792838a55cabb3569ab";

    public static void main(String[] args) throws IOException, InterruptedException {
        class MyThread extends Thread implements Comparable<MyThread>{

            public MyThread(String url) {
                this.url = url;
            }

            String url; Weather weather;

            @Override
            public void run() {
                try {
                    URL weather_url = new URL(url);
                    InputStream stream = (InputStream) weather_url.getContent();
                    Gson gson = new Gson();
                    weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                } catch (IOException e) {}
            }

            @Override
            public int compareTo(MyThread o) {
                return -Double.compare(this.weather.main.temp, o.weather.main.temp);
            }

        }

        int[] cities = {2759794, 2950159, 2800866, 2618425, 2964574, 2643743, 3117735, 524901, 2988507, 3067696};

        ArrayList<MyThread> cityThread = new ArrayList<>();

        for(int i = 0; i < cities.length; i++){
            MyThread weather_thread = new MyThread(String.format(API_URL, cities[i]));
            cityThread.add(weather_thread);
        }

        for (MyThread  c: cityThread) {
            c.start();
            c.join();
        }

        Collections.sort(cityThread);

        for(MyThread c : cityThread) {
            System.out.println(c.weather);
        }

    }
}