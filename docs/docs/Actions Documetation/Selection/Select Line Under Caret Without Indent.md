## What Does It Do?

Selects the line, where the caret is, without leading/trailing white-space.

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

## Before

```kotlin
class HelloWorld {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			println|("Hello World!")
		}
	}
}
```

## After

```kotlin
class HelloWorld {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			[println|("Hello World!")]
		}
	}
}
```
