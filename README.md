# deviget-android-challenge
This app solves the Deviget Android Challenge.

The app load paginated posts from Reddit in order to show the first 50 Top Posts. Room library is used to persist the status of each post (read or hidden) between each session.

In order to maintain this code decoupled, testable and robust, the architecture of this app was designed using [clean architecture](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html). Each Product Feature contains the following layers represented as packages in the src module folder:
- **Domain Layer**: Contains the business logic of each module, totally independent of the device, the networking data or the ui. Each business logic task is represented as an *Use Case*.
- **Data Layer**: Represented by *Repositories* which have the responsibility to access and get data from the different sources (Networking, device and Local Database).
- **Presentation Layer**: Layer responsible for displaying the information provided by the business logic layer, using the UI interface. This layer use the [Google's architecture components](https://developer.android.com/topic/libraries/architecture/) approach to manipulate data between the activities/fragment and the presentation classes (represented by ViewModels).

The communication and transformation of data into the layers and the domain was represented by *Mapper* classes. Each user flow which implements these three layers was developed using **async, event-based and reactive programming** with [RxJava](https://github.com/ReactiveX/RxJava).

### Dependency Injection
All the app component dependencies are injected using [Koin](https://insert-koin.io/), which provides a lightweight and pure Kotlin dependency injection mechanism with [glorious support for Android architecture classes such as ViewModels](https://insert-koin.io/docs/1.0/documentation/reference/index.html#_architecture_components_with_koin_viewmodel). Each architecture layer has an extra package called *module* who contains the injections of all the dependencies used by this module.

### Unit Testing
For demonstration purposes, It was implemented a set of unit test classes that intends to test one of each component of the architecture. These test classes were designed to check the output of the class being tested, using [Mockito](https://site.mockito.org/) as a mocking mechanism of their inputs and dependencies. Also it uses Robolectric when Android instrumented artifacts were needed.