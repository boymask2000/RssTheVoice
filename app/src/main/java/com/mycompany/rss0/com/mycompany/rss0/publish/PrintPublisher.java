package com.mycompany.rss0.com.mycompany.rss0.publish;

/**
 * Created by giovanni on 4/18/15.
 */
public class PrintPublisher implements Publisher{
    @Override
    public void publish(String s) {
        System.out.println(s);
    }

    @Override
    public void clean() {

    }
}
