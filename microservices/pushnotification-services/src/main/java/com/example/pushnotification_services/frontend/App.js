import React, { useEffect } from 'react';
import { View, Text, Alert } from 'react-native';
import * as Notifications from 'expo-notifications';
import * as Device from 'expo-device';

export default function App() {
  useEffect(() => {
    registerForPushNotificationsAsync().then(token => {
      fetch('http://your-backend-url/api/tokens', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token }),
      });
    });

    const subscription = Notifications.addNotificationReceivedListener(notification => {
      const data = notification.request.content.data;
      Alert.alert(
        `Flight ${data.flightNumber} Update`,
        `Gate: ${data.gate}\nStatus: ${data.status}\nTime: ${data.time}`
      );
    });

    return () => subscription.remove();
  }, []);

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>Flightly Notifications Ready</Text>
    </View>
  );
}

async function registerForPushNotificationsAsync() {
  if (!Device.isDevice) {
    alert('Must use physical device for Push Notifications');
    return;
  }

  const { status: existingStatus } = await Notifications.getPermissionsAsync();
  let finalStatus = existingStatus;

  if (existingStatus !== 'granted') {
    const { status } = await Notifications.requestPermissionsAsync();
    finalStatus = status;
  }

  if (finalStatus !== 'granted') {
    alert('Failed to get push token');
    return;
  }

  const tokenData = await Notifications.getExpoPushTokenAsync();
  return tokenData.data;
}
