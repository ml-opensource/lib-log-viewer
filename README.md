# lib-log-viewer (Name under discussion)

A library for storing and analysing logs within a debugged application. You can: 
- Group logs into categories (e.g. "API calls", "Database writes", "Calculation results" etc.)
- Attach common severity levels to logs (e.g. "Warning", "Error" etc.) as well as add custom ones
- View logs for a specific category in real time
- View details for each log 

## Future plans 
- Take screenshot together with the log
- Various log sharing options

# Usage

## Initialize the logging library 

You need to initialize the library before logging anything. Usually you'd want to do this in `onCreate(...)` of your `Application` child class. 

```kotlin
Logger.initialize(context)
```

By default, the logs are stored in a Room-backed database. However, you can create your own implementation of `LogRepository` 
and use that instead. 

```kotlin
Logger.initialize(context, myRepository)
``` 

## Logging 

Your log entry contains the following information:
- The time of logging (inserted automatically)
- A category name to group your logs under ("General" by default)
- A severity level (e.g. warning, error, debug etc.); verbose by default.
- A tag (optional)
- A throwable (optional)
- A log message 

You can store logs with:
```kotlin
Logger.log(
    message,
    severityLevel,
    tag,
    categoryName,
    throwable
)
```

Helper extensions are also provided to reflect the more common Android `Log` class functions:
```kotlin
Logger.v(/* arguments without the severity level */)
Logger.i(/* arguments without the severity level */)
Logger.d(/* arguments without the severity level */)
Logger.w(/* arguments without the severity level */)
Logger.e(/* arguments without the severity level */)
Logger.wtf(/* arguments without the severity level */)
```

## Viewing logs

You can view your logs in the `LogViewerActivity`:
```kotlin
LogViewerActivity
    .createIntent(context)
    .let { startActivity(it) }
```

### First a category has to be selected.

![Log categories screenshot](/.readme/img/scr1.png?raw=true "Log categories screenshot")

### Then all the logs for that category will be shown.

![Log list screenshot](/.readme/img/scr2.png?raw=true "Log list screenshot")

### Landscape orientation allows more message text to be seen. Line numbers are also shown then.

![Landscape log list screenshot](/.readme/img/scr5.png?raw=true "Landscape log list screenshot")

### Tapping on a log opens its details.

![Assert log details screenshot](/.readme/img/scr3.png?raw=true "Assert log details screenshot")
![Debug log details screenshot](/.readme/img/scr4.png?raw=true "Debug log details screenshot")

## Using with Timber

### Setting up

```kotlin
Timber.plant(Logger.tree)
```

These calls will be redirected to the logging library.
```kotlin
Timber.log(CommonSeverityLevels.[LEVEL].severity.id, throwable, message, ... args)
Timber.d(throwable, message, ...args)
```

### Severity Levels

When using methods such as `Timber.d`, `Timber` will pass constants from `android.util.Log` to the tree. These constants map almost exactly to the severity levels of the logging library. The only exception is `wtf`. `Timber` will pass `ASSERT` when calling `wtf`, therefore, logging `ASSERT` with `Timber` can be done with either of these calls
```kotlin
Timber.wtf("Message")
Timber.log(CommonSeverityLevels.ASSERT.severity.id, "Message")
```

When passing id of one of the `CommonSeverityLevels`, `Timber` will pass the id to the tree.

Therefore, the only way to log `wtf` with Timber
```kotlin
Timber.log(CommonSeverityLevels.WTF.severity.id, "Message")
```

### Tags

`LoggerTree` extends from `DebugTree` therefore by default the tags will be class names. The logging library will reflect the tag which is currently used with `Timber`.

### Category

To change logging category for the next logs
```kotlin
Logger.category = "My Category"
Timber.d("This log is going to My Category")
```

### Etc

When logging throwables with `Timber`, `Timber` will append the throwable to the message, the log with throwable in the list may contain another line with text from the throwable
