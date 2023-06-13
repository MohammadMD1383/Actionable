For `java` the syntax is as follows:

```AdvancedSearch
[<entry> [<identifier> [<parameters>]]] [<inner-block>] 
```

and inside inner block as follows:

```AdvancedSearch
[<entry> [<identifier> [<parameters>]]] [<inner-block>]
# or
[<identifier> [<parameters>]] [<inner-block>]
```

### Example

```AdvancedSearch
language: 'java'
scope: 'all'
scan-source: 'false'

$class {
    extends 'java.lang.Runnable'
    implements 'my.package.Interface'
    has-method 'doSomething' { has-param 'java.lang.String _' }
}
```

This piece of code tells everything! no need to describe what you will find...

[//]: # (@formatter:off)
!!! note
    all the variables, identifiers, parameters and properties with their possible values are documented.
    you can see the documentation right inside the IDE in completion and using quick doc ++ctrl+q++.
[//]: # (@formatter:on)

## Properties

* `scope`: the search scope: can be `project` or `all`
* `scan-source`: if set to `false` the search uses stubs only, otherwise full sources will be scanned

## Variables ($...)

* `$type`: any type: class, interface, ...
* `$class`
* `$interface`
* `$annotation`
* `$method`

## Identifiers

* `has-param`: checks that this method has exactly all the parameters specified
* `with-param`: like `has-param`, but checks that at least these parameters are present
* `name-matches`: checks that the name of the entry matches this regexp
* `super-of`: checks that this type is the super type of all types specified in parameters
* `extends`: checks that this type extends all the types specified in parameters
* `implements`: checks that this type implements all the interfaces specified in parameters
* `direct-super-of`: check that this type is directly the super type of all types specified in parameters
* `extends-directly`: check that this type directly extends all the types specified in parameters
* `implements-directly`: check that this type directly implements all the interfaces specified in parameters
* `has-modifier`: checks that this entry has at least all the modifiers specified in parameters
* `has-method`: checks that this type has methods with names specified in parameters
* `has-method-directly`: checks that this type has methods that are defined in the type itself, not just inherited
* `is-anonymous`: checks that this type is an anonymous class
* `not-anonymous`: checks that this type is not an anonymous class
