# Sunny

Prevent Tachiyomi and Mihon from throttling your requests by ignoring rate limits.

## Features

* Ignore extension-specific rate limits (makes page loading and browsing faster).
* Increase max number of concurrent downloads from 2 to 16.

## What is this?

Mihon and Tachiyomi have a rate limit system based on [OkHttp interceptors](https://square.github.io/okhttp/features/interceptors). This system is intended to prevent manga sources from being overloaded with too many requests and to avoid specific users from being blocked due to high network traffic (i.e. bulk downloads). This is achieved by deliberately waiting for a specific amount of seconds before sending HTTP requests.

This is a simple but efficient system that can prevent a lot of issues. However, the downside is that nowadays these rate limits are applied to every extension; even those that do not actually require any rate limit. "just in case".

Unfortunately, this results in browsing and downloading being slower than necessary.

Bellow is a video comparison:

![Video comparison](https://raw.githubusercontent.com/Kartatz/r/main/sunny.gif)

**On the left side**: Tachiyomi **with** the Sunny module enabled.  
**On the right side**: Tachiyomi **without** the Sunny module.

Note that it takes more than 30 seconds to completely load the page without the module.

## Supported versions

The minimum supported version is Tachiyomi v0.12.0. It might be compatible with older releases, but that is not guaranteed to work.

This was tested up to Mihon v0.16.5.
