<%--

//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2004 Nov 18: Fixed problem with category display when nodeLabel can't be found. Bill Ayres.
// 2003 Feb 07: Fixed URLEncoder issues.
// 2002 Nov 26: Fixed breadcrumbs issue.
// 
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//

--%>

<%@page language="java"
	contentType="text/html"
	session="true"
	import="org.opennms.web.category.*,
		org.opennms.web.element.NetworkElementFactory,
		org.opennms.web.MissingParameterException,
		java.util.*,
		org.opennms.netmgt.xml.rtc.Node
		"
%>

<%!
    public CategoryModel model = null;
    
    public void init() throws ServletException {
        try {
            this.model = CategoryModel.getInstance();            
        }
        catch( java.io.IOException e ) {
            throw new ServletException("Could not instantiate the CategoryModel", e);
        }
        catch( org.exolab.castor.xml.MarshalException e ) {
            throw new ServletException("Could not instantiate the CategoryModel", e);
        }
        catch( org.exolab.castor.xml.ValidationException e ) {
            throw new ServletException("Could not instantiate the CategoryModel", e);
        }        

    }
%>

<%
    String categoryName = request.getParameter("category");

    if (categoryName == null) {
        throw new MissingParameterException("category");
    }

    Category category = this.model.getCategory(categoryName);

    if (category == null) {
        throw new CategoryNotFoundException(categoryName);
    }

    //put the nodes in a tree map to sort by name
    TreeMap nodeMap = new TreeMap();    
    Enumeration nodeEnum = category.enumerateNode();
    
    while (nodeEnum.hasMoreElements()) {
        Node node = (Node) nodeEnum.nextElement();
        String nodeLabel =
		NetworkElementFactory.getNodeLabel((int)node.getNodeid());
        // nodeMap.put( nodeLabel, node );

        if (nodeLabel != null) {
            nodeMap.put(nodeLabel, node);
        } else {
           nodeMap.put("nodeId=" + node.getNodeid(), node);
        }
    }
    
    Set keySet = nodeMap.keySet();
    Iterator nameIterator = keySet.iterator();
%>

<jsp:include page="/includes/header.jsp" flush="false" >
  <jsp:param name="title" value="Category Service Level Monitoring" />
  <jsp:param name="headTitle" value="<%=category.getName()%>" />
  <jsp:param name="headTitle" value="Category" />
  <jsp:param name="headTitle" value="SLM" />
  <jsp:param name="breadcrumb" value="<a href='rtc/index.jsp'>SLM</a>" />
  <jsp:param name="breadcrumb" value="Category"/>
</jsp:include>

      <h3><%=category.getName()%></h3>
      <% if( category.getComment() != null ) { %>      
        <p><%=category.getComment()%></p>
      <% } %>

      <!-- Last updated <%=category.getLastUpdated()%> -->

      <table class="standardfirst">
        <tr>
          <td class="standardheader">Nodes</td>
          <td class="standardheader">Outages</td>
          <td class="standardheader">24hr Avail</td>
        </tr>
      
        <%  
            while( nameIterator.hasNext() ) {
                String nodeLabel = (String)nameIterator.next();
                Node node = (Node)nodeMap.get(nodeLabel);
                
                double value = node.getNodevalue();
        
                if( value >= 0 ) {
                    long serviceCount = node.getNodesvccount();        
                    long serviceDownCount = node.getNodesvcdowncount();
                    double servicePercentage = 100.0;
                
                    if( serviceCount > 0 ) {
                       servicePercentage = ((double)(serviceCount-serviceDownCount))/(double)serviceCount*100.0;
                    }
                
                    String color = CategoryUtil.getCategoryColor( category, value );
                    String outageColor = CategoryUtil.getCategoryColor( category, servicePercentage );
        %>
                    <tr>
                      <td class="standard"><a href="element/node.jsp?node=<%=node.getNodeid()%>"><%=nodeLabel%></a></td>
                      <td class="standard" BGCOLOR="<%=outageColor%>" align="right"><%=serviceDownCount%> of <%=serviceCount%></td>
                      <td class="standard" BGCOLOR="<%=color%>" ALIGN="right" WIDTH="30%"><b><%=CategoryUtil.formatValue(value)%>%</b></td>
                    </tr>
            <%  } %>
        <%  } %>
        
      <tr>
        <td class="standardheaderplain" colspan="3">Percentage over last 24 hours</td> <%-- next iteration, read this from same properties file that sets up for RTCVCM --%></td>
      </tr>    
    </table>


<jsp:include page="/includes/footer.jsp" flush="false" />
