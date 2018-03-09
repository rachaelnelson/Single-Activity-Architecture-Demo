# Single-Activity-Architecture-Demo
An old demo of the single activity architecture for management of screens &amp; navigation from about a year ago. 

### The Concept, The Issues
This takes the single Activity, fragment-per-screen approach to navigating between screens. It seemed great at the time, but realistically all the toolbar and bottom navigation being held by the activity became pretty cumbersome, as all that functionality must be forwarded to fragment superclass. It really served to simplify the complexity of the CoordinatorLayout hierarchy. What must go: fragments. Managing the backstack and bugs within transactions and where they can and cannot be commited is the true time killer when this concept was built into a couple of large apps.

### What I'd do differently:
1. Obviously get rid of fragments, as lifecycle management became complex (doable...but why would you want to when vetted, less buggy frameworks exist?)
2. Start using the Conductor library for screen navigation & view caching
3. Start using Android Architecture Components (I have been abiding by the MVP pattern but would like to go MVVM without dealing with xml work required with Android Data Binding library)
4. Start using Kotlin, because it's succinct and is very close to Swift for parity between Android & iOS app simultaneous product development
5. Allow for multiple Activities extending the BaseActivity functionality, with pre-configured Toolbar or CoordinatorLayoot configurations

### Going further...
6. Use Realm database, because it's performant with encyption and low-memory consumption (to be added to app layer)
7. Picasso for image loading
8. Create a separate network layer with Retrofit services & RxJava/Retrofit adapter, implement caching/network fetching mechanism
9. Break out reusable features into module libraries
10. Get an Instant App feature module going
11. Implement screen navigation tracking for analytics 
