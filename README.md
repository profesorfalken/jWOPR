![](https://img.shields.io/github/license/profesorfalken/jPowerShell.svg)

# jWOPR

Simple Java API that allows you to interact with different AI providers as:

- OpenAI

Quick example:

```java
WOPR.get().ask("Who was the programmer of WOPR?"));
```

Result:

```
The WOPR was programmed by Professor Falken in the 1983 movie WarGames.
```

## Installation

You can add jWOPR to your project as dependency.

For example, for Maven you have just to add to your pom.xml:

    <dependency>
        <groupId>com.profesorfalken</groupId>
        <artifactId>jWOPR</artifactId>
        <version>1.0.1</version>
    </dependency>

Or, if you are using Gradle:

    implementation group: 'com.profesorfalken', name: 'jWOPR', version: '1.0.1'

If you wish, you can simply download the JAR file and add it to your
classpath. https://repo1.maven.org/maven2/com/profesorfalken/jWOPR/1.0.1/jWOPR-1.0.1.jar

## Usage

For the moment, only a basic operation is available.

**ask**:  perform a question or ask the IA to complete a task

In order to use it, you should:

* Create an instance of jWOPR.
* Override the configuration if needed.
* Send a message to the IA using the ask() method.

```java
WOPRResponse woprResponse=wopr.ask("Say 'This is a test.'");

        Assertions.assertEquals("This is a test.",woprResponse.getText());
``` 

Make sure that you set your API key using an environment variable called

* OPENAPI_KEY

Or via configuration as described in the next sections.

If an error occurs, you can check the cause using the method *WOPRResponse#getError()*

## Configuration

You can configure jWOPR using one of the following methods:

* Setting a file called *jwopr_configuration.properties* in your classpath.

This file will can have one of the following properties:

**openai.model**: the IA model. See https://platform.openai.com/docs/models for more details.
**openai.temperature**: sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more
random, while lower values like 0.2 will make it more focused and deterministic.
**openai.maxTokens**: the maximum number of tokens allowed for the generated answer

Default values:

```
openai.model=text-davinci-003
openai.temperature=0
openai.maxTokens=2048
```

**Note**: you can also configure here your API Key with **openai.authToken** but it is recommended to use the
environment variable for security reasons.

* Using a Map object containing the data to override.

It is possible set the configuration for the instance using the *withConfiguration* method.

Example :

```java
Map<String, String> configuration=new HashMap<>();
        configuration.put("model","text-davinci-002");

        WOPR wopr=WOPR.get().withConfiguration(configuration);
        wopr.ask("Say 'This is a test.'"); //Check response
```
