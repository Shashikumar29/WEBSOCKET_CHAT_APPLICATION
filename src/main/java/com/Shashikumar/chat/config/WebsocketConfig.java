package com.Shashikumar.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration   
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer { 
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}


// 1️⃣ Class-level annotations
// @Configuration
// @EnableWebSocketMessageBroker
// public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
// 🔹 @Configuration
// Marks this class as a Spring configuration class
// Spring will load it at startup and apply the settings defined inside
// 🔹 @EnableWebSocketMessageBroker
// Enables WebSocket message handling using STOMP
// Internally:
// Turns on WebSocket support
// Enables a message broker (for publish–subscribe messaging)
// Required when using @MessageMapping, @SendTo, etc.
// 🔹 implements WebSocketMessageBrokerConfigurer
// Allows you to customize WebSocket behavior
// You override methods to configure:
// Endpoints
// Message broker
// Prefixes
// 2️⃣ Registering WebSocket endpoint
// @Override
// public void registerStompEndpoints(StompEndpointRegistry registry) {
//     registry.addEndpoint("/ws").withSockJS();
// }
// 🔹 What is this doing?
// This defines the WebSocket connection URL.
// 🔹 /ws
// This is the endpoint clients connect to
// Example (browser):
// new SockJS("http://localhost:8080/ws");
// 🔹 withSockJS()
// Enables SockJS fallback
// Useful when:
// Browser doesn’t support WebSockets
// Corporate firewalls block WebSockets
// Falls back to:
// HTTP streaming
// Long polling
// 📌 Without SockJS, only pure WebSocket connections are allowed.
// 3️⃣ Configuring the message broker
// @Override
// public void configureMessageBroker(MessageBrokerRegistry registry) {
//     registry.setApplicationDestinationPrefixes("/app");
//     registry.enableSimpleBroker("/topic");
// }
// This is the most important part.
// 4️⃣ setApplicationDestinationPrefixes("/app")
// 🔹 What does /app mean?
// Prefix for messages sent FROM client TO server
// Messages with this prefix are routed to:
// @MessageMapping methods in controllers
// 🔹 Example
// Client sends:
// stompClient.send("/app/chat", {}, JSON.stringify(msg));
// Server receives:
// @MessageMapping("/chat")
// public void handleChat(ChatMessage msg) {
// }
// 📌 /app is not a real endpoint, it’s just a routing prefix.
// 5️⃣ enableSimpleBroker("/topic")
// 🔹 What is the simple broker?
// A lightweight in-memory message broker
// Handles broadcasting messages to subscribers
// 🔹 /topic
// Prefix for server → client messages
// Used for publish–subscribe
// 🔹 Example
// Client subscribes:
// stompClient.subscribe("/topic/messages", callback);
// Server sends:
// @SendTo("/topic/messages")
// 📌 All subscribed clients receive the message.
// 6️⃣ Message flow (VERY IMPORTANT)
// 🔁 Full message flow example
// 1️⃣ Client connects
// http://localhost:8080/ws
// 2️⃣ Client sends message
// /app/chat
// 3️⃣ Server handles it
// @MessageMapping("/chat")
// @SendTo("/topic/messages")
// public ChatMessage send(ChatMessage msg) {
//     return msg;
// }
// 4️⃣ Broker broadcasts
// /topic/messages
// 5️⃣ Clients receive message
// 7️⃣ Visual summary
// CLIENT
//   |
//   |  SEND  -->  /app/chat
//   |
// SPRING CONTROLLER (@MessageMapping)
//   |
//   |  PUBLISH --> /topic/messages
//   |
// SIMPLE BROKER
//   |
//   |  BROADCAST
//   |
// CLIENTS (subscribed to /topic/messages)
