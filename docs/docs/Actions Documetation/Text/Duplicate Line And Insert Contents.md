# Duplicate Line And Insert Contents

## What Does It Do?

Duplicates a line and inserts contents gotten from an input dialog.

[//]: # (@formatter:off)

!!! note
		* Using multiple carets are supported.
		* Selections are supported.

!!! warning
		Multiline duplication is not supported yet.

[//]: # (@formatter:on)

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

### Before

```go
| := "variable-[aaaaa]|"
```

### Action

1. Execute the action
2. Insert the following content to the input dialog:
   ```
   username
   password
   ```
3. Press ++enter++

### After

```go
[username]| := "variable-[username]|"
[password]| := "variable-[password]|"
```

## Remarks

[//]: # (@formatter:off)

!!! info
		There is a setting under `Actionable > Text` that you can check it if you
		want the replacement to be case-preserving.

		before:
		```
		aCamelCaseName
		```

		your input:
		```
		HELLO_WORLD
		PascalCase
		```

		after:
		```
		helloWorld
		pascalCase
		```
