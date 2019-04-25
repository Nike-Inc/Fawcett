Fawcett
-------
![Fawcett logo][logo]

A collection of [Monocle](http://julien-truffaut.github.io/Monocle/) lenses for navigating Amazon's API models.

Setup
-----

Include the following in your `build.sbt`. If you're using AWS Java SDK version 1 change the `awsSdkVersion` to `"v1"`.
```
val awsSdkVersion = "v2"
...
resolvers += Resolver.bintrayRepo("nike", "maven"),
libraryDependencies += "com.nike.fawcett" %% s"fawcett-sqs-$awsSdkVersion" % "0.1.2",
```


Usage
-----

The lenses for Amazon's models are contained in an object of the same name with "Lens" appended. For instance, if we
need to operate on a `SendMessageBatchRequest` we have lenses defined on the object `SendMessageBatchRequestLens`.

Example of getting all the message bodies from a `com.amazonaws.services.sqs.model.ReceiveMessageResult`.
```
import com.nike.fawcett.sqs.ReceiveMessageResultLens

val messageBodies: List[String] = ReceiveMessageResultLens.allBodies(receiveMessageResult)
```

Example of setting random de-duplication identifiers on messages in a
`com.amazonaws.services.sqs.model.SendMessageBatchRequest`. Here we start with an existing SendMessageBatchRequest,
`originalRequest`, and then modify all the existing or unset de-duplication ids and get a new
`SendMessageBatchRequestLens`, `updatedRequest`. It's important to note these are two completely different values,
`originalRequest` has not been modified.

```
import com.nike.fawcett.sqs.SendMessageBatchRequestLens
import java.util.UUID

val updatedRequest: SendMessageBatchRequest =
  SendMessageBatchRequestLens.allMessageDeduplicationIds.modify(_ => UUID.randomUUID.toString)(originalRequest)
```

If we define a `Getter` from Monocle, we can start composing lenses. Here we create a function to extract notes from
our message results.
```
import monocle.Getter
import monocle.functions.all._
import com.nike.fawcett.sqs.ReceiveMessageResultLens
import com.amazonaws.services.sqs.model.ReceiveMessageResult

case class Note(body: String)

val noteGetter: Getter[Message, Note] = Getter(message => Note(message.getBody))

val getNotes: ReceiveMessageResult => List[Note] =
  ReceiveMessageResultLens.messages composeTraversal each composeGetter noteGetter getAll _
```

Contributing
------------

See our [contributing guidelines](CONTRIBUTING.md) before you start.

Adding Lenses
-------------

You can reduce the amount of typing required by using the giter8 scaffolding provided for lenses.
Inside of sbt run `g8Scaffold lens` from the root project.
Or just from the command line `sbt "g8Scaffold lens"`

This will create the basic lens trait and test structure for you.

Maintainers
-----------
* [vendamere](https://github.com/vendamere) Peter Vendamere

[Release notes](CHANGELOG.md)

License
-------

Fawcett is released under the [BSD-3 clause](LICENSE.md) license.

[logo]: images/monocle-outline-Fawcett@250px.png "Fawcett logo"
