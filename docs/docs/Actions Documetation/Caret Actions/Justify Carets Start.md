Aligns the carets at the leftmost column.

## Example

| Symbol | Meaning |
|:------:|:-------:|
| &vert; |  Caret  |

### Before

[//]: # (@formatter:off)
```java
String longtext = "longtext";|
String verylongtext = "verylongtext";|
String veryverylongtext = "veryverylongtext";|
```
[//]: # (@formatter:on)

### After

[//]: # (@formatter:off)
```java
String longtext = "longtext";|
String verylongtext = "verylo|ngtext";
String veryverylongtext = "ve|ryverylongtext";
```
[//]: # (@formatter:on)
