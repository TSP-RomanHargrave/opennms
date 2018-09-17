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

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Dictionary;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.OffHeapStore;
import org.opennms.core.ipc.sink.api.OffHeapQueue;
import org.opennms.core.ipc.sink.api.WriteFailedException;
import org.osgi.service.cm.ConfigurationAdmin;

public class H2OffHeapStore implements OffHeapQueue {

    private static String fileName = System.getProperty("org.opennms.minion.sink.queue.filename");
    private final static String MAX_OFFHEAP_SIZE = "maxOffHeapSize";
    private MVStore store;
    private long maxSizeInBytes;
    private final ConfigurationAdmin configAdmin;
    // module specific maps.
    private Map<String, MVMap<String, byte[]>> offHeapModuleMap = new ConcurrentHashMap<>();
    private Map<String, BlockingQueue<String>> queueModuleMap = new ConcurrentHashMap<>();

    public H2OffHeapStore(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    public void init() throws IOException {
        OffHeapStore fileStore = new OffHeapStore();
        if (fileName != null) {
            store = new MVStore.Builder().fileName(fileName).open();
        } else {
            // Use offHeap store.
            store = new MVStore.Builder().fileStore(fileStore).open();
        }
        Dictionary<String, Object> properties = configAdmin.getConfiguration(MAX_OFFHEAP_SIZE).getProperties();
        if (properties != null && properties.get(MAX_OFFHEAP_SIZE) != null) {
            if (properties.get(MAX_OFFHEAP_SIZE) instanceof String) {
                maxSizeInBytes = Long.parseLong((String) properties.get(MAX_OFFHEAP_SIZE));
            }
        }
    }

    @Override
    public String writeMessage(byte[] message, String moduleName) throws WriteFailedException {
        long storeSize = store.getFileStore().size();
        if (storeSize + message.length > maxSizeInBytes) {
            throw new WriteFailedException();
        }
        MVMap<String, byte[]> mvMap = offHeapModuleMap.get(moduleName);
        if (mvMap == null) {
            mvMap = store.openMap(moduleName);
            offHeapModuleMap.put(moduleName, mvMap);
            BlockingQueue<String> keys = new LinkedBlockingQueue<String>();
            queueModuleMap.put(moduleName, keys);
        }
        String uuid = UUID.randomUUID().toString();
        mvMap.put(uuid, message);
        BlockingQueue<String> keys = queueModuleMap.get(moduleName);
        keys.add(uuid);
        return uuid;
    }
    
    @Override
    public AbstractMap.SimpleImmutableEntry<String, byte[]> readNextMessage(String moduleName)
            throws InterruptedException {
        BlockingQueue<String> keys = queueModuleMap.get(moduleName);
        if (keys == null) {
            return null;
        }
        String uuid = keys.take();
        MVMap<String, byte[]> mvMap = offHeapModuleMap.get(moduleName);
        if (mvMap != null) {
            byte[] value = mvMap.get(uuid);
            mvMap.remove(uuid);
            AbstractMap.SimpleImmutableEntry<String, byte[]> keyValue = new AbstractMap.SimpleImmutableEntry<String, byte[]>(
                    uuid, value);
            return keyValue;
        }
        return null;
    }

    public void destroy() {
        System.out.println("file size of h2 data store is " + store.getFileStore().size());
        store.getFileStore().close();
        store.close();
    }

    public long getSize() {
        // To update size calculations.
        store.commit();
        return store.getFileStore().size();

    }

}
