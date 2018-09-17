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

package org.opennms.core.ipc.sink.common.offheap;

import java.util.Dictionary;

import org.opennms.core.ipc.sink.api.OffHeapQueue;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

public class OffHeapServiceLoader {
    
    private static BundleContext context;
    public static final String OFFHEAP_CONFIG = "org.opennms.core.ipc.sink.offheap";
    public static final String ENABLE_OFFHEAP = "enable.offheap";

    public BundleContext getBundleContext() {
        return context;
    }

    public static void setBundleContext(BundleContext bundleContext) {
        context = bundleContext;
    }
    
    public static boolean isOffHeapEnabled() {
        boolean useOffHeap = false;
        if (context != null) {
            try {
                ConfigurationAdmin configAdmin = context
                        .getService(context.getServiceReference(ConfigurationAdmin.class));
                Dictionary<String, Object> properties = configAdmin.getConfiguration(OFFHEAP_CONFIG).getProperties();
                if (properties != null && properties.get(ENABLE_OFFHEAP) != null) {
                    if (properties.get(ENABLE_OFFHEAP) instanceof String) {
                        useOffHeap = Boolean.parseBoolean((String) properties.get(ENABLE_OFFHEAP));
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return useOffHeap;
    }
    
    public static OffHeapQueue getOffHeapQueue() {
        if (context != null) {
            try {
                return context.getService(context.getServiceReference(OffHeapQueue.class));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

}
