Chapter14-SpringMVC-REST
======================

This is a Maven / Spring 4 / Servlet 3.0 API / RESTful web application that utilizes Spring Data JPA
repositories and uses @RepositoryRestResource to create an automatic REST endpoint for the repository's
operations to perform CRUD operations in a mysql database.

This application uses two view templates technologies, Thyemleaf 3 and the traditional JSP approach.
It also demonstrates usage of none template views such as Excel and PDF views.

The technologies used by this application are top notch industry standard Java libraries as per May 2017
and are managed by Maven. Everything in this application is open source. As such, it utilizes tools. such
as Flyway DB to initialize the database schema and manage it's integrity and takes advantage of Spring
MVC's web application developing features such I18N support (RTL too), CSS Theme support, pagination, filtered
search queries, custom type conversions, RESTfull API testings, JQuery AJAX calls and more.

Features of this application:
=============================
* N-Tier Architecture
	* MVC Web Application
* Servlet 3 API
	* No web.xml
	* Web Initialization with AbstractAnnotationConfigDispatcherServletInitializer or WebApplicationInitializer
* Spring 4 Configuration Standards
	* No spring-beans.xml or applicationContext.xml files
	* All configurations are java annotations based
	* Demonstrating profile specific configurations
	* Demonstrating @Autowire usage
* Spring 4 Bean Life Cycle
	* Demonstrating using bean life cycle stages
	* Demonstrating using events
* Spring AOP Support
	* Using Spring Aspects to perform Auditing
	* AspectJ standards
* Spring DAO CRUD operation
	* CRUD
	* Search (filtering)
* Spring DATA / JPA Repositories
	* Pagination support
	* Sorting (ASC/DESC)
	* @Query support
	* Using JPA Audit Annotations
	* Using @EntityListeners
	* JPA Configurations
* Spring DATA @RepositoryRestResource
	* Automatic RESTfull end points
	* Application-Level Profile Semantics (ALPS) documents,
	* HAL Browser with spring-data-rest-hal-browser
* Unit testing with MockMVC
	* Using the @WebAppConfiguration to inject a web application context
	* Using MockMVC perform methods
	* Using MockMvcResultMatchers.jsonPath
	* Using MockMvcResultMatchers.status
	* Using MockMvcResultMatchers.content
	* Alternative: Using RestTemplate to test RESTfull end points
* Database Schema Integrity management
	* Flyway DB
* Multiple View Technologies
	* Thymeleaf
	* JSP
	* Excel view
	* PDF generation
* RESTfull client
	* JQuery based HTML client
	* JQuery AJAX calls
	* JQuery DOM manipulations
* Demonstrating Spring MVC Exception handlers
	* Controller Advice (global exception handling)
* Demonstrating Request Mapping and Handlers
	* @RequestMapping
	* @GetMapping
	* @PathVariable - REST style
	* @RequestParam
* Spring Type Conversions
	* via Converter<S,T> implementations
	* Defining conversionService bean
* Using Property Place Holder Configurer
	* Reading values from properties files
	* Using the @Value annotation to initialize fields
* Internationalization support
	* Using resource bundles
	* Multiple languages
	* Using UTF-8 RTL (bidi) languages support
	* Defining a locale resolver
	* Using multiple date formats
* Project Dependency Management
	* Maven POM
	* Maven plugins
	* Using Tomcat7 plugin

### TO CURL AND PRETTY PRINT JSON ###

export HOST=localhost:8080/spring-rest
curl -v -H "Content-Type: application/json" -X GET $HOST/api/hello/Tomer/Silverman | python -mjson.tool

OR:

curl -v -G http://localhost:8080/spring-rest/api/hello/Tomer/Silverman | python -mjson.tool

OR:

curl -v -H "User-Agent: Spoofed 53.4" -H "Accept: */*" -H "Content-Type: application/json" -X GET  $HOST/api/hello/Tomer/Silverman | jq

### To CURL the Contact Repository ###

export HOST=localhost:8080/spring-rest

Then:

query contacts profile:

curl -v -G $HOST/api/profile/contacts | jq

query paged contacts:

curl -v -G $HOST/api/contacts | jq

query search functions

curl -X "GET" -v -H "Content-Type: application/json" -H "Cache-Control: no-cache" -k "$HOST/api/contacts/search" | jq

execute a query by example:

curl -X "GET" -v -H "Content-Type: application/json" -H "Cache-Control: no-cache" -k "$HOST/api/contacts/search/byExample?ssn=123&firstName=&lastName=&dateOfBirth=06/21/1978&married=true&children=&page=0&size=10&sort=id,asc" | jq

save a new contact

curl -X "POST" -v -H "Content-Type: application/json" -H "Accept: application/json" -H "Cache-Control: no-cache" -d '{"contactSSN": {"ssn":"123-45-6789"},"firstName":"Lucky","lastName":"Luke","dateOfBirth":"1954-08-21","married":true,"children":2}' -k "$HOST/api/contacts"

update a contact:

curl -X "POST" -v -H "Content-Type: application/json" -H "Accept: application/json" -H "Cache-Control: no-cache" -d '{"id":39,"contactSSN": {"ssn":"123-45-6789"},"firstName":"Lucky","lastName":"Luchiano","dateOfBirth":"1954-08-21","married":false,"children":0}' -k "$HOST/api/contacts"

delete a contact

curl -X "DELETE" -v -H "Content-Type: application/json" -H "Cache-Control: no-cache" -k "$HOST/api/contacts/35"

patch a contact

curl -X "PATCH" -v  -H "Content-Type: application/json" -d '{"contactSSN":{"ssn":"123-45-6789"},"firstName":"Lucky","lastName":"Luke","dateOfBirth":"1954-08-21","married":true,"children":1}' -k "$HOST/api/contacts/42" | jq

or PUT

curl -X "PUT" -v  -H "Content-Type: application/json" -d '{"contactSSN":{"ssn":"123-45-6789"},"firstName":"Lucky","lastName":"Luke","dateOfBirth":"1949-08-21","married":true,"children":3}' -k "$HOST/api/contacts/42" | jq


### TO CREATE MYSQL DUMP ###

mysqldump -u root --opt --hex-blob --databases spring --default-character-set=utf8 --lock-tables=false --extended-insert=false --complete-insert --verbose > sql/dump.sql

MYSQL CONNECTION EXAMPLE:

mysql -h <host> -u <username> -p<password> <database>

mysql -h us-cdbr-iron-east-03.cleardb.net -u b2ebc2d6b74dfe -p1706cd9b ad_f09282f654b6be7

### RUN ON TOMCAT USING MAVEN TOMCAT7 PLUGIN ###

mvn compile tomcat7:run

to shut down:

mvn tomcat7:shutdown

### TO MAKE A GIT COMMIT AND STAGE ALL ###

git commit -a -m "commit message"

to push to github:

git push https://github.com/tnsilver/Spring4-MVC master


### TO CLEAR A GIT REPOSITORY (BETTER BE A LONELY USER OR THIS WILL F%$K UP EVERYONE!!!) ###

Delete the .git directory locally.

Recreate the git repostory:

$ cd (project-directory)

$ git init

$ (add some files)

$ git add .

$ git commit -m 'Initial commit'

Push to remote server, overwriting. Remember you're going to mess everyone else up doing this … you better be the only client.

$ git remote add origin <url>

$ git push --force --set-upstream origin master

### TO CREATE A DEPLOYABLE WAR ###

mvn clean compile war:war


#### TO PUSH THE APP TO PIVOTAL ####

from project directory:

mysqlstage.sh < sql/drop.sql

mvn clean compile war:war -P cloud

cf login -a https://api.run.pivotal.io -u <USER> -p <PASSWORD> -o  -s staging

STAGE:

cf push -p target/spring-rest.war -f manifest.yml

