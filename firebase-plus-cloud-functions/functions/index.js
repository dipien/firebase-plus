const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.pushConfig = functions.remoteConfig.onUpdate((versionMetadata) => {
  // Create FCM payload to send data message to REMOTE_CONFIG_PUSH topic.
  const payload = {
    topic: "REMOTE_CONFIG_PUSH",
    data: {
      "REMOTE_CONFIG_STATUS": "STALE"
    },
    fcm_options: {
      "analytics_label": "REMOTE_CONFIG_PUSH",
    },
  };
  // Use the Admin SDK to send the ping via FCM.
  return admin.messaging().send(payload).then((resp) => {
    console.log(resp);
    return null;
  });
});