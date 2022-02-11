# Intellij runner on multi module project
This project setup show some issues I encountered with the Intellij IDEA runner. 
All the issues described here are present on the Intellij 2021.3.1 release (Build #IC-213.6461.79, built on December 28, 2021).

## Project context
The project rely on code generation (**Dagger2**).  
It is composed of 4 modules:
- `android` (a simple android module used here to trigger the intellij android plugin)
- `core` (which contains a class that can print the current project version)
- `game` (a java module which relies on `core` to create a jar that print the current project version)
- `chat` (a kotlin module which relies on `core` to create a jar that print the current project version)


When running the `game` and `chat` main classes using the gradle runner, everything works fine as expected
(classes are generated, compiled, resources are processed and gathered into the classpath).  
However, my real project have 5 launch configurations and launching them all at the same time using a compound can take up to 3 minutes
using Gradle when it is about 10 seconds using the Intellij IDEA runner.


Unfortunately, when I switch to the Intellij IDEA runner a couple of odd things start appearing.  
I understand that the IDEA runner do miss some gradle feature and I am fine with that but its build actions are inconsistents
thus it does not allow me to have a nice workaround.  

Below I described 4 test cases that summarize the issues encountered.


## Case 1
### Given
- `android plugin` enabled
- `android.tasks.runner.restricted` set to `true` (*This option is available via the Intellij registry panel*)
- `Run/Debug` option set to `Intellij IDEA`

### When
- Launching the main class from the game module (Java module)

### Then
#### Build
- Does not perform any gradle tasks
- Generates the dagger classes (*.java*) directly into the module in the module under `src/main/generated`
- Compiles the classes into the `build/classes/java/main` folder
- Does not copy the module resources into the default Intellij `out` build folder nor into the gradle `build` folder
#### Editor
- Does see the generated dagger classes from the `src/main/generated` folder
#### Run execution
- Includes the `build/classes/java/main` folder in the classpath for running the app
- It does not include the `build/resources/main` folder in the classpath for running the app
- However, if an `out/production/resources` folder is present when the main is launched, it adds it into the classpath as well

### Questions
- Why Intellij do not copy the module resources folder into the `out` folder automatically?

## Case 2
### Given
- `android plugin` enabled
- `android.tasks.runner.restricted` set to `true` (*This option is available via the Intellij registry panel*)
- `Run/Debug` option set to `Intellij IDEA`

### When
- Launching the main class from the chat module (Kotlin module)

### Then
#### Build
- Does not perform any gradle tasks
- Does not generate any dagger classes (thus creating compilations errors)

If I manually generate the dagger classes file using gradle kaptKotlin task, it also:
- Compiles all classes into the `build/classes/java/main` folder
- Does not copy the module resources into the default Intellij `out` build folder nor into the gradle `build` folder
#### Editor
- Does see the generated dagger classes from the `build/generated/source/kapt/main` folder
#### Run execution
- Includes the `build/classes/java/main` folder in the classpath for running the app
- Includes the `build/tmp/kapt3/classes/main` folder in the classpath for running the app (empty folder)
- It does not include the `build/resources/main` folder in the classpath for running the app
- However, if an `out/production/resources` folder is present when the main is launched, it adds it into the classpath as well

### Questions
- Why Intellij do not generate the kotlin dagger classes?
- Why Intellij do not copy the module resources folder into the `out` folder automatically?
- Is it expected that Intellij copy the Kotlin classes into the `build/classes/java/main` instead of `build/classes/kotlin/main`?
- Why the folder `build\tmp\kapt3\classes\main` is added to the classpath?

## Notes
*The cases 3&4 are the same as 1&2 with the `android.task.runner.restricted` set to false*  
The behaviors are much different.
## Case 3

### Given
- `android plugin` enabled
- `android.tasks.runner.restricted` set to `false` (*This option is available via the Intellij registry panel*)
- `Run/Debug` option set to `Intellij IDEA`

### When
- Launching the main class from the game module (Java module)

### Then
#### Build
- Generate the dagger classes (*.java*) directly into the `build/generated/sources/annotationProcessor/java/main` folder
- Trigger gradle `compileJava`, and `testClasses` tasks (which triggers the `processResources` task as well)
- Compile the classes into the "build/classes/java/main" folder
- Does not copy the module resources into the default Intellij `out` build folder
#### Editor
- Does not see the generated dagger classes from the `build/generated/sources/annotationProcessor/java/main` folder
#### Run execution
- Include the `build/classes/java/main` folder in the classpath for running the app
- It does not include the `build/resources/main` folder in the classpath for running the app
- However, if an `out/production/resources` folder is present when the main is launched, it adds it into the classpath as well

### Questions
I assume in this case the gradle tasks are run because of the android plugin runner and it should not be the case.
Why Intellij does not copy the module resources into the `out` folder?
Why intellij does not use the `build/resources/main` folder into the classpath to run the app?
Why the editor does not see the generated dagger classes from the `build/generated/sources/annotationProcessor/java/main` folder?


## Case 4
### Given
- `android plugin` enabled
- `android.tasks.runner.restricted` set to `false` (*This option is available via the Intellij registry panel*)
- `Run/Debug` option set to `Intellij IDEA`

### When
- Launching the main class from the chat module (Kotlin module)

### Then
#### Build
- Generate the dagger classes (*.java*) directly into the `build/generated/source/kapt/main` folder
- Trigger gradle `compileJava`, and `testClasses` tasks (which triggers the `processResources` task as well)
- It compile the java classes (dagger generated) into the `build/classes/java/main` folder
  It compile the kotlin classes (module classes) into the `build/classes/kotlin/main` folder
- Does not copy the module resources into the default Intellij `out` build folder
#### Editor
- Does see the generated dagger classes from the `build/generated/source/kapt/main` folder
#### Run execution
- Include the `build/classes/java/main` folder in the classpath for running the app
- It does not include the `build/resources/main` folder in the classpath for running the app
- It does not include the `build/classes/kotlin/main` folder in the classpath for running the app (thus making the app fail)
- However, if an `out/production/resources` folder is present when the main is launched, it adds it into the classpath as well

### Questions
I assume in this case the gradle task are run because of the android plugin runner and it should not be the case.
Why Intellij does not copy the module resources into the `out` folder?
Why intellij does not use the `build/resources/main` folder into the classpath to run the app?
Why intellij does not include the `build/classes/kotlin/main` folder in the classpath for running the app