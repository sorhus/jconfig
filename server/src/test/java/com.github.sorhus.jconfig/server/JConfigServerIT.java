package com.github.sorhus.jconfig.server;

import com.github.sorhus.jconfig.client.JConfigClient;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class JConfigServerIT {

    @Test
    public void testMemory() throws Exception {

        new Thread() {
            @Override
            public void run() {
                try {
                    JconfigServer jconfigServer = new JconfigServer();
                    jconfigServer.init();
                } catch (Exception e) {}
            }
        }.start();

        Thread.sleep(2000); // Wait for server to initialise

        JConfigClient client = new JConfigClient();

        client.put("key1", "value1");
        client.put("key2", "value2");

        assertThat(client.get("key1"), equalTo("value1"));
        assertThat(client.get("key2"), equalTo("value2"));

    }
}
