# http4k-by-example application 

<a href="https://travis-ci.org/http4k/http4k-by-example" target="_top"><img src="https://travis-ci.org/http4k/http4k-by-example.svg?branch=master"/></a>&nbsp;&nbsp;&nbsp;
[![codecov](https://codecov.io/gh/http4k/http4k-by-example/branch/master/graph/badge.svg)](https://codecov.io/gh/http4k/http4k-by-example)

#### about
Complete TDD'd example [http4k](http://http4k.org) application showcasing a lot of the http4k features for building and testing apps. 

##### Features:
- Composable routing in both standard and contract (OpenAPI) forms with automatic parameter marshalling and unmarshalling (Headers/Query/Path/Body/Forms)
- HTTP response building, including sample JSON library support (Jackson) and auto-data class instance marshalling
- OpenAPI v3 documentation and JSON schema generation from example model objects and OAuth-based security
- Automatic invalid request handling
- Endpoint security via an OAuth (including simple OAuth Server implementation)
- Templating system (Handlebars)
- Typesafe Form handling with validation and error feedback
- Configured via typesafe 12-factor configuration
- Serving of static resources

##### Testing features:
- Testing applications completely in-memory for ultra fast test suites
- Approval-based testing for testing JSON and HTML responses
- Hamkrest matchers for easy assertions on http4k objects
- Reusable Fake HTTP dependencies, with behaviour proven by Consumer Driven Contracts
- WebDriver usage for browser-based testing
- Simulating failures with the http4k ChaosEngine

It has been developed in a London-TDD style with outside-in acceptance testing and CDCs for outside dependencies,
to give a complete overview of how the app would look when finished. 

#### running this demo app
1. Clone this repo
2. Run `RunnableEnvironment` from an IDE. This will start the application on port 9000, which has been configured to use a fake versions of the remote dependencies (on ports 10000, 11000 and 12000)
3. Just point your browser at [http://localhost:9000/](http://localhost:9000/)
4. OAuth login details are `user:password`

<hr/>
<hr/>

# building security system

#### requirements
This example models a simple building security system accessible over HTTP. Requirements are:

#### Functional:
1. Users can ask to be let into and out of the building.
1. Usernames are checked for validity against a remote HTTP UserDirectory system.
1. Successful entries and exits are logged in a remote HTTP EntryLogger system.
1. Ability to check on the current inhabitants of a building.
1. Users are tracking in a binary state - inside or not (outside). Only people outside the building can enter, and vice versa.
1. Custom UI (OAuth protected) to add users.

#### Operational:
1. API documentation should be available with security enforced via OAuth login.
1. All API HTTP endpoints are protected with bearer token to only allow authorised access.
1. Logging of every successful requests should be made.
1. Support distributed tracing via Zipkin headers
