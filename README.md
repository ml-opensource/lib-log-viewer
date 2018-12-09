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
- A category name to group your logs under
- A severity level (e.g. warning, error, debug etc.); verbose by default 
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

### First a category has to be selected:

![Log categories screenshot](/.readme/img/scr1.png?raw=true "Log categories screenshot")

### Then all the logs for that category will be shown: 

![Log list screenshot](/.readme/img/scr2.png?raw=true "Log list screenshot")

### There is also a landscape mode allowing to see more of the message text. It also adds line numbers: 

![Landscape log list screenshot](/.readme/img/scr5.png?raw=true "Landscape log list screenshot")

### Tapping on a log opens its details:

![Assert log details screenshot](/.readme/img/scr3.png?raw=true "Assert log details screenshot")
![Debug log details screenshot](/.readme/img/scr4.png?raw=true "Debug log details screenshot")
