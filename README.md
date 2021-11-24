# InjectClass-idea-plugin

该插件是为了配合https://github.com/mudongjing/classInject，主要的功能在于通过在类上添加注解`@InjectClass(path="java包")`，插件将自动判断对应的java包是否存在，且不是当前类对应的包，当该java包合法，则识别包下的类，注意，这里不会向下识别刺激java包中的类。

当获得包中的类，则将这些类作为属性字段添加到被注解的类，同时添加对应的setter/getter方法。

效果如同Lombok，使用了代码增强的方法，不会对原有的代码造成侵入。
