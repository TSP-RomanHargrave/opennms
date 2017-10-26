/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.telemetry.adapters.netflow.ipfix.ie;

import java.nio.ByteBuffer;

import org.opennms.netmgt.telemetry.adapters.netflow.ipfix.InvalidPacketException;
import org.opennms.netmgt.telemetry.adapters.netflow.ipfix.session.Session;

public abstract class Value {

    public interface Parser {
        Value parse(final Session session, final ByteBuffer buffer) throws InvalidPacketException;

        int getMaximumFieldLength();

        int getMinimumFieldLength();
    }

    private final String name;

    protected Value(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}