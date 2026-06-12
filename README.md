# Phpstorm plugin smarty helper
Template file autocomplete and Ctrl+Click navigation helper to a template or even a function definition.

```
<{extends file="frontend/register/index.tpl"}>
<{function_to_call('arg', [1, 'test'])}>
```

## Support
- Runtime 27.0.7.
- Phpstorm 2025.*

## Build
- ``./gradlew buildPlugin``
- ``ls -la build/distributions/``
- You should see something like smarty-1.0.0.zip

## Install
- Open phpstorm
- Settings / Plugins / Install from Disk and select zip file.
- or [use latest release build](https://github.com/scr34m/phpstorm-smarty-plugin/releases/latest)
