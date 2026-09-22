# java-static-builder-enforcement

Demonstration of detecting and enforcing inner Builder classes
to be static in Java using ArchUnit and Checkstyle.

## Purpose

This demo is not only about the builder class, as it is about
overall enforcement of code quality standards using deterministic rules.

### About the inner Builder

This is an overall known antipattern and bad practice that's easy to spot.

However, little is preventing a developer from introducing it :)

The problem with the non-static Builder is that it gets a reference to its
enclosing instance, and the compiler passes it through the constructor.

You end up having to instantiate an object to call a builder.
At that point you should understand your mistake already, but
if you're creative, you can still add bugs.

## Demo

Record [`User`](static-builder-demo/src/main/java/com/flexksx/User.java) has a static
Builder, while [`LeakyUser`](static-builder-demo/src/main/java/com/flexksx/LeakyUser.java)
has the inner one.
Everything else about them is identical.

Compile them and look at what you actually got:

```
$ javap 'com/flexksx/User$Builder.class'
public com.flexksx.User$Builder();

$ javap 'com/flexksx/LeakyUser$Builder.class'
public com.flexksx.LeakyUser$Builder(com.flexksx.LeakyUser);
```

There is the enclosing instance!

### Catching it in the source

Checkstyle can do this with one XPath query.
Look at [`checkstyle.xml`](checkstyle.xml):

```xml
<module name="MatchXpath">
    <property name="query"
              value="//OBJBLOCK/CLASS_DEF[./IDENT[@text='Builder'] and not(./MODIFIERS/LITERAL_STATIC)]"/>
    <message key="matchxpath.match" value="A nested class named Builder must be static."/>
</module>
```

Run it:

```
$ ./gradlew checkstyleMain
[ERROR] src/main/java/com/flexksx/LeakyUser.java:15:3: A nested class named Builder must be static. [MatchXpath]
```

The catch is that the query keys on the identifier `Builder`. 
Name it `UserBuilder` and it passes.

If you want to restrict any `Builder`, then just use an appropriate regex.

### Catching it in the bytecode

ArchUnit works on compiled classes, so it sees what `javap` sees.
From [`ArchUnitRuleTest.java`](static-builder-demo/src/test/java/com/flexksx/ArchUnitRuleTest.java):

```java
classes().that().haveSimpleName("Builder").should().notBeInnerClasses();
```

That fails with `Class <com.flexksx.LeakyUser$Builder> is an inner class`.

Being an inner class is a flag on the class file, which is why the rule is one line and there is nothing to parse.

The tradeoff is the timing, since the code has to compile first, so this runs later than Checkstyle does.

## What do I do now?

Whatever you want.

Configure your own style and architectural rules in the repo.

Then you can run them in CI, in pre-commit, in agent hooks,
so you don't have to sacrifice quality in favor of speed.
