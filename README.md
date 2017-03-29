# Jasig Widget Portlets

[![Linux Build Status](https://travis-ci.org/Jasig/JasigWidgetPortlets.svg?branch=master)](https://travis-ci.org/Jasig/JasigWidgetPortlets)
[![Windows Build status](https://ci.appveyor.com/api/projects/status/k3r6p91ts9giq5us/branch/master?svg=true)](https://ci.appveyor.com/project/ChristianMurphy/jasigwidgetportlets/branch/master)

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
