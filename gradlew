#!/usr/bin/env sh

##############################################################################
##
##  Gradle wrapper script for Linux/macOS.
##  If gradle-wrapper.jar is missing, this script downloads the Gradle
##  distribution directly so CI works without committing the binary jar.
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=$(ls -ld "$PRG")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=$(dirname "$PRG")"/$link"
    fi
done
APP_HOME=$(dirname "$PRG")
APP_HOME=$(cd "$APP_HOME" && pwd -P)

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

# Add default JVM options here.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# OS specific support (must be 'true' or 'false').
cygwin=false
darwin=false
nonstop=false
case "$(uname)" in
    CYGWIN* )  cygwin=true  ;;
    Darwin* )  darwin=true  ;;
    NONSTOP* ) nonstop=true ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

##############################################################################
## Bootstrap: download gradle-wrapper.jar if it is missing.
##############################################################################
if [ ! -f "$CLASSPATH" ]; then
    echo "gradle-wrapper.jar not found; downloading Gradle distribution directly..."
    GRADLE_VERSION="8.7"
    GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
    DIST_BASE="$GRADLE_USER_HOME/wrapper/dists"
    DIST_DIR="$DIST_BASE/gradle-${GRADLE_VERSION}-bin"
    GRADLE_HOME="$DIST_DIR/gradle-${GRADLE_VERSION}"

    if [ ! -d "$GRADLE_HOME" ]; then
        mkdir -p "$DIST_DIR"
        DIST_ZIP="$DIST_DIR/gradle-${GRADLE_VERSION}-bin.zip"
        DIST_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
        echo "  -> Downloading $DIST_URL"
        if command -v curl > /dev/null 2>&1; then
            curl -fsSL "$DIST_URL" -o "$DIST_ZIP"
        elif command -v wget > /dev/null 2>&1; then
            wget -q "$DIST_URL" -O "$DIST_ZIP"
        else
            echo "ERROR: Neither curl nor wget found. Cannot download Gradle." >&2
            exit 1
        fi
        unzip -q "$DIST_ZIP" -d "$DIST_DIR"
        rm -f "$DIST_ZIP"
    fi

    exec "$GRADLE_HOME/bin/gradle" --project-dir "$APP_HOME" "$@"
fi

##############################################################################
## Standard wrapper execution path (when gradle-wrapper.jar exists).
##############################################################################

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = "true" ] || [ "$msys" = "true" ] ; then
    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
    CLASSPATH=$( cygpath --path --mixed "$CLASSPATH" )
    JAVACMD=$( cygpath --unix "$JAVACMD" )
fi

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java > /dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=$(save "$@")

# Collect all arguments for the java command, following the shell quoting and substitution rules
eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "\"-Dorg.gradle.appname=$APP_BASE_NAME\"" -classpath "\"$CLASSPATH\"" org.gradle.wrapper.GradleWrapperMain "$APP_ARGS"

exec "$JAVACMD" "$@"
