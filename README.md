# Sunny

Prevent Tachiyomi and Mihon from throttling your requests by ignoring rate limits.

## Features

* Ignore extension-specific rate limits (makes page loading, downloading and browsing faster).
* Increase max number of concurrent downloads from 2 to 16.

## What is this?

Tachiyomi has a rate limit system that was designed to prevent manga sources from being overloaded with too many requests and to avoid specific users from being blocked by manga sources due to high network traffic (i.e. bulk downloads). This is achieved by deliberately waiting for a specific amount of seconds before sending networks requests.

The downside is that nowadays these rate limits are applied to every extension; even those that do not actually require any rate limit. "just in case".

Unfortunately, this results in browsing and downloading being slower than necessary, degrading the overall user experience.

Bellow is a video comparison:

![Video comparison](https://raw.githubusercontent.com/Kartatz/r/main/sunny.gif)

**Left side**: Tachiyomi **with** the Sunny module enabled.  
**Right side**: Tachiyomi **without** the Sunny module.

## Usage

This module requires root and an API-compatible Xposed implementation to work.

Firstly, download and install the APK from the [releases](https://github.com/Xposed-Modules-Repo/com.amanoteam.sunny/releases/latest/download/app-release.apk) page.

- **LSposed (Android 8 and up)**
  - Go to the modules section, enable Sunny, and mark the Tachiyomi package in the list of apps. Then, kill/force-close the Tachiyomi process.

- **Legacy Xposed (Android 7 and below)**
  - Go to the modules section, enable Sunny, and reboot the device.

## Supported versions

The minimum supported version is Tachiyomi v0.12.0. It might be compatible with older releases, but that is not guaranteed to work.

This was tested up to Mihon v0.17.1.

## Non-root alternative

If you are looking for a non-root alternative, try my [fork](https://github.com/AmanoTeam/mihon) of Mihon. It's the original Mihon app with my changes on top of it.