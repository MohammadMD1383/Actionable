Switches between _expression style_ and _block body style_ functions if possible.

## Example

position of the caret is unimportant.

### Before

```kotlin
fun test(): Int {
	return readln().toInt() + 546
}
```

### Before

```kotlin
fun test() = readln().toInt() + 546
```
