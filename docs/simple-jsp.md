# Simple JSP Portlet

## Table of Contents
  - [Description](#desc)
  - [Feature Overview](#features)
  - [Common Use Cases](#usage)
  - [Portlet Preferences](#prefs)
  - [Additional Configuration](#config)
    - [Portlet.xml Settings](#settings)
    - [JSP Location](#jspdir)
    - [Examples](#examples)

## <a name="desc"></a> Description
The Simple JSP Portlet is used to create a JSP-backed portlet.
Some user attributes and additional properties can be used for dynamic content.

## <a name="features"></a> Feature Overview
  - JSTL, Expressive Language (i.e. EL), and Java scriptlet code available to the JSP
  - User attributes defined in portlet.xml are available in JSP
  - A simple properties file can define static values that are available in JSP

## <a name="usage"></a> Common Use Cases
Simple JSP Portlet is often used to handle dynamic content that depends on user attributes.
One common case is to show a list of links based on a user's affiliation.
This is done by leveraging the user's role attribute in a JSTL `c:choose` tags.

## <a name="prefs"></a> Portlet Preferences
Simple JSP Portlet has one important portlet preference:
  - `SimpleJspPortletController.jspName` - the `JSP` file name, minus `.jsp` suffix

## <a name="config"></a> Additional Configuration
Due to the nature of Simple JSP Portlet's dependency on JSPs and the portlet spec,
there are several additional configuration considerations.

### <a name="settings"></a> Portlet.xml Settings
Only user attributes defined in the `portlet.xml` for Jasig Widget Portlets are available
in these JSPs. Often, this file needs to be copied from this project to appropriate location
in _uPortal-start_'s overlay of `jasig-widget-portlets`:

```
overlays/jasig-widget-portlets/src/main/webapp/WEB-INF/portlet.xml
```

Add more user attributes as desired, after the current `user.login.id` is defined:

```xml
    <user-attribute>
        <description>Username attribute name for uPortal user</description>
        <name>user.login.id</name>
    </user-attribute>
    <user-attribute>
        <description>Username attribute name for uPortal user</description>
        <name>department</name>
    </user-attribute>
    <user-attribute>
        <description>Username attribute name for uPortal user</description>
        <name>displayName</name>
    </user-attribute>
    <user-attribute>
        <description>Username attribute name for uPortal user</description>
        <name>mail</name>
    </user-attribute>
    <user-attribute>
        <description>Username attribute name for uPortal user</description>
        <name>title</name>
    </user-attribute>
```

### <a name="jspdir"></a>JSP Location
JSPs for the Simple JSP Portlet should be placed in the following location in _uPortal-start_:

```bash
overlays/jasig-widget-portlets/src/main/webapp/WEB-INF/jsp/
```


### <a name="examples"></a>Examples
An example of a portlet definition file, `data/quickstart/portlet-definition/myjsp.portlet-definition.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<portlet-definition xmlns="https://source.jasig.org/schemas/uportal/io/portlet-definition" xmlns:ns2="https://source.jasig.org/schemas/uportal" xmlns:ns3="https://source.jasig.org/schemas/uportal/io/permission-owner" xmlns:ns4="https://source.jasig.org/schemas/uportal/io/stylesheet-descriptor" xmlns:ns5="https://source.jasig.org/schemas/uportal/io/portlet-type" xmlns:ns6="https://source.jasig.org/schemas/uportal/io/user" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="4.3" xsi:schemaLocation="https://source.jasig.org/schemas/uportal/io/portlet-definition https://source.jasig.org/schemas/uportal/io/portlet-definition/portlet-definition-4.3.xsd">
    <title>My JSP</title>
    <name>My JSP</name>
    <fname>my-jsp</fname>
    <desc>Test JSP invoker</desc>
    <type>Portlet</type>
    <timeout>1000</timeout>
    <portlet-descriptor>
        <ns2:webAppName>/jasig-widget-portlets</ns2:webAppName>
        <ns2:portletName>SimpleJspPortlet</ns2:portletName>
    </portlet-descriptor>
    <category>Demonstration</category>
    <category>Development</category>
    <group>Everyone</group>
    <permissions>
        <permission system="UP_PORTLET_SUBSCRIBE" activity="BROWSE">
            <group>Everyone</group>
        </permission>
    </permissions>
    <portlet-preference>
        <name>SimpleJspPortletController.jspName</name>
        <readOnly>false</readOnly>
        <value>myJsp</value>
    </portlet-preference>
</portlet-definition>
```

The corresponding example JSP, `overlays/jasig-widget-portlets/src/main/webapp/WEB-INF/jsp/myJsp.jsp`:

```jsp
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
  </head>
  <body>
    <h3>UserInfo</h3>
      <ul>
        <c:forEach var="userAttr" items="${userInfo}">
        <li>${userAttr}</li>
        </c:forEach>
      </ul>
    <h3>Properties</h3>
      <ul>
        <c:forEach var="prop" items="${property}">
        <li>${prop}</li>
        </c:forEach>
      </ul>
    <h3>Resolver</h3>
      <ul>
        <li>portal.server=${resolver.getProperty('portal.server')}</li>
        <li>portal.context=${resolver.getProperty('portal.context')}</li>
      </ul>
   </body>
</html>
```
