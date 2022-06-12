## Description

This API has two main endpoints:
1. Return basic Pokemon information.
2. Return basic Pokemon information but with a ‘fun’ translation of the Pokemon description.

![diagram](https://user-images.githubusercontent.com/3872574/173253358-bca47faa-2543-4c88-b6e6-d6111bd3ed8d.png)

## Considerations

This is not a production ready application. In order to "productionise" this, you should consider the following:

- **Health check**: I didn't provide a health check endpoint. With Spring Boot this is very straightforward, though. In a production environment we should consider adding a liveness and a readiness probe.
- **Errors**: I only implemented very basic error handling for this API using a 3rd party library, we could define our error responses more explicitly to have more control over what's returned during unhappy paths. I'm only explicitly handling NOT_FOUND, we should handle other client errors that we may care about.
- **Optionals**: since I'm using RestTemplate as my HTTP client library, and this component throws exceptions for anything that's not a 200, I'm using the same paradigm to handle errors when consuming 3rd party APIs, including NOT_FOUND. A more explicit alternative would be to use Optional to signify that the resource may or may not exist. 
- **Security**: this API is not protected in any way. You should consider adding authentication and rate-limiting at the very least. This can be achieved with an API key or token based authentication (e.g. with a JWT as an access token). A quick way to achieve this is with an API gateway or with OAuth 2.0. Another thing to consider would be anomaly detection and bot protection to prevent malicious actors from abusing our system. 3rd party services like PerimeterX can be quite handy for this. And of course, we'll need an SSL/TLS certificate and use HTTPS.
- **Circuit breakers**: this service relies on 3rd party APIs as downstream dependencies. These can be unreliable. In order to aggravate an issue with these dependencies we should implement a back-off policy with a circuit breaker.
- **Caching**: the data that this service returns changes very infrequently. Therefore, we could introduce a server-side cache. That way we would optimise our use of 3rd party quotas, minimise network traffic, increase our resiliency to downstream issues and improve performance by reducing the number of hops for each request. The drawback is that we would potentially serve stale data at times.
- **Testing**: only basic unit tests have been included. Other useful types of tests, e.g. integration, load testing, could be added.
- **Monitoring and alerting**: no metrics are being emitted from this service. In a production environment, it would be important to capture metrics for both happy and unhappy paths that will help us understand how our product is being used and create monitors that would feed into an alerting system to be handled by an on-call engineer (e.g. PagerDuty). Alternatively we could use logs instead of metrics. Some tools (e.g. Datadog) are really effective at ingesting logs and generating insights that can be used for monitoring in a similar way as metrics. We would need a playbook / runbook to document the service properly and outline how to troubleshoot issues and potentially a monitoring dashboard to display data we consider relevant (e.g. request per second, errors per second, CPU, memory...). We could consider introducing a correlation ID for tracing, although some tools do this for you automatically (e.g. Datadog).
- **Build**: I defined a very simple Gradle build, but I did not include any style checks or linting rules, nor any pre-commit hooks. I also defined a single environment as externalised configuration (application.properties), but we would typically have different sets of properties per environment i.e. local, staging, production.
- **Deployment**: any considerations about deployment and infrastructure have been left out. It would make sense to define build steps required by the continuous integration tool of choice (e.g. DroneCI, CircleCI, Jenkins...) as well as the deployment of the service. This could include quality gates for static analysis. We should also define as code the infrastructure in which this service would run (e.g. CloudFormation or Terraform). If we use something like Kubernetes, we should define a manifest to declare the desired state of our service in the cluster.
- **Feature flags**: typically when introducing new features we would consider using feature flags that would allow us to roll out or back changes quickly and safely. Depending on our experimentation framework, they can also be used for that purpose.
- **API versioning**: we did not versioned our API endpoints. If we introduce breaking changes, it will have a negative impact on our consumers. A common way to handle that is to version our API and maintain multiple live versions when they are not backwards compatible.
- **API docs**: I didn't set up any API docs for this service. Using something like Swagger, it's fairly trivial to generate this documentation automatically from the code. In a production environment, generating documentation automatically prevents it from becoming stale and saves a lot of time.
- **Catalog**: we could include metadata about our service in the repository to be used by a service catalog (e.g. OpsLevel, Backstage). This would include things like type of service and owning team.
- **Contributor guidelines**: I didn't include a CONTRIBUTING.md file or pull request checklist. These are important tools to ensure there is consistency when there are contributions from multiple developers.
- **Separation of concerns**: we could consider extracting the translation module into its own separate microservice if there was a need to scale it differently, or if we needed a second team to work on it more independently. Alternatively we could keep it and build a modular monolith with clearer boundaries, or organise the code in a monorepo style.

### Other considerations

- **Sustainability**: one of the 3rd party APIs supports GraphQL. We could consider using it in order to minimise the amount of data transferred over the wire. This has an impact on the carbon footprint of our application (and it's more efficient). GraphQL support is Beta at the moment, however, so that wouldn't be suitable for production just yet.
- **Internationalisation**: the default language for descriptions, including translations, in our service is English. It would make sense to consider supporting other languages and confirming that encoding works as expected (with tests).
- **Response type**: at the moment our API only returns JSON. It may make sense to allow the client to specify how they want the data to be returned e.g. XML, typically using a header or a request parameter. This is a product consideration, and would depend on the exact requirements, but it's something to keep in mind if our consumers are varied in nature.

## How to run it

I use [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), to make building this repository as easy as possible. The Wrapper is a script that invokes a declared version of Gradle, downloading it beforehand if necessary. I also provide a Dockerfile.

Pre-requisites:
- Docker - [how to get it and install it on your machine](https://www.docker.com/get-started/).

To build and run the docker image use the following commands:

```
docker build -t com.plopcas/pokemon .
docker run -p 8080:8080 -t com.plopcas/pokemon
```

The application will then be available at http://localhost:8080

To try it out, visit:
- http://localhost:8080/pokemon/pikachu
- http://localhost:8080/pokemon/translated/pikachu
