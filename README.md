# Xpress Test 

What I have done so far is build a rest
API that:

- Logs a user in using email and password
- Generates a token after login 
- Users can use that token to send request to a protected route /airtime/buy
- Users can buy airtime by specifying the amount they want to buy


Technical Details

- I added logging config
- In memory database (Storing user data as static object accessible
throughout the application)
- I added Unit testing 
- I added a test suite
- Ensured separation of concern 

Controllers
- AuthController handles login for users, it gives the user a token if the credentials given are correct 
- Airtime controller manages the purchase of airtime
Utils
- I Created dummy data that can be shared throughout the application (We dont have a database)
-  AuthUtils Generates token, decodes token and generates payment hash
- Airtime helper class helps GetUniqueAirtimeCode that helps send a request to xpress api to get unique code on products
- LogUserContext helps us store a static variable in each thread. When we get user data from token in the interceptor
we store tha user data here. This way, we can share this data between all parts of the server using that thread. We cleanup that data after we are done with the request
- Response handler helps us make sure all our reponses are uniform and consistent
- UserInterceptor makes sure only authenticated users can use the airtime/* route
this filter is configured in the App config XpressApplication.java
- Validation handler formats error messages that come up from Class validation exceptions 


