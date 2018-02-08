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

import org.opennms.netmgt.telemetry.listeners.api.utils.BufferUtils;
import org.opennms.netmgt.telemetry.listeners.sflow.InvalidPacketException;

// struct mib2_ip_group {
//   unsigned int ipForwarding;
//   unsigned int ipDefaultTTL;
//   unsigned int ipInReceives;
//   unsigned int ipInHdrErrors;
//   unsigned int ipInAddrErrors;
//   unsigned int ipForwDatagrams;
//   unsigned int ipInUnknownProtos;
//   unsigned int ipInDiscards;
//   unsigned int ipInDelivers;
//   unsigned int ipOutRequests;
//   unsigned int ipOutDiscards;
//   unsigned int ipOutNoRoutes;
//   unsigned int ipReasmTimeout;
//   unsigned int ipReasmReqds;
//   unsigned int ipReasmOKs;
//   unsigned int ipReasmFails;
//   unsigned int ipFragOKs;
//   unsigned int ipFragFails;
//   unsigned int ipFragCreates;
// };

public class Mib2IpGroup {
    public final long ipForwarding;
    public final long ipDefaultTTL;
    public final long ipInReceives;
    public final long ipInHdrErrors;
    public final long ipInAddrErrors;
    public final long ipForwDatagrams;
    public final long ipInUnknownProtos;
    public final long ipInDiscards;
    public final long ipInDelivers;
    public final long ipOutRequests;
    public final long ipOutDiscards;
    public final long ipOutNoRoutes;
    public final long ipReasmTimeout;
    public final long ipReasmReqds;
    public final long ipReasmOKs;
    public final long ipReasmFails;
    public final long ipFragOKs;
    public final long ipFragFails;
    public final long ipFragCreates;

    public Mib2IpGroup(final ByteBuffer buffer) throws InvalidPacketException {
        this.ipForwarding = BufferUtils.uint32(buffer);
        this.ipDefaultTTL = BufferUtils.uint32(buffer);
        this.ipInReceives = BufferUtils.uint32(buffer);
        this.ipInHdrErrors = BufferUtils.uint32(buffer);
        this.ipInAddrErrors = BufferUtils.uint32(buffer);
        this.ipForwDatagrams = BufferUtils.uint32(buffer);
        this.ipInUnknownProtos = BufferUtils.uint32(buffer);
        this.ipInDiscards = BufferUtils.uint32(buffer);
        this.ipInDelivers = BufferUtils.uint32(buffer);
        this.ipOutRequests = BufferUtils.uint32(buffer);
        this.ipOutDiscards = BufferUtils.uint32(buffer);
        this.ipOutNoRoutes = BufferUtils.uint32(buffer);
        this.ipReasmTimeout = BufferUtils.uint32(buffer);
        this.ipReasmReqds = BufferUtils.uint32(buffer);
        this.ipReasmOKs = BufferUtils.uint32(buffer);
        this.ipReasmFails = BufferUtils.uint32(buffer);
        this.ipFragOKs = BufferUtils.uint32(buffer);
        this.ipFragFails = BufferUtils.uint32(buffer);
        this.ipFragCreates = BufferUtils.uint32(buffer);
    }
}
