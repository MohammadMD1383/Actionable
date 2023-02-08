---
title: Actionable
---

## Duplicate Line and Paste Contents from Clipboard

Duplicates line under caret for each line of clipboard content and pastes that line at current caret position.

---

### example:

| symbol              | meaning |
| ------------------- | ------- |
| <code>&#124;</code> | caret   |

copy/have in clipboard:
```java
A
B
C
```
having:
```java
String s| = "some string";
```
executing action will produce:
```java
String sA| = "some string";
String sB| = "some string";
String sC| = "some string";
```

Note: If any text is selected, it will be replaced with pasted line's value.

[&larr; Back](index.md)