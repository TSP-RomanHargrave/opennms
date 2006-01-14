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
	import="org.opennms.web.notification.*,
        	org.opennms.web.MissingParameterException
	"
%>

<%!
    NotificationModel model = new NotificationModel();
%>

<%
    String username = request.getParameter( "username" );
    
    
    if( username == null ) {
        throw new MissingParameterException( "username" );
    }

    //allow all users to acknowlege notices even if they don't own them
    boolean editable = true; //request.getRemoteUser().equals( username );
    Notification[] notices = this.model.getOutstandingNotices( username );   
%>

<jsp:include page="/includes/header.jsp" flush="false" >
  <jsp:param name="title" value="Notifications Listing" />
  <jsp:param name="headTitle" value="Notifications Listing" />
  <jsp:param name="breadcrumb" value="<a href='notification/index.jsp'>Notification</a>" />
  <jsp:param name="breadcrumb" value="List" />
</jsp:include>

<script language="Javascript" type="text/javascript" >
    function checkAllCheckboxes() 
    {
       if (verifyUser())
       {
          for( i = 0; i < document.acknowledge_form.notices.length; i++ ) 
          {
              document.acknowledge_form.notices[i].checked = true
          }
          submitForm();
       }
    }
    
    function submitChecked()
    {
        if (verifyUser())
        {
            submitForm();
        }
    }
    
    function submitForm()
    {
        document.acknowledge_form.submit();
    }
    
    function verifyUser()
    {
        if (document.acknowledge_form.notifUser.value != document.acknowledge_form.curUser.value)
        {
            return confirm("You are not the owner of these notifications. Are you sure you want to acknowledge them?");
        }
        return true;
    }
</script>

<h3>Notifications for <%=username%> </h3>

<% if( editable ) { %>
  <form method="post" action="notification/acknowledge.jsp" name="acknowledge_form" >
    <input type="hidden" name="notifUser" value="<%=username%>">
    <input type="hidden" name="curUser" value="<%=request.getRemoteUser()%>">
<% } %>

<table class="standard">
  <tr>
    <% if( editable ) { %>
      <td class="standardheader"width="5%">Ack</th>
    <% } %>
    <td class="standardheader" width="5%">Notice</th>
    <td class="standardheader" width="5%">Event ID</th>
    <td class="standardheader" width="25%">Time Sent</th>
    <td class="standardheader">Message</th>
  </tr>

  <% for( int i = 0; i < notices.length; i++ ) { %>
<%--
  TODO: Implement even/odd row shading
    <tr <% if( i%2 == 0 ) out.print( "BGCOLOR=\"#cccccc\""); %> valign="top">
--%>
    <tr>
      <!--all users can acknowlege any notice -->          
      <% if( editable ) { %>
        <td class="standard">
          <input type="checkbox" name="notices" value="<%=notices[i].getId()%>" />
        </td>
      <% } %>
      <td class="standard"><a href="notification/detail.jsp?notice=<%=notices[i].getId()%>"><%=notices[i].getId()%></a></td>
      <td class="standard"><a href="event/detail.jsp?id=<%=notices[i].getEventId()%>"><%=notices[i].getEventId()%></a></td>
      <td class="standard"><%=notices[i].getTimeSent()%></td>            
      <td class="standard"><%=notices[i].getTextMessage()%></td>
    </tr>
  <% } %>
</table>

<br>

<% if( editable ) { %>
  <input TYPE="button" VALUE="Acknowledge" onClick="submitChecked()"/>
  <input TYPE="button" VALUE="Acknowledge All" onClick="checkAllCheckboxes()"/>
  <input TYPE="reset" />

  <p>
    <font size="-1">       
      Check the boxes next to the notices you are acknowledging and then
      click the <em>Acknowledge</em> button.  

      <br/>

      Or to acknowledge all the listed notices, click the
      <em>Acknowledge All</em> button.

      <br/>

      Or click the <em>Reset</em> button to clear all selections.
    </font>
  </p>

</form>
<% } %>

<jsp:include page="/includes/footer.jsp" flush="false" />
