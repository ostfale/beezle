appender("FILE", FileAppender) {
    file = "${System.getenv('USERPROFILE')}/beezle.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "[%date{ISO8601}] %level %logger - %msg%n"
    }
}

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

logger("de.ostfale", TRACE, ["CONSOLE", "FILE"], false)
root(TRACE, ["CONSOLE", "FILE"])