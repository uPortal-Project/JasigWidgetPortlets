## DEPRECATED -- no longer maintained

This repository is deprecated and will not receive further updates. Of the eleven widgets it bundles, seven depend on Google APIs that were sunset years ago (Google Loader 2015, Maps v3.6, Google Gadgets ~2016); the remaining widgets are JSP demos, trivial date/dictionary/embed shapes, or have been absorbed into uPortal core (`AppLauncherPortlet`).

uPortal-start no longer ships `JasigWidgetPortlets` in the default overlay set (see [uPortal-Project/uPortal-start#696](https://github.com/uPortal-Project/uPortal-start/pull/696)).

**Recommended action for deployers:** remove this overlay from your deployment and drop any quickstart portlet definitions that bind to `/jasig-widget-portlets`. If you still actively use the dictionary, calendar, or youtube widget shapes, open an issue -- they can be ported to a small Lit web component on request.

The repository will remain on GitHub for historical reference but receives no further releases.

---

# Jasig Widget Portlets

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.jasig.portlet/jasig-widget-portlets/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.jasig.portlet/jasig-widget-portlets)
[![CI](https://github.com/Jasig/JasigWidgetPortlets/actions/workflows/CI.yml/badge.svg?branch=master)](https://github.com/Jasig/JasigWidgetPortlets/actions/workflows/CI.yml)

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
