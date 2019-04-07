# http4k-contract sample application 

<a href="https://travis-ci.org/http4k/http4k-contract-example-app" target="_top"><img src="https://travis-ci.org/http4k/http4k-contract-example-app.svg?branch=master"/></a>&nbsp;&nbsp;&nbsp;
[![Coverage Status](https://coveralls.io/repos/github/http4k/http4k-contract-example-app/badge.svg?branch=master)](https://coveralls.io/github/http4k/http4k-contract-example-app?branch=master)

#### about
Complete TDD'd example [http4k](http://http4k.org) application showcasing a lot of the http4k features for building apps:

- Composable routing in both standard and contract (Swagger) forms
- HTTP request routing with automatic parameter marshalling and unmarshalling (Headers/Query/Path/Body)
- HTTP clients with request creation and route spec reuse for Fake Server implementations
- HTTP response building, including sample JSON library support (Jackson) and auto-data class instance marshalling
- Swagger 2.0 documentation and JSON schema generation from example model objects
- Automatic invalid request handling
- Endpoint security via an API-key header - the key is "realSecret"
- Templating system (Handlebars)
- Typesafe Form handling with validation and error feedback.
- Configuration via typesafe configuration
- Serving of static resources

It has been developed in a London-TDD style with outside-in acceptance testing and CDCs for outside dependencies,
to give a complete overview of how the app would look when finished. The code itself has been left without optimisation of
imports in order to aid comprehension - which is a little frustrating from a maintainer perspective (as you always want your 
code looking as awesome as possible! :).

#### running this demo app
1. Clone this repo
2. Run `RunnableEnvironment` from an IDE. This will start the application on port 9000 
which has been configured to use a fake versions of the remote dependencies (on ports 10000 and 11000)
3. Just point your browser at <a href="http://localhost:9000/">http://localhost:9000/</a>

<hr/>
<hr/>

# building security system

#### requirements
This example models a simple building security system accessible over HTTP. Requirements are:

1. Users can ask to be let into and out of the building.
2. Usernames are checked for validity against a remote HTTP UserDirectory system.
3. Successful entries and exits are logged in a remote HTTP EntryLogger system.
4. Ability to check on the current inhabitants of a building.
5. Users are tracking in a binary state - inside or not (outside). Only people outside the building can enter, and vice versa.
6. All HTTP endpoints are protected with a secret HTTP header to only allow authorised access.
7. API documentation should be available.
8. Logging of every successful requests should be made.
