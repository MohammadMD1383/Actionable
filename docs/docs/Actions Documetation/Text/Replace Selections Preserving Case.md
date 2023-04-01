Replaces selections while preserving the case of the source.

It shows an input dialog with a single line text field.

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

### Before

```html

<div>
	[PascalCase]|
	[camelCase]|
	[snake_case]|
</div>
```

### Action

1. Execute the action
2. Enter `kebab-case`
3. Press ++enter++

### After

```html

<div>
	[KebabCase]|
	[kebabCase]|
	[kebab_case]|
</div>
```
