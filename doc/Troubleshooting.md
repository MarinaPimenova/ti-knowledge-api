## How to put version into application.yml from build.gradle

Since `application.yml` already contains many Spring placeholders 
such as ${SERVER_PORT:8081}, then use Gradle's `ReplaceTokens` filter 
rather than `expand()`. 
This changes only `@project.version@` 
and leaves all Spring environment placeholders untouched.

Add the following to your build.gradle:

```groovy
tasks.processResources {
    filesMatching('application.yml') {
        filter(org.apache.tools.ant.filters.ReplaceTokens, tokens: [
            'project.version': project.version.toString()
        ])
    }
}
```

Be careful with solution such as:
```groovy
processResources {
    filesMatching('application.yml') {
        expand(project.properties)
    }
}
```
because `expand()` process `${...}` expressions as well. 
That can interfere with Spring's runtime environment-variable placeholders.

`ReplaceTokens` is much safer here because it performs only this substitution:

`@project.version@ → 0.0.1-SNAPSHOT`

---

## Java 
Yes — this output shows that you now have the **full OpenJDK 21 installed**, including both JDK and JRE:

```text
openjdk-21-jdk
openjdk-21-jdk-headless
openjdk-21-jre
openjdk-21-jre-headless
```

So **you don't need to uninstall anything** to fix the Gradle issue. The next step is to make sure Gradle detects the JDK rather than only the JRE.

### 1. Check Java and `javac`

Run:

```bash
java -version
javac -version
```

Both should report Java 21.

Then:

```bash
which java
which javac
```

You should get something like:

```text
/usr/bin/java
/usr/bin/javac
```

Check where they actually point:

```bash
readlink -f "$(which java)"
readlink -f "$(which javac)"
```

Expected:

```text
/usr/lib/jvm/java-21-openjdk-amd64/bin/java
/usr/lib/jvm/java-21-openjdk-amd64/bin/javac
```

### 2. Set `JAVA_HOME`

Run:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH="$JAVA_HOME/bin:$PATH"
```

Verify:

```bash
echo $JAVA_HOME
```

and:

```bash
$JAVA_HOME/bin/javac -version
```

### 3. Check Gradle again

Now run:

```bash
./gradlew -q javaToolchains
```

You should see:

```text
+ Ubuntu JDK 21
    | Location:           /usr/lib/jvm/java-21-openjdk-amd64
    | Language Version:   21
    | Vendor:             Ubuntu
    | Architecture:       amd64
    | Is JDK:             true
```

The critical value is:

```text
Is JDK: true
```

### 4. If Gradle still says `Is JDK: false`

Then I would check the actual directory:

```bash
ls -la /usr/lib/jvm/java-21-openjdk-amd64/
```

and:

```bash
ls -la /usr/lib/jvm/java-21-openjdk-amd64/bin/javac
```

If `javac` exists, explicitly tell Gradle which JDK to use.

Create or edit:

```text
~/.gradle/gradle.properties
```

and add:

```properties
org.gradle.java.home=/usr/lib/jvm/java-21-openjdk-amd64
```

Then stop the existing Gradle daemon:

```bash
./gradlew --stop
```

and check again:

```bash
./gradlew -q javaToolchains
```

Finally:

```bash
./gradlew clean build
```

### One important observation

Your package list confirms that the JDK installation itself is correct:

```text
openjdk-21-jdk
openjdk-21-jdk-headless
```

So **do not purge Java again**. The original problem was that Gradle saw only:

```text
Ubuntu JRE 21
Is JDK: false
```

Now we need to determine why Gradle isn't recognizing the newly installed JDK. The commands above will pinpoint that.
---

## Refresh dependencies

```bash
./gradlew clean build --refresh-dependencies
./gradlew bootRun
```