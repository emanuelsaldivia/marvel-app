Package structure
- common: contains common classes
- data: contains model data structure
-- model: contains business and network model
-- repository: contains repositories for each business model
- di: contains dependency injection modules
- network: contains network services and utils
- ui: contains presentation classified by feature (i.e: character feature contains it's fragments and view models)
- usecase: contains business logic

Architecture
- MVVM

Technology stack
- Dependency injection: Toothpick
- Android navigation
- Network: Retrofit
- Images: Glide
- coroutines
- Testing: Junit4
- mocking: Mockk, MockWebServer

Expected values for layers
Network layer: Returns a Retrofit Response with a Network DTO
Repository layer: Receives a Retrofit Response a returns an Outcome
View Model layer: Receives an Outcome and returns a presentation Resource

Network model
- MarvelDataWrapper: Generic Marvel API response, must contain a Network DTO according to the network call
- DataNetworkDto: contains generic Marvel API response data and results