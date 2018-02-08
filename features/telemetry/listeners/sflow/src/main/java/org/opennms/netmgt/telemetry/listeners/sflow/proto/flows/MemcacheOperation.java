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

import org.opennms.netmgt.telemetry.listeners.api.utils.BufferUtils;
import org.opennms.netmgt.telemetry.listeners.sflow.InvalidPacketException;
import org.opennms.netmgt.telemetry.listeners.sflow.proto.AsciiString;

// struct memcache_operation {
//   memcache_protocol protocol;  /* protocol */
//   memcache_cmd cmd;            /* command */
//   string key<255>;             /* key used to store/retrieve data */
//   unsigned int nkeys;          /* number of keys
//                                   (including sampled key) */
//   unsigned int value_bytes;    /* size of the value (in bytes) */
//   unsigned int uS;             /* duration of the operation
//                                   (in microseconds) */
//   memcache_status status;      /* status of command */
// };

public class MemcacheOperation {
    public final MemcacheProtocol protocol;
    public final MemcacheCmd cmd;
    public final AsciiString key;
    public final long nkeys;
    public final long value_bytes;
    public final long uS;
    public final MemcacheStatus status;

    public MemcacheOperation(final ByteBuffer buffer) throws InvalidPacketException {
        this.protocol = MemcacheProtocol.from(buffer);
        this.cmd = MemcacheCmd.from(buffer);
        this.key = new AsciiString(buffer, Optional.empty());
        this.nkeys = BufferUtils.uint32(buffer);
        this.value_bytes = BufferUtils.uint32(buffer);
        this.uS = BufferUtils.uint32(buffer);
        this.status = MemcacheStatus.from(buffer);
    }
}
