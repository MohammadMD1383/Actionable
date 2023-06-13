# For Developers

## Contribute To Actionable

If you're interested in developing and improving actionable plugin, you're welcome!

1. Fork Actionable
2. add advanced search functionality in package: `ir.mmd.intellijDev.Actionable.find.advanced.agent` by creating a new package representing the
   language you want to provide advanced search support.
3. register the new language support in `plugin.xml` like so: `<advancedSearch.providerFactory factoryClass="..." language="..." />`
4. create a PR.
5. that's it!

[//]: # (@formatter:off)
!!! note
    for examples of how to implement such functionality you can explore the `ir.mmd.intellijDev.Actionable.find.advanced.agent.java` package.

    for Q&A please refer to the discussions page of the actionable repository. feel free to ask any question.
[//]: # (@formatter:on)

## Separate Plugin

If you're developer of a plugin (especially a custom language), and want to provide advanced search support, you're welcome!

1. add Actionable dependency to your plugin in `build.gradle` (v4.5 onwards, which includes advanced search)
2. add Actionable dependency to your `plugin.xml`: `<depends optional="true" config-file="...">ir.mmd.intellijDev.Actionable</depends>`
3. reload project
4. add actionable extension namespace: `<extensions defaultExtensionNs="ir.mmd.intellijDev.Actionable">`
5. add advanced search provider: `<advancedSearch.providerFactory factoryClass="..." language="..." />`<br>
   for `providerFactory` you need to extend `ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchProviderFactoryBean`<br>
   `language` is your language that you want to provide this functionality for.
6. go with examples inside `ir.mmd.intellijDev.Actionable.find.advanced.agent.java` package
7. ask any questions in discussions page of the actionable repository.
8. publish your plugin!
