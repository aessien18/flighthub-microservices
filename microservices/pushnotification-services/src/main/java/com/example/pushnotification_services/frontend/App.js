import React, { useEffect, useState } from 'react';
import { View, Text, Alert, Platform } from 'react-native';
import * as Notifications from 'expo-notifications';
import * as Device from 'expo-device';
import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';

// TODO: Replace with your Firebase project configuration
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID",
  measurementId: "YOUR_MEASUREMENT_ID"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

export default function App() {
  const [fcmToken, setFcmToken] = useState(null);

  useEffect(() => {
    async function registerForPushNotificationsAsync() {
      if (!Device.isDevice) {
        Alert.alert('Must use physical device for Push Notifications');
        return;
      }

      const { status: existingStatus } = await Notifications.getPermissionsAsync();
      let finalStatus = existingStatus;

      if (existingStatus !== 'granted') {
        const { status } = await Notifications.requestPermissionsAsync();
        finalStatus = status;
      }

      if (finalStatus !== 'granted') {
        Alert.alert('Failed to get push token for push notification!');
        return;
      }

      // Get FCM token
      try {
        const currentToken = await getToken(messaging, { vapidKey: 'YOUR_VAPID_KEY' }); // TODO: Replace with your VAPID key
        if (currentToken) {
          console.log('FCM Token:', currentToken);
          setFcmToken(currentToken);
          // Send FCM token to your backend
          fetch('http://localhost:8080/api/tokens', { // TODO: Replace with your actual backend URL
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token: currentToken }),
          })
          .then(response => response.text())
          .then(data => console.log(data))
          .catch(error => console.error('Error sending token to backend:', error));
        } else {
          console.log('No FCM registration token available. Request permission to generate one.');
        }
      } catch (err) {
        console.error('An error occurred while retrieving token. ', err);
      }
    }

    registerForPushNotificationsAsync();

    // Handle incoming messages while the app is in the foreground
    const unsubscribe = onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      const data = payload.data;
      if (data) {
        Alert.alert(
          `Flight ${data.flightNumber} Update`,
          `Gate: ${data.gate}\nStatus: ${data.status}\nTime: ${data.time}`
        );
      }
    });

    return () => unsubscribe();
  }, []);

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>Flightly Notifications Ready</Text>
      {fcmToken && <Text>FCM Token: {fcmToken}</Text>}
    </View>
  );
}