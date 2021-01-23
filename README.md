# Jasig Widget Portlets

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.jasig.portlet/jasig-widget-portlets/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.jasig.portlet/jasig-widget-portlets)
[![build status](https://github.com/Jasig/JasigWidgetPortlets/workflows/CI/badge.svg?branch=master)](https://github.com/Jasig/JasigWidgetPortlets/actions)

This is a [Sponsored Portlet][] in the uPortal ecosystem.

## Configuration

See also [documentation in the external wiki][Jasig Widget Portlets in Confluence].

### Using Encrypted Property Values

You may optionally provide sensitive configuration items -- such as service passwords and API keys -- in encrypted format.  Use the [Jasypt CLI Tools](http://www.jasypt.org/cli.html) to encrypt the sensitive value, then include it in a `.properties` file like this:

```
hibernate.connection.password=ENC(9ffpQXJi/EPih9o+Xshm5g==)
```

Specify the encryption key using the `UP_JASYPT_KEY` environment variable.

[Sponsored Portlet]: https://wiki.jasig.org/display/PLT/Jasig+Sponsored+Portlets
[Jasig Widget Portlets in Confluence]: https://wiki.jasig.org/display/PLT/Jasig+Widget+Portlets
