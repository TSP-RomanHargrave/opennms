/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017-2017 The OpenNMS Group, Inc.
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

package org.opennms.features.topology.plugins.topo.asset;

import java.util.Arrays;
import java.util.List;

public class UriParameters {

	public static final String  PROVIDER_ID="providerId";
	public static final String  ASSET_LAYERS="assetLayers";
	public static final String  FILTER="filter";
	public static final String  LABEL="label";
	public static final String  BREADCRUMB_STRATEGY="breadcrumbStrategy";
	public static final String  PREFFERED_LAYOUT="preferredLayout";
	
	public static final List<String> ALL_PARAMETERS= Arrays.asList(PROVIDER_ID,ASSET_LAYERS,FILTER,LABEL,BREADCRUMB_STRATEGY,PREFFERED_LAYOUT);
}