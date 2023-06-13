# Introduction To Advanced Search

As the name shouts, this is a utility feature that helps you search in the project in an advanced level!
Even better than IDE does!

_(Though this feature is still at ==early== stages... :))_

## Quick Overview

[//]: # (@formatter:off)
!!! note
    This feature is currently implemented for java.
[//]: # (@formatter:on)

1. Create a file _(may be an scratch file, not to mess out your project structure)_ with `.aas` extension.
2. with the help of ==auto completion== go ahead!
3. you will find a gutter icon on first statement, click on that, and the search process will start shortly.
4. you will see your search results in the find toolwindow as soon as one item is found!

Take a look at this example:

```AdvancedSearch title='hello world.aas'
language: 'Java'
scope: 'all'

$class extends 'java.lang.Runnable'
```

with this, you will find all classes that extend `java.lang.Runnable`

To see detailed description about all supported language please visit [here](Supported%20Languages/index.md)

## Basics

1. ==the first thing is to specify the `language` property.== this tells the engine which language you want to do search in.
2. after that auto completion will be expanded, and all the variables, identifiers, parameters that are supported for that language will become
   available.

## For Developers

If you're interested in advanced search, and you want to implement this feature for your favorite language,
you can refer to this [section](For%20Developers/index.md) for more info on how to extend this...
