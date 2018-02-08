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

package org.opennms.netmgt.telemetry.listeners.sflow.proto.flows;

import java.nio.ByteBuffer;
import java.util.Optional;

import org.opennms.netmgt.telemetry.listeners.sflow.InvalidPacketException;
import org.opennms.netmgt.telemetry.listeners.sflow.proto.AsciiString;

// struct jvm_runtime {
//   string vm_name<64>;      /* vm name */
//   string vm_vendor<32>;    /* the vendor for the JVM */
//   string vm_version<32>;   /* the version for the JVM */
// };

public class JvmRuntime {
    public final AsciiString vm_name;
    public final AsciiString vm_vendor;
    public final AsciiString vm_version;

    public JvmRuntime(final ByteBuffer buffer) throws InvalidPacketException {
        this.vm_name = new AsciiString(buffer, Optional.empty());
        this.vm_vendor = new AsciiString(buffer, Optional.empty());
        this.vm_version = new AsciiString(buffer, Optional.empty());
    }
}
