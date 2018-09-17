/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.ipc.sink.offheap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.core.ipc.sink.api.WriteFailedException;
import org.osgi.service.cm.ConfigurationAdmin;

public class H2DataStoreTest {

    private H2OffHeapStore queue;

    private boolean perf = false;

    @Before
    public void setup() throws IOException {
        // System.setProperty("org.opennms.minion.sink.queue.filename",
        // "/home/chandra/dev/test/cq/h2/h2-data.data");
        ConfigurationAdmin configAdmin = mock(ConfigurationAdmin.class, RETURNS_DEEP_STUBS);
        queue = new H2OffHeapStore(configAdmin);
        queue.init();
    }

    @Test
    public void testH2DataStore() throws InterruptedException, WriteFailedException {

        System.out.println("Size of store " + queue.getSize());
        long beforeWrite = System.currentTimeMillis();
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 1000; i++) {
                String message = "This is " + i + " trap message";
                try {
                    queue.writeMessage(message.getBytes(), "traps");
                } catch (WriteFailedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 1000; i++) {
                String message = "This is " + i + " syslog message";
                try {
                    queue.writeMessage(message.getBytes(), "syslog");
                } catch (WriteFailedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        for (int i = 0; i < 1000; i++) {
            String message = "This is " + i + " event message";
            queue.writeMessage(message.getBytes(), "events");
        }
        
        System.out.println("Size of store " + queue.getSize());
        long afterWrite = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            AbstractMap.SimpleImmutableEntry<String, byte[]> keyValue = queue.readNextMessage("traps");
            if (!perf) {
                String message = new String(keyValue.getValue());
                String matcher = "This is " + i + " trap message";
                assertEquals(matcher, message);
            }
        }
        for (int i = 0; i < 1000; i++) {
            AbstractMap.SimpleImmutableEntry<String, byte[]> keyValue = queue.readNextMessage("syslog");
            if (!perf) {
                String message = new String(keyValue.getValue());
                String matcher = "This is " + i + " syslog message";
                assertEquals(matcher, message);
            }
        }
        for (int i = 0; i < 1000; i++) {
            AbstractMap.SimpleImmutableEntry<String, byte[]> keyValue = queue.readNextMessage("events");
            if (!perf) {
                String message = new String(keyValue.getValue());
                String matcher = "This is " + i + " event message";
                assertEquals(matcher, message);
            }
        }
        long afterRead = System.currentTimeMillis();
        // if (perf) {
        System.out.println("Total Write time  " + (afterWrite - beforeWrite));
        System.out.println("Total read time  " + (afterRead - afterWrite));
        System.out.println("Total time  " + (afterRead - beforeWrite));
        // }
        Thread.sleep(5000);
        System.out.println("Size of store " + queue.getSize());
    }

    @After
    public void destroy() throws InterruptedException {
        queue.destroy();
        System.out.println("Size of store " + queue.getSize());
    }

}
