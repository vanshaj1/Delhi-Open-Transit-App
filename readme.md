# Objective:
The aim of this project is to provide users with real-time information about the location of
the bus they are traveling on, along with upcoming stops. This app serves as a guide for
users unfamiliar with Delhi's bus system.

# Overview:
The Delhi Open Transit App is a groundbreaking project designed to revolutionize public
transportation in Delhi, India, by providing commuters with a seamless
and efficient way to navigate the city's bus network. Developed using Kotlin, a modern
programming language for Android app development, the app offers a user-friendly
interface and innovative features to enhance the commuting experience.
* Real-time Bus Tracking
* User Interaction
* Location-Based Services
* To and From Stops
* Route Information

# Flow:
First, the application prompts users to indicate if they are currently traveling. If the
response is affirmative, it directs users to a screen where they can input the bus route
number. Utilizing GeoFusion, the app retrieves real-time information about the ongoing
bus stop based on the bus's location and the user's coordinates.
Alternatively, if the user responds negatively, the app presents another screen where
users can input the source and destination bus stops. From a pool of 4000 buses, the
app filters the buses that serve these stops and displays their route numbers. Upon
selecting a bus, users are redirected to a screen showing all the stops along the
selected bus route.

# Scope:
The Delhi Open Transit App for tracking buses is a project aimed at enhancing public
transportation services within the city of Delhi, India. The scope of this project
encompasses the development of a mobile application that enables commuters to track
the real-time location and arrival times of buses operating across different routes within
the city.
* Real-Time Bus Tracking
* Route Planning
* Bus Schedule Information
* Integration with Other Services

# User Interface:
* Splash Screen: Introduces the app.

    <img height="400" src="./UI Images/1.jpg" width="200"/><br/>

* Home Screen: Central hub of the app.

    <img height="400" src="./UI Images/2.jpg" width="200"/><br/>

* Yes/No Screen: Asks if the user is currently traveling.
    
    <img height="400" src="./UI Images/3.jpg" width="200"/><br/>
    
    * if selected No then :
         * User Can ask which route buses go to destination from source
             
            <img height="400" src="./UI Images/5.jpg" width="200"/><br/>
         
         * All buses that go to destination from source
             
            <img height="400" src="./UI Images/6.jpg" width="200"/><br/>
         
         * Can click each bus and can see detailed view
             
            <img height="400" src="./UI Images/7.jpg" width="200"/><br/>
    
    * if Selected yes then:  
        * Users will be asked about the current bus they are in and what is their destination
             
            <img height="400" src="./UI Images/8.jpg" width="200"/><br/>
        
        * Based on information recived about bus and destination **Live bus location tracking** will start
             
            <img height="400" src="./UI Images/9.jpg" width="200"/><br/>

# Main Features:
* Live Location Tracking
* Information about upcoming bus stops.
* Notification when the destination is approaching.

# Formula Used:
Euclidean Distance formula for calculating distance between bus stops and user
location:
distance = sqrt((x2 - x1)^2 + (y2 - y1)^2)

# Libraries Used:
* GeoFusion
* Google Play Maps API

# Sensor Used:
* fusedLocation
The Delhi Open Transit App leverages the Fused Location Provider API, a part of Google
Play services, to obtain accurate and up-to-date location information from various
sources such as Wi-Fi and cellular networks. This integration ensures that users receive
precise location data, enabling the app to deliver real-time bus tracking and
personalized route planning features effectively.

# Database:
* SQLite
* Database created using Open Transit Delhi API maintained by IIITD.

# Database Creation:
A Python script was used to extract data from the protocol buffer file provided by the
API.

# Assumptions:
* Utilizing static data from the Delhi Open Transit website.
* Assumes users know the source and destination bus stops.
* Assumes direct bus routes without interchanges.
* Assumes users know how to reach the source bus stand.

# Conclusion
This project bridges the gap for users unfamiliar with Delhi's bus system, providing them
with essential real-time information for a smoother travel experienc