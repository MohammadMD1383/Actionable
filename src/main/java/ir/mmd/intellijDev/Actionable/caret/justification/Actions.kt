package ir.mmd.intellijDev.Actionable.caret.justification

import ir.mmd.intellijDev.Actionable.internal.doc.Documentation
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep


@Keep
@Documentation(
	title = "Justify Carets End and Shift Text",
	description = "Aligns carets to the rightmost one, and also shifts the text with moved carets",
	example = """
		| symbol              | meaning |
		| ------------------- | ------- |
		| <code>&#124;</code> | caret   |
		
		having:
		```java
		String s |= "some string";
		String someString |= "some another string";
		```
		will produce:
		```java
		String s          |= "some string";
		String someString |= "some another string";
		```
	"""
)
class JustifyCaretsEndAndShift : JustifyAction(JustifyCaretUtil::justifyCaretsEndWithShifting)

@Keep
@Documentation(
	title = "Justify Carets Start",
	description = "Aligns the carets to the leftmost one",
	example = """
		| symbol              | meaning |
		| ------------------- | ------- |
		| <code>&#124;</code> | caret   |
		
		having:
		```java
		class |A {
		  final int |a = 122;
		  String |str = null;
		}
		```
		will produce:
		```java
		class |A {
		  fina|l int a = 122;
		  Stri|ng str = null;
		}
		```
	"""
)
class JustifyCaretsStart : JustifyAction(JustifyCaretUtil::justifyCaretsStart)

@Documentation(
	title = "Justify Carets End",
	description = "Aligns the carets to the leftmost one",
	example = """
		| symbol              | meaning |
		| ------------------- | ------- |
		| <code>&#124;</code> | caret   |
		
		having:
		```java
		class |A {
		  final int |a = 122;
		  String |str = null;
		}
		```
		will produce:
		```java
		class A {   |
		  final int |a = 122;
		  String str| = null;
		}
		```
	"""
)
@Keep
class JustifyCaretsEnd : JustifyAction(JustifyCaretUtil::justifyCaretsEnd)
