# Firebase+ Cloud Functions

Your Firebase project must be on the Blaze (pay-as-you-go) plan to use Cloud Functions.

### Setup

Modify the Firebase project id on /firebase-plus-cloud-functions/.firebaserc file

### Deploy functions

    firebase deploy --only functions

### Functions

#### pushConfig

This function is triggered by Firebase Remote Config in response to Remote Config events, including the publication of a new config version or the rollback to an older version.
The function sets a REMOTE_CONFIG_STATUS parameter, and then sends that as the data payload of an FCM message to all clients subscribed to the REMOTE_CONFIG_PUSH topic. 
It also includes an analytics label, so you can see the pushes on the Reports tab in the Firebase console.
