## Remove Duplicate Selections

This action will remove duplicate selections (should be used with multi-caret mode)

---

### example:

| symbol   | meaning         |
|----------|-----------------|
| `(`      | selection start |
| `)`      | selection end   |
| `&#124;` | caret           |

having:
```
(text 1)|
(text 2)|
(text 1)|
```
will produce:
```
(text 1)|
(text 2)|
|
```
