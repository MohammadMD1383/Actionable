## What Does It Do?

Removes duplicate selections :) Look at the example.

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

### Before

[//]: # (@formatter:off)
```java
[String a = "Hello World"]|
[String b = "Hello World"]|
[String c = "Hello World"]|
[String a = "Hello World"]|
[String a = "Hello World"]|
[String a = "Hello World"]|
[String d = "Hello World"]|
```
[//]: # (@formatter:on)

### After

[//]: # (@formatter:off)
```java
[String a = "Hello World"]|
[String b = "Hello World"]|
[String c = "Hello World"]|
|
|
|
[String d = "Hello World"]|
```
[//]: # (@formatter:on)
