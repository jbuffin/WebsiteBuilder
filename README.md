WebsiteBuilder

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

---

##What is WebsiteBuilder
I have been a long time user of WordPress and love it for what it is. But I wanted something that could easily be hosted in the cloud, host multiple sites, and be super easy to customize. 
That's where this repo comes into play. It's written in Scala using the [Play Framework](http://www.playframework.com) which makes it deployable to Heroku or CloudFoundry out of the box. 
It supports multiple sites that can be easily set up with a custom domain name. Pages are easily created and are edited directly while visiting the page. Pages are completely customizable. 
No need for templates with a pre-determined layout. The page layout can be completely customized by the user by placing widgets in a grid system. The grid system is a 12-column responsive grid 
by [Twitter Bootstrap](www.getbootstrap.com). Bootstrap is easily customizable and the themes can be customized simply by allowing the user to upload their own .css file.
Now, at this point, a lot of this is still in the early phases of development and there is a roadmap to get there that I will include in this readme. By the time I hit 1.0, this site builder 
will be easily downloaded and installed on Heroku or other PaaS hosting solution and up and running custom sites in just a few minutes.

##How does it work
###The router
At the core of the WebsiteBuilder is the routing engine. The router takes a request and determines what site you are 
trying to reach. It searches its database to find that site and, if found, finds the page from the uri that you are 
trying to reach. If the page exists, the router assembles the page layout and passes everything off
to the page view, which builds the page up and presents it to the user.
###Editing pages
Pages are edited directly on the page. The site admin navigates to the page while logged in and can directly edit everything 
on the page. I use `contenteditable` sections to allow the text to be edited and a bar at the top to add other elements to the page. The default username/password for the admin user is admin/admin and the default account is set up on first-run. Users can login by navigating to /login.

##How to get started
###Dependencies
You will need:
- postgresql database (This will be phased out)
- MongoDB
- The Play Framework 2.10
- jdk6 or later

###Steps to get up and running
After you have the dependencies installed, you'll be ready to get started.
####Create a database in Postgresql
Open your database admin tool (I like pgAdmin III). Create a database and give it a name (I called mine _postgres_. Remember this name for later). Add a login 
role and grant all on the new database you created. That's it, the tables will be automatically built up the first time you run the app.
####Create database in MongoDB
Open a terminal (Linux) or cmd (Windows) and type `mongo`. Create a database and remember the database name for later. I use "websitebuilder" as the db name.
####Configure the app
There are two configurations that need to be edited. They are both contained in [conf/application.conf](conf/application.conf). 
The first is the secret key. You need to edit the line that has `application.secret="somesecretkey"`. You'll want to change it because it is 
what the crypto functions will use to hash passwords and such so edit yours. The second is the `db.default.url`. edit the url part to have your username and password
that you set up in the _Create a database_ step. The url is in the form `postgres://username:password@servername:portnum/databasename`. For development, the database is 
probably `localhost` with the default port number of 5432. I use postgres for my username, password and database name. You'll need to add a new line that reads `mongodb.uri="mongodb://localhost:27017/websitebuilder"`. 
####Run the app
Open a terminal window (command window in Windows) and go to the root of the project and type `play run`. Open the admin site in a web browser: http://localhost:9000/admin-home 
and add a site and a page.
####I like to edit my hosts file
It is really useful to edit your hosts file so that you can have domains that point to your app. In Windows, I use a program called 
_Hosts File Editor_ (right to the point, eh?). You can search Google to figure that out. You'll want to set up domains in the hosts file 
that point to 127.0.0.1. This is really useful to see that the routing works. Alternately, you can create a _localhost_ site. Simply add a site 
in the admin page with the domain _localhost_.

##Deploying to Heroku
The included [Procfile](Procfile) is already set up to get up and running on Heroku. All you need to do is create a Heroku app and push the code. Heroku will take care of the rest.
